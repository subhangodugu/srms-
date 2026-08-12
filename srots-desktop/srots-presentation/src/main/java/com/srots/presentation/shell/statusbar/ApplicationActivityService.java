package com.srots.presentation.shell.statusbar;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Publishes current application activity for StatusBar consumption.
 * Does not execute imports/syncs — presentation state only.
 */
public final class ApplicationActivityService {

    private final ObjectProperty<ApplicationActivity> current =
            new SimpleObjectProperty<>(ApplicationActivity.idle());

    public ReadOnlyObjectProperty<ApplicationActivity> currentActivityProperty() {
        return current;
    }

    public ApplicationActivity getCurrentActivity() {
        ApplicationActivity value = current.get();
        return value == null ? ApplicationActivity.idle() : value;
    }

    public void publish(ApplicationActivity activity) {
        current.set(activity == null ? ApplicationActivity.idle() : activity);
    }

    public void clear() {
        current.set(ApplicationActivity.idle());
    }

    public void markFailed(String message) {
        current.set(ApplicationActivity.builder(ApplicationActivityType.ERROR)
                .message(message == null || message.isBlank() ? "Operation failed" : message)
                .failed(true)
                .build());
    }
}
