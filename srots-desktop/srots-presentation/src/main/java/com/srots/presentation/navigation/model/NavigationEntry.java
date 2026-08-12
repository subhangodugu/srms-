package com.srots.presentation.navigation.model;

import java.util.Objects;

/**
 * History-friendly navigation entry. Stores route state, never JavaFX nodes.
 */
public record NavigationEntry(
        NavigationRouteId route,
        String label,
        RouteParameters parameters,
        long timestampMillis,
        NavigationSource source) {

    public NavigationEntry {
        Objects.requireNonNull(route, "route");
        label = label == null ? route.name() : label;
        parameters = parameters == null ? RouteParameters.empty() : parameters;
        source = source == null ? NavigationSource.UNKNOWN : source;
    }

    public static NavigationEntry of(NavigationRouteId route, RouteParameters parameters, NavigationSource source) {
        return new NavigationEntry(route, route.name(), parameters, System.currentTimeMillis(), source);
    }
}
