package com.srots.presentation.search;

import com.srots.application.search.GlobalSearchService;
import com.srots.application.search.SearchQuery;
import com.srots.application.search.SearchResponse;
import com.srots.application.search.SearchResult;
import com.srots.application.search.SearchResultGroup;
import com.srots.presentation.components.overlays.command.SrotsCommandPalette;
import com.srots.presentation.navigation.model.NavigationRouteId;
import com.srots.presentation.navigation.service.NavigationService;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/**
 * Presentation state for Global Search. No DB/REST/provider logic.
 */
public final class SrotsGlobalSearchViewModel {

    public static final int DEBOUNCE_MS = 200;

    private final StringProperty queryText = new SimpleStringProperty("");
    private final ObjectProperty<SrotsSearchFilter> filter =
            new SimpleObjectProperty<>(SrotsSearchFilter.ALL);
    private final ObjectProperty<SrotsSearchMode> mode =
            new SimpleObjectProperty<>(SrotsSearchMode.SEARCH);
    private final ObjectProperty<SrotsGlobalSearchState> state =
            new SimpleObjectProperty<>(SrotsGlobalSearchState.CLOSED);
    private final BooleanProperty open = new SimpleBooleanProperty(false);
    private final BooleanProperty searching = new SimpleBooleanProperty(false);
    private final StringProperty statusMessage = new SimpleStringProperty("");
    private final IntegerProperty selectedIndex = new SimpleIntegerProperty(-1);
    private final ObservableList<SrotsSearchListEntry> entries = FXCollections.observableArrayList();

    private final PauseTransition debounce = new PauseTransition(Duration.millis(DEBOUNCE_MS));
    private final AtomicReference<String> latestRequestId = new AtomicReference<>("");
    private final SearchHistoryService historyService = new SearchHistoryService();

    private GlobalSearchService searchService;
    private NavigationService navigationService;
    private Supplier<List<SrotsCommandPalette.CommandItem>> commandSupplier = List::of;

    public SrotsGlobalSearchViewModel() {
        debounce.setOnFinished(e -> runSearch(queryText.get()));
        queryText.addListener((obs, o, n) -> {
            if (!isOpen()) {
                return;
            }
            updateModeFromQuery(n);
            debounce.stop();
            debounce.playFromStart();
        });
        filter.addListener((obs, o, n) -> {
            if (isOpen()) {
                debounce.stop();
                runSearch(queryText.get());
            }
        });
    }

    public void bind(
            GlobalSearchService searchService,
            NavigationService navigationService,
            Supplier<List<SrotsCommandPalette.CommandItem>> commandSupplier) {
        this.searchService = Objects.requireNonNull(searchService, "searchService");
        this.navigationService = Objects.requireNonNull(navigationService, "navigationService");
        this.commandSupplier = commandSupplier == null ? List::of : commandSupplier;
    }

    public StringProperty queryTextProperty() {
        return queryText;
    }

    public ObjectProperty<SrotsSearchFilter> filterProperty() {
        return filter;
    }

    public ReadOnlyObjectProperty<SrotsSearchMode> modeProperty() {
        return mode;
    }

    public ReadOnlyObjectProperty<SrotsGlobalSearchState> stateProperty() {
        return state;
    }

    public BooleanProperty openProperty() {
        return open;
    }

    public boolean isOpen() {
        return open.get();
    }

    public ReadOnlyBooleanProperty searchingProperty() {
        return searching;
    }

    public ReadOnlyStringProperty statusMessageProperty() {
        return statusMessage;
    }

    public IntegerProperty selectedIndexProperty() {
        return selectedIndex;
    }

    public ObservableList<SrotsSearchListEntry> getEntries() {
        return entries;
    }

    public SearchHistoryService getHistoryService() {
        return historyService;
    }

    public void open() {
        open.set(true);
        state.set(SrotsGlobalSearchState.IDLE);
        showIdleContent();
    }

    public void close() {
        debounce.stop();
        open.set(false);
        state.set(SrotsGlobalSearchState.CLOSED);
        selectedIndex.set(-1);
    }

