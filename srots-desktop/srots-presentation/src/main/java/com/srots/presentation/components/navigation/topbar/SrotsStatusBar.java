package com.srots.presentation.components.navigation.topbar;

import com.srots.presentation.shell.statusbar.SrotsStatusBarState;
import com.srots.presentation.shell.statusbar.SrotsStatusBarViewModel;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.css.PseudoClass;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

/**
 * Persistent application status bar: connection, activity, environment/version.
 * Presentation only — no DB / REST / auth / business operations.
 */
public class SrotsStatusBar extends HBox {

    private static final double COMPACT_BREAKPOINT = 1180;
    private static final PseudoClass READY = PseudoClass.getPseudoClass("ready");
    private static final PseudoClass BUSY = PseudoClass.getPseudoClass("busy");
    private static final PseudoClass SYNCING = PseudoClass.getPseudoClass("syncing");
    private static final PseudoClass OFFLINE = PseudoClass.getPseudoClass("offline");
    private static final PseudoClass ERROR = PseudoClass.getPseudoClass("error");
    private static final PseudoClass DEGRADED = PseudoClass.getPseudoClass("degraded");

    private final ObjectProperty<SrotsStatusBarViewModel> viewModel = new SimpleObjectProperty<>();

    private final HBox leftRegion = new HBox(8);
    private final HBox centerRegion = new HBox(8);
    private final HBox rightRegion = new HBox(8);
    private final SrotsConnectionIndicator connectionIndicator = new SrotsConnectionIndicator();
    private final Label activityLabel = new Label("Ready");
    private final ProgressBar progressBar = new ProgressBar();
    private final ProgressIndicator indeterminate = new ProgressIndicator();
    private final Label environmentLabel = new Label();
    private final Region spacer = new Region();

    private ChangeListener<Number> widthListener;
    private InvalidationListener connectionListener;
    private InvalidationListener activityTextListener;
    private InvalidationListener environmentListener;
    private InvalidationListener barStateListener;
    private InvalidationListener progressListener;

    public SrotsStatusBar() {
        getStyleClass().addAll("srots-statusbar", "srots-status-bar");
        setAlignment(Pos.CENTER_LEFT);
        setSpacing(12);
        setPadding(new Insets(0, 24, 0, 24));
        setMinHeight(28);
        setPrefHeight(32);
        setMaxHeight(32);
        setAccessibleText("Status bar");

        leftRegion.getStyleClass().add("srots-status-left");
        leftRegion.setAlignment(Pos.CENTER_LEFT);
        connectionIndicator.getStyleClass().addAll("srots-status-connection", "srots-status-indicator");
        connectionIndicator.setAccessibleText("Connection status");
        leftRegion.getChildren().add(connectionIndicator);

        centerRegion.getStyleClass().add("srots-status-center");
        centerRegion.setAlignment(Pos.CENTER_LEFT);
        activityLabel.getStyleClass().addAll("srots-status-activity", "srots-caption");
        activityLabel.setMaxWidth(420);
        activityLabel.setTextOverrun(javafx.scene.control.OverrunStyle.ELLIPSIS);

        progressBar.getStyleClass().add("srots-status-progress");
        progressBar.setPrefWidth(72);
        progressBar.setMaxHeight(4);
        progressBar.setVisible(false);
        progressBar.setManaged(false);

        indeterminate.getStyleClass().add("srots-status-progress-indeterminate");
        indeterminate.setPrefSize(14, 14);
        indeterminate.setMaxSize(14, 14);
        indeterminate.setVisible(false);
        indeterminate.setManaged(false);

        centerRegion.getChildren().addAll(progressBar, indeterminate, activityLabel);

        rightRegion.getStyleClass().add("srots-status-right");
        rightRegion.setAlignment(Pos.CENTER_RIGHT);
        environmentLabel.getStyleClass().addAll("srots-status-environment", "srots-status-version");
        environmentLabel.setAccessibleText("Environment and version");
        rightRegion.getChildren().add(environmentLabel);

        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox.setHgrow(centerRegion, Priority.ALWAYS);

        getChildren().addAll(leftRegion, centerRegion, spacer, rightRegion);
        applyPseudoState(SrotsStatusBarState.READY);

        widthListener = (obs, o, width) -> {
            SrotsStatusBarViewModel model = viewModel.get();
            if (model != null) {
                model.setCompactMode(width != null && width.doubleValue() < COMPACT_BREAKPOINT);
            }
        };
        widthProperty().addListener(widthListener);
    }

