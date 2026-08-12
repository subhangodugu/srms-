package com.srots.presentation.navigation.model;

import java.util.Objects;

/**
 * Typed navigation request. Prefer this over string routes.
 */
public record NavigationRequest(
        NavigationRouteId route,
        RouteParameters parameters,
        NavigationSource source,
        boolean replaceHistory) {

    public NavigationRequest {
        Objects.requireNonNull(route, "route");
        parameters = parameters == null ? RouteParameters.empty() : parameters;
        source = source == null ? NavigationSource.UNKNOWN : source;
    }

    public static NavigationRequest of(NavigationRouteId route) {
        return new NavigationRequest(route, RouteParameters.empty(), NavigationSource.SYSTEM, false);
    }

    public static NavigationRequest of(NavigationRouteId route, NavigationSource source) {
        return new NavigationRequest(route, RouteParameters.empty(), source, false);
    }

    public static NavigationRequest of(NavigationRouteId route, RouteParameters parameters, NavigationSource source) {
        return new NavigationRequest(route, parameters, source, false);
    }

    public NavigationContext toContext() {
        return NavigationContext.of(route, parameters);
    }
}
