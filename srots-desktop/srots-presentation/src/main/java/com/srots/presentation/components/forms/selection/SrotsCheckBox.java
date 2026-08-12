package com.srots.presentation.components.forms.selection;

import javafx.scene.control.CheckBox;

/** Styled check box. Presentation only. */
public class SrotsCheckBox extends CheckBox {

    public SrotsCheckBox() {
        this(null);
    }

    public SrotsCheckBox(String text) {
        super(text == null ? "" : text);
        getStyleClass().add("srots-check-box");
    }
}