    public void toggle() {
        if (isOpen()) {
            close();
        } else {
            open();
        }
    }

    public void setFilter(SrotsSearchFilter value) {
        filter.set(value == null ? SrotsSearchFilter.ALL : value);
    }

    public void clearQuery() {
        queryText.set("");
        showIdleContent();
    }

    public void moveSelection(int delta) {
        if (entries.isEmpty()) {
            selectedIndex.set(-1);
            return;
        }
        int current = selectedIndex.get();
        int next = current;
        int guard = entries.size() + 2;
        while (guard-- > 0) {
            next = Math.floorMod(next + delta, entries.size());
            if (entries.get(next).isSelectable()) {
                selectedIndex.set(next);
                return;
            }
            if (current < 0) {
                delta = 1;
            }
        }
    }

    public void activateSelected() {
        int index = selectedIndex.get();
        if (index < 0 || index >= entries.size()) {
            activateFirstSelectable();
            return;
        }
        activate(entries.get(index));
    }

    public void activate(SrotsSearchListEntry entry) {
        if (entry == null || !entry.isSelectable()) {
            return;
        }
        if (entry.getKind() == SrotsSearchListEntry.Kind.RECENT) {
            queryText.set(entry.getLabel());
            runSearch(entry.getLabel());
            return;
        }
        if (entry.getKind() == SrotsSearchListEntry.Kind.COMMAND) {
            close();
            if (entry.getCommandAction() != null) {
                entry.getCommandAction().run();
            }
            return;
        }
        SearchResult result = entry.getResult();
        if (result == null) {
            return;
        }
        historyService.record(queryText.get());
        close();
        navigate(result);
    }

    void applyResponseForTest(String requestId, SearchResponse response) {
        applyResponse(requestId, response, null);
    }

    void setLatestRequestIdForTest(String requestId) {
        latestRequestId.set(requestId == null ? "" : requestId);
    }

    private void updateModeFromQuery(String text) {
        String value = text == null ? "" : text.trim();
        if (value.startsWith(">")) {
            mode.set(SrotsSearchMode.COMMAND);
            state.set(SrotsGlobalSearchState.COMMAND_MODE);
        } else {
            mode.set(SrotsSearchMode.SEARCH);
        }
    }

    private void runSearch(String raw) {
        if (!isOpen()) {
            return;
        }
        String text = raw == null ? "" : raw.trim();
        if (mode.get() == SrotsSearchMode.COMMAND || text.startsWith(">")) {
            runCommandSearch(text.startsWith(">") ? text.substring(1).trim() : text);
            return;
        }
        if (text.length() < SearchQuery.MIN_TEXT_LENGTH) {
            searching.set(false);
            showIdleContent();
            state.set(SrotsGlobalSearchState.IDLE);
            return;
        }
        if (searchService == null) {
            searching.set(false);
            statusMessage.set("Search is temporarily unavailable.");
            state.set(SrotsGlobalSearchState.ERROR);
            entries.setAll(SrotsSearchListEntry.hint("Search is temporarily unavailable."));
            return;
        }

        String requestId = UUID.randomUUID().toString();
        latestRequestId.set(requestId);
        searching.set(true);
        state.set(SrotsGlobalSearchState.SEARCHING);
        statusMessage.set("Searching...");

        SearchQuery query = SearchQuery.builder()
                .requestId(requestId)
                .text(text)
                .scope(filter.get().scope())
                .build();

        searchService.search(query).whenComplete((response, error) -> Platform.runLater(() ->
                applyResponse(requestId, response, error)));
    }

