package com.srots.presentation.navigation.registry;

import com.srots.presentation.navigation.model.NavigationItem;
import com.srots.presentation.navigation.model.NavigationRouteId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Immutable {@link NavigationRegistry} indexed by route id and sorted by order.
 */
public final class DefaultNavigationRegistry implements NavigationRegistry {

    private final Map<NavigationRouteId, NavigationItem> byRoute;
    private final List<NavigationItem> allItems;

    public DefaultNavigationRegistry(Collection<NavigationItem> items) {
        Objects.requireNonNull(items, "items");
        List<NavigationItem> sorted = items.stream()
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingInt(NavigationItem::getOrder)
                        .thenComparing(item -> item.getRoute().name()))
                .toList();

        Map<NavigationRouteId, NavigationItem> index = new LinkedHashMap<>();
        for (NavigationItem item : sorted) {
            NavigationRouteId route = item.getRoute();
            if (index.containsKey(route)) {
                throw new IllegalArgumentException("Duplicate navigation route: " + route);
            }
            index.put(route, item);
        }
        this.byRoute = Map.copyOf(index);
        this.allItems = List.copyOf(sorted);
    }

    public DefaultNavigationRegistry(List<? extends FeatureNavigationProvider> providers) {
        this(flatten(providers));
    }

    public DefaultNavigationRegistry(FeatureNavigationProvider... providers) {
        this(providers == null ? List.of() : List.of(providers));
    }

    private static List<NavigationItem> flatten(Collection<? extends FeatureNavigationProvider> providers) {
        if (providers == null || providers.isEmpty()) {
            return List.of();
        }
        List<NavigationItem> items = new ArrayList<>();
        for (FeatureNavigationProvider provider : providers) {
            if (provider == null) {
                continue;
            }
            Collection<NavigationItem> contributed = provider.getNavigationItems();
            if (contributed != null) {
                items.addAll(contributed);
            }
        }
        return items;
    }

    @Override
    public Optional<NavigationItem> find(NavigationRouteId routeId) {
        if (routeId == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(byRoute.get(routeId));
    }

    @Override
    public List<NavigationItem> rootItems() {
        return allItems.stream()
                .filter(item -> item.getParentRoute() == null)
                .toList();
    }

    @Override
    public List<NavigationItem> children(NavigationRouteId parent) {
        if (parent == null) {
            return List.of();
        }
        return allItems.stream()
                .filter(item -> parent.equals(item.getParentRoute()))
                .toList();
    }

    @Override
    public List<NavigationItem> allItems() {
        return allItems;
    }

    @Override
    public List<NavigationItem> breadcrumbPath(NavigationRouteId routeId) {
        LinkedList<NavigationItem> path = new LinkedList<>();
        Optional<NavigationItem> current = find(routeId);
        while (current.isPresent()) {
            NavigationItem item = current.get();
            path.addFirst(item);
            NavigationRouteId parent = item.getParentRoute();
            if (parent == null) {
                break;
            }
            current = find(parent);
        }
        return List.copyOf(path);
    }

    @Override
    public Optional<NavigationItem> findSidebarSelection(NavigationRouteId routeId) {
        Optional<NavigationItem> found = find(routeId);
        if (found.isEmpty()) {
            return Optional.empty();
        }

        NavigationItem item = found.get();
        while (true) {
            NavigationRouteId parentId = item.getParentRoute();
            if (parentId == null) {
                return Optional.of(item);
            }
            Optional<NavigationItem> parent = find(parentId);
            if (parent.isEmpty()) {
                return Optional.of(item);
            }
            // Parent is a root → highlight this child in the sidebar.
            if (parent.get().getParentRoute() == null) {
                return Optional.of(item);
            }
            item = parent.get();
        }
    }
}
