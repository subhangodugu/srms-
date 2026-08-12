package com.srots.presentation.navigation.resolver;

import com.srots.presentation.navigation.model.NavigationRouteId;

/**
 * Resolves a route to view metadata (title / description).
 */
public interface ViewResolver {

    ViewDefinition resolve(NavigationRouteId route);
}
