package com.srots.presentation.components.actions.menu;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

/**
 * Factory helper for simple SROTS context menus.
 */
public final class SrotsMenu {

    private final ContextMenu contextMenu = new ContextMenu();

    private SrotsMenu() {
        contextMenu.getStyleClass().add("srots-menu");
    }

    public static SrotsMenu create() {
        return new SrotsMenu();
    }

    public SrotsMenu addItem(String label, Runnable action) {
        MenuItem item = new MenuItem(label == null ? "" : label);
        item.getStyleClass().add("srots-menu-item");
        if (action != null) {
            item.setOnAction(e -> action.run());
        }
        contextMenu.getItems().add(item);
        return this;
    }

    public ContextMenu getContextMenu() {
        return contextMenu;
    }

    public void show(javafx.scene.Node anchor, double screenX, double screenY) {
        contextMenu.show(anchor, screenX, screenY);
    }

    public void hide() {
        contextMenu.hide();
    }
}
