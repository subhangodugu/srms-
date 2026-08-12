package com.srots.presentation.components.feedback.offline;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Offline / sync presentation state. No network or DB logic.
 */
public class SrotsOfflineState extends VBox {

    public enum OfflineMode {
        OFFLINE("You're offline", "Changes will sync when connection returns.", "srots-offline"),
        SYNCING("Syncing…", "Uploading local changes.", "srots-syncing"),
        SYNC_FAILED("Sync failed", "Could not sync local changes. Retry when online.", "srots-sync-error"),
        LOCAL_CHANGES("Local changes pending", "You have unsynced edits on this device.", "srots-local-changes");

        private final String title;
        private final String description;
        private final String statusClass;

        OfflineMode(String title, String description, String statusClass) {
            this.title = title;
            this.description = description;
            this.statusClass = statusClass;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public String getStatusClass() {
            return statusClass;
        }
    }

    private final Label titleLabel = new Label();
    private final Label descriptionLabel = new Label();
    private OfflineMode mode = OfflineMode.OFFLINE;

    public SrotsOfflineState() {
        this(OfflineMode.OFFLINE);
    }

    public SrotsOfflineState(OfflineMode mode) {
        getStyleClass().addAll("srots-empty-state", "srots-permission-denied");
        setAlignment(Pos.CENTER);
        setSpacing(12);

        titleLabel.getStyleClass().add("srots-state-title");
        descriptionLabel.getStyleClass().add("srots-state-message");
        descriptionLabel.setWrapText(true);
        descriptionLabel.setMaxWidth(420);

        getChildren().addAll(titleLabel, descriptionLabel);
        setMode(mode);
    }

    public void setMode(OfflineMode mode) {
        if (mode == null) {
            mode = OfflineMode.OFFLINE;
        }
        if (this.mode != null) {
            titleLabel.getStyleClass().remove(this.mode.getStatusClass());
        }
        this.mode = mode;
        titleLabel.setText(mode.getTitle());
        descriptionLabel.setText(mode.getDescription());
        if (!titleLabel.getStyleClass().contains(mode.getStatusClass())) {
            titleLabel.getStyleClass().add(mode.getStatusClass());
        }
    }

    public OfflineMode getMode() {
        return mode;
    }
}
