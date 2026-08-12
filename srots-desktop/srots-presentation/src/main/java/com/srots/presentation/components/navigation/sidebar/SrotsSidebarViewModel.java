package com.srots.presentation.components.navigation.sidebar;

import com.srots.presentation.navigation.model.NavigationGroup;
import com.srots.presentation.navigation.model.NavigationItem;
import com.srots.presentation.navigation.model.NavigationRouteId;
import com.srots.presentation.navigation.model.NavigationVisibility;
import com.srots.presentation.navigation.registry.NavigationRegistry;
import com.srots.presentation.navigation.service.NavigationService;
import com.srots.presentation.navigation.service.NavigationVisibilityService;
import com.srots.presentation.navigation.service.UserAccessContext;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Presentation state for {@link SrotsSidebar}. No database / REST / business logic.
 */
public final class SrotsSidebarViewModel {

    private final BooleanProperty collapsed = new SimpleBooleanProperty(false);
    private final ObjectProperty<NavigationRouteId> currentRoute = new SimpleObjectProperty<>();
    private final ObjectProperty<NavigationRouteId> activeSidebarRoute = new SimpleObjectProperty<>();
    private final IntegerProperty revision = new SimpleIntegerProperty(0);
    private final ObservableList<GroupPresentation> groups = FXCollections.observableArrayList();
    private final Map<NavigationGroup, Boolean> groupExpanded = new EnumMap<>(NavigationGroup.class);

    private NavigationRegistry registry;
    private NavigationService navigationService;
    private NavigationVisibilityService visibilityService;
    private UserAccessContext accessContext;
    private Consumer<NavigationRouteId> navigateHandler = route -> {
    };

    public SrotsSidebarViewModel() {
        for (NavigationGroup group : NavigationGroup.values()) {
            groupExpanded.put(group, true);
        }
    }

    public void bind(
            NavigationRegistry registry,
            NavigationService navigationService,
            NavigationVisibilityService visibilityService,
            UserAccessContext accessContext) {
        this.registry = Objects.requireNonNull(registry);
        this.navigationService = Objects.requireNonNull(navigationService);
        this.visibilityService = Objects.requireNonNull(visibilityService);
        this.accessContext = Objects.requireNonNull(accessContext);
        this.navigateHandler = navigationService::navigate;

        navigationService.currentRouteProperty().addListener((obs, o, route) -> applyRoute(route));
        applyRoute(navigationService.currentRoute());
        refresh();
    }

    public void refresh() {
        groups.clear();
        if (registry == null || visibilityService == null || accessContext == null) {
            revision.set(revision.get() + 1);
            return;
        }

        List<NavigationItem> visible = visibilityService.filterVisible(registry.allItems(), accessContext);
        Map<NavigationGroup, List<ItemPresentation>> byGroup = new LinkedHashMap<>();

        for (NavigationItem item : visible) {
            if (item.getVisibility() == NavigationVisibility.HIDDEN) {
                continue;
            }
            if (item.getParentRoute() != null) {
                NavigationItem parent = registry.find(item.getParentRoute()).orElse(null);
                if (parent != null && parent.getParentRoute() != null) {
                    continue; // skip deep grandchildren in sidebar
                }
            }
            // Settings is rendered in the footer
            if (item.getRoute() == NavigationRouteId.SETTINGS) {
                continue;
            }
            byGroup.computeIfAbsent(item.getGroup(), g -> new ArrayList<>())
                    .add(toPresentation(item));
        }

        Set<NavigationGroup> standalone = EnumSet.of(NavigationGroup.OVERVIEW, NavigationGroup.WORKSPACE);
        for (NavigationGroup group : NavigationGroup.values()) {
            List<ItemPresentation> items = byGroup.get(group);
            if (items == null || items.isEmpty()) {
                continue;
            }
            boolean isStandalone = standalone.contains(group);
            groups.add(new GroupPresentation(group, group.displayTitle(), isStandalone, List.copyOf(items)));
            groupExpanded.putIfAbsent(group, true);
        }

        expandGroupForRoute(currentRoute.get());
        revision.set(revision.get() + 1);
    }

    public void requestNavigate(NavigationRouteId route) {
        if (route != null) {
            navigateHandler.accept(route);
        }
    }

    public BooleanProperty collapsedProperty() {
        return collapsed;
    }

    public boolean isCollapsed() {
        return collapsed.get();
    }

    public void setCollapsed(boolean value) {
        collapsed.set(value);
    }

    public ObjectProperty<NavigationRouteId> currentRouteProperty() {
        return currentRoute;
    }

    public NavigationRouteId getCurrentRoute() {
        return currentRoute.get();
    }

    public ObjectProperty<NavigationRouteId> activeSidebarRouteProperty() {
        return activeSidebarRoute;
    }

    public NavigationRouteId getActiveSidebarRoute() {
        return activeSidebarRoute.get();
    }

    public IntegerProperty revisionProperty() {
        return revision;
    }

    public ObservableList<GroupPresentation> getGroups() {
        return groups;
    }

    public boolean isGroupExpanded(NavigationGroup group) {
        return groupExpanded.getOrDefault(group, true);
    }

    public void setGroupExpanded(NavigationGroup group, boolean expanded) {
        if (group != null) {
            groupExpanded.put(group, expanded);
        }
    }

    private void applyRoute(NavigationRouteId route) {
        NavigationRouteId sidebarRoute = route;
        if (registry != null && route != null) {
            sidebarRoute = registry.findSidebarSelection(route)
                    .map(NavigationItem::getRoute)
                    .orElse(route);
            expandGroupForRoute(route);
        }
        // Update sidebar highlight before currentRoute so UI listeners see the new selection.
        activeSidebarRoute.set(sidebarRoute);
        currentRoute.set(route);
    }

    private void expandGroupForRoute(NavigationRouteId route) {
        if (registry == null || route == null) {
            return;
        }
        registry.find(route).ifPresent(item -> groupExpanded.put(item.getGroup(), true));
        registry.findSidebarSelection(route).ifPresent(item -> groupExpanded.put(item.getGroup(), true));
    }

    private static ItemPresentation toPresentation(NavigationItem item) {
        boolean enabled = item.isEnabled() && item.getVisibility() != NavigationVisibility.DISABLED;
        String reason = enabled ? null : "Coming soon";
        return new ItemPresentation(
                item.getRoute(),
                item.getTitle(),
                item.getIconKey(),
                item.getBadgeCount(),
                enabled,
                reason);
    }

    public record ItemPresentation(
            NavigationRouteId route,
            String title,
            String iconKey,
            Integer badgeCount,
            boolean enabled,
            String disabledReason) {
    }

    public record GroupPresentation(
            NavigationGroup id,
            String title,
            boolean standalone,
            List<ItemPresentation> items) {
    }
}
