package com.srots.presentation.components.layout.page;

import javafx.scene.Node;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Standard page layout with optional header and a growing content region.
 */
public class SrotsPageContainer extends VBox {

    private final StackPane headerRegion = new StackPane();
    private final StackPane contentRegion = new StackPane();

    public SrotsPageContainer() {
        getStyleClass().add("srots-page");
        headerRegion.setManaged(false);
        headerRegion.setVisible(false);
        VBox.setVgrow(contentRegion, Priority.ALWAYS);
        getChildren().addAll(headerRegion, contentRegion);
    }

    public void setHeader(Node header) {
        if (header == null) {
            headerRegion.getChildren().clear();
            headerRegion.setManaged(false);
            headerRegion.setVisible(false);
            return;
        }
        headerRegion.getChildren().setAll(header);
        headerRegion.setManaged(true);
        headerRegion.setVisible(true);
    }

    public void setContent(Node content) {
        contentRegion.getChildren().setAll(content == null ? java.util.List.of() : java.util.List.of(content));
    }
}
