package com.srots.presentation.components.utility.formatting;

import java.text.NumberFormat;
import java.util.Locale;

/** Static number formatting helpers. */
public final class SrotsNumberFormat {

    private static final NumberFormat INTEGER = NumberFormat.getIntegerInstance(Locale.ENGLISH);
    private static final NumberFormat PERCENT = NumberFormat.getPercentInstance(Locale.ENGLISH);

    static {
        PERCENT.setMaximumFractionDigits(0);
        PERCENT.setMinimumFractionDigits(0);
    }

    private SrotsNumberFormat() {}

    public static String formatInteger(long value) {
        return INTEGER.format(value);
    }

    public static String formatInteger(Number value) {
        if (value == null) {
            return "";
        }
        return INTEGER.format(value.longValue());
    }

    /** Formats 0.0–1.0 as percent, or values &gt; 1 as already-percent (e.g. 62 → 62%). */
    public static String formatPercent(double value) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            return "";
        }
        double ratio = value > 1.0 ? value / 100.0 : value;
        return PERCENT.format(ratio);
    }

    public static String formatPercent(Number value) {
        if (value == null) {
            return "";
        }
        return formatPercent(value.doubleValue());
    }
}
