package com.srots.presentation.components.data.filter;

import java.util.function.Consumer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;

/** Filter dropdown with prompt. Emits selection via onSelected — no data fetch. */
public class SrotsFilterDropdown<T> extends VBox {

    private final ComboBox<T> comboBox = new ComboBox<>();
    private Consumer<T> onSelected;

    public SrotsFilterDropdown() {
        this(null, FXCollections.observableArrayList());
    }

    public SrotsFilterDropdown(String prompt) {
        this(prompt, FXCollections.observableArrayList());
    }

    public SrotsFilterDropdown(String prompt, ObservableList<T> items) {
        super(2);
        getStyleClass().add("srots-filter-dropdown");
        comboBox.getStyleClass().add("srots-combo-box");
        comboBox.setItems(items);
        if (prompt != null && !prompt.isBlank()) {
            comboBox.setPromptText(prompt);
        }
        comboBox.valueProperty().addListener((obs, oldV, newV) -> {
            if (onSelected != null && newV != null) {
                onSelected.accept(newV);
            }
        });
        getChildren().add(comboBox);
    }

    public ComboBox<T> getComboBox() {
        return comboBox;
    }

    public void setItems(ObservableList<T> items) {
        comboBox.setItems(items);
    }

    public void setOnSelected(Consumer<T> onSelected) {
        this.onSelected = onSelected;
    }

    public void setPromptText(String prompt) {
        comboBox.setPromptText(prompt);
    }

    public T getSelected() {
        return comboBox.getValue();
    }

    public void clearSelection() {
        comboBox.getSelectionModel().clearSelection();
    }
}
