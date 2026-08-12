package com.srots.presentation.navigation.resolver;

import com.srots.presentation.navigation.model.NavigationRouteId;

/**
 * Lightweight metadata for a navigable view (no JavaFX nodes).
 */
public record ViewDefinition(NavigationRouteId route, String title, String description) {
}
