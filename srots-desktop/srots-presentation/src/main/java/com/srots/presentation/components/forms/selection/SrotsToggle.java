package com.srots.presentation.components.forms.selection;

import javafx.scene.control.ToggleButton;

/** Styled toggle button. Presentation only. */
public class SrotsToggle extends ToggleButton {

    public SrotsToggle() {
        this(null);
    }

    public SrotsToggle(String text) {
        super(text == null ? "" : text);
        getStyleClass().add("srots-toggle");
    }
}
