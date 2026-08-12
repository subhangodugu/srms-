package com.srots.presentation.components.navigation.sidebar;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.css.PseudoClass;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

/**
 * Data-driven sidebar navigation row: icon, label, optional badge, active/disabled states.
 */
public class SrotsNavigationItem extends Button {

    private static final PseudoClass ACTIVE = PseudoClass.getPseudoClass("active");

    private final BooleanProperty active = new SimpleBooleanProperty(false);
    private final BooleanProperty collapsedChrome = new SimpleBooleanProperty(false);
    private final StringProperty itemText = new SimpleStringProperty("");
    private final StringProperty iconKey = new SimpleStringProperty("");
    private final IntegerProperty badgeCount = new SimpleIntegerProperty(0);

    private final Label iconLabel = new Label();
    private final Label textLabel = new Label();
    private final Label badgeLabel = new Label();
    private final Tooltip tooltip = new Tooltip();

    public SrotsNavigationItem() {
        this("", null);
    }

    public SrotsNavigationItem(String text) {
        this(text, null);
    }

    public SrotsNavigationItem(String text, Runnable onAction) {
        getStyleClass().setAll("srots-nav-item", "srots-sidebar-item");
        setMaxWidth(Double.MAX_VALUE);
        setAlignment(Pos.CENTER_LEFT);
        setMnemonicParsing(false);

        iconLabel.getStyleClass().addAll("srots-sidebar-item-icon");
        textLabel.getStyleClass().addAll("srots-sidebar-item-label");
        badgeLabel.getStyleClass().addAll("srots-sidebar-item-badge");
        badgeLabel.setVisible(false);
        badgeLabel.setManaged(false);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox content = new HBox(10, iconLabel, textLabel, spacer, badgeLabel);
        content.setAlignment(Pos.CENTER_LEFT);
        content.getStyleClass().add("srots-sidebar-item-content");
        setGraphic(content);
        setText(null);

        itemText.addListener((obs, o, n) -> applyText(n));
        iconKey.addListener((obs, o, n) -> applyIcon(n));
        badgeCount.addListener((obs, o, n) -> applyBadge(n == null ? 0 : n.intValue()));
        active.addListener((obs, was, isActive) -> applyActive(isActive));
        collapsedChrome.addListener((obs, was, collapsed) -> applyCollapsed(collapsed));
        disabledProperty().addListener((obs, was, disabled) -> {
            if (disabled) {
                if (!getStyleClass().contains("disabled")) {
                    getStyleClass().add("disabled");
                }
            } else {
                getStyleClass().remove("disabled");
            }
        });

        setItemText(text);
        applyIcon(iconKey.get());
        applyCollapsed(false);

        if (onAction != null) {
            setOnAction(e -> onAction.run());
        }
    }

    public BooleanProperty activeProperty() {
        return active;
    }

    public boolean isActive() {
        return active.get();
    }

    public void setActive(boolean value) {
        active.set(value);
    }

    public BooleanProperty collapsedChromeProperty() {
        return collapsedChrome;
    }

    public void setCollapsedChrome(boolean collapsed) {
        collapsedChrome.set(collapsed);
    }

    public StringProperty itemTextProperty() {
        return itemText;
    }

    public String getItemText() {
        return itemText.get();
    }

    public void setItemText(String value) {
        itemText.set(value == null ? "" : value);
    }

    public StringProperty iconKeyProperty() {
        return iconKey;
    }

    public void setIconKey(String key) {
        iconKey.set(key == null ? "" : key);
    }

    public IntegerProperty badgeCountProperty() {
        return badgeCount;
    }

    public void setBadgeCount(Integer count) {
        badgeCount.set(count == null ? 0 : count);
    }

    private void applyText(String value) {
        String label = value == null ? "" : value;
        textLabel.setText(label);
        setAccessibleText(label);
        tooltip.setText(label);
        applyCollapsed(collapsedChrome.get());
    }

    private void applyIcon(String key) {
        iconLabel.setText(NavigationIconResolver.glyphFor(key));
    }

    private void applyBadge(int count) {
        String formatted = NavigationBadgeFormatter.format(count);
        boolean show = !formatted.isEmpty() && !collapsedChrome.get();
        badgeLabel.setText(formatted);
        badgeLabel.setVisible(show);
        badgeLabel.setManaged(show);
    }

    private void applyActive(boolean value) {
        pseudoClassStateChanged(ACTIVE, value);
        if (value) {
            if (!getStyleClass().contains("srots-nav-item-active")) {
                getStyleClass().add("srots-nav-item-active");
            }
            if (!getStyleClass().contains("srots-sidebar-active")) {
                getStyleClass().add("srots-sidebar-active");
            }
        } else {
            getStyleClass().removeAll("srots-nav-item-active", "srots-sidebar-active");
        }
    }

    private void applyCollapsed(boolean collapsed) {
        textLabel.setVisible(!collapsed);
        textLabel.setManaged(!collapsed);
        applyBadge(badgeCount.get());
        if (collapsed) {
            if (getTooltip() == null) {
                setTooltip(tooltip);
            }
        } else {
            setTooltip(null);
        }
    }
}
