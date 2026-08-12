package com.srots.presentation.components.layout.page;

import com.srots.presentation.components.actions.button.SrotsButton;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * Page header with breadcrumb, title, description, and action slots.
 */
public class SrotsPageHeader extends VBox {

    private final StringProperty breadcrumbText = new SimpleStringProperty("");
    private final StringProperty titleText = new SimpleStringProperty("");
    private final StringProperty descriptionText = new SimpleStringProperty("");

    private final Label breadcrumb = new Label();
    private final Label title = new Label();
    private final Label description = new Label();
    private final HBox actions = new HBox(12);

    public SrotsPageHeader() {
        this("", "", "");
    }

    public SrotsPageHeader(String breadcrumbText, String titleText, String descriptionText) {
        getStyleClass().add("srots-page-header");

        breadcrumb.getStyleClass().add("srots-breadcrumb");
        breadcrumb.textProperty().bind(this.breadcrumbText);

        title.getStyleClass().add("srots-page-title");
        title.textProperty().bind(this.titleText);

        description.getStyleClass().add("srots-page-description");
        description.setWrapText(true);
        description.textProperty().bind(this.descriptionText);
        description.managedProperty().bind(this.descriptionText.isNotEmpty());
        description.visibleProperty().bind(this.descriptionText.isNotEmpty());

        actions.setAlignment(Pos.CENTER_RIGHT);

        HBox titleRow = new HBox(16);
        titleRow.getStyleClass().add("srots-page-header-row");
        titleRow.setAlignment(Pos.CENTER_LEFT);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        titleRow.getChildren().addAll(title, spacer, actions);

        breadcrumb.managedProperty().bind(this.breadcrumbText.isNotEmpty());
        breadcrumb.visibleProperty().bind(this.breadcrumbText.isNotEmpty());

        getChildren().addAll(breadcrumb, titleRow, description);

        setBreadcrumbText(breadcrumbText);
        setTitleText(titleText);
        setDescriptionText(descriptionText);
    }

    public SrotsPageHeader withPrimaryAction(String label, Runnable action) {
        SrotsButton button = SrotsButton.primary(label);
        if (action != null) {
            button.setOnAction(e -> action.run());
        }
        actions.getChildren().add(0, button);
        return this;
    }

    public SrotsPageHeader withSecondaryAction(String label, Runnable action) {
        SrotsButton button = SrotsButton.secondary(label);
        if (action != null) {
            button.setOnAction(e -> action.run());
        }
        actions.getChildren().add(button);
        return this;
    }

    public SrotsPageHeader withAction(Node node) {
        if (node != null) {
            actions.getChildren().add(node);
        }
        return this;
    }

    public StringProperty breadcrumbTextProperty() {
        return breadcrumbText;
    }

    public String getBreadcrumbText() {
        return breadcrumbText.get();
    }

    public void setBreadcrumbText(String value) {
        breadcrumbText.set(value == null ? "" : value);
    }

    public StringProperty titleTextProperty() {
        return titleText;
    }

    public String getTitleText() {
        return titleText.get();
    }

    public void setTitleText(String value) {
        titleText.set(value == null ? "" : value);
    }

    public StringProperty descriptionTextProperty() {
        return descriptionText;
    }

    public String getDescriptionText() {
        return descriptionText.get();
    }

    public void setDescriptionText(String value) {
        descriptionText.set(value == null ? "" : value);
    }

    public HBox getActions() {
        return actions;
    }
}
