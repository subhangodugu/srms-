package com.srots.presentation.navigation.event;

import com.srots.presentation.navigation.model.NavigationRouteId;
import com.srots.presentation.navigation.model.RouteParameters;
import java.util.Objects;

/**
 * Immutable navigation lifecycle event for presentation listeners.
 */
public record NavigationEvent(
        NavigationEventType type,
        NavigationRouteId route,
        RouteParameters parameters,
        String message) {

    public NavigationEvent {
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(route, "route");
        parameters = parameters == null ? RouteParameters.empty() : parameters;
    }
}
