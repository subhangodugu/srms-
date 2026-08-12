package com.srots.presentation.components.layout;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;

/**
 * Padded content host for page body regions.
 */
public class SrotsContentContainer extends StackPane {

    public SrotsContentContainer() {
        getStyleClass().add("srots-content");
    }

    public SrotsContentContainer(Node content) {
        this();
        setContent(content);
    }

    public void setContent(Node content) {
        getChildren().setAll(content == null ? java.util.List.of() : java.util.List.of(content));
    }
}
