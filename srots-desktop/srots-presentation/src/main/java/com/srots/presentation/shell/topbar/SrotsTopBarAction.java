package com.srots.presentation.shell.topbar;

import java.util.Objects;

/**
 * Data-driven contextual TopBar action. Execution stays outside business logic.
 */
public final class SrotsTopBarAction {

    private final String id;
    private final String label;
    private final String iconKey;
    private final String tooltip;
    private final boolean enabled;
    private final boolean visible;
    private final int priority;
    private final Runnable onAction;

    private SrotsTopBarAction(Builder builder) {
        this.id = Objects.requireNonNull(builder.id, "id");
        this.label = Objects.requireNonNull(builder.label, "label");
        this.iconKey = builder.iconKey;
        this.tooltip = builder.tooltip == null || builder.tooltip.isBlank() ? builder.label : builder.tooltip;
        this.enabled = builder.enabled;
        this.visible = builder.visible;
        this.priority = builder.priority;
        this.onAction = builder.onAction == null ? () -> {
        } : builder.onAction;
    }

    public static Builder builder(String id, String label) {
        return new Builder(id, label);
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public String getIconKey() {
        return iconKey;
    }

    public String getTooltip() {
        return tooltip;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isVisible() {
        return visible;
    }

    public int getPriority() {
        return priority;
    }

    public void execute() {
        onAction.run();
    }

    public static final class Builder {
        private final String id;
        private final String label;
        private String iconKey;
        private String tooltip;
        private boolean enabled = true;
        private boolean visible = true;
        private int priority;
        private Runnable onAction;

        private Builder(String id, String label) {
            this.id = id;
            this.label = label;
        }

        public Builder iconKey(String iconKey) {
            this.iconKey = iconKey;
            return this;
        }

        public Builder tooltip(String tooltip) {
            this.tooltip = tooltip;
            return this;
        }

        public Builder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder visible(boolean visible) {
            this.visible = visible;
            return this;
        }

        public Builder priority(int priority) {
            this.priority = priority;
            return this;
        }

        public Builder onAction(Runnable onAction) {
            this.onAction = onAction;
            return this;
        }

        public SrotsTopBarAction build() {
            return new SrotsTopBarAction(this);
        }
    }
}
