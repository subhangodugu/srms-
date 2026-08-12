package com.srots.presentation.components.information.activity;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/** Single activity feed row: title + relative time meta. */
public class SrotsActivityItem extends VBox {

    private final Label titleLabel = new Label();
    private final Label metaLabel = new Label();

    public SrotsActivityItem(String title, String relativeTime) {
        super(2);
        getStyleClass().add("srots-activity-item");

        titleLabel.getStyleClass().add("srots-activity-title");
        metaLabel.getStyleClass().add("srots-activity-meta");

        setTitle(title);
        setRelativeTime(relativeTime);
        getChildren().addAll(titleLabel, metaLabel);
    }

    public void setTitle(String title) {
        titleLabel.setText(title == null ? "" : title);
    }

    public void setRelativeTime(String relativeTime) {
        metaLabel.setText(relativeTime == null ? "" : relativeTime);
    }
}
