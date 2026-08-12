package com.srots.presentation.components.feedback.empty;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/** Empty-state panel with optional primary/secondary actions. Callbacks only. */
public class SrotsEmptyState extends VBox {

    private final Label titleLabel = new Label();
    private final Label descriptionLabel = new Label();
    private final Button primaryButton = new Button();
    private final Button secondaryButton = new Button();
    private final HBox actions = new HBox(10);

    public SrotsEmptyState() {
        this("Nothing here", null);
    }

    public SrotsEmptyState(String title, String description) {
        getStyleClass().add("srots-empty-state");
        setAlignment(Pos.CENTER);
        setSpacing(12);

        titleLabel.getStyleClass().add("srots-state-title");
        descriptionLabel.getStyleClass().add("srots-state-message");
        descriptionLabel.setWrapText(true);
        descriptionLabel.setMaxWidth(420);

        primaryButton.getStyleClass().addAll("srots-button", "srots-primary-button");
        secondaryButton.getStyleClass().addAll("srots-button", "srots-secondary-button");
        primaryButton.setVisible(false);
        primaryButton.setManaged(false);
        secondaryButton.setVisible(false);
        secondaryButton.setManaged(false);

        actions.setAlignment(Pos.CENTER);
        actions.getChildren().addAll(primaryButton, secondaryButton);

        getChildren().addAll(titleLabel, descriptionLabel, actions);
        setTitle(title);
        setDescription(description);
    }

    public void setTitle(String title) {
        titleLabel.setText(title == null ? "" : title);
    }

    public void setDescription(String description) {
        descriptionLabel.setText(description == null ? "" : description);
        boolean show = description != null && !description.isBlank();
        descriptionLabel.setVisible(show);
        descriptionLabel.setManaged(show);
    }

    public void setPrimaryAction(String label, Runnable onAction) {
        configureAction(primaryButton, label, onAction);
    }

    public void setSecondaryAction(String label, Runnable onAction) {
        configureAction(secondaryButton, label, onAction);
    }

    private static void configureAction(Button button, String label, Runnable onAction) {
        boolean show = label != null && !label.isBlank();
        button.setText(show ? label : "");
        button.setVisible(show);
        button.setManaged(show);
        button.setOnAction(e -> {
            if (onAction != null) {
                onAction.run();
            }
        });
    }
}
