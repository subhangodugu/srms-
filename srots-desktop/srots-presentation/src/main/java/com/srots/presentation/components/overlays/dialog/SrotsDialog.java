package com.srots.presentation.components.overlays.dialog;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * Base Stage dialog helper. Applies ThemeLoader when available.
 */
public class SrotsDialog {

    protected final Stage stage = new Stage();
    protected final VBox root = new VBox(16);

    public SrotsDialog(String title, Node content, Window owner) {
        root.getStyleClass().add("srots-dialog");
        if (content != null) {
            root.getChildren().add(content);
        }

        stage.setTitle(title == null ? "" : title);
        if (owner != null) {
            stage.initOwner(owner);
        }
        stage.initModality(Modality.WINDOW_MODAL);

        Scene scene = new Scene(root);
        applyTheme(scene);
        stage.setScene(scene);
    }

    public Stage getStage() {
        return stage;
    }

    public VBox getRoot() {
        return root;
    }

    public void setContent(Node content) {
        root.getChildren().clear();
        if (content != null) {
            root.getChildren().add(content);
        }
    }

    public void show() {
        stage.show();
    }

    public void showAndWait() {
        stage.showAndWait();
    }

    public void close() {
        stage.close();
    }

    public static void applyTheme(Scene scene) {
        try {
            Class<?> loader = Class.forName("com.srots.presentation.components.layout.ThemeLoader");
            loader.getMethod("apply", Scene.class).invoke(null, scene);
            if (scene.getStylesheets().isEmpty()) {
                loadThemeFallback(scene);
            }
        } catch (ReflectiveOperationException ex) {
            loadThemeFallback(scene);
        }
    }

    private static void loadThemeFallback(Scene scene) {
        var theme = SrotsDialog.class.getResource("/css/theme.css");
        if (theme != null) {
            scene.getStylesheets().setAll(theme.toExternalForm());
        }
    }

    protected void wireEscapeToClose() {
        stage.getScene().setOnKeyPressed(e -> {
            if (e.getCode() == javafx.scene.input.KeyCode.ESCAPE) {
                stage.close();
            }
        });
    }
}
