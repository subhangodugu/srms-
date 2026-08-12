package com.srots.presentation.components.information.activity;

import java.util.List;
import javafx.scene.layout.VBox;

/** Vertical activity feed. Presentation only. */
public class SrotsActivityFeed extends VBox {

    public SrotsActivityFeed() {
        super(10);
        getStyleClass().add("srots-activity-feed");
    }

    public void setItems(List<SrotsActivityItem> items) {
        getChildren().clear();
        if (items != null) {
            getChildren().addAll(items);
        }
    }
}
