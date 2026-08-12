package com.srots.presentation.components.navigation.breadcrumb;

import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.util.List;

/**
 * Clickable breadcrumb trail. Action may be null for the current crumb.
 */
public class SrotsBreadcrumb extends HBox {

    public record Crumb(String label, Runnable action) {}

    public SrotsBreadcrumb() {
        getStyleClass().add("srots-breadcrumb");
        setAlignment(Pos.CENTER_LEFT);
        setSpacing(6);
    }

    public void setItems(List<Crumb> items) {
        getChildren().clear();
        if (items == null || items.isEmpty()) {
            return;
        }
        for (int i = 0; i < items.size(); i++) {
            if (i > 0) {
                Label separator = new Label("›");
                separator.getStyleClass().add("srots-breadcrumb-separator");
                getChildren().add(separator);
            }
            Crumb crumb = items.get(i);
            String label = crumb.label() == null ? "" : crumb.label();
            boolean isCurrent = crumb.action() == null || i == items.size() - 1;
            if (isCurrent && crumb.action() == null) {
                Label current = new Label(label);
                current.getStyleClass().add("srots-breadcrumb-current");
                getChildren().add(current);
            } else {
                Hyperlink link = new Hyperlink(label);
                link.getStyleClass().add("srots-breadcrumb-link");
                Runnable action = crumb.action();
                if (action != null) {
                    link.setOnAction(e -> action.run());
                } else {
                    link.setDisable(true);
                }
                getChildren().add(link);
            }
        }
    }
}
