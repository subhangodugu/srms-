package com.srots.presentation.components.overlays.drawer;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Side detail panel: header (title + close), content, footer actions.
 */
public class SrotsDetailPanel extends VBox {

    private final Label titleLabel = new Label();
    private final StackPane contentRegion = new StackPane();
    private final HBox footer = new HBox(10);
    private Runnable onClose;

    public SrotsDetailPanel() {
        this("");
    }

    public SrotsDetailPanel(String title) {
        getStyleClass().add("srots-detail-panel");
        setSpacing(16);

        titleLabel.getStyleClass().add("srots-section-title");
        setTitle(title);

        Button close = new Button("✕");
        close.getStyleClass().add("srots-icon-button");
        close.setOnAction(e -> {
            if (onClose != null) {
                onClose.run();
            } else {
                setVisible(false);
                setManaged(false);
            }
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox header = new HBox(8, titleLabel, spacer, close);
        header.setAlignment(Pos.CENTER_LEFT);

        VBox.setVgrow(contentRegion, Priority.ALWAYS);
        footer.setAlignment(Pos.CENTER_RIGHT);

        getChildren().addAll(header, contentRegion, footer);
    }

    public void setTitle(String title) {
        titleLabel.setText(title == null ? "" : title);
    }

    public void setContent(Node content) {
        contentRegion.getChildren().setAll(content == null ? java.util.List.of() : java.util.List.of(content));
    }

    public void setFooterActions(Node... actions) {
        footer.getChildren().setAll(actions == null ? java.util.List.of() : java.util.List.of(actions));
    }

    public void setOnClose(Runnable onClose) {
        this.onClose = onClose;
    }
}
