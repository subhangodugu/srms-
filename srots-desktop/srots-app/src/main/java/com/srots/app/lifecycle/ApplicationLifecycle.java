package com.srots.app.lifecycle;

import com.srots.app.bootstrap.AppContainer;
import com.srots.app.bootstrap.ApplicationBootstrap;
import com.srots.app.bootstrap.ApplicationConfig;
import com.srots.app.bootstrap.ShutdownCoordinator;
import com.srots.app.bootstrap.SrotsBuildInfo;
import com.srots.app.bootstrap.StartupException;
import com.srots.app.constants.AppConstants;
import com.srots.app.shell.PrimaryWindowFactory;
import com.srots.infrastructure.mock.configuration.DataMode;
import com.srots.presentation.splash.SplashLifecycleState;
import com.srots.presentation.splash.SrotsSplashViewModel;
import com.srots.presentation.splash.SrotsSplashWindow;
import com.srots.presentation.splash.StartupPhase;
import com.srots.presentation.splash.StartupProgress;
import com.srots.presentation.splash.StartupTaskState;
import com.srots.presentation.window.SrotsWindowManager;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Coordinates splash → background bootstrap → main window. No feature business logic.
 */
public final class ApplicationLifecycle {

    private static final Logger log = LoggerFactory.getLogger(ApplicationLifecycle.class);

    private final ApplicationConfig config;
    private final Stage primaryStage;
    private final SrotsSplashViewModel splashViewModel;
    private final SrotsSplashWindow splashWindow;

    private final AtomicReference<ApplicationBootstrap> bootstrapRef = new AtomicReference<>();
    private final AtomicReference<SrotsStartupTask> taskRef = new AtomicReference<>();
    private final AtomicReference<ShutdownCoordinator> shutdownRef = new AtomicReference<>();
    private final ExecutorService startupExecutor = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "srots-startup");
        t.setDaemon(true);
        return t;
    });

    private boolean mainWindowShown;
    private final AtomicReference<SrotsWindowManager> windowManagerRef = new AtomicReference<>();

    public ApplicationLifecycle(ApplicationConfig config, Stage primaryStage) {
        this.config = Objects.requireNonNull(config, "config");
        this.primaryStage = Objects.requireNonNull(primaryStage, "primaryStage");
        this.splashViewModel = new SrotsSplashViewModel();
        this.splashViewModel.setVersion(SrotsBuildInfo.displayVersion());
        this.splashViewModel.setSubtitle(AppConstants.APP_FULL_NAME);
        if (!config.isProduction()) {
            String badge = config.dataMode() == DataMode.MOCK
                    ? "DEVELOPMENT · Mock Data"
                    : "DEVELOPMENT";
            this.splashViewModel.setEnvironmentBadge(badge);
        }
        this.splashWindow = new SrotsSplashWindow(splashViewModel);
        this.splashWindow.setOnRetry(this::retry);
        this.splashWindow.setOnExit(this::exitApplication);
        this.bootstrapRef.set(new ApplicationBootstrap(config));
    }

    public void start() {
        log.info("SROTS starting");
        try {
            primaryStage.initStyle(StageStyle.UNDECORATED);
        } catch (IllegalArgumentException | IllegalStateException ex) {
            log.debug("Primary stage style already set", ex);
        }
        primaryStage.hide();
        splashWindow.show();
        splashViewModel.setLifecycleState(SplashLifecycleState.INITIALIZING);
        runStartup();
    }

    public void shutdown() {
        cancelCurrentTask();
        SrotsWindowManager windowManager = windowManagerRef.get();
        if (windowManager != null) {
            try {
                windowManager.saveWindowState();
            } catch (Exception ex) {
                log.warn("Unable to save window state during shutdown", ex);
            }
        }
        splashWindow.dispose();
        ShutdownCoordinator coordinator = shutdownRef.get();
        if (coordinator != null) {
            coordinator.shutdown();
        }
        startupExecutor.shutdownNow();
    }

    private void runStartup() {
        cancelCurrentTask();
        ApplicationBootstrap bootstrap = bootstrapRef.get();
        SrotsStartupTask task = new SrotsStartupTask(bootstrap, progress ->
                Platform.runLater(() -> splashViewModel.apply(progress)));
        taskRef.set(task);
        splashViewModel.setTaskState(StartupTaskState.RUNNING);

        task.setOnSucceeded(e -> {
            AppContainer container = task.getValue();
            if (container == null) {
                onFailed(new StartupException("Startup cancelled."));
                return;
            }
            shutdownRef.set(bootstrap.shutdownCoordinator());
            Platform.runLater(() -> openMainWindow(container));
        });
        task.setOnFailed(e -> onFailed(task.getException()));
        task.setOnCancelled(e -> {
            splashViewModel.setTaskState(StartupTaskState.CANCELLED);
            splashViewModel.apply(StartupProgress.failed("Startup cancelled."));
        });

        startupExecutor.execute(task);
    }

    private void openMainWindow(AppContainer container) {
        try {
            splashViewModel.apply(StartupProgress.of(StartupPhase.UI, "Loading interface..."));
            PrimaryWindowFactory windowFactory = new PrimaryWindowFactory(container, config);
            windowFactory.createAndShow(primaryStage);
            windowManagerRef.set(windowFactory.getWindowManager());
            mainWindowShown = true;

            splashViewModel.apply(StartupProgress.of(StartupPhase.READY));
            splashViewModel.setLifecycleState(SplashLifecycleState.READY);
            splashViewModel.setTaskState(StartupTaskState.COMPLETED);
            log.info("SROTS ready");

            splashWindow.closeWithFade(() -> {
                primaryStage.toFront();
                log.info("{} desktop ready.", AppConstants.APP_NAME);
            });
        } catch (Exception ex) {
            onFailed(ex);
        }
    }

    private void onFailed(Throwable error) {
        log.error("SROTS startup failed", error);
        Platform.runLater(() -> {
            StartupProgress failure = SrotsStartupTask.failureProgress(error);
            splashViewModel.apply(failure);
            splashViewModel.setStatusMessage("SROTS could not start.");
            if (splashViewModel.getErrorMessage() == null || splashViewModel.getErrorMessage().isBlank()) {
                splashViewModel.apply(StartupProgress.failed("Unable to initialize the application."));
            }
            splashViewModel.setTaskState(StartupTaskState.FAILED);
            splashViewModel.setLifecycleState(SplashLifecycleState.FAILED);
            if (!splashWindow.isShowing()) {
                splashWindow.show();
            }
        });
    }

    private void retry() {
        if (mainWindowShown) {
            return;
        }
        log.info("Retrying SROTS startup");
        cancelCurrentTask();
        ApplicationBootstrap bootstrap = bootstrapRef.get();
        bootstrap.resetForRetry();
        splashViewModel.resetForRetry();
        runStartup();
    }

    private void exitApplication() {
        log.info("Exit requested from splash");
        cancelCurrentTask();
        splashWindow.closeImmediately();
        ShutdownCoordinator coordinator = shutdownRef.get();
        if (coordinator != null) {
            coordinator.shutdown();
        }
        startupExecutor.shutdownNow();
        Platform.exit();
    }

    private void cancelCurrentTask() {
        SrotsStartupTask current = taskRef.getAndSet(null);
        if (current != null) {
            current.requestCancel();
        }
    }

    public SrotsSplashWindow splashWindow() {
        return splashWindow;
    }

    public SrotsSplashViewModel splashViewModel() {
        return splashViewModel;
    }
}
