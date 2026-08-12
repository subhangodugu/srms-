package com.srots.presentation.components.forms.selection;

import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

/** Styled radio button. Presentation only. */
public class SrotsRadioButton extends RadioButton {

    public SrotsRadioButton() {
        this(null);
    }

    public SrotsRadioButton(String text) {
        super(text == null ? "" : text);
        getStyleClass().add("srots-radio-button");
    }

    public SrotsRadioButton(String text, ToggleGroup group) {
        this(text);
        setToggleGroup(group);
    }
}