    private void applyResponse(String requestId, SearchResponse response, Throwable error) {
        if (!Objects.equals(latestRequestId.get(), requestId)) {
            return;
        }
        searching.set(false);
        if (error != null || response == null) {
            statusMessage.set("Search is temporarily unavailable.");
            state.set(SrotsGlobalSearchState.ERROR);
            entries.setAll(SrotsSearchListEntry.hint("Search is temporarily unavailable."));
            selectedIndex.set(-1);
            return;
        }

        List<SrotsSearchListEntry> next = new ArrayList<>();
        for (SearchResultGroup group : response.groups()) {
            next.add(SrotsSearchListEntry.group(group));
            for (SearchResult result : group.results()) {
                next.add(SrotsSearchListEntry.result(result));
            }
        }
        if (response.partialFailure() && !response.providerErrors().isEmpty()) {
            next.add(SrotsSearchListEntry.hint(String.join(" · ", response.providerErrors())));
        }

        if (next.stream().noneMatch(e -> e.getKind() == SrotsSearchListEntry.Kind.RESULT)) {
            statusMessage.set("No results found for \"" + queryText.get() + "\"");
            state.set(SrotsGlobalSearchState.NO_RESULTS);
            entries.setAll(
                    SrotsSearchListEntry.hint("No results found"),
                    SrotsSearchListEntry.hint("Try a different keyword."));
            selectedIndex.set(-1);
            return;
        }

        entries.setAll(next);
        statusMessage.set("");
        state.set(SrotsGlobalSearchState.RESULTS);
        selectFirstSelectable();
    }

    private void runCommandSearch(String text) {
        searching.set(false);
        state.set(SrotsGlobalSearchState.COMMAND_MODE);
        String q = text == null ? "" : text.toLowerCase(Locale.ROOT);
        List<SrotsSearchListEntry> next = new ArrayList<>();
        next.add(SrotsSearchListEntry.group("Commands"));
        List<SrotsCommandPalette.CommandItem> commands = commandSupplier.get();
        for (SrotsCommandPalette.CommandItem command : commands) {
            if (command == null) {
                continue;
            }
            String label = command.label() == null ? "" : command.label();
            if (!q.isEmpty()
                    && !label.toLowerCase(Locale.ROOT).contains(q)
                    && (command.id() == null || !command.id().toLowerCase(Locale.ROOT).contains(q))) {
                continue;
            }
            next.add(SrotsSearchListEntry.command(label, command.action()));
        }
        if (next.size() == 1) {
            next.add(SrotsSearchListEntry.hint("No matching commands."));
            selectedIndex.set(-1);
        } else {
            selectFirstSelectable(next);
        }
        entries.setAll(next);
        statusMessage.set("");
    }

    private void showIdleContent() {
        List<SrotsSearchListEntry> next = new ArrayList<>();
        List<String> recent = historyService.recent();
        if (!recent.isEmpty()) {
            next.add(SrotsSearchListEntry.group("Recent searches"));
            for (String item : recent) {
                next.add(SrotsSearchListEntry.recent(item));
            }
        }
        next.add(SrotsSearchListEntry.group("Suggested commands"));
        List<SrotsCommandPalette.CommandItem> commands = commandSupplier.get();
        int count = 0;
        for (SrotsCommandPalette.CommandItem command : commands) {
            if (command == null || count >= 6) {
                break;
            }
            next.add(SrotsSearchListEntry.command(command.label(), command.action()));
            count++;
        }
        if (count == 0) {
            next.add(SrotsSearchListEntry.hint("Type at least 2 characters to search."));
        }
        entries.setAll(next);
        selectFirstSelectable();
        statusMessage.set("");
    }

    private void selectFirstSelectable() {
        selectFirstSelectable(entries);
    }

    private void selectFirstSelectable(List<SrotsSearchListEntry> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isSelectable()) {
                selectedIndex.set(i);
                return;
            }
        }
        selectedIndex.set(-1);
    }

    private void activateFirstSelectable() {
        for (SrotsSearchListEntry entry : entries) {
            if (entry.isSelectable()) {
                activate(entry);
                return;
            }
        }
    }

    private void navigate(SearchResult result) {
        if (navigationService == null || result == null) {
            return;
        }
        String routeId = result.getRouteId();
        if (routeId == null || routeId.isBlank()) {
            return;
        }
        try {
            NavigationRouteId route = NavigationRouteId.valueOf(routeId);
            navigationService.navigate(route);
        } catch (IllegalArgumentException ignored) {
            // Unknown route ids are ignored; destination authorization remains in navigation guards.
        }
    }
}
