package com.srots.presentation.navigation.model;

import java.util.Objects;

/**
 * Immutable catalog entry for a navigable sidebar / menu destination.
 * Badge count is mutable for demo overlays only.
 */
public final class NavigationItem {

    private final String id;
    private final String title;
    private final String iconKey;
    private final NavigationRouteId route;
    private final NavigationRouteId parentRoute;
    private final NavigationGroup group;
    private final int order;
    private final NavigationVisibility visibility;
    private final boolean enabled;
    private final String requiredPermission;
    private final String requiredRole;
    private Integer badgeCount;

    private NavigationItem(Builder builder) {
        this.route = Objects.requireNonNull(builder.route, "route");
        this.title = Objects.requireNonNull(builder.title, "title");
        this.group = Objects.requireNonNull(builder.group, "group");
        this.id = builder.id != null ? builder.id : route.id();
        this.iconKey = builder.iconKey;
        this.parentRoute = builder.parentRoute;
        this.order = builder.order;
        this.visibility = builder.visibility != null ? builder.visibility : NavigationVisibility.VISIBLE;
        this.enabled = builder.enabled;
        this.requiredPermission = builder.requiredPermission;
        this.requiredRole = builder.requiredRole;
        this.badgeCount = builder.badgeCount;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getIconKey() {
        return iconKey;
    }

    public NavigationRouteId getRoute() {
        return route;
    }

    /** Alias for {@link #getParentRoute()}. */
    public NavigationRouteId getParentId() {
        return parentRoute;
    }

    public NavigationRouteId getParentRoute() {
        return parentRoute;
    }

    public NavigationGroup getGroup() {
        return group;
    }

    public int getOrder() {
        return order;
    }

    public NavigationVisibility getVisibility() {
        return visibility;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getRequiredPermission() {
        return requiredPermission;
    }

    public String getRequiredRole() {
        return requiredRole;
    }

    public Integer getBadgeCount() {
        return badgeCount;
    }

    public void setBadgeCount(Integer badgeCount) {
        this.badgeCount = badgeCount;
    }

    public static final class Builder {

        private String id;
        private String title;
        private String iconKey;
        private NavigationRouteId route;
        private NavigationRouteId parentRoute;
        private NavigationGroup group;
        private int order;
        private NavigationVisibility visibility = NavigationVisibility.VISIBLE;
        private boolean enabled = true;
        private String requiredPermission;
        private String requiredRole;
        private Integer badgeCount;

        private Builder() {
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder iconKey(String iconKey) {
            this.iconKey = iconKey;
            return this;
        }

        public Builder route(NavigationRouteId route) {
            this.route = route;
            return this;
        }

        public Builder parent(NavigationRouteId parentRoute) {
            this.parentRoute = parentRoute;
            return this;
        }

        public Builder parentId(NavigationRouteId parentId) {
            this.parentRoute = parentId;
            return this;
        }

        public Builder parentRoute(NavigationRouteId parentRoute) {
            this.parentRoute = parentRoute;
            return this;
        }

        public Builder group(NavigationGroup group) {
            this.group = group;
            return this;
        }

        public Builder order(int order) {
            this.order = order;
            return this;
        }

        public Builder visibility(NavigationVisibility visibility) {
            this.visibility = visibility;
            return this;
        }

        public Builder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder permission(String requiredPermission) {
            this.requiredPermission = requiredPermission;
            return this;
        }

        public Builder requiredPermission(String requiredPermission) {
            this.requiredPermission = requiredPermission;
            return this;
        }

        public Builder role(String requiredRole) {
            this.requiredRole = requiredRole;
            return this;
        }

        public Builder requiredRole(String requiredRole) {
            this.requiredRole = requiredRole;
            return this;
        }

        public Builder badgeCount(Integer badgeCount) {
            this.badgeCount = badgeCount;
            return this;
        }

        public NavigationItem build() {
            return new NavigationItem(this);
        }
    }
}
