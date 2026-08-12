package com.srots.presentation.components.overlays.dialog;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Window;

/**
 * Form dialog with Cancel + Save/Submit. Does not validate domain.
 */
public class SrotsFormDialog extends SrotsDialog {

    private final Runnable onSubmit;

    public SrotsFormDialog(
            Window owner,
            String title,
            Node formContent,
            String submitLabel,
            Runnable onSubmit) {
        super(title, null, owner);
        this.onSubmit = onSubmit;

        Label titleLabel = new Label(title == null ? "" : title);
        titleLabel.getStyleClass().add("srots-section-title");

        VBox header = new VBox(titleLabel);
        header.getStyleClass().add("srots-dialog-header");

        VBox body = new VBox(12);
        if (formContent != null) {
            body.getChildren().add(formContent);
        }

        Button cancel = new Button("Cancel");
        cancel.getStyleClass().addAll("srots-button", "srots-secondary-button");
        cancel.setOnAction(e -> close());

        Button submit = new Button(submitLabel == null || submitLabel.isBlank() ? "Save" : submitLabel);
        submit.getStyleClass().addAll("srots-button", "srots-primary-button");
        submit.setOnAction(e -> {
            if (this.onSubmit != null) {
                this.onSubmit.run();
            }
            close();
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox footer = new HBox(12, spacer, cancel, submit);
        footer.setAlignment(Pos.CENTER_RIGHT);
        footer.getStyleClass().add("srots-dialog-footer");

        root.getChildren().setAll(header, body, footer);
        root.setPrefWidth(480);
        wireEscapeToClose();
    }
}
