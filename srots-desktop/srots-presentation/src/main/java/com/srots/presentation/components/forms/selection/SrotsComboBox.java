package com.srots.presentation.components.forms.selection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

/** Styled combo box. Presentation only. */
public class SrotsComboBox<T> extends ComboBox<T> {

    public SrotsComboBox() {
        this(FXCollections.observableArrayList());
    }

    public SrotsComboBox(ObservableList<T> items) {
        super(items);
        getStyleClass().add("srots-combo-box");
    }

    public SrotsComboBox(String prompt) {
        this();
        setPromptText(prompt);
    }
}
