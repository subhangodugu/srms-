package com.srots.presentation.components.feedback.notification;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Notification center UI. Receives data only — mark-read via callbacks.
 */
public class SrotsNotificationCenter extends VBox {

    private final ListView<SrotsNotificationItem> listView = new ListView<>();
    private Consumer<SrotsNotificationItem> onMarkRead;

    public SrotsNotificationCenter() {
        getStyleClass().add("srots-empty-state");
        setSpacing(8);

        Label heading = new Label("Notifications");
        heading.getStyleClass().add("srots-state-title");

        listView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(SrotsNotificationItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                    return;
                }
                Label title = new Label(item.getTitle());
                title.getStyleClass().add("srots-state-title");
                if (item.isRead()) {
                    title.setOpacity(0.65);
                }
                Label body = new Label(item.getBody());
                body.getStyleClass().add("srots-state-message");
                body.setWrapText(true);
                Label time = new Label(item.getTimestampText());
                time.getStyleClass().add("srots-caption");
                VBox box = new VBox(4, title, body, time);
                box.setAlignment(Pos.CENTER_LEFT);
                setGraphic(box);
                setText(null);
            }
        });

        listView.setOnMouseClicked(e -> {
            SrotsNotificationItem selected = listView.getSelectionModel().getSelectedItem();
            if (selected != null && !selected.isRead()) {
                selected.setRead(true);
                listView.refresh();
                if (onMarkRead != null) {
                    onMarkRead.accept(selected);
                }
            }
        });

        VBox.setVgrow(listView, Priority.ALWAYS);
        getChildren().addAll(heading, listView);
    }

    public void setItems(List<SrotsNotificationItem> items) {
        listView.getItems().setAll(items == null ? List.of() : new ArrayList<>(items));
    }

    public List<SrotsNotificationItem> getItems() {
        return List.copyOf(listView.getItems());
    }

    public void setOnMarkRead(Consumer<SrotsNotificationItem> onMarkRead) {
        this.onMarkRead = onMarkRead;
    }

    public ListView<SrotsNotificationItem> getListView() {
        return listView;
    }
}
