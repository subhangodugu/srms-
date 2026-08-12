package com.srots.presentation.navigation.state;

import com.srots.presentation.navigation.model.NavigationRouteId;
import com.srots.presentation.navigation.model.RouteParameters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Desktop navigation history. Stores route + parameters only (never JavaFX nodes).
 */
public final class NavigationHistory {

    private final int maxSize;
    private final List<NavigationHistoryEntry> backStack = new ArrayList<>();
    private final List<NavigationHistoryEntry> forwardStack = new ArrayList<>();

    public NavigationHistory() {
        this(100);
    }

    public NavigationHistory(int maxSize) {
        if (maxSize < 1) {
            throw new IllegalArgumentException("maxSize must be >= 1");
        }
        this.maxSize = maxSize;
    }

    public void push(NavigationRouteId route, RouteParameters parameters) {
        Objects.requireNonNull(route, "route");
        backStack.add(new NavigationHistoryEntry(
                route,
                parameters == null ? RouteParameters.empty() : parameters,
                System.currentTimeMillis()));
        trim();
        forwardStack.clear();
    }

    public boolean canGoBack() {
        return !backStack.isEmpty();
    }

    public boolean canGoForward() {
        return !forwardStack.isEmpty();
    }

    public NavigationHistoryEntry popBack(NavigationHistoryEntry current) {
        if (backStack.isEmpty()) {
            return null;
        }
        NavigationHistoryEntry previous = backStack.remove(backStack.size() - 1);
        if (current != null) {
            forwardStack.add(current);
        }
        return previous;
    }

    public NavigationHistoryEntry popForward(NavigationHistoryEntry current) {
        if (forwardStack.isEmpty()) {
            return null;
        }
        NavigationHistoryEntry next = forwardStack.remove(forwardStack.size() - 1);
        if (current != null) {
            backStack.add(current);
        }
        return next;
    }

    public void clear() {
        backStack.clear();
        forwardStack.clear();
    }

    public List<NavigationHistoryEntry> backEntries() {
        return Collections.unmodifiableList(backStack);
    }

    public List<NavigationHistoryEntry> forwardEntries() {
        return Collections.unmodifiableList(forwardStack);
    }

    private void trim() {
        while (backStack.size() > maxSize) {
            backStack.remove(0);
        }
    }
}
