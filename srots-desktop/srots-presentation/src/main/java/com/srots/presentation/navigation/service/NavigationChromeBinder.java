package com.srots.presentation.navigation.service;

import com.srots.presentation.components.navigation.breadcrumb.SrotsBreadcrumb;
import com.srots.presentation.components.navigation.sidebar.SrotsNavigationGroup;
import com.srots.presentation.components.navigation.sidebar.SrotsNavigationItem;
import com.srots.presentation.components.navigation.sidebar.SrotsSidebar;
import com.srots.presentation.components.navigation.sidebar.SrotsSidebarViewModel;
import com.srots.presentation.navigation.model.NavigationGroup;
import com.srots.presentation.navigation.model.NavigationItem;
import com.srots.presentation.navigation.model.NavigationRouteId;
import com.srots.presentation.navigation.model.NavigationVisibility;
import com.srots.presentation.navigation.registry.NavigationRegistry;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Binds {@link NavigationRegistry} + {@link NavigationService} to sidebar and breadcrumbs.
 */
public final class NavigationChromeBinder {

    private final NavigationService navigationService;
    private final NavigationRegistry registry;
    private final NavigationVisibilityService visibilityService;
    private final UserAccessContext accessContext;
    private final Map<NavigationRouteId, SrotsNavigationItem> sidebarButtons = new LinkedHashMap<>();
    private SrotsSidebarViewModel sidebarViewModel;

    public NavigationChromeBinder(
            NavigationService navigationService,
            NavigationRegistry registry,
            NavigationVisibilityService visibilityService,
            UserAccessContext accessContext) {
        this.navigationService = Objects.requireNonNull(navigationService);
        this.registry = Objects.requireNonNull(registry);
        this.visibilityService = Objects.requireNonNull(visibilityService);
        this.accessContext = Objects.requireNonNull(accessContext);
    }

    /**
     * Preferred integration: full {@link SrotsSidebar} with ViewModel.
     */
    public SrotsSidebarViewModel bindSidebar(SrotsSidebar sidebar) {
        Objects.requireNonNull(sidebar, "sidebar");
        sidebarViewModel = new SrotsSidebarViewModel();
        sidebarViewModel.bind(registry, navigationService, visibilityService, accessContext);
        sidebar.setViewModel(sidebarViewModel);
        return sidebarViewModel;
    }

    /**
     * Legacy container binding used by older tests — still data-driven via registry.
     */
    public void bindSidebar(VBox navContainer) {
        Objects.requireNonNull(navContainer, "navContainer");
        navContainer.getChildren().clear();
        sidebarButtons.clear();

        List<NavigationItem> visible = visibilityService.filterVisible(registry.allItems(), accessContext);
        Map<NavigationGroup, List<NavigationItem>> byGroup = new EnumMap<>(NavigationGroup.class);
        for (NavigationItem item : visible) {
            if (item.getVisibility() == NavigationVisibility.HIDDEN) {
                continue;
            }
            if (item.getParentRoute() != null) {
                NavigationItem parent = registry.find(item.getParentRoute()).orElse(null);
                if (parent != null && parent.getParentRoute() != null) {
                    continue;
                }
            }
            byGroup.computeIfAbsent(item.getGroup(), g -> new ArrayList<>()).add(item);
        }

        for (NavigationGroup group : NavigationGroup.values()) {
            List<NavigationItem> items = byGroup.get(group);
            if (items == null || items.isEmpty()) {
                continue;
            }
            SrotsNavigationGroup groupNode = new SrotsNavigationGroup(group.displayTitle());
            for (NavigationItem item : items) {
                SrotsNavigationItem button = new SrotsNavigationItem(item.getTitle());
                button.setIconKey(item.getIconKey());
                button.setBadgeCount(item.getBadgeCount());
                button.setDisable(!item.isEnabled() || item.getVisibility() == NavigationVisibility.DISABLED);
                button.setOnAction(e -> navigationService.navigate(item.getRoute()));
                sidebarButtons.put(item.getRoute(), button);
                groupNode.addItem(button);
            }
            navContainer.getChildren().add(groupNode);
        }

        navigationService.currentRouteProperty().addListener((obs, oldRoute, newRoute) -> syncSelection(newRoute));
        syncSelection(navigationService.currentRoute());
    }

    public void bindBreadcrumb(SrotsBreadcrumb breadcrumb) {
        Objects.requireNonNull(breadcrumb, "breadcrumb");
        navigationService.currentRouteProperty().addListener((obs, o, route) -> updateBreadcrumb(breadcrumb, route));
        updateBreadcrumb(breadcrumb, navigationService.currentRoute());
    }

    public SrotsSidebarViewModel getSidebarViewModel() {
        return sidebarViewModel;
    }

    public void refreshSidebar() {
        if (sidebarViewModel != null) {
            sidebarViewModel.refresh();
        }
    }

    private void syncSelection(NavigationRouteId route) {
        NavigationRouteId selected = registry.findSidebarSelection(route)
                .map(NavigationItem::getRoute)
                .orElse(route);
        for (Map.Entry<NavigationRouteId, SrotsNavigationItem> entry : sidebarButtons.entrySet()) {
            entry.getValue().setActive(entry.getKey() == selected);
        }
    }

    private void updateBreadcrumb(SrotsBreadcrumb breadcrumb, NavigationRouteId route) {
        if (route == null) {
            breadcrumb.setItems(List.of());
            return;
        }
        List<NavigationItem> path = registry.breadcrumbPath(route);
        List<SrotsBreadcrumb.Crumb> crumbs = new ArrayList<>();
        for (int i = 0; i < path.size(); i++) {
            NavigationItem item = path.get(i);
            boolean last = i == path.size() - 1;
            NavigationRouteId target = item.getRoute();
            crumbs.add(new SrotsBreadcrumb.Crumb(
                    item.getTitle(),
                    last ? null : () -> navigationService.navigate(target)));
        }
        if (crumbs.isEmpty()) {
            crumbs.add(new SrotsBreadcrumb.Crumb(route.name(), null));
        }
        breadcrumb.setItems(crumbs);
    }
}
