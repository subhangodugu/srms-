package com.srots.application.search;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Aggregates providers, ranks results, and applies access filtering.
 */
public final class DefaultGlobalSearchService implements GlobalSearchService {

    private static final Map<SearchEntityType, String> GROUP_LABELS = new EnumMap<>(SearchEntityType.class);

    static {
        GROUP_LABELS.put(SearchEntityType.EMPLOYEE, "Employees");
        GROUP_LABELS.put(SearchEntityType.PROJECT, "Projects");
        GROUP_LABELS.put(SearchEntityType.CUSTOMER, "Customers");
        GROUP_LABELS.put(SearchEntityType.PRODUCT, "Products");
        GROUP_LABELS.put(SearchEntityType.TASK, "Tasks");
        GROUP_LABELS.put(SearchEntityType.RELEASE, "Releases");
        GROUP_LABELS.put(SearchEntityType.SERVICE_DESK, "Service Desk");
        GROUP_LABELS.put(SearchEntityType.COMPTY, "COMPTY");
        GROUP_LABELS.put(SearchEntityType.KNOWLEDGE, "Knowledge");
        GROUP_LABELS.put(SearchEntityType.SETTINGS, "Settings");
    }

    private final List<SearchProvider> providers;
    private final SearchAccessContext accessContext;
    private final Executor executor;
    private final int perGroupLimit;

    public DefaultGlobalSearchService(List<SearchProvider> providers, SearchAccessContext accessContext) {
        this(providers, accessContext, Executors.newCachedThreadPool(r -> {
            Thread t = new Thread(r, "srots-global-search");
            t.setDaemon(true);
            return t;
        }), 15);
    }

    public DefaultGlobalSearchService(
            List<SearchProvider> providers,
            SearchAccessContext accessContext,
            Executor executor,
            int perGroupLimit) {
        this.providers = List.copyOf(Objects.requireNonNull(providers, "providers"));
        this.accessContext = accessContext == null ? SearchAccessContext.allowAll() : accessContext;
        this.executor = executor == null ? Runnable::run : executor;
        this.perGroupLimit = Math.max(1, perGroupLimit);
    }

    @Override
    public SearchAccessContext accessContext() {
        return accessContext;
    }

    @Override
    public CompletableFuture<SearchResponse> search(SearchQuery query) {
        SearchQuery safe = query == null ? SearchQuery.of("") : query;
        if (!safe.isSearchable()) {
            return CompletableFuture.completedFuture(
                    new SearchResponse(safe.requestId(), List.of(), List.of(), false));
        }
        if (!accessContext.isAuthenticated()) {
            return CompletableFuture.completedFuture(
                    new SearchResponse(safe.requestId(), List.of(), List.of("Not authenticated"), false));
        }

        return CompletableFuture.supplyAsync(() -> execute(safe), executor);
    }

    /** Package-visible for deterministic unit tests. */
    SearchResponse executeSync(SearchQuery query) {
        return execute(query == null ? SearchQuery.of("") : query);
    }

    private SearchResponse execute(SearchQuery query) {
        Map<SearchEntityType, List<SearchResult>> byType = new LinkedHashMap<>();
        List<String> errors = new ArrayList<>();

        for (SearchProvider provider : providers) {
            if (provider == null || !provider.supports(query.scope())) {
                continue;
            }
            try {
                List<SearchResult> hits = provider.search(query, accessContext);
                if (hits == null) {
                    continue;
                }
                for (SearchResult hit : hits) {
                    if (hit == null || !accessContext.canAccess(hit.getType())) {
                        continue;
                    }
                    if (!query.scope().includes(hit.getType())) {
                        continue;
                    }
                    double score = hit.getScore() > 0
                            ? hit.getScore()
                            : SearchRanking.score(query.text(), hit.getTitle(), hit.getSubtitle(), hit.getDescription());
                    if (score <= 0) {
                        continue;
                    }
                    byType.computeIfAbsent(hit.getType(), t -> new ArrayList<>()).add(hit.withScore(score));
                }
            } catch (RuntimeException ex) {
                errors.add("Unable to load " + provider.id());
            }
        }

        List<SearchResultGroup> groups = new ArrayList<>();
        for (Map.Entry<SearchEntityType, List<SearchResult>> entry : byType.entrySet()) {
            List<SearchResult> ranked = entry.getValue().stream()
                    .sorted(Comparator.comparingDouble(SearchResult::getScore).reversed()
                            .thenComparing(SearchResult::getTitle, String.CASE_INSENSITIVE_ORDER))
                    .limit(perGroupLimit)
                    .toList();
            if (!ranked.isEmpty()) {
                groups.add(new SearchResultGroup(
                        entry.getKey(),
                        GROUP_LABELS.getOrDefault(entry.getKey(), entry.getKey().name()),
                        ranked));
            }
        }

        groups.sort(Comparator.comparing(g -> GROUP_LABELS.getOrDefault(g.type(), g.type().name())));
        int total = 0;
        List<SearchResultGroup> limited = new ArrayList<>();
        for (SearchResultGroup group : groups) {
            if (total >= query.limit()) {
                break;
            }
            int remaining = query.limit() - total;
            List<SearchResult> slice = group.results().size() <= remaining
                    ? group.results()
                    : group.results().subList(0, remaining);
            limited.add(new SearchResultGroup(group.type(), group.label(), slice));
            total += slice.size();
        }

        return new SearchResponse(query.requestId(), limited, errors, !errors.isEmpty());
    }
}
