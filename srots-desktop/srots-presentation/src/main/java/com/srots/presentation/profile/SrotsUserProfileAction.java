package com.srots.presentation.profile;

import java.util.Objects;

/**
 * Visible profile-menu action. Visibility/enabled flags are presentation-only.
 */
public final class SrotsUserProfileAction {

    private final String id;
    private final String label;
    private final String iconGlyph;
    private final boolean visible;
    private final boolean enabled;
    private final SrotsUserProfileActionType type;
    private final int priority;
    private final String requiredPermission;
    private final Runnable onAction;

    private SrotsUserProfileAction(Builder builder) {
        this.id = Objects.requireNonNull(builder.id, "id");
        this.label = builder.label == null || builder.label.isBlank() ? builder.id : builder.label.trim();
        this.iconGlyph = builder.iconGlyph == null ? "" : builder.iconGlyph;
        this.visible = builder.visible;
        this.enabled = builder.enabled;
        this.type = builder.type == null ? SrotsUserProfileActionType.COMMAND : builder.type;
        this.priority = builder.priority;
        this.requiredPermission = builder.requiredPermission == null || builder.requiredPermission.isBlank()
                ? null
                : builder.requiredPermission.trim();
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

    public String getIconGlyph() {
        return iconGlyph;
    }

    public boolean isVisible() {
        return visible;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public SrotsUserProfileActionType getType() {
        return type;
    }

    public int getPriority() {
        return priority;
    }

    public String getRequiredPermission() {
        return requiredPermission;
    }

    public void execute() {
        if (enabled && visible) {
            onAction.run();
        }
    }

    public SrotsUserProfileAction withEnabled(boolean enabled) {
        return builder(id, label)
                .iconGlyph(iconGlyph)
                .visible(visible)
                .enabled(enabled)
                .type(type)
                .priority(priority)
                .requiredPermission(requiredPermission)
                .onAction(onAction)
                .build();
    }

    public SrotsUserProfileAction withVisible(boolean visible) {
        return builder(id, label)
                .iconGlyph(iconGlyph)
                .visible(visible)
                .enabled(enabled)
                .type(type)
                .priority(priority)
                .requiredPermission(requiredPermission)
                .onAction(onAction)
                .build();
    }

    public static final class Builder {
        private final String id;
        private final String label;
        private String iconGlyph = "";
        private boolean visible = true;
        private boolean enabled = true;
        private SrotsUserProfileActionType type = SrotsUserProfileActionType.COMMAND;
        private int priority = 100;
        private String requiredPermission;
        private Runnable onAction;

        private Builder(String id, String label) {
            this.id = id;
            this.label = label;
        }

        public Builder iconGlyph(String iconGlyph) {
            this.iconGlyph = iconGlyph;
            return this;
        }

        public Builder visible(boolean visible) {
            this.visible = visible;
            return this;
        }

        public Builder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder type(SrotsUserProfileActionType type) {
            this.type = type;
            return this;
        }

        public Builder priority(int priority) {
            this.priority = priority;
            return this;
        }

        public Builder requiredPermission(String requiredPermission) {
            this.requiredPermission = requiredPermission;
            return this;
        }

        public Builder onAction(Runnable onAction) {
            this.onAction = onAction;
            return this;
        }

        public SrotsUserProfileAction build() {
            return new SrotsUserProfileAction(this);
        }
    }
}
