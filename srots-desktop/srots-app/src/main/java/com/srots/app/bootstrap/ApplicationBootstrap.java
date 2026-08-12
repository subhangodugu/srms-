package com.srots.app.bootstrap;

import com.srots.app.constants.AppConstants;
import com.srots.infrastructure.mock.configuration.DataMode;
import com.srots.infrastructure.mock.configuration.ProductionMockGuard;
import com.srots.presentation.splash.StartupPhase;
import com.srots.presentation.splash.StartupProgress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Application bootstrap orchestrator. Publishes {@link StartupProgress}; does not create UI nodes.
 */
public final class ApplicationBootstrap {

    private static final Logger log = LoggerFactory.getLogger(ApplicationBootstrap.class);

    private final ApplicationConfig config;
    private AppContainer container;
    private ShutdownCoordinator shutdownCoordinator;
    private boolean completed;

    public ApplicationBootstrap(ApplicationConfig config) {
        this.config = Objects.requireNonNull(config, "config");
    }

    public synchronized AppContainer bootstrap() {
        return bootstrap(progress -> {
        });
    }

    public synchronized AppContainer bootstrap(Consumer<StartupProgress> progressConsumer) {
        Consumer<StartupProgress> progress = progressConsumer == null ? p -> {
        } : progressConsumer;

        if (completed && container != null) {
            publish(progress, StartupProgress.of(StartupPhase.READY));
            return container;
        }

        log.info("Bootstrapping {} {} env={} dataMode={}",
                AppConstants.APP_NAME,
                AppConstants.APP_VERSION,
                config.environment(),
                config.dataMode());

        try {
            publish(progress, StartupProgress.of(StartupPhase.BOOTSTRAP));
            publish(progress, StartupProgress.of(StartupPhase.CONFIGURATION));
            validateConfiguration(config);

            publish(progress, StartupProgress.of(StartupPhase.LOGGING));
            log.info("Startup phase: LOGGING");

            publish(progress, StartupProgress.of(StartupPhase.DEPENDENCIES));
            log.info("Startup phase: DEPENDENCIES");
            this.container = AppContainer.create(config);
            this.shutdownCoordinator = new ShutdownCoordinator(container);

            publish(progress, StartupProgress.of(StartupPhase.DATA));
            log.info("Startup phase: DATA");

            publish(progress, StartupProgress.of(StartupPhase.NAVIGATION));
            log.info("Startup phase: NAVIGATION");

            publish(progress, StartupProgress.of(StartupPhase.THEME));
            log.info("Startup phase: THEME");

            publish(progress, StartupProgress.of(StartupPhase.UI));
            log.info("Startup phase: UI");

            this.completed = true;
            log.info("Dependency graph initialized. Presentation infrastructure ready for shell load.");
            return container;
        } catch (StartupException ex) {
            publish(progress, StartupProgress.failed(ex.userMessage()));
            throw ex;
        } catch (Exception ex) {
            StartupException wrapped = new StartupException(
                    "Unable to initialize SROTS. Please verify configuration and try again.",
                    ex);
            publish(progress, StartupProgress.failed(wrapped.userMessage()));
            throw wrapped;
        }
    }

    /**
     * Clears completed state so a controlled retry can run again.
     */
    public synchronized void resetForRetry() {
        if (container != null) {
            try {
                container.shutdown();
            } catch (Exception ex) {
                log.warn("Error releasing container before retry", ex);
            }
        }
        container = null;
        shutdownCoordinator = null;
        completed = false;
    }

    public ApplicationConfig config() {
        return config;
    }

    public AppContainer container() {
        return container;
    }

    public ShutdownCoordinator shutdownCoordinator() {
        return shutdownCoordinator;
    }

    public boolean isCompleted() {
        return completed;
    }

    static void validateConfiguration(ApplicationConfig config) {
        if (config.isProduction() && config.dataMode() == DataMode.MOCK) {
            throw new StartupException(
                    "Mock data mode is not allowed in production. Set srots.data.mode=LOCAL or REMOTE.");
        }
        if (config.dataMode() != DataMode.MOCK) {
            throw new StartupException(
                    "Data mode " + config.dataMode()
                            + " is not available yet. Use -Dsrots.data.mode=MOCK for development.");
        }
        try {
            var probe = new com.srots.infrastructure.mock.configuration.MockConfiguration();
            probe.setRuntimeEnvironment(config.environment());
            probe.setDataMode(config.dataMode());
            ProductionMockGuard.assertSafe(probe);
        } catch (IllegalStateException ex) {
            throw new StartupException(ex.getMessage(), ex);
        }
    }

    private static void publish(Consumer<StartupProgress> consumer, StartupProgress update) {
        log.info("Startup phase: {}", update.phase());
        consumer.accept(update);
    }
}
