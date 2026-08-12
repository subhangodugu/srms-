package com.srots.presentation.shell.statusbar;

import com.srots.presentation.components.navigation.topbar.SrotsConnectionState;
import com.srots.presentation.shell.topbar.TopBarApplicationState;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;

/**
 * Presentation state for {@link com.srots.presentation.components.navigation.topbar.SrotsStatusBar}.
 * Consumes connection / activity / environment abstractions — no I/O.
 */
public final class SrotsStatusBarViewModel {

    private final ObjectProperty<SrotsConnectionState> connectionState =
            new SimpleObjectProperty<>(SrotsConnectionState.ONLINE);
    private final ObjectProperty<ApplicationActivity> activity =
            new SimpleObjectProperty<>(ApplicationActivity.idle());
    private final ObjectProperty<SrotsStatusBarState> barState =
            new SimpleObjectProperty<>(SrotsStatusBarState.READY);
    private final StringProperty activityText = new SimpleStringProperty("Ready");
    private final StringProperty environmentText = new SimpleStringProperty("");
    private final StringProperty versionText = new SimpleStringProperty("");
    private final DoubleProperty progress = new SimpleDoubleProperty(-1);
    private final BooleanProperty compactMode = new SimpleBooleanProperty(false);
    private final BooleanProperty visible = new SimpleBooleanProperty(true);

    private TopBarApplicationState applicationState;
    private ApplicationActivityService activityService;
    private StatusBarEnvironmentInfo environmentInfo =
            StatusBarEnvironmentInfo.of("development", "MOCK", "0.1.0", false);

    private ChangeListener<SrotsConnectionState> connectionListener;
    private ChangeListener<ApplicationActivity> activityListener;

    public void bind(
            TopBarApplicationState applicationState,
            ApplicationActivityService activityService,
            StatusBarEnvironmentInfo environmentInfo) {
        detach();
        this.applicationState = applicationState;
        this.activityService = activityService == null ? new ApplicationActivityService() : activityService;
        this.environmentInfo = environmentInfo == null
                ? StatusBarEnvironmentInfo.of("development", "MOCK", "0.1.0", false)
                : environmentInfo;

        if (this.applicationState != null) {
            connectionListener = (obs, o, state) -> applyConnection(state);
            this.applicationState.connectionStateProperty().addListener(connectionListener);
            applyConnection(this.applicationState.getConnectionState());
        } else {
            applyConnection(SrotsConnectionState.UNKNOWN);
        }

        activityListener = (obs, o, next) -> applyActivity(next);
        this.activityService.currentActivityProperty().addListener(activityListener);
        applyActivity(this.activityService.getCurrentActivity());

        refreshEnvironmentText();
        compactMode.addListener((obs, o, compact) -> refreshEnvironmentText());
    }

    public void detach() {
        if (applicationState != null && connectionListener != null) {
            applicationState.connectionStateProperty().removeListener(connectionListener);
        }
        if (activityService != null && activityListener != null) {
            activityService.currentActivityProperty().removeListener(activityListener);
        }
        connectionListener = null;
        activityListener = null;
    }

    public void setEnvironmentInfo(StatusBarEnvironmentInfo info) {
        this.environmentInfo = info == null
                ? StatusBarEnvironmentInfo.of("development", "MOCK", "0.1.0", false)
                : info;
        refreshEnvironmentText();
    }

    public void setCompactMode(boolean compact) {
        compactMode.set(compact);
    }

    public ObjectProperty<SrotsConnectionState> connectionStateProperty() {
        return connectionState;
    }

    public SrotsConnectionState getConnectionState() {
        return connectionState.get() == null ? SrotsConnectionState.UNKNOWN : connectionState.get();
    }

    public ObjectProperty<ApplicationActivity> activityProperty() {
        return activity;
    }

    public ApplicationActivity getActivity() {
        return activity.get() == null ? ApplicationActivity.idle() : activity.get();
    }

