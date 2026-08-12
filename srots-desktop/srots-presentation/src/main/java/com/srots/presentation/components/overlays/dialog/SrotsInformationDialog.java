package com.srots.presentation.components.overlays.dialog;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Window;

/** Simple OK information dialog. */
public final class SrotsInformationDialog {

    private SrotsInformationDialog() {}

    public static void show(Window owner, String title, String message) {
        Label titleLabel = new Label(title == null ? "Information" : title);
        titleLabel.getStyleClass().add("srots-section-title");

        Label body = new Label(message == null ? "" : message);
        body.getStyleClass().add("srots-body");
        body.setWrapText(true);
        body.setMaxWidth(360);

        VBox header = new VBox(8, titleLabel, body);
        header.getStyleClass().add("srots-dialog-header");

        Button ok = new Button("OK");
        ok.getStyleClass().addAll("srots-button", "srots-primary-button");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox footer = new HBox(12, spacer, ok);
        footer.setAlignment(Pos.CENTER_RIGHT);
        footer.getStyleClass().add("srots-dialog-footer");

        VBox content = new VBox(16, header, footer);
        SrotsDialog dialog = new SrotsDialog(title == null ? "Information" : title, content, owner);
        dialog.getRoot().setPrefWidth(400);
        dialog.wireEscapeToClose();
        ok.setOnAction(e -> dialog.close());
        dialog.showAndWait();
    }
}
