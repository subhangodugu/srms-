package com.srots.presentation.components.feedback.toast;

import javafx.util.Duration;

/**
 * Toast message data. Presentation only.
 */
public record SrotsToast(Variant variant, String text, Duration duration) {

    public enum Variant {
        INFO("srots-toast-info"),
        SUCCESS("srots-toast-success"),
        WARNING("srots-toast-warning"),
        ERROR("srots-toast-error");

        private final String styleClass;

        Variant(String styleClass) {
            this.styleClass = styleClass;
        }

        public String getStyleClass() {
            return styleClass;
        }
    }

    public static final Duration DEFAULT_DURATION = Duration.seconds(3);

    public SrotsToast {
        if (variant == null) {
            variant = Variant.INFO;
        }
        if (text == null) {
            text = "";
        }
        if (duration == null) {
            duration = DEFAULT_DURATION;
        }
    }

    public static SrotsToast success(String text) {
        return new SrotsToast(Variant.SUCCESS, text, DEFAULT_DURATION);
    }

    public static SrotsToast info(String text) {
        return new SrotsToast(Variant.INFO, text, DEFAULT_DURATION);
    }

    public static SrotsToast warning(String text) {
        return new SrotsToast(Variant.WARNING, text, DEFAULT_DURATION);
    }

    public static SrotsToast error(String text) {
        return new SrotsToast(Variant.ERROR, text, DEFAULT_DURATION);
    }
}
