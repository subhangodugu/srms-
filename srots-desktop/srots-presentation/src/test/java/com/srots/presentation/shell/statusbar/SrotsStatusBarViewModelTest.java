package com.srots.presentation.shell.statusbar;

import com.srots.presentation.components.navigation.topbar.SrotsConnectionState;
import com.srots.presentation.components.support.JavaFxTestSupport;
import com.srots.presentation.shell.topbar.TopBarApplicationState;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SrotsStatusBarViewModelTest {

    @BeforeAll
    static void bootFx() throws Exception {
        JavaFxTestSupport.ensureToolkit();
    }

    @Test
    void readyState_whenConnectedAndIdle() {
        TopBarApplicationState appState = new TopBarApplicationState();
        appState.setConnectionState(SrotsConnectionState.ONLINE);
        ApplicationActivityService activities = new ApplicationActivityService();
        SrotsStatusBarViewModel vm = new SrotsStatusBarViewModel();
        vm.bind(appState, activities, StatusBarEnvironmentInfo.of("production", "REMOTE", "1.0.0", true));

        assertEquals(SrotsStatusBarState.READY, vm.getBarState());
        assertEquals("Ready", vm.getActivityText());
        assertEquals(SrotsConnectionState.ONLINE, vm.getConnectionState());
        assertTrue(vm.getEnvironmentText().contains("Production"));
        assertTrue(vm.getEnvironmentText().contains("v1.0.0"));
        assertFalse(vm.getEnvironmentText().toLowerCase().contains("mock"));
    }

    @Test
    void offlineState_showsWorkingOffline() {
        TopBarApplicationState appState = new TopBarApplicationState();
        appState.setConnectionState(SrotsConnectionState.OFFLINE);
        ApplicationActivityService activities = new ApplicationActivityService();
        SrotsStatusBarViewModel vm = new SrotsStatusBarViewModel();
        vm.bind(appState, activities, StatusBarEnvironmentInfo.of("development", "MOCK", "0.1.0", false));

        assertEquals(SrotsStatusBarState.OFFLINE, vm.getBarState());
        assertEquals("Working offline", vm.getActivityText());
    }

    @Test
    void syncingActivity_updatesMessage() {
        TopBarApplicationState appState = TopBarApplicationState.developmentDefaults();
        ApplicationActivityService activities = new ApplicationActivityService();
        SrotsStatusBarViewModel vm = new SrotsStatusBarViewModel();
        vm.bind(appState, activities, StatusBarEnvironmentInfo.of("development", "MOCK", "0.1.0", false));

        activities.publish(ApplicationActivity.builder(ApplicationActivityType.SYNCING)
                .message("Synchronizing...")
                .build());
        assertEquals(SrotsStatusBarState.SYNCING, vm.getBarState());
        assertEquals("Synchronizing...", vm.getActivityText());

        activities.clear();
        assertEquals(SrotsStatusBarState.READY, vm.getBarState());
        assertEquals("Ready", vm.getActivityText());
    }

    @Test
    void progress_appendsPercentWhenDeterminate() {
        TopBarApplicationState appState = TopBarApplicationState.developmentDefaults();
        ApplicationActivityService activities = new ApplicationActivityService();
        SrotsStatusBarViewModel vm = new SrotsStatusBarViewModel();
        vm.bind(appState, activities, StatusBarEnvironmentInfo.of("development", "MOCK", "0.1.0", false));

        activities.publish(ApplicationActivity.builder(ApplicationActivityType.EXPORTING)
                .message("Exporting report...")
                .progress(0.65)
                .build());
        assertEquals(SrotsStatusBarState.BUSY, vm.getBarState());
        assertEquals("Exporting report... 65%", vm.getActivityText());
        assertTrue(vm.hasDeterminateProgress());
        assertEquals(0.65, vm.getProgress(), 0.001);
    }

    @Test
    void errorState_showsOperationFailed() {
        TopBarApplicationState appState = TopBarApplicationState.developmentDefaults();
        ApplicationActivityService activities = new ApplicationActivityService();
        SrotsStatusBarViewModel vm = new SrotsStatusBarViewModel();
        vm.bind(appState, activities, StatusBarEnvironmentInfo.of("development", "MOCK", "0.1.0", false));

        activities.markFailed("Operation failed");
        assertEquals(SrotsStatusBarState.ERROR, vm.getBarState());
        assertEquals("Operation failed", vm.getActivityText());
    }

    @Test
    void developmentEnvironment_showsMockData() {
        StatusBarEnvironmentInfo info = StatusBarEnvironmentInfo.of("development", "MOCK", "0.1.0-SNAPSHOT", false);
        assertEquals("Development · Mock Data · SROTS v0.1.0", info.formatRightText(false));
        assertEquals("Development · v0.1.0", info.formatRightText(true));
    }

    @Test
    void statePriority_errorBeatsOffline() {
        assertEquals(
                SrotsStatusBarState.ERROR,
                SrotsStatusBarState.resolve(true, true, true, true, true));
        assertEquals(
                SrotsStatusBarState.OFFLINE,
                SrotsStatusBarState.resolve(false, true, true, true, true));
        assertEquals(
                SrotsStatusBarState.READY,
                SrotsStatusBarState.resolve(false, false, false, false, false));
    }

    @Test
    void version_comesFromEnvironmentInfo() {
        SrotsStatusBarViewModel vm = new SrotsStatusBarViewModel();
        vm.bind(
                TopBarApplicationState.developmentDefaults(),
                new ApplicationActivityService(),
                StatusBarEnvironmentInfo.of("test", "LOCAL", "1.2.0", false));
        assertEquals("v1.2.0", vm.getVersionText());
        assertTrue(vm.getEnvironmentText().contains("Test"));
    }
}
