package com.srots.presentation.components.layout.page;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Page section with optional title and content slot.
 */
public class SrotsSection extends VBox {

    private final StringProperty titleText = new SimpleStringProperty("");
    private final Label titleLabel = new Label();
    private final StackPane contentHost = new StackPane();

    public SrotsSection() {
        this(null, null);
    }

    public SrotsSection(String title) {
        this(title, null);
    }

    public SrotsSection(String title, Node content) {
        getStyleClass().add("srots-section");

        titleLabel.getStyleClass().add("srots-section-title");
        titleLabel.textProperty().bind(titleText);
        titleLabel.managedProperty().bind(titleText.isNotEmpty());
        titleLabel.visibleProperty().bind(titleText.isNotEmpty());

        getChildren().addAll(titleLabel, contentHost);
        setTitle(title);
        setContent(content);
    }

    public void setTitle(String title) {
        titleText.set(title == null ? "" : title);
    }

    public String getTitle() {
        return titleText.get();
    }

    public StringProperty titleProperty() {
        return titleText;
    }

    public void setContent(Node content) {
        contentHost.getChildren().setAll(content == null ? java.util.List.of() : java.util.List.of(content));
    }
}
