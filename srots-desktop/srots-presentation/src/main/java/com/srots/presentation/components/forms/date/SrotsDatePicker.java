package com.srots.presentation.components.forms.date;

import java.time.LocalDate;
import javafx.scene.control.DatePicker;

/** Styled date picker. Presentation only. */
public class SrotsDatePicker extends DatePicker {

    public SrotsDatePicker() {
        this(null);
    }

    public SrotsDatePicker(LocalDate value) {
        super(value);
        getStyleClass().add("srots-date-picker");
    }
}
