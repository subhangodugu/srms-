package com.srots.presentation.components.utility.date;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import javafx.scene.control.Label;

/** Relative time label helper ("5 minutes ago"). Simple English. */
public class SrotsRelativeTime extends Label {

    public SrotsRelativeTime() {
        this((Instant) null);
    }

    public SrotsRelativeTime(Instant instant) {
        getStyleClass().add("srots-caption");
        setInstant(instant);
    }

    public SrotsRelativeTime(LocalDateTime dateTime) {
        getStyleClass().add("srots-caption");
        setDateTime(dateTime);
    }

    public void setInstant(Instant instant) {
        setText(format(instant));
    }

    public void setDateTime(LocalDateTime dateTime) {
        setText(format(dateTime));
    }

    public static String format(Instant instant) {
        if (instant == null) {
            return "";
        }
        return formatDuration(Duration.between(instant, Instant.now()));
    }

    public static String format(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        Instant instant = dateTime.atZone(ZoneId.systemDefault()).toInstant();
        return format(instant);
    }

    private static String formatDuration(Duration duration) {
        long seconds = duration.getSeconds();
        boolean future = seconds < 0;
        long abs = Math.abs(seconds);

        String unit;
        long value;
        if (abs < 60) {
            value = abs;
            unit = value == 1 ? "second" : "seconds";
        } else if (abs < 3600) {
            value = abs / 60;
            unit = value == 1 ? "minute" : "minutes";
        } else if (abs < 86400) {
            value = abs / 3600;
            unit = value == 1 ? "hour" : "hours";
        } else if (abs < 2_592_000) {
            value = abs / 86400;
            unit = value == 1 ? "day" : "days";
        } else if (abs < 31_536_000) {
            value = abs / 2_592_000;
            unit = value == 1 ? "month" : "months";
        } else {
            value = abs / 31_536_000;
            unit = value == 1 ? "year" : "years";
        }

        if (value == 0) {
            return "just now";
        }
        return future ? "in " + value + " " + unit : value + " " + unit + " ago";
    }
}
