package com.srots.presentation.window;

import com.srots.presentation.components.utility.icons.SrotsIcon;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Caption buttons for the primary window: minimize, maximize/restore, close.
 */
public final class SrotsWindowControls extends HBox {

    private final Button minimizeButton = new Button(SrotsIcon.MINIMIZE.getGlyph());
    private final Button maximizeButton = new Button(SrotsIcon.MAXIMIZE.getGlyph());
    private final Button closeButton = new Button(SrotsIcon.CLOSE.getGlyph());

    private Stage stage;
    private ChangeListener<Boolean> maximizedListener;

    public SrotsWindowControls() {
        getStyleClass().add("srots-window-controls");
        setAlignment(Pos.CENTER_RIGHT);
        setSpacing(4);
        setAccessibleText("Window controls");

        styleButton(minimizeButton, "srots-window-control-minimize", "Minimize");
        styleButton(maximizeButton, "srots-window-control-maximize", "Maximize");
        styleButton(closeButton, "srots-window-control-close", "Close");

        minimizeButton.setOnAction(e -> {
            if (stage != null) {
                stage.setIconified(true);
            }
        });
        maximizeButton.setOnAction(e -> {
            if (stage != null) {
                stage.setMaximized(!stage.isMaximized());
            }
        });
        closeButton.setOnAction(e -> {
            if (stage == null) {
                return;
            }
            stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
            if (stage.isShowing()) {
                stage.close();
            }
        });

        getChildren().addAll(minimizeButton, maximizeButton, closeButton);
    }

    public void attach(Stage stage) {
        detach();
        this.stage = stage;
        if (stage == null) {
            return;
        }
        maximizedListener = (obs, was, maximized) -> syncMaximizeButton(Boolean.TRUE.equals(maximized));
        stage.maximizedProperty().addListener(maximizedListener);
        syncMaximizeButton(stage.isMaximized());
    }

    public void detach() {
        if (stage != null && maximizedListener != null) {
            stage.maximizedProperty().removeListener(maximizedListener);
        }
        maximizedListener = null;
        stage = null;
    }

    public Button getMinimizeButton() {
        return minimizeButton;
    }

    public Button getMaximizeButton() {
        return maximizeButton;
    }

    public Button getCloseButton() {
        return closeButton;
    }

    private void syncMaximizeButton(boolean maximized) {
        if (maximized) {
            maximizeButton.setText(SrotsIcon.RESTORE.getGlyph());
            maximizeButton.setTooltip(new Tooltip("Restore"));
            maximizeButton.setAccessibleText("Restore");
        } else {
            maximizeButton.setText(SrotsIcon.MAXIMIZE.getGlyph());
            maximizeButton.setTooltip(new Tooltip("Maximize"));
            maximizeButton.setAccessibleText("Maximize");
        }
    }

    private static void styleButton(Button button, String extraClass, String label) {
        button.getStyleClass().addAll("srots-window-control", extraClass);
        button.setTooltip(new Tooltip(label));
        button.setAccessibleText(label);
        button.setFocusTraversable(false);
        button.setPickOnBounds(true);
        button.setMnemonicParsing(false);
    }
}
