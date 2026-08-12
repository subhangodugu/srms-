package com.srots.presentation.shell.statusbar;

import java.util.Objects;
import java.util.UUID;

/**
 * Immutable snapshot of a user-facing application activity for the StatusBar.
 */
public final class ApplicationActivity {

    private final String id;
    private final ApplicationActivityType type;
    private final String message;
    private final Double progress;
    private final boolean failed;

    private ApplicationActivity(Builder builder) {
        this.id = builder.id == null || builder.id.isBlank() ? UUID.randomUUID().toString() : builder.id;
        this.type = Objects.requireNonNullElse(builder.type, ApplicationActivityType.WORKING);
        this.message = builder.message == null ? "" : builder.message.trim();
        this.progress = builder.progress;
        this.failed = builder.failed;
    }

    public static ApplicationActivity idle() {
        return builder(ApplicationActivityType.IDLE).message("Ready").build();
    }

    public static Builder builder(ApplicationActivityType type) {
        return new Builder(type);
    }

    public String getId() {
        return id;
    }

    public ApplicationActivityType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public Double getProgress() {
        return progress;
    }

    public boolean hasDeterminateProgress() {
        return progress != null && progress >= 0.0 && progress <= 1.0;
    }

    public boolean isFailed() {
        return failed;
    }

    public boolean isIdle() {
        return type == ApplicationActivityType.IDLE && !failed;
    }

    public static final class Builder {
        private String id;
        private final ApplicationActivityType type;
        private String message;
        private Double progress;
        private boolean failed;

        private Builder(ApplicationActivityType type) {
            this.type = type;
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder progress(Double progress) {
            this.progress = progress;
            return this;
        }

        public Builder failed(boolean failed) {
            this.failed = failed;
            return this;
        }

        public ApplicationActivity build() {
            return new ApplicationActivity(this);
        }
    }
}
