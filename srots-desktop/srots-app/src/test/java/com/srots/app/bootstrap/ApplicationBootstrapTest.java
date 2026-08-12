package com.srots.app.bootstrap;

import com.srots.infrastructure.mock.configuration.DataMode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApplicationBootstrapTest {

    @Test
    void fromSystemProperties_readsEnvAndMode() {
        System.setProperty(ApplicationConfig.PROP_ENV, "development");
        System.setProperty(ApplicationConfig.PROP_DATA_MODE, "MOCK");
        ApplicationConfig config = ApplicationConfig.fromSystemProperties(new String[] {"--dev"});
        assertEquals("development", config.environment());
        assertEquals(DataMode.MOCK, config.dataMode());
        assertTrue(config.isDevelopment());
        assertFalse(config.isProduction());
        assertEquals(1, config.launchArguments().length);
    }

    @Test
    void fromSystemProperties_rejectsInvalidMode() {
        System.setProperty(ApplicationConfig.PROP_DATA_MODE, "NOT_A_MODE");
        assertThrows(StartupException.class, () -> ApplicationConfig.fromSystemProperties(new String[0]));
        System.setProperty(ApplicationConfig.PROP_DATA_MODE, "MOCK");
    }

    @Test
    void validateConfiguration_rejectsProductionMock() {
        ApplicationConfig config = new ApplicationConfig("production", DataMode.MOCK, new String[0]);
        assertThrows(StartupException.class, () -> ApplicationBootstrap.validateConfiguration(config));
    }

    @Test
    void validateConfiguration_rejectsUnimplementedRemoteMode() {
        ApplicationConfig config = new ApplicationConfig("development", DataMode.REMOTE, new String[0]);
        assertThrows(StartupException.class, () -> ApplicationBootstrap.validateConfiguration(config));
    }

    @Test
    void validateConfiguration_allowsDevelopmentMock() {
        ApplicationConfig config = new ApplicationConfig("development", DataMode.MOCK, new String[0]);
        ApplicationBootstrap.validateConfiguration(config);
    }

    @Test
    void bootstrap_createsContainerAndShutdownCoordinator() {
        ApplicationConfig config = new ApplicationConfig("development", DataMode.MOCK, new String[0]);
        ApplicationBootstrap bootstrap = new ApplicationBootstrap(config);
        AppContainer container = bootstrap.bootstrap();
        assertEquals(config, container.getConfig());
        assertTrue(bootstrap.isCompleted());
        assertEquals(container, AppContainer.getInstance());
        bootstrap.shutdownCoordinator().shutdown();
    }
}
