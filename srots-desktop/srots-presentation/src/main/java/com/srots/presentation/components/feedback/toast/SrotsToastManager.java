package com.srots.presentation.components.feedback.toast;

import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Shows non-blocking toast messages in a StackPane overlay host.
 * Presentation only — no business / network logic.
 */
public final class SrotsToastManager {

    private StackPane host;
    private final VBox tray = new VBox(8);

    public SrotsToastManager() {
        tray.setAlignment(Pos.TOP_RIGHT);
        tray.setMouseTransparent(false);
        tray.setMaxWidth(360);
        tray.getStyleClass().add("srots-toast-tray");
        StackPane.setAlignment(tray, Pos.TOP_RIGHT);
    }

    public void attach(StackPane host) {
        if (this.host != null) {
            this.host.getChildren().remove(tray);
        }
        this.host = host;
        if (host != null && !host.getChildren().contains(tray)) {
            host.getChildren().add(tray);
        }
    }

    public void show(SrotsToast toast) {
        if (host == null || toast == null) {
            return;
        }
        Label label = new Label(toast.text());
        label.setWrapText(true);
        label.setMaxWidth(340);
        label.getStyleClass().addAll("srots-toast", toast.variant().getStyleClass());

        tray.getChildren().add(label);

        PauseTransition pause = new PauseTransition(toast.duration());
        pause.setOnFinished(e -> tray.getChildren().remove(label));
        pause.play();
    }

    public void showSuccess(String text) {
        show(SrotsToast.success(text));
    }

    public void showInfo(String text) {
        show(SrotsToast.info(text));
    }

    public void showWarning(String text) {
        show(SrotsToast.warning(text));
    }

    public void showError(String text) {
        show(SrotsToast.error(text));
    }
}
