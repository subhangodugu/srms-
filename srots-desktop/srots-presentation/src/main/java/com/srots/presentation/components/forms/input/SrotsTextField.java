package com.srots.presentation.components.forms.input;

import com.srots.presentation.components.utility.SrotsSize;
import javafx.scene.control.TextField;

/** Styled text field. Presentation only. */
public class SrotsTextField extends TextField {

    public SrotsTextField() {
        this(null, null);
    }

    public SrotsTextField(String prompt) {
        this(prompt, null);
    }

    public SrotsTextField(String prompt, SrotsSize size) {
        getStyleClass().add("srots-text-field");
        if (prompt != null) {
            setPromptText(prompt);
        }
        applySize(size);
    }

    public void setSize(SrotsSize size) {
        getStyleClass().removeAll("srots-size-small", "srots-size-standard", "srots-size-large");
        applySize(size);
    }

    private void applySize(SrotsSize size) {
        if (size == null) {
            return;
        }
        switch (size) {
            case SMALL -> getStyleClass().add("srots-size-small");
            case LARGE -> getStyleClass().add("srots-size-large");
            case STANDARD -> getStyleClass().add("srots-size-standard");
        }
    }
}