    public ObjectProperty<SrotsStatusBarState> barStateProperty() {
        return barState;
    }

    public SrotsStatusBarState getBarState() {
        return barState.get() == null ? SrotsStatusBarState.READY : barState.get();
    }

    public StringProperty activityTextProperty() {
        return activityText;
    }

    public String getActivityText() {
        return activityText.get();
    }

    public StringProperty environmentTextProperty() {
        return environmentText;
    }

    public String getEnvironmentText() {
        return environmentText.get();
    }

    public StringProperty versionTextProperty() {
        return versionText;
    }

    public String getVersionText() {
        return versionText.get();
    }

    public DoubleProperty progressProperty() {
        return progress;
    }

    public double getProgress() {
        return progress.get();
    }

    public boolean hasDeterminateProgress() {
        return progress.get() >= 0.0 && progress.get() <= 1.0;
    }

    public BooleanProperty compactModeProperty() {
        return compactMode;
    }

    public BooleanProperty visibleProperty() {
        return visible;
    }

    private void applyConnection(SrotsConnectionState state) {
        connectionState.set(state == null ? SrotsConnectionState.UNKNOWN : state);
        recomputeState();
    }

    private void applyActivity(ApplicationActivity next) {
        ApplicationActivity safe = next == null ? ApplicationActivity.idle() : next;
        activity.set(safe);
        activityText.set(resolveActivityText(safe));
        if (safe.hasDeterminateProgress()) {
            progress.set(safe.getProgress());
        } else {
            progress.set(-1);
        }
        recomputeState();
    }

    private void recomputeState() {
        SrotsConnectionState connection = getConnectionState();
        ApplicationActivity current = getActivity();
        boolean offline = connection == SrotsConnectionState.OFFLINE;
        boolean degraded = connection == SrotsConnectionState.SYNC_ERROR
                || connection == SrotsConnectionState.UNKNOWN;
        boolean syncing = connection == SrotsConnectionState.SYNCING
                || current.getType() == ApplicationActivityType.SYNCING;
        boolean busy = !current.isIdle()
                && current.getType() != ApplicationActivityType.ERROR
                && current.getType() != ApplicationActivityType.SYNCING;
        boolean error = current.isFailed() || current.getType() == ApplicationActivityType.ERROR;

        SrotsStatusBarState next = SrotsStatusBarState.resolve(error, offline, degraded, syncing, busy);
        barState.set(next);

        if (next == SrotsStatusBarState.OFFLINE && current.isIdle()) {
            activityText.set("Working offline");
        } else if (next == SrotsStatusBarState.DEGRADED && current.isIdle()) {
            activityText.set("Limited connectivity");
        } else if (next == SrotsStatusBarState.READY && current.isIdle()) {
            activityText.set("Ready");
        }
    }

    private void refreshEnvironmentText() {
        boolean compact = compactMode.get();
        environmentText.set(environmentInfo.formatRightText(compact));
        versionText.set(environmentInfo.versionLabel());
    }

    static String resolveActivityText(ApplicationActivity activity) {
        if (activity == null) {
            return "Ready";
        }
        if (activity.getMessage() != null && !activity.getMessage().isBlank()) {
            String message = activity.getMessage();
            if (activity.hasDeterminateProgress()) {
                int pct = (int) Math.round(activity.getProgress() * 100.0);
                if (!message.contains("%")) {
                    return message + " " + pct + "%";
                }
            }
            return message;
        }
        return switch (activity.getType()) {
            case IDLE -> "Ready";
            case SYNCING -> "Synchronizing...";
            case SAVING -> "Saving...";
            case LOADING -> "Loading...";
            case IMPORTING -> "Importing...";
            case EXPORTING -> "Exporting...";
            case DEPLOY -> "Deployment in progress...";
            case BUILD -> "Building...";
            case PROCESS, WORKING -> "Processing...";
            case ERROR -> "Operation failed";
        };
    }
}
