package com.srots.application.search.mock;

import com.srots.application.search.EntityReference;
import com.srots.application.search.SearchAccessContext;
import com.srots.application.search.SearchEntityType;
import com.srots.application.search.SearchProvider;
import com.srots.application.search.SearchQuery;
import com.srots.application.search.SearchRanking;
import com.srots.application.search.SearchResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * In-memory catalog provider for development. Same contract as production providers.
 */
public final class MockCatalogSearchProvider implements SearchProvider {

    private final String providerId;
    private final SearchEntityType type;
    private final List<SearchResult> catalog;
    private final boolean fail;

    public MockCatalogSearchProvider(String providerId, SearchEntityType type, List<SearchResult> catalog) {
        this(providerId, type, catalog, false);
    }

    public MockCatalogSearchProvider(
            String providerId,
            SearchEntityType type,
            List<SearchResult> catalog,
            boolean fail) {
        this.providerId = Objects.requireNonNull(providerId);
        this.type = Objects.requireNonNull(type);
        this.catalog = List.copyOf(catalog == null ? List.of() : catalog);
        this.fail = fail;
    }

    @Override
    public String id() {
        return providerId;
    }

    @Override
    public Set<SearchEntityType> supportedTypes() {
        return Set.of(type);
    }

    @Override
    public List<SearchResult> search(SearchQuery query, SearchAccessContext access) {
        if (fail) {
            throw new IllegalStateException("Simulated provider failure: " + providerId);
        }
        if (query == null || !query.isSearchable()) {
            return List.of();
        }
        if (access != null && !access.canAccess(type)) {
            return List.of();
        }
        List<SearchResult> matches = new ArrayList<>();
        for (SearchResult item : catalog) {
            double score = SearchRanking.score(query.text(), item.getTitle(), item.getSubtitle(), item.getDescription());
            if (score > 0) {
                matches.add(item.withScore(score));
            }
        }
        return matches;
    }

    public static SearchResult item(
            String id,
            SearchEntityType type,
            String title,
            String subtitle,
            String routeId) {
        return new SearchResult(
                id,
                type,
                title,
                subtitle,
                "",
                0,
                routeId,
                new EntityReference(type, id));
    }
}
