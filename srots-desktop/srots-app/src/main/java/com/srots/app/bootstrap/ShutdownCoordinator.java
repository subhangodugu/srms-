package com.srots.app.bootstrap;

import com.srots.app.constants.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Coordinates orderly application shutdown. No business logic.
 */
public final class ShutdownCoordinator {

    private static final Logger log = LoggerFactory.getLogger(ShutdownCoordinator.class);

    private final AppContainer container;
    private volatile boolean stopped;

    public ShutdownCoordinator(AppContainer container) {
        this.container = container;
    }

    public synchronized void shutdown() {
        if (stopped) {
            return;
        }
        stopped = true;
        log.info("Shutting down {} {} ...", AppConstants.APP_NAME, AppConstants.APP_VERSION);
        try {
            if (container != null) {
                container.shutdown();
            }
        } catch (Exception ex) {
            log.warn("Error while releasing application resources", ex);
        }
        log.info("{} shutdown complete.", AppConstants.APP_NAME);
    }
}