    public void setViewModel(SrotsStatusBarViewModel model) {
        detachViewModel();
        viewModel.set(model);
        if (model == null) {
            return;
        }

        connectionListener = obs -> connectionIndicator.setState(model.getConnectionState());
        model.connectionStateProperty().addListener(connectionListener);
        connectionIndicator.setState(model.getConnectionState());

        activityTextListener = obs -> applyActivityText(model.getActivityText());
        model.activityTextProperty().addListener(activityTextListener);
        applyActivityText(model.getActivityText());

        environmentListener = obs -> {
            environmentLabel.setText(nullToEmpty(model.getEnvironmentText()));
            boolean show = model.getEnvironmentText() != null && !model.getEnvironmentText().isBlank();
            environmentLabel.setVisible(show);
            environmentLabel.setManaged(show);
        };
        model.environmentTextProperty().addListener(environmentListener);
        environmentLabel.setText(nullToEmpty(model.getEnvironmentText()));

        barStateListener = obs -> applyPseudoState(model.getBarState());
        model.barStateProperty().addListener(barStateListener);
        applyPseudoState(model.getBarState());

        progressListener = obs -> applyProgress(model);
        model.progressProperty().addListener(progressListener);
        model.barStateProperty().addListener(progressListener);
        applyProgress(model);

        visibleProperty().bind(model.visibleProperty());
    }

    public SrotsStatusBarViewModel getViewModel() {
        return viewModel.get();
    }

    public SrotsConnectionIndicator getConnectionIndicator() {
        return connectionIndicator;
    }

    public Label getActivityLabel() {
        return activityLabel;
    }

    public Label getEnvironmentLabel() {
        return environmentLabel;
    }

    public void dispose() {
        detachViewModel();
        if (widthListener != null) {
            widthProperty().removeListener(widthListener);
            widthListener = null;
        }
    }

    private void detachViewModel() {
        SrotsStatusBarViewModel model = viewModel.get();
        if (model == null) {
            return;
        }
        if (connectionListener != null) {
            model.connectionStateProperty().removeListener(connectionListener);
        }
        if (activityTextListener != null) {
            model.activityTextProperty().removeListener(activityTextListener);
        }
        if (environmentListener != null) {
            model.environmentTextProperty().removeListener(environmentListener);
        }
        if (barStateListener != null) {
            model.barStateProperty().removeListener(barStateListener);
        }
        if (progressListener != null) {
            model.progressProperty().removeListener(progressListener);
            model.barStateProperty().removeListener(progressListener);
        }
        visibleProperty().unbind();
        connectionListener = null;
        activityTextListener = null;
        environmentListener = null;
        barStateListener = null;
        progressListener = null;
    }

    private void applyActivityText(String text) {
        String value = nullToEmpty(text);
        activityLabel.setText(value);
        activityLabel.setAccessibleText("Current operation " + value);
        if (!value.isBlank()) {
            activityLabel.setTooltip(new Tooltip(value));
        } else {
            activityLabel.setTooltip(null);
        }
    }

    private void applyProgress(SrotsStatusBarViewModel model) {
        SrotsStatusBarState state = model.getBarState();
        boolean active = state == SrotsStatusBarState.BUSY || state == SrotsStatusBarState.SYNCING;
        if (!active) {
            progressBar.setVisible(false);
            progressBar.setManaged(false);
            indeterminate.setVisible(false);
            indeterminate.setManaged(false);
            return;
        }
        if (model.hasDeterminateProgress()) {
            progressBar.setProgress(model.getProgress());
            progressBar.setVisible(true);
            progressBar.setManaged(true);
            indeterminate.setVisible(false);
            indeterminate.setManaged(false);
        } else {
            progressBar.setVisible(false);
            progressBar.setManaged(false);
            indeterminate.setVisible(true);
            indeterminate.setManaged(true);
        }
    }

    private void applyPseudoState(SrotsStatusBarState state) {
        SrotsStatusBarState safe = state == null ? SrotsStatusBarState.READY : state;
        pseudoClassStateChanged(READY, safe == SrotsStatusBarState.READY);
        pseudoClassStateChanged(BUSY, safe == SrotsStatusBarState.BUSY);
        pseudoClassStateChanged(SYNCING, safe == SrotsStatusBarState.SYNCING);
        pseudoClassStateChanged(OFFLINE, safe == SrotsStatusBarState.OFFLINE);
        pseudoClassStateChanged(ERROR, safe == SrotsStatusBarState.ERROR);
        pseudoClassStateChanged(DEGRADED, safe == SrotsStatusBarState.DEGRADED);
        getStyleClass().removeAll(
                "srots-status-ready",
                "srots-status-busy",
                "srots-status-syncing",
                "srots-status-offline",
                "srots-status-error",
                "srots-status-degraded");
        getStyleClass().add("srots-status-" + safe.name().toLowerCase());
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}
