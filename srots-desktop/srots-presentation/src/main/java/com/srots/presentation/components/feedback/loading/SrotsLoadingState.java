package com.srots.presentation.components.feedback.loading;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;

/** Compact loading state panel. Presentation only. */
public class SrotsLoadingState extends VBox {

    private final Label messageLabel = new Label();

    public SrotsLoadingState() {
        this("Loading…");
    }

    public SrotsLoadingState(String message) {
        getStyleClass().add("srots-empty-state");
        setAlignment(Pos.CENTER);
        setSpacing(12);

        ProgressIndicator indicator = new ProgressIndicator();
        indicator.setPrefSize(36, 36);

        Label title = new Label("Loading");
        title.getStyleClass().add("srots-state-title");

        messageLabel.getStyleClass().add("srots-state-message");
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(420);
        setMessage(message);

        getChildren().addAll(indicator, title, messageLabel);
    }

    public void setMessage(String message) {
        messageLabel.setText(message == null || message.isBlank() ? "Please wait…" : message);
    }
}
