package com.srots.presentation.components.layout.split;

import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.StackPane;

/**
 * Master/detail split layout. Presentation only.
 */
public class SrotsSplitView extends SplitPane {

    private final StackPane masterHost = new StackPane();
    private final StackPane detailHost = new StackPane();

    public SrotsSplitView() {
        getStyleClass().add("srots-split-view");
        detailHost.getStyleClass().add("srots-split-detail");
        getItems().addAll(masterHost, detailHost);
        setDividerPositions(0.35);
    }

    public void setMaster(Node master) {
        masterHost.getChildren().setAll(master == null ? java.util.List.of() : java.util.List.of(master));
    }

    public void setDetail(Node detail) {
        detailHost.getChildren().setAll(detail == null ? java.util.List.of() : java.util.List.of(detail));
    }
}
