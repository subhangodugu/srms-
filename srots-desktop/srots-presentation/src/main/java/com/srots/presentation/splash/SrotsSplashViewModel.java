package com.srots.presentation.splash;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Observable splash presentation state. No bootstrap or business logic.
 */
public final class SrotsSplashViewModel {

    private final ObjectProperty<StartupPhase> phase =
            new SimpleObjectProperty<>(this, "phase", StartupPhase.BOOTSTRAP);
    private final DoubleProperty progress = new SimpleDoubleProperty(this, "progress", 0.0);
    private final BooleanProperty indeterminate = new SimpleBooleanProperty(this, "indeterminate", false);
    private final StringProperty statusMessage =
            new SimpleStringProperty(this, "statusMessage", StartupPhase.BOOTSTRAP.defaultMessage());
    private final StringProperty version = new SimpleStringProperty(this, "version", "");
    private final StringProperty subtitle =
            new SimpleStringProperty(this, "subtitle", "Enterprise Desktop Platform");
    private final StringProperty environmentBadge = new SimpleStringProperty(this, "environmentBadge", "");
    private final BooleanProperty failed = new SimpleBooleanProperty(this, "failed", false);
    private final StringProperty errorMessage = new SimpleStringProperty(this, "errorMessage", "");
    private final ObjectProperty<SplashLifecycleState> lifecycleState =
            new SimpleObjectProperty<>(this, "lifecycleState", SplashLifecycleState.CREATED);
    private final ObjectProperty<StartupTaskState> taskState =
            new SimpleObjectProperty<>(this, "taskState", StartupTaskState.IDLE);

    public void apply(StartupProgress update) {
        if (update == null) {
            return;
        }
        phase.set(update.phase());
        progress.set(update.progress());
        statusMessage.set(update.message());
        failed.set(update.phase() == StartupPhase.FAILED);
        if (update.phase() == StartupPhase.FAILED) {
            errorMessage.set(update.message());
            lifecycleState.set(SplashLifecycleState.FAILED);
            taskState.set(StartupTaskState.FAILED);
        } else {
            errorMessage.set("");
        }
    }

    public void resetForRetry() {
        failed.set(false);
        errorMessage.set("");
        indeterminate.set(false);
        apply(StartupProgress.of(StartupPhase.BOOTSTRAP));
        lifecycleState.set(SplashLifecycleState.INITIALIZING);
        taskState.set(StartupTaskState.IDLE);
    }

    public ObjectProperty<StartupPhase> phaseProperty() { return phase; }
    public StartupPhase getPhase() { return phase.get(); }
    public void setPhase(StartupPhase value) { phase.set(value); }

    public DoubleProperty progressProperty() { return progress; }
    public double getProgress() { return progress.get(); }
    public void setProgress(double value) { progress.set(value); }

    public BooleanProperty indeterminateProperty() { return indeterminate; }
    public boolean isIndeterminate() { return indeterminate.get(); }
    public void setIndeterminate(boolean value) { indeterminate.set(value); }

    public StringProperty statusMessageProperty() { return statusMessage; }
    public String getStatusMessage() { return statusMessage.get(); }
    public void setStatusMessage(String value) { statusMessage.set(value); }

    public StringProperty versionProperty() { return version; }
    public String getVersion() { return version.get(); }
    public void setVersion(String value) { version.set(value); }

    public StringProperty subtitleProperty() { return subtitle; }
    public String getSubtitle() { return subtitle.get(); }
    public void setSubtitle(String value) { subtitle.set(value); }

    public StringProperty environmentBadgeProperty() { return environmentBadge; }
    public String getEnvironmentBadge() { return environmentBadge.get(); }
    public void setEnvironmentBadge(String value) { environmentBadge.set(value); }

    public BooleanProperty failedProperty() { return failed; }
    public boolean isFailed() { return failed.get(); }

    public StringProperty errorMessageProperty() { return errorMessage; }
    public String getErrorMessage() { return errorMessage.get(); }

    public ObjectProperty<SplashLifecycleState> lifecycleStateProperty() { return lifecycleState; }
    public SplashLifecycleState getLifecycleState() { return lifecycleState.get(); }
    public void setLifecycleState(SplashLifecycleState value) { lifecycleState.set(value); }

    public ObjectProperty<StartupTaskState> taskStateProperty() { return taskState; }
    public StartupTaskState getTaskState() { return taskState.get(); }
    public void setTaskState(StartupTaskState value) { taskState.set(value); }
}
