package com.srots.presentation.components.utility.date;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javafx.scene.control.Label;

/** Label for LocalDateTime as "dd MMM yyyy · HH:mm". */
public class SrotsDateTimeDisplay extends Label {

    private static final DateTimeFormatter FORMAT =
            DateTimeFormatter.ofPattern("dd MMM yyyy · HH:mm", Locale.ENGLISH);

    public SrotsDateTimeDisplay() {
        this(null);
    }

    public SrotsDateTimeDisplay(LocalDateTime dateTime) {
        getStyleClass().add("srots-caption");
        setDateTime(dateTime);
    }

    public void setDateTime(LocalDateTime dateTime) {
        setText(dateTime == null ? "" : FORMAT.format(dateTime));
    }

    public static String format(LocalDateTime dateTime) {
        return dateTime == null ? "" : FORMAT.format(dateTime);
    }
}
