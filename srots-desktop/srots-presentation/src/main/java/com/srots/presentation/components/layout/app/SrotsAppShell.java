package com.srots.presentation.components.layout.app;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

/**
 * Application chrome shell: sidebar, top bar, content host, and status bar slots.
 * Presentation only — no routing or auth logic.
 */
public class SrotsAppShell extends BorderPane {

    private final StackPane contentHost = new StackPane();

    public SrotsAppShell() {
        getStyleClass().add("srots-shell");
        contentHost.getStyleClass().add("srots-content");
        setCenter(contentHost);
    }

    public void setSidebar(Node sidebar) {
        setLeft(sidebar);
    }

    public void setTopBar(Node topBar) {
        setTop(topBar);
    }

    public void setStatusBar(Node statusBar) {
        setBottom(statusBar);
    }

    public void setContent(Node content) {
        contentHost.getChildren().setAll(content == null ? java.util.List.of() : java.util.List.of(content));
    }

    public StackPane getContentHost() {
        return contentHost;
    }
}
