package com.srots.presentation.components.navigation.topbar;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Label;

/** Sync presentation indicator — no network logic. */
public class SrotsSyncIndicator extends Label {

    private final ObjectProperty<SrotsConnectionState> state =
            new SimpleObjectProperty<>(SrotsConnectionState.ONLINE);

    public SrotsSyncIndicator() {
        getStyleClass().add("srots-caption");
        state.addListener((o, a, b) -> refresh(b));
        refresh(SrotsConnectionState.ONLINE);
    }

    public ObjectProperty<SrotsConnectionState> stateProperty() {
        return state;
    }

    public void setState(SrotsConnectionState value) {
        state.set(value == null ? SrotsConnectionState.ONLINE : value);
    }

    public SrotsConnectionState getState() {
        return state.get();
    }

    private void refresh(SrotsConnectionState value) {
        getStyleClass().removeAll("srots-online", "srots-offline", "srots-syncing", "srots-sync-error");
        switch (value) {
            case ONLINE -> {
                setText("Last synchronized: just now");
                getStyleClass().add("srots-online");
            }
            case SYNCING -> {
                setText("Synchronizing...");
                getStyleClass().add("srots-syncing");
            }
            case OFFLINE -> {
                setText("Offline — local cache only");
                getStyleClass().add("srots-offline");
            }
            case SYNC_ERROR -> {
                setText("Sync error");
                getStyleClass().add("srots-sync-error");
            }
        }
    }
}
