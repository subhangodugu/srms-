package com.srots.presentation.components.overlays.dialog;

import java.util.concurrent.CompletableFuture;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Window;

/**
 * Confirmation dialog. Cancel + confirm (optional danger). Escape closes.
 * No DB / business operations.
 */
public final class SrotsConfirmationDialog {

    private SrotsConfirmationDialog() {}

    public static CompletableFuture<Boolean> show(
            Window owner,
            String title,
            String message,
            String confirmLabel,
            boolean dangerConfirm) {
        CompletableFuture<Boolean> result = new CompletableFuture<>();
        show(owner, title, message, confirmLabel, dangerConfirm,
                () -> result.complete(true),
                () -> result.complete(false));
        return result;
    }

    public static void show(
            Window owner,
            String title,
            String message,
            String confirmLabel,
            boolean dangerConfirm,
            Runnable onConfirm,
            Runnable onCancel) {

        Label titleLabel = new Label(title == null ? "Confirm" : title);
        titleLabel.getStyleClass().add("srots-section-title");

        Label body = new Label(message == null ? "" : message);
        body.getStyleClass().add("srots-body");
        body.setWrapText(true);
        body.setMaxWidth(360);

        VBox header = new VBox(8, titleLabel, body);
        header.getStyleClass().add("srots-dialog-header");

        Button cancel = new Button("Cancel");
        cancel.getStyleClass().addAll("srots-button", "srots-secondary-button");

        Button confirm = new Button(confirmLabel == null || confirmLabel.isBlank() ? "Confirm" : confirmLabel);
        confirm.getStyleClass().addAll("srots-button",
                dangerConfirm ? "srots-danger-button" : "srots-primary-button");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox footer = new HBox(12, spacer, cancel, confirm);
        footer.setAlignment(Pos.CENTER_RIGHT);
        footer.getStyleClass().add("srots-dialog-footer");

        VBox content = new VBox(16, header, footer);
        content.setPadding(new Insets(0));

        SrotsDialog dialog = new SrotsDialog(title == null ? "Confirm" : title, content, owner);
        dialog.getRoot().setPrefWidth(420);
        dialog.wireEscapeToClose();

        final boolean[] settled = {false};
        Runnable settleCancel = () -> {
            if (settled[0]) {
                return;
            }
            settled[0] = true;
            if (onCancel != null) {
                onCancel.run();
            }
        };
        Runnable settleConfirm = () -> {
            if (settled[0]) {
                return;
            }
            settled[0] = true;
            if (onConfirm != null) {
                onConfirm.run();
            }
        };

        cancel.setOnAction(e -> {
            dialog.close();
            settleCancel.run();
        });
        confirm.setOnAction(e -> {
            dialog.close();
            settleConfirm.run();
        });

        dialog.getStage().setOnHidden(e -> settleCancel.run());
        dialog.showAndWait();
    }
}
