package com.srots.app.bootstrap;

import com.srots.presentation.splash.StartupPhase;
import com.srots.presentation.splash.StartupProgress;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApplicationBootstrapProgressTest {

    @Test
    void bootstrap_publishesRealStartupPhases() {
        ApplicationConfig config = new ApplicationConfig("development",
                com.srots.infrastructure.mock.configuration.DataMode.MOCK, new String[0]);
        ApplicationBootstrap bootstrap = new ApplicationBootstrap(config);
        List<StartupPhase> phases = new ArrayList<>();
        bootstrap.bootstrap(progress -> phases.add(progress.phase()));

        assertTrue(phases.contains(StartupPhase.CONFIGURATION));
        assertTrue(phases.contains(StartupPhase.DEPENDENCIES));
        assertTrue(phases.contains(StartupPhase.DATA));
        assertTrue(phases.contains(StartupPhase.UI));
        assertEquals(StartupPhase.UI, phases.get(phases.size() - 1));
        bootstrap.shutdownCoordinator().shutdown();
    }

    @Test
    void resetForRetry_allowsSecondBootstrap() {
        ApplicationConfig config = new ApplicationConfig("development",
                com.srots.infrastructure.mock.configuration.DataMode.MOCK, new String[0]);
        ApplicationBootstrap bootstrap = new ApplicationBootstrap(config);
        bootstrap.bootstrap();
        bootstrap.resetForRetry();
        AppContainer again = bootstrap.bootstrap(p -> {
        });
        assertEquals(config, again.getConfig());
        bootstrap.shutdownCoordinator().shutdown();
    }

    @Test
    void failureProgress_usesSafeMessage() {
        StartupProgress progress = com.srots.app.lifecycle.SrotsStartupTask.failureProgress(
                new StartupException("Unable to initialize the application."));
        assertEquals(StartupPhase.FAILED, progress.phase());
        assertEquals("Unable to initialize the application.", progress.message());
    }
}
