package com.srots.presentation.components.data.table;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;

/**
 * Row action buttons: View / Edit / Delete / More overflow.
 * Emits callbacks only — no business logic.
 */
public class SrotsRowActions extends HBox {

    private Runnable onView;
    private Runnable onEdit;
    private Runnable onDelete;
    private Runnable onMore;

    private final Button viewButton = iconButton("View");
    private final Button editButton = iconButton("Edit");
    private final Button deleteButton = iconButton("Delete");
    private final MenuButton moreButton = new MenuButton("More");

    public SrotsRowActions() {
        super(4);
        setAlignment(Pos.CENTER_LEFT);
        getStyleClass().add("srots-row-actions");

        viewButton.setOnAction(e -> run(onView));
        editButton.setOnAction(e -> run(onEdit));
        deleteButton.setOnAction(e -> run(onDelete));
        deleteButton.getStyleClass().add("srots-danger-button");

        moreButton.getStyleClass().addAll("srots-button", "srots-tertiary-button");
        MenuItem moreItem = new MenuItem("More…");
        moreItem.setOnAction(e -> run(onMore));
        moreButton.getItems().add(moreItem);

        getChildren().addAll(viewButton, editButton, deleteButton, moreButton);
    }

    public void setOnView(Runnable onView) {
        this.onView = onView;
    }

    public void setOnEdit(Runnable onEdit) {
        this.onEdit = onEdit;
    }

    public void setOnDelete(Runnable onDelete) {
        this.onDelete = onDelete;
    }

    public void setOnMore(Runnable onMore) {
        this.onMore = onMore;
    }

    public void setShowView(boolean show) {
        setVisibleManaged(viewButton, show);
    }

    public void setShowEdit(boolean show) {
        setVisibleManaged(editButton, show);
    }

    public void setShowDelete(boolean show) {
        setVisibleManaged(deleteButton, show);
    }

    public void setShowMore(boolean show) {
        setVisibleManaged(moreButton, show);
    }

    public MenuButton getMoreButton() {
        return moreButton;
    }

    private static Button iconButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().addAll("srots-button", "srots-tertiary-button", "srots-icon-button");
        return button;
    }

    private static void run(Runnable action) {
        if (action != null) {
            action.run();
        }
    }

    private static void setVisibleManaged(javafx.scene.Node node, boolean show) {
        node.setVisible(show);
        node.setManaged(show);
    }
}
