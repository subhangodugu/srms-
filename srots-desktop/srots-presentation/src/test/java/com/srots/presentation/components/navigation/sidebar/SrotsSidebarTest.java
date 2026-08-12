package com.srots.presentation.components.navigation.sidebar;

import com.srots.presentation.components.support.JavaFxTestSupport;
import com.srots.presentation.navigation.NavigationModule;
import com.srots.presentation.navigation.model.NavigationGroup;
import com.srots.presentation.navigation.model.NavigationItem;
import com.srots.presentation.navigation.model.NavigationRouteId;
import com.srots.presentation.navigation.registry.DefaultNavigationRegistry;
import com.srots.presentation.navigation.registry.FeatureNavigationProvider;
import com.srots.presentation.navigation.resolver.DefaultPlaceholderViewFactory;
import com.srots.presentation.navigation.service.NavigationVisibilityService;
import com.srots.presentation.navigation.service.StaticUserAccessContext;
import com.srots.presentation.navigation.host.SrotsContentHost;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SrotsSidebarTest {

    @BeforeAll
    static void bootFx() throws Exception {
        JavaFxTestSupport.ensureToolkit();
    }

    @Test
    void badgeFormatter_capsLargeCounts() {
        assertEquals("", NavigationBadgeFormatter.format(null));
        assertEquals("", NavigationBadgeFormatter.format(0));
        assertEquals("12", NavigationBadgeFormatter.format(12));
        assertEquals("999+", NavigationBadgeFormatter.format(12_500));
    }

    @Test
    void iconResolver_returnsGlyphForKnownKeys() {
        assertEquals(NavigationIconResolver.glyphFor("overview"), NavigationIconResolver.glyphFor("OVERVIEW"));
        assertFalse(NavigationIconResolver.glyphFor("settings").isBlank());
        assertFalse(NavigationIconResolver.glyphFor("unknown-key").isBlank());
    }

    @Test
    void sidebar_bindsRegistryAndTracksActiveRoute() throws Exception {
        JavaFxTestSupport.runOnFxThread(() -> {
            NavigationModule module = NavigationModule.createDefault();
            SrotsSidebar sidebar = new SrotsSidebar();
            module.chromeBinder().bindSidebar(sidebar);

            assertFalse(sidebar.getViewModel().getGroups().isEmpty());
            assertTrue(sidebar.getStyleClass().contains("srots-sidebar"));

            module.navigationService().navigate(NavigationRouteId.OVERVIEW);
            assertEquals(NavigationRouteId.OVERVIEW, sidebar.getViewModel().getCurrentRoute());

            module.navigationService().navigate(NavigationRouteId.COMPANY_EMPLOYEES);
            assertEquals(NavigationRouteId.COMPANY_EMPLOYEES, sidebar.getViewModel().getCurrentRoute());
            assertTrue(hasActiveItem(sidebar));

            module.navigationService().navigate(NavigationRouteId.COMPTY_RELEASES);
            assertEquals(NavigationRouteId.COMPTY, sidebar.getViewModel().getActiveSidebarRoute());
            assertTrue(hasActiveItem(sidebar));

            module.navigationService().navigate(NavigationRouteId.WORKSPACE_PROJECTS);
            assertEquals(NavigationRouteId.WORKSPACE_PROJECTS, sidebar.getViewModel().getActiveSidebarRoute());
            assertTrue(isRouteActive(sidebar, NavigationRouteId.WORKSPACE_PROJECTS));

            module.navigationService().navigate(NavigationRouteId.WORKSPACE_ISSUES);
            assertEquals(NavigationRouteId.WORKSPACE_ISSUES, sidebar.getViewModel().getActiveSidebarRoute());
            assertTrue(isRouteActive(sidebar, NavigationRouteId.WORKSPACE_ISSUES));
            assertFalse(isRouteActive(sidebar, NavigationRouteId.WORKSPACE_PROJECTS));
        });
    }

    @Test
    void sidebar_collapseExpandsAndShowsTooltips() throws Exception {
        JavaFxTestSupport.runOnFxThread(() -> {
            NavigationModule module = NavigationModule.createDefault();
            SrotsSidebar sidebar = new SrotsSidebar();
            module.chromeBinder().bindSidebar(sidebar);

            assertFalse(sidebar.isCollapsed());
            sidebar.setCollapsed(true);
            assertTrue(sidebar.isCollapsed());
            assertTrue(sidebar.getStyleClass().contains("srots-sidebar-collapsed"));

            SrotsNavigationItem first = firstItem(sidebar);
            assertTrue(first != null);
            assertTrue(first.getTooltip() != null);

            sidebar.setCollapsed(false);
            assertFalse(sidebar.isCollapsed());
            assertFalse(sidebar.getStyleClass().contains("srots-sidebar-collapsed"));
        });
    }

    @Test
    void group_collapseHidesChildren() throws Exception {
        JavaFxTestSupport.runOnFxThread(() -> {
            SrotsNavigationGroup group = new SrotsNavigationGroup("Engineering");
            SrotsNavigationItem item = new SrotsNavigationItem("Projects");
            group.addItem(item);
            assertTrue(group.isExpanded());
            assertTrue(group.getItemsHost().isVisible());

            group.setExpanded(false);
            assertFalse(group.isExpanded());
            assertFalse(group.getItemsHost().isVisible());

            group.setExpanded(true);
            assertTrue(group.getItemsHost().isVisible());
        });
    }

    @Test
    void viewModel_filtersByAuthorizationContext() {
        FeatureNavigationProvider provider = () -> List.of(
                NavigationItem.builder()
                        .title("Overview")
                        .route(NavigationRouteId.OVERVIEW)
                        .group(NavigationGroup.OVERVIEW)
                        .order(1)
                        .build(),
                NavigationItem.builder()
                        .title("Administration")
                        .route(NavigationRouteId.SETTINGS)
                        .group(NavigationGroup.SYSTEM)
                        .order(2)
                        .role("ADMIN")
                        .build(),
                NavigationItem.builder()
                        .title("Workspace")
                        .route(NavigationRouteId.WORKSPACE)
                        .group(NavigationGroup.WORKSPACE)
                        .order(3)
                        .build()
        );

        NavigationVisibilityService visibility = new NavigationVisibilityService();
        var registry = new DefaultNavigationRegistry(List.of(provider));

        SrotsSidebarViewModel adminVm = new SrotsSidebarViewModel();
        adminVm.bind(
                registry,
                NavigationModule.create(
                        List.of(provider),
                        StaticUserAccessContext.admin(),
                        new DefaultPlaceholderViewFactory(),
                        new SrotsContentHost(),
                        () -> false,
                        () -> true).navigationService(),
                visibility,
                StaticUserAccessContext.admin());
        // Settings is footer-only; administration role item with SETTINGS route is skipped from groups
        assertTrue(adminVm.getGroups().stream().anyMatch(g -> g.id() == NavigationGroup.OVERVIEW));

        SrotsSidebarViewModel employeeVm = new SrotsSidebarViewModel();
        var employeeModule = NavigationModule.create(
                List.of(provider),
                StaticUserAccessContext.employee(),
                new DefaultPlaceholderViewFactory(),
                new SrotsContentHost(),
                () -> false,
                () -> true);
        employeeVm.bind(
                registry,
                employeeModule.navigationService(),
                visibility,
                StaticUserAccessContext.employee());

        boolean hasAdminOnly = employeeVm.getGroups().stream()
                .flatMap(g -> g.items().stream())
                .anyMatch(i -> i.route() == NavigationRouteId.SETTINGS);
        assertFalse(hasAdminOnly);
        assertTrue(employeeVm.getGroups().stream()
                .flatMap(g -> g.items().stream())
                .anyMatch(i -> i.route() == NavigationRouteId.OVERVIEW));
    }

    @Test
    void navigationItem_supportsDisabledAndBadge() throws Exception {
        JavaFxTestSupport.runOnFxThread(() -> {
            SrotsNavigationItem item = new SrotsNavigationItem("Service Desk");
            item.setBadgeCount(12);
            assertEquals("Service Desk", item.getItemText());
            item.setBadgeCount(12_500);
            item.setDisable(true);
            assertTrue(item.isDisable());

            item.setActive(true);
            assertTrue(item.isActive());
            assertTrue(item.getStyleClass().contains("srots-sidebar-active"));
        });
    }

    @Test
    void catalogAnalytics_requiresAdminRole() {
        NavigationVisibilityService visibility = new NavigationVisibilityService();
        Collection<NavigationItem> all = new DefaultNavigationRegistry(
                List.of((FeatureNavigationProvider) () ->
                        List.of(NavigationItem.builder()
                                .title("Analytics")
                                .route(NavigationRouteId.ANALYTICS)
                                .group(NavigationGroup.SYSTEM)
                                .role("ADMIN")
                                .build()))).allItems();

        assertEquals(1, visibility.filterVisible(all, StaticUserAccessContext.admin()).size());
        assertEquals(0, visibility.filterVisible(all, StaticUserAccessContext.employee()).size());
        assertEquals(0, visibility.filterVisible(all, new StaticUserAccessContext(true, Set.of(), Set.of("EMPLOYEE"))).size());
    }

    private static boolean isRouteActive(SrotsSidebar sidebar, NavigationRouteId route) {
        for (var node : sidebar.getNavigationHost().getChildren()) {
            if (node instanceof SrotsNavigationItem item && item.isActive()) {
                if (route.equals(item.getProperties().get("srots.route"))) {
                    return true;
                }
            }
            if (node instanceof SrotsNavigationGroup group) {
                for (var child : group.getItemsHost().getChildren()) {
                    if (child instanceof SrotsNavigationItem item && item.isActive()
                            && route.equals(item.getProperties().get("srots.route"))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean hasActiveItem(SrotsSidebar sidebar) {
        return sidebar.getNavigationHost().getChildren().stream().anyMatch(node -> {
            if (node instanceof SrotsNavigationItem item) {
                return item.isActive();
            }
            if (node instanceof SrotsNavigationGroup group) {
                return group.getItemsHost().getChildren().stream()
                        .filter(SrotsNavigationItem.class::isInstance)
                        .map(SrotsNavigationItem.class::cast)
                        .anyMatch(SrotsNavigationItem::isActive);
            }
            return false;
        });
    }

    private static SrotsNavigationItem firstItem(SrotsSidebar sidebar) {
        for (var node : sidebar.getNavigationHost().getChildren()) {
            if (node instanceof SrotsNavigationItem item) {
                return item;
            }
            if (node instanceof SrotsNavigationGroup group) {
                for (var child : group.getItemsHost().getChildren()) {
                    if (child instanceof SrotsNavigationItem item) {
                        return item;
                    }
                }
            }
        }
        return null;
    }
}
