package com.srots.presentation.components.forms.input;

import javafx.scene.control.PasswordField;

/** Styled password field. Presentation only. */
public class SrotsPasswordField extends PasswordField {

    public SrotsPasswordField() {
        this(null);
    }

    public SrotsPasswordField(String prompt) {
        getStyleClass().addAll("srots-text-field", "srots-password-field");
        if (prompt != null) {
            setPromptText(prompt);
        }
    }
}
