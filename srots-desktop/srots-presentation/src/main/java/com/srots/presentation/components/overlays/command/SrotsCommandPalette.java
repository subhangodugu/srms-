package com.srots.presentation.components.overlays.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

/**
 * Command palette UI foundation. Filters demo/passed-in CommandItems only.
 * Ctrl+K wiring is left to the application.
 */
public class SrotsCommandPalette {

    public record CommandItem(String id, String label, Runnable action) {}

    private final Stage stage = new Stage();
    private final ObservableList<CommandItem> allItems = FXCollections.observableArrayList();
    private final FilteredList<CommandItem> filtered = new FilteredList<>(allItems, item -> true);
    private final TextField searchField = new TextField();
    private final ListView<CommandItem> listView = new ListView<>(filtered);

    public SrotsCommandPalette(Window owner, List<CommandItem> items) {
        if (items != null) {
            allItems.setAll(items);
        }

        stage.initOwner(owner);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initStyle(StageStyle.UTILITY);
        stage.setTitle("Command palette");

        searchField.setPromptText("Search commands…");
        searchField.getStyleClass().add("srots-text-field");
        searchField.textProperty().addListener((obs, o, n) -> {
            String q = n == null ? "" : n.trim().toLowerCase(Locale.ROOT);
            filtered.setPredicate(item -> q.isEmpty()
                    || (item.label() != null && item.label().toLowerCase(Locale.ROOT).contains(q))
                    || (item.id() != null && item.id().toLowerCase(Locale.ROOT).contains(q)));
        });

        listView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(CommandItem item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.label());
            }
        });
        listView.setOnMouseClicked(e -> {
            if (e.getClickCount() >= 2) {
                runSelected();
            }
        });

        VBox root = new VBox(10, searchField, listView);
        root.getStyleClass().add("srots-dialog");
        root.setPadding(new Insets(16));
        root.setPrefSize(480, 360);
        VBox.setVgrow(listView, Priority.ALWAYS);

        javafx.scene.Scene scene = new javafx.scene.Scene(root);
        com.srots.presentation.components.overlays.dialog.SrotsDialog.applyTheme(scene);
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                stage.close();
            } else if (e.getCode() == KeyCode.ENTER) {
                runSelected();
            }
        });
        stage.setScene(scene);
    }

    /** Convenience when ThemeLoader access via package helper is preferred. */
    public static SrotsCommandPalette withDemo(Window owner) {
        List<CommandItem> demo = new ArrayList<>();
        demo.add(new CommandItem("demo.open", "Open demo", () -> {}));
        demo.add(new CommandItem("demo.refresh", "Refresh view", () -> {}));
        return new SrotsCommandPalette(owner, demo);
    }

    public void setItems(List<CommandItem> items) {
        allItems.setAll(items == null ? List.of() : items);
    }

    public void show() {
        searchField.clear();
        stage.show();
        searchField.requestFocus();
    }

    public void close() {
        stage.close();
    }

    public Stage getStage() {
        return stage;
    }

    private void runSelected() {
        CommandItem item = listView.getSelectionModel().getSelectedItem();
        if (item == null && !filtered.isEmpty()) {
            item = filtered.get(0);
        }
        if (item != null && item.action() != null) {
            stage.close();
            item.action().run();
        }
    }
}
