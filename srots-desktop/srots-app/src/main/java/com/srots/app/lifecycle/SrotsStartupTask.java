package com.srots.app.lifecycle;

import com.srots.app.bootstrap.AppContainer;
import com.srots.app.bootstrap.ApplicationBootstrap;
import com.srots.app.bootstrap.StartupException;
import com.srots.presentation.splash.StartupPhase;
import com.srots.presentation.splash.StartupProgress;
import com.srots.presentation.splash.StartupTaskState;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * Background startup task. Publishes progress; does not mutate JavaFX nodes.
 */
public final class SrotsStartupTask extends Task<AppContainer> {

    private static final Logger log = LoggerFactory.getLogger(SrotsStartupTask.class);

    private final ApplicationBootstrap bootstrap;
    private final Consumer<StartupProgress> progressConsumer;
    private final AtomicBoolean cancelledFlag = new AtomicBoolean(false);

    public SrotsStartupTask(ApplicationBootstrap bootstrap, Consumer<StartupProgress> progressConsumer) {
        this.bootstrap = Objects.requireNonNull(bootstrap, "bootstrap");
        this.progressConsumer = progressConsumer == null ? p -> {
        } : progressConsumer;
    }

    @Override
    protected AppContainer call() {
        if (isCancelled() || cancelledFlag.get()) {
            return null;
        }
        return bootstrap.bootstrap(progress -> {
            if (isCancelled() || cancelledFlag.get()) {
                throw new StartupException("Startup cancelled.");
            }
            progressConsumer.accept(progress);
            updateMessage(progress.message());
            updateProgress(progress.progress(), 1.0);
        });
    }

    public void requestCancel() {
        cancelledFlag.set(true);
        cancel(true);
    }

    public StartupTaskState mapState() {
        if (isCancelled() || cancelledFlag.get()) {
            return StartupTaskState.CANCELLED;
        }
        Worker.State state = getState();
        if (state == Worker.State.RUNNING || state == Worker.State.SCHEDULED) {
            return StartupTaskState.RUNNING;
        }
        if (state == Worker.State.SUCCEEDED) {
            return StartupTaskState.COMPLETED;
        }
        if (state == Worker.State.FAILED) {
            return StartupTaskState.FAILED;
        }
        return StartupTaskState.IDLE;
    }

    public static String safeFailureMessage(Throwable error) {
        if (error instanceof StartupException startupException) {
            return startupException.userMessage();
        }
        Throwable cause = error;
        while (cause.getCause() != null && cause.getCause() != cause) {
            if (cause instanceof StartupException startupException) {
                return startupException.userMessage();
            }
            cause = cause.getCause();
        }
        if (cause instanceof StartupException startupException) {
            return startupException.userMessage();
        }
        return "Unable to initialize the application.";
    }

    public static StartupProgress failureProgress(Throwable error) {
        log.error("SROTS startup failed", error);
        return StartupProgress.failed(safeFailureMessage(error));
    }
}
