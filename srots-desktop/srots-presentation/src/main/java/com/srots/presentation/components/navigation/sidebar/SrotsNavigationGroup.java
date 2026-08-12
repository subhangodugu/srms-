package com.srots.presentation.components.navigation.sidebar;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * Collapsible sidebar navigation group: header + child items.
 */
public class SrotsNavigationGroup extends VBox {

    private final StringProperty title = new SimpleStringProperty("");
    private final BooleanProperty expanded = new SimpleBooleanProperty(true);
    private final BooleanProperty sidebarCollapsed = new SimpleBooleanProperty(false);

    private final Label titleLabel = new Label();
    private final Label chevronLabel = new Label("▾");
    private final Button headerButton = new Button();
    private final VBox itemsHost = new VBox(2);

    public SrotsNavigationGroup() {
        this("");
    }

    public SrotsNavigationGroup(String titleText) {
        getStyleClass().addAll("srots-sidebar-group", "srots-nav-group");
        setSpacing(2);

        titleLabel.getStyleClass().addAll("srots-sidebar-group-header", "srots-nav-group-title");
        titleLabel.textProperty().bind(title);
        chevronLabel.getStyleClass().add("srots-sidebar-group-chevron");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox headerContent = new HBox(6, titleLabel, spacer, chevronLabel);
        headerContent.setAlignment(Pos.CENTER_LEFT);

        headerButton.getStyleClass().setAll("srots-sidebar-group-toggle");
        headerButton.setMaxWidth(Double.MAX_VALUE);
        headerButton.setGraphic(headerContent);
        headerButton.setText(null);
        headerButton.setOnAction(e -> setExpanded(!isExpanded()));
        headerButton.setFocusTraversable(true);

        itemsHost.getStyleClass().add("srots-sidebar-group-items");

        getChildren().addAll(headerButton, itemsHost);

        title.addListener((obs, o, n) -> headerButton.setAccessibleText(
                (n == null ? "Group" : n) + (isExpanded() ? " expanded" : " collapsed")));
        expanded.addListener((obs, was, isExpanded) -> applyExpanded(isExpanded));
        sidebarCollapsed.addListener((obs, was, collapsed) -> applySidebarCollapsed(collapsed));

        setTitle(titleText);
        applyExpanded(true);
        applySidebarCollapsed(false);
    }

    public StringProperty titleProperty() {
        return title;
    }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String value) {
        title.set(value == null ? "" : value);
    }

    public BooleanProperty expandedProperty() {
        return expanded;
    }

    public boolean isExpanded() {
        return expanded.get();
    }

    public void setExpanded(boolean value) {
        expanded.set(value);
    }

    public BooleanProperty sidebarCollapsedProperty() {
        return sidebarCollapsed;
    }

    public void setSidebarCollapsed(boolean value) {
        sidebarCollapsed.set(value);
    }

    public void addItem(SrotsNavigationItem item) {
        if (item != null) {
            itemsHost.getChildren().add(item);
        }
    }

    public void addNode(Node node) {
        if (node != null) {
            itemsHost.getChildren().add(node);
        }
    }

    public void clearItems() {
        itemsHost.getChildren().clear();
    }

    public VBox getItemsHost() {
        return itemsHost;
    }

    private void applyExpanded(boolean isExpanded) {
        chevronLabel.setText(isExpanded ? "▾" : "▸");
        itemsHost.setVisible(isExpanded);
        itemsHost.setManaged(isExpanded);
        headerButton.setAccessibleText(getTitle() + (isExpanded ? " expanded" : " collapsed"));
        if (isExpanded) {
            getStyleClass().remove("srots-sidebar-group-collapsed");
        } else if (!getStyleClass().contains("srots-sidebar-group-collapsed")) {
            getStyleClass().add("srots-sidebar-group-collapsed");
        }
    }

    private void applySidebarCollapsed(boolean collapsed) {
        // When the whole sidebar is icon-only, hide group headers but keep items visible.
        headerButton.setVisible(!collapsed);
        headerButton.setManaged(!collapsed);
        if (collapsed) {
            itemsHost.setVisible(true);
            itemsHost.setManaged(true);
        } else {
            applyExpanded(isExpanded());
        }
    }
}
