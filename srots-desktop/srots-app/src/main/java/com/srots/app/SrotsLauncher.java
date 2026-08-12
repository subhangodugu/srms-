package com.srots.app;

import com.srots.app.bootstrap.StartupException;
import com.srots.app.constants.AppConstants;
import javafx.application.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Process entry point for the SROTS native desktop application.
 *
 * <pre>
 * OS → SrotsLauncher → JavaFX Runtime → ApplicationBootstrap → AppShell → Default Route
 * </pre>
 */
public final class SrotsLauncher {

    private static final Logger log = LoggerFactory.getLogger(SrotsLauncher.class);

    private SrotsLauncher() {
    }

    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler((thread, error) -> {
            LoggerFactory.getLogger(SrotsLauncher.class)
                    .error("Uncaught exception on thread {}", thread.getName(), error);
        });

        try {
            log.info("Starting {} {} ...", AppConstants.APP_NAME, AppConstants.APP_VERSION);
            Application.launch(SROTSApplication.class, args);
        } catch (StartupException ex) {
            log.error("Launcher aborted: {}", ex.userMessage(), ex);
            System.err.println(AppConstants.APP_NAME + " failed to start: " + ex.userMessage());
            System.exit(1);
        } catch (Exception ex) {
            log.error("Launcher aborted unexpectedly", ex);
            System.err.println(AppConstants.APP_NAME + " failed to start. See logs for details.");
            System.exit(1);
        }
    }
}
