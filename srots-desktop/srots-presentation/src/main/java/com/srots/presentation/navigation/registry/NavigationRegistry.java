package com.srots.presentation.navigation.registry;

import com.srots.presentation.navigation.model.NavigationItem;
import com.srots.presentation.navigation.model.NavigationRouteId;
import java.util.List;
import java.util.Optional;

/**
 * Immutable lookup over registered navigation destinations.
 */
public interface NavigationRegistry {

    Optional<NavigationItem> find(NavigationRouteId routeId);

    /** Root items with no parent. */
    List<NavigationItem> rootItems();

    List<NavigationItem> children(NavigationRouteId parent);

    List<NavigationItem> allItems();

    /** Ancestors plus self, ordered root → leaf along the parent chain. */
    List<NavigationItem> breadcrumbPath(NavigationRouteId routeId);

    /**
     * Walks up parents until an item suitable for sidebar highlight
     * (root item, or direct child of a root).
     */
    Optional<NavigationItem> findSidebarSelection(NavigationRouteId routeId);
}
