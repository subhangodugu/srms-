package com.srots.presentation.components.layout.panel;

import javafx.scene.Node;
import javafx.scene.layout.VBox;

/**
 * Elevated surface panel for grouping related content.
 */
public class SrotsPanel extends VBox {

    public SrotsPanel() {
        getStyleClass().addAll("srots-surface-elevated", "srots-card");
        setSpacing(12);
    }

    public SrotsPanel(Node... children) {
        this();
        if (children != null && children.length > 0) {
            getChildren().addAll(children);
        }
    }
}
