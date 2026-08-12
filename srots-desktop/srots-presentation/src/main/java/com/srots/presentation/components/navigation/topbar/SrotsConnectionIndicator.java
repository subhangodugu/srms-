package com.srots.presentation.components.navigation.topbar;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

/**
 * Visual connection/sync state indicator. Presentation only.
 * Bound to {@link SrotsConnectionState} — never hardcodes Online.
 */
public class SrotsConnectionIndicator extends HBox {

    private final ObjectProperty<SrotsConnectionState> state =
            new SimpleObjectProperty<>(SrotsConnectionState.OFFLINE);
    private final Region dot = new Region();
    private final Label label = new Label();

    public SrotsConnectionIndicator() {
        getStyleClass().add("srots-connection-indicator");
        setAlignment(Pos.CENTER_LEFT);
        setSpacing(6);

        dot.getStyleClass().add("srots-connection-dot");
        label.getStyleClass().add("srots-connection-label");

        getChildren().addAll(dot, label);
        state.addListener((obs, oldState, newState) -> applyState(newState));
        applyState(state.get());
    }

    public SrotsConnectionIndicator(SrotsConnectionState initial) {
        this();
        setState(initial == null ? SrotsConnectionState.OFFLINE : initial);
    }

    public ObjectProperty<SrotsConnectionState> stateProperty() {
        return state;
    }

    public SrotsConnectionState getState() {
        return state.get();
    }

    public void setState(SrotsConnectionState value) {
        state.set(value == null ? SrotsConnectionState.OFFLINE : value);
    }

    /** Status caption for accessibility and smoke tests. */
    public String getText() {
        return label.getText();
    }

    public Label getLabel() {
        return label;
    }

    private void applyState(SrotsConnectionState value) {
        getStyleClass().removeAll(
                "srots-online", "srots-offline", "srots-syncing", "srots-sync-error", "srots-connection-unknown");
        SrotsConnectionState safe = value == null ? SrotsConnectionState.UNKNOWN : value;
        switch (safe) {
            case ONLINE -> {
                label.setText("Connected");
                getStyleClass().add("srots-online");
            }
            case OFFLINE -> {
                label.setText("Disconnected");
                getStyleClass().add("srots-offline");
            }
            case SYNCING -> {
                label.setText("Reconnecting...");
                getStyleClass().add("srots-syncing");
            }
            case SYNC_ERROR -> {
                label.setText("Connection unavailable");
                getStyleClass().add("srots-sync-error");
            }
            case UNKNOWN -> {
                label.setText("Connection unavailable");
                getStyleClass().add("srots-connection-unknown");
            }
        }
        setAccessibleText("Connection status: " + label.getText());
    }
}
