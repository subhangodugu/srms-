package com.srots.presentation.navigation.model;

import java.util.Objects;

/**
 * Snapshot of a navigation request or current destination.
 */
public record NavigationContext(
        NavigationRouteId route,
        RouteParameters parameters,
        NavigationRouteId sourceRoute,
        NavigationRouteId returnRoute) {

    public NavigationContext {
        Objects.requireNonNull(route, "route");
        parameters = parameters == null ? RouteParameters.empty() : parameters;
    }

    public static NavigationContext of(NavigationRouteId route) {
        return new NavigationContext(route, RouteParameters.empty(), null, null);
    }

    public static NavigationContext of(NavigationRouteId route, RouteParameters parameters) {
        return new NavigationContext(route, parameters, null, null);
    }
}
