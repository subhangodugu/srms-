package com.srots.presentation.navigation;

import com.srots.presentation.components.support.JavaFxTestSupport;
import com.srots.presentation.navigation.guard.AuthenticationGuard;
import com.srots.presentation.navigation.guard.FeatureAvailabilityGuard;
import com.srots.presentation.navigation.guard.GuardDecision;
import com.srots.presentation.navigation.guard.PermissionGuard;
import com.srots.presentation.navigation.guard.UnsavedChangesGuard;
import com.srots.presentation.navigation.host.SrotsContentHost;
import com.srots.presentation.navigation.model.NavigationContext;
import com.srots.presentation.navigation.model.NavigationGroup;
import com.srots.presentation.navigation.model.NavigationItem;
import com.srots.presentation.navigation.model.NavigationRequest;
import com.srots.presentation.navigation.model.NavigationRouteId;
import com.srots.presentation.navigation.model.NavigationVisibility;
import com.srots.presentation.navigation.model.RouteParameters;
import com.srots.presentation.navigation.registry.CoreNavigationProvider;
import com.srots.presentation.navigation.registry.DefaultNavigationRegistry;
import com.srots.presentation.navigation.registry.NavigationRegistry;
import com.srots.presentation.navigation.resolver.DefaultPlaceholderViewFactory;
import com.srots.presentation.navigation.service.DefaultNavigationService;
import com.srots.presentation.navigation.service.DevOpenAccessContext;
import com.srots.presentation.navigation.service.NavigationVisibilityService;
import com.srots.presentation.navigation.service.UserAccessContext;
import com.srots.presentation.navigation.state.NavigationStatus;
import javafx.scene.control.Label;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NavigationArchitectureTest {

    @BeforeAll
    static void bootFx() throws Exception {
        JavaFxTestSupport.ensureToolkit();
    }

    @Test
    void registry_registersLookupHierarchyAndComptyPath() {
        NavigationRegistry registry = new DefaultNavigationRegistry(new CoreNavigationProvider());

        assertTrue(registry.find(NavigationRouteId.COMPANY_EMPLOYEES).isPresent());
        assertFalse(registry.children(NavigationRouteId.COMPTY).isEmpty());
        assertTrue(registry.children(NavigationRouteId.COMPTY).stream()
                .anyMatch(i -> i.getRoute() == NavigationRouteId.COMPTY_RELEASES));

        List<NavigationItem> crumbs = registry.breadcrumbPath(NavigationRouteId.COMPTY_RELEASES);
        assertEquals(
                List.of(NavigationRouteId.PRODUCTS, NavigationRouteId.COMPTY, NavigationRouteId.COMPTY_RELEASES),
                crumbs.stream().map(NavigationItem::getRoute).toList());

        assertEquals(
                NavigationRouteId.COMPTY,
                registry.findSidebarSelection(NavigationRouteId.COMPTY_RELEASES).orElseThrow().getRoute());
    }

    @Test
    void navigation_backForwardAndParameters() throws Exception {
        JavaFxTestSupport.runOnFxThread(() -> {
            DefaultNavigationService service = createService(new DevOpenAccessContext(), List.of());

            service.navigate(NavigationRouteId.OVERVIEW);
            service.navigate(NavigationRouteId.PRODUCTS);
            service.navigate(NavigationRouteId.COMPTY);
            service.navigate(
                    NavigationRouteId.RELEASE_DETAILS,
                    RouteParameters.of("releaseId", "789"));

            assertEquals(NavigationRouteId.RELEASE_DETAILS, service.currentRoute());
            assertEquals("789", service.state().getCurrentParameters().get("releaseId"));
            assertTrue(service.canGoBack());

            service.back();
            assertEquals(NavigationRouteId.COMPTY, service.currentRoute());
            assertTrue(service.canGoForward());

            service.forward();
            assertEquals(NavigationRouteId.RELEASE_DETAILS, service.currentRoute());
            assertEquals("789", service.state().getCurrentParameters().get("releaseId"));
        });
    }

    @Test
    void unknownRoute_showsPageUnavailable() throws Exception {
        JavaFxTestSupport.runOnFxThread(() -> {
            DefaultNavigationService service = createService(new DevOpenAccessContext(), List.of());
            service.navigate(NavigationRouteId.OVERVIEW);
            service.navigate(NavigationRouteId.UNKNOWN);
            assertEquals(NavigationRouteId.OVERVIEW, service.currentRoute());
            assertEquals(NavigationStatus.FAILED, service.state().getStatus());
            assertFalse(service.contentHost().getHost().getChildren().isEmpty());
            assertTrue(service.contentHost().getHost().getChildren().get(0)
                    .getStyleClass().contains("srots-navigation-status"));
        });
    }

    @Test
    void sameRoute_doesNotRecreateView() throws Exception {
        JavaFxTestSupport.runOnFxThread(() -> {
            DefaultNavigationService service = createService(new DevOpenAccessContext(), List.of());
            service.navigate(NavigationRouteId.PROJECTS);
            var first = service.contentHost().getCurrentView();
            service.navigate(NavigationRouteId.PROJECTS);
            assertSame(first, service.contentHost().getCurrentView());
            assertFalse(service.canGoBack());
        });
    }

    @Test
    void navigationRequest_drivesTypedNavigate() throws Exception {
        JavaFxTestSupport.runOnFxThread(() -> {
            DefaultNavigationService service = createService(new DevOpenAccessContext(), List.of());
            service.navigate(NavigationRequest.of(
                    NavigationRouteId.PROJECTS,
                    com.srots.presentation.navigation.model.NavigationSource.SIDEBAR));
            assertEquals(NavigationRouteId.PROJECTS, service.currentRoute());
        });
    }

    @Test
    void unauthorized_showsAccessDeniedWithoutChangingRoute() throws Exception {
        JavaFxTestSupport.runOnFxThread(() -> {
            NavigationRegistry registry = new DefaultNavigationRegistry(List.of(
                    NavigationItem.builder()
                            .title("Settings")
                            .route(NavigationRouteId.SETTINGS)
                            .group(NavigationGroup.SYSTEM)
                            .permission("SETTINGS_ADMIN")
                            .build(),
                    NavigationItem.builder()
                            .title("Overview")
                            .route(NavigationRouteId.OVERVIEW)
                            .group(NavigationGroup.OVERVIEW)
                            .build()));
            UserAccessContext denied = new FixedAccessContext(true, Set.of(), Set.of());
            DefaultNavigationService service = new DefaultNavigationService(
                    registry,
                    new DefaultPlaceholderViewFactory(),
                    new SrotsContentHost(),
                    List.of(new PermissionGuard(registry, denied)));

            service.navigate(NavigationRouteId.OVERVIEW);
            service.navigate(NavigationRouteId.SETTINGS);
            assertEquals(NavigationRouteId.OVERVIEW, service.currentRoute());
            assertEquals(NavigationStatus.BLOCKED, service.state().getStatus());
            assertTrue(service.contentHost().getHost().getChildren().get(0)
                    .getStyleClass().contains("srots-navigation-status"));
        });
    }

    @Test
    void permissionGuard_blocksMissingPermission() {
        NavigationRegistry registry = new DefaultNavigationRegistry(List.of(
                NavigationItem.builder()
                        .title("Settings")
                        .route(NavigationRouteId.SETTINGS)
                        .group(NavigationGroup.SYSTEM)
                        .permission("SETTINGS_ADMIN")
                        .build()));
        UserAccessContext denied = new FixedAccessContext(true, Set.of(), Set.of());
        PermissionGuard guard = new PermissionGuard(registry, denied);

        GuardDecision decision = guard.evaluate(
                NavigationContext.of(NavigationRouteId.OVERVIEW),
                NavigationContext.of(NavigationRouteId.SETTINGS));
        assertFalse(decision.allowed());
        assertEquals(PermissionGuard.PERMISSION_DENIED, decision.message());
    }

    @Test
    void unsavedChangesGuard_cancelsWhenDeclined() {
        UnsavedChangesGuard guard = new UnsavedChangesGuard(() -> true, () -> false);
        GuardDecision decision = guard.evaluate(
                NavigationContext.of(NavigationRouteId.COMPANY_EMPLOYEES),
                NavigationContext.of(NavigationRouteId.PROJECTS));
        assertTrue(decision.cancelled());
    }

    @Test
    void featureAvailabilityGuard_blocksDisabled() {
        NavigationRegistry registry = new DefaultNavigationRegistry(List.of(
                NavigationItem.builder()
                        .title("Analytics")
                        .route(NavigationRouteId.ANALYTICS)
                        .group(NavigationGroup.SYSTEM)
                        .visibility(NavigationVisibility.DISABLED)
                        .build()));
        FeatureAvailabilityGuard guard = new FeatureAvailabilityGuard(registry);
        GuardDecision decision = guard.evaluate(
                NavigationContext.of(NavigationRouteId.OVERVIEW),
                NavigationContext.of(NavigationRouteId.ANALYTICS));
        assertFalse(decision.allowed());
    }

    @Test
    void authenticationGuard_blocksWhenUnauthenticated() {
        AuthenticationGuard guard = new AuthenticationGuard(new FixedAccessContext(false, Set.of(), Set.of()));
        GuardDecision decision = guard.evaluate(
                null,
                NavigationContext.of(NavigationRouteId.PROJECTS));
        assertFalse(decision.allowed());
        assertEquals(AuthenticationGuard.AUTHENTICATION_REQUIRED, decision.message());
    }

    @Test
    void visibilityService_hidesUnauthorizedItems() {
        NavigationVisibilityService visibility = new NavigationVisibilityService();
        List<NavigationItem> items = List.of(
                NavigationItem.builder()
                        .title("Employees")
                        .route(NavigationRouteId.COMPANY_EMPLOYEES)
                        .group(NavigationGroup.COMPANY)
                        .permission("EMPLOYEE_READ")
                        .build(),
                NavigationItem.builder()
                        .title("Overview")
                        .route(NavigationRouteId.OVERVIEW)
                        .group(NavigationGroup.OVERVIEW)
                        .build());

        List<NavigationItem> visible = visibility.filterVisible(
                items,
                new FixedAccessContext(true, Set.of(), Set.of()));
        assertEquals(1, visible.size());
        assertEquals(NavigationRouteId.OVERVIEW, visible.get(0).getRoute());
    }

    @Test
    void navigationService_appliesGuardsAndContentHost() throws Exception {
        JavaFxTestSupport.runOnFxThread(() -> {
            AtomicBoolean blocked = new AtomicBoolean();
            DefaultNavigationService service = createService(
                    new DevOpenAccessContext(),
                    List.of((from, to) -> {
                        if (to.route() == NavigationRouteId.SETTINGS) {
                            blocked.set(true);
                            return GuardDecision.block("blocked");
                        }
                        return GuardDecision.allow();
                    }));

            service.navigate(NavigationRouteId.OVERVIEW);
            assertFalse(service.contentHost().getHost().getChildren().isEmpty());

            service.navigate(NavigationRouteId.SETTINGS);
            assertTrue(blocked.get());
            assertEquals(NavigationRouteId.OVERVIEW, service.currentRoute());
            assertEquals(NavigationStatus.BLOCKED, service.state().getStatus());
        });
    }

    @Test
    void contentHost_replacesViewWithoutSceneChange() throws Exception {
        JavaFxTestSupport.runOnFxThread(() -> {
            SrotsContentHost host = new SrotsContentHost();
            Label first = new Label("one");
            Label second = new Label("two");
            host.setView(first);
            host.setView(second);
            assertEquals(1, host.getHost().getChildren().size());
            assertEquals(second, host.getHost().getChildren().get(0));
        });
    }

    private static DefaultNavigationService createService(
            UserAccessContext access,
            List<com.srots.presentation.navigation.guard.NavigationGuard> extraGuards) {
        NavigationRegistry registry = new DefaultNavigationRegistry(new CoreNavigationProvider());
        List<com.srots.presentation.navigation.guard.NavigationGuard> guards = new java.util.ArrayList<>();
        guards.add(new AuthenticationGuard(access));
        guards.add(new PermissionGuard(registry, access));
        guards.add(new FeatureAvailabilityGuard(registry));
        guards.addAll(extraGuards);
        return new DefaultNavigationService(
                registry,
                new DefaultPlaceholderViewFactory(),
                new SrotsContentHost(),
                guards);
    }

    private record FixedAccessContext(
            boolean authenticated,
            Set<String> permissions,
            Set<String> roles) implements UserAccessContext {

        @Override
        public boolean hasPermission(String permission) {
            return permissions.contains(permission);
        }

        @Override
        public boolean hasRole(String role) {
            return roles.contains(role);
        }

        @Override
        public boolean isAuthenticated() {
            return authenticated;
        }
    }
}
