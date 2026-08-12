package com.srots.presentation.components.feedback.alert;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Compact inline alert with INFO / SUCCESS / WARNING / ERROR variants.
 * Presentation only — no business logic.
 */
public class SrotsAlert extends HBox {

    public enum Variant {
        INFO("srots-alert-info"),
        SUCCESS("srots-alert-success"),
        WARNING("srots-alert-warning"),
        ERROR("srots-alert-danger");

        private final String styleClass;

        Variant(String styleClass) {
            this.styleClass = styleClass;
        }

        public String getStyleClass() {
            return styleClass;
        }
    }

    private final Label titleLabel = new Label();
    private final Label descriptionLabel = new Label();
    private final Button actionButton = new Button();
    private final Button dismissButton = new Button("✕");
    private Runnable onDismiss;
    private Runnable onAction;

    public SrotsAlert() {
        this(Variant.INFO, "", null);
    }

    public SrotsAlert(Variant variant, String title, String description) {
        getStyleClass().addAll("srots-alert", variant.getStyleClass());
        setSpacing(12);
        setAlignment(Pos.CENTER_LEFT);

        titleLabel.getStyleClass().add("srots-state-title");
        descriptionLabel.getStyleClass().add("srots-state-message");
        descriptionLabel.setWrapText(true);

        VBox text = new VBox(4, titleLabel, descriptionLabel);
        HBox.setHgrow(text, Priority.ALWAYS);

        actionButton.getStyleClass().addAll("srots-button", "srots-secondary-button");
        actionButton.setVisible(false);
        actionButton.setManaged(false);
        actionButton.setOnAction(e -> {
            if (onAction != null) {
                onAction.run();
            }
        });

        dismissButton.getStyleClass().add("srots-icon-button");
        dismissButton.setVisible(false);
        dismissButton.setManaged(false);
        dismissButton.setOnAction(e -> {
            if (onDismiss != null) {
                onDismiss.run();
            } else {
                setVisible(false);
                setManaged(false);
            }
        });

        getChildren().addAll(text, actionButton, dismissButton);
        setTitle(title);
        setDescription(description);
    }

    public void setVariant(Variant variant) {
        getStyleClass().removeAll(
                "srots-alert-info", "srots-alert-success",
                "srots-alert-warning", "srots-alert-danger");
        if (variant != null) {
            getStyleClass().add(variant.getStyleClass());
        }
    }

    public void setTitle(String title) {
        titleLabel.setText(title == null ? "" : title);
        titleLabel.setVisible(title != null && !title.isBlank());
        titleLabel.setManaged(titleLabel.isVisible());
    }

    public void setDescription(String description) {
        descriptionLabel.setText(description == null ? "" : description);
        descriptionLabel.setVisible(description != null && !description.isBlank());
        descriptionLabel.setManaged(descriptionLabel.isVisible());
    }

    public void setAction(String label, Runnable onAction) {
        this.onAction = onAction;
        boolean show = label != null && !label.isBlank();
        actionButton.setText(show ? label : "");
        actionButton.setVisible(show);
        actionButton.setManaged(show);
    }

    public void setDismissible(boolean dismissible) {
        dismissButton.setVisible(dismissible);
        dismissButton.setManaged(dismissible);
    }

    public void setOnDismiss(Runnable onDismiss) {
        this.onDismiss = onDismiss;
    }

    /** Convenience factory. */
    public static SrotsAlert of(Variant variant, String title, String description) {
        return new SrotsAlert(variant, title, description);
    }
}
