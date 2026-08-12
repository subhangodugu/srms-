package com.srots.presentation.components.utility.date;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javafx.scene.control.Label;

/** Label formatting LocalDate as "dd MMM yyyy". */
public class SrotsDateDisplay extends Label {

    private static final DateTimeFormatter FORMAT =
            DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH);

    public SrotsDateDisplay() {
        this(null);
    }

    public SrotsDateDisplay(LocalDate date) {
        getStyleClass().add("srots-caption");
        setDate(date);
    }

    public void setDate(LocalDate date) {
        setText(date == null ? "" : FORMAT.format(date));
    }

    public static String format(LocalDate date) {
        return date == null ? "" : FORMAT.format(date);
    }
}
