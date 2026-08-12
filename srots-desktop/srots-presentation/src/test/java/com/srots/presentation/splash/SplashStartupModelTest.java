package com.srots.presentation.splash;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SplashStartupModelTest {

    @Test
    void startupPhase_hasMonotonicProgressUntilReady() {
        assertTrue(StartupPhase.CONFIGURATION.progress() > StartupPhase.BOOTSTRAP.progress());
        assertTrue(StartupPhase.DEPENDENCIES.progress() > StartupPhase.CONFIGURATION.progress());
        assertTrue(StartupPhase.READY.progress() >= StartupPhase.UI.progress());
        assertEquals(1.0, StartupPhase.READY.progress(), 0.0001);
    }

    @Test
    void startupProgress_clampsAndDefaultsMessage() {
        StartupProgress progress = new StartupProgress(StartupPhase.DATA, 2.5, "  ");
        assertEquals(1.0, progress.progress(), 0.0001);
        assertEquals(StartupPhase.DATA.defaultMessage(), progress.message());
    }

    @Test
    void splashViewModel_appliesProgressAndFailure() {
        SrotsSplashViewModel viewModel = new SrotsSplashViewModel();
        viewModel.apply(StartupProgress.of(StartupPhase.DEPENDENCIES));
        assertEquals(StartupPhase.DEPENDENCIES, viewModel.getPhase());
        assertEquals(StartupPhase.DEPENDENCIES.progress(), viewModel.getProgress(), 0.0001);
        assertFalse(viewModel.isFailed());

        viewModel.apply(StartupProgress.failed("Unable to initialize the application."));
        assertTrue(viewModel.isFailed());
        assertEquals(SplashLifecycleState.FAILED, viewModel.getLifecycleState());
        assertEquals(StartupTaskState.FAILED, viewModel.getTaskState());
    }

    @Test
    void splashViewModel_resetForRetryClearsFailure() {
        SrotsSplashViewModel viewModel = new SrotsSplashViewModel();
        viewModel.apply(StartupProgress.failed("boom"));
        viewModel.resetForRetry();
        assertFalse(viewModel.isFailed());
        assertEquals(StartupPhase.BOOTSTRAP, viewModel.getPhase());
        assertEquals(SplashLifecycleState.INITIALIZING, viewModel.getLifecycleState());
        assertEquals(StartupTaskState.IDLE, viewModel.getTaskState());
    }

    @Test
    void lifecycleStates_includeExpectedValues() {
        assertEquals(SplashLifecycleState.CREATED, SplashLifecycleState.valueOf("CREATED"));
        assertEquals(SplashLifecycleState.READY, SplashLifecycleState.valueOf("READY"));
        assertEquals(StartupTaskState.RUNNING, StartupTaskState.valueOf("RUNNING"));
        assertEquals(StartupTaskState.COMPLETED, StartupTaskState.valueOf("COMPLETED"));
    }
}
