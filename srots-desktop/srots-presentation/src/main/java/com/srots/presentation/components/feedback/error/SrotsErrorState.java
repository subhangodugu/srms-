package com.srots.presentation.components.feedback.error;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/** Error-state panel with Retry action. Presentation only. */
public class SrotsErrorState extends VBox {

    private final Label titleLabel = new Label();
    private final Label descriptionLabel = new Label();
    private final Button retryButton = new Button("Retry");

    public SrotsErrorState() {
        this("Something went wrong", null, null);
    }

    public SrotsErrorState(String title, String description, Runnable onRetry) {
        getStyleClass().add("srots-error-state");
        setAlignment(Pos.CENTER);
        setSpacing(12);

        titleLabel.getStyleClass().add("srots-state-title");
        descriptionLabel.getStyleClass().add("srots-state-message");
        descriptionLabel.setWrapText(true);
        descriptionLabel.setMaxWidth(420);

        retryButton.getStyleClass().addAll("srots-button", "srots-secondary-button");
        setOnRetry(onRetry);

        getChildren().addAll(titleLabel, descriptionLabel, retryButton);
        setTitle(title);
        setDescription(description);
    }

    public void setTitle(String title) {
        titleLabel.setText(title == null ? "Something went wrong" : title);
    }

    public void setDescription(String description) {
        descriptionLabel.setText(description == null ? "" : description);
        boolean show = description != null && !description.isBlank();
        descriptionLabel.setVisible(show);
        descriptionLabel.setManaged(show);
    }

    public void setOnRetry(Runnable onRetry) {
        boolean show = onRetry != null;
        retryButton.setVisible(show);
        retryButton.setManaged(show);
        retryButton.setOnAction(e -> {
            if (onRetry != null) {
                onRetry.run();
            }
        });
    }
}
