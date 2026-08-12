package com.srots.presentation.components.forms.input;

import javafx.scene.control.TextArea;

/** Styled text area. Presentation only. */
public class SrotsTextArea extends TextArea {

    public SrotsTextArea() {
        this(null);
    }

    public SrotsTextArea(String prompt) {
        getStyleClass().add("srots-text-area");
        setWrapText(true);
        if (prompt != null) {
            setPromptText(prompt);
        }
    }
}
