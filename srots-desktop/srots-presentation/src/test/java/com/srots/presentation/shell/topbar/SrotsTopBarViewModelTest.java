package com.srots.presentation.shell.topbar;

import com.srots.presentation.components.navigation.topbar.SrotsConnectionState;
import com.srots.presentation.components.support.JavaFxTestSupport;
import com.srots.presentation.navigation.NavigationModule;
import com.srots.presentation.navigation.model.NavigationRouteId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SrotsTopBarViewModelTest {

    @BeforeAll
    static void bootFx() throws Exception {
        JavaFxTestSupport.ensureToolkit();
    }

    @Test
    void pageTitleAndBreadcrumb_followNavigation() throws Exception {
        JavaFxTestSupport.runOnFxThread(() -> {
            NavigationModule module = NavigationModule.createDefault();
            SrotsTopBarViewModel vm = new SrotsTopBarViewModel();
            vm.bind(module.registry(), module.navigationService(), TopBarApplicationState.developmentDefaults());

            module.navigationService().navigate(NavigationRouteId.OVERVIEW);
            assertEquals("Overview", vm.getPageTitle());
            assertTrue(vm.getBreadcrumbText().contains("Overview"));

            module.navigationService().navigate(NavigationRouteId.COMPANY_EMPLOYEES);
            assertEquals("Employees", vm.getPageTitle());

            module.navigationService().navigate(NavigationRouteId.PROJECTS);
            assertEquals("Projects", vm.getPageTitle());

            module.navigationService().navigate(NavigationRouteId.COMPTY);
            assertEquals("COMPTY", vm.getPageTitle());

            module.navigationService().navigate(NavigationRouteId.COMPTY_RELEASES);
            assertEquals("COMPTY Releases", vm.getPageTitle());
            assertTrue(vm.getBreadcrumbText().contains("COMPTY"));
            assertFalse(vm.getBreadcrumbs().isEmpty());
        });
    }

    @Test
    void userConnectionAndNotifications_comeFromApplicationState() throws Exception {
        JavaFxTestSupport.runOnFxThread(() -> {
            NavigationModule module = NavigationModule.createDefault();
            TopBarApplicationState state = new TopBarApplicationState();
            state.setCurrentUser(new TopBarUserInfo("Subhan", "Administrator"));
            state.setConnectionState(SrotsConnectionState.OFFLINE);
            state.setNotificationCount(3);

            SrotsTopBarViewModel vm = new SrotsTopBarViewModel();
            vm.bind(module.registry(), module.navigationService(), state);

            assertEquals("Subhan", vm.getCurrentUser().displayName());
            assertEquals("Administrator", vm.getCurrentUser().roleLabel());
            assertEquals(SrotsConnectionState.OFFLINE, vm.getConnectionState());
            assertEquals(SrotsTopBarState.OFFLINE, vm.getUiState());
            assertEquals(3, vm.getNotificationCount());

            state.setNotificationCount(150);
            assertEquals(150, vm.getNotificationCount());
            assertEquals("99+", TopBarNotificationBadgeFormatter.format(vm.getNotificationCount()));

            state.setConnectionState(SrotsConnectionState.ONLINE);
            assertEquals(SrotsTopBarState.READY, vm.getUiState());
        });
    }

    @Test
    void actions_limitVisibleToThree() {
        AtomicBoolean ran = new AtomicBoolean(false);
        SrotsTopBarViewModel vm = new SrotsTopBarViewModel();
        vm.setActions(List.of(
                SrotsTopBarAction.builder("a1", "One").priority(1).onAction(() -> {
                }).build(),
                SrotsTopBarAction.builder("a2", "Two").priority(2).onAction(() -> {
                }).build(),
                SrotsTopBarAction.builder("a3", "Three").priority(3).onAction(() -> {
                }).build(),
                SrotsTopBarAction.builder("a4", "Four").priority(4).onAction(() -> ran.set(true)).build()
        ));
        assertEquals(3, vm.getActions().size());
        assertFalse(ran.get());
    }

    @Test
    void breadcrumbFormatter_compactsLongPaths() {
        assertEquals("Products / COMPTY / Releases",
                SrotsTopBarViewModel.formatBreadcrumb(List.of("Products", "COMPTY", "Releases"), false));
        assertEquals("Products / … / Releases",
                SrotsTopBarViewModel.formatBreadcrumb(List.of("Products", "COMPTY", "Board", "Releases"), true));
    }

    @Test
    void notificationBadge_formatsRanges() {
        assertEquals("", TopBarNotificationBadgeFormatter.format(0));
        assertEquals("3", TopBarNotificationBadgeFormatter.format(3));
        assertEquals("99", TopBarNotificationBadgeFormatter.format(99));
        assertEquals("99+", TopBarNotificationBadgeFormatter.format(100));
    }

    @Test
    void userFallback_neverNull() {
        assertEquals("User", TopBarUserInfo.fallback().displayName());
        assertEquals("User", new TopBarUserInfo(null, null).displayName());
    }
}
