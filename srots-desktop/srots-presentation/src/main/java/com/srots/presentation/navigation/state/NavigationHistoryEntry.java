package com.srots.presentation.navigation.state;

import com.srots.presentation.navigation.model.NavigationRouteId;
import com.srots.presentation.navigation.model.RouteParameters;
import java.util.Objects;

/**
 * One entry in the presentation navigation history stack.
 */
public record NavigationHistoryEntry(
        NavigationRouteId route,
        RouteParameters parameters,
        long timestampMillis) {

    public NavigationHistoryEntry {
        Objects.requireNonNull(route, "route");
        parameters = parameters == null ? RouteParameters.empty() : parameters;
    }
}
