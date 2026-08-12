package com.srots.presentation.navigation.service;

import com.srots.presentation.navigation.model.NavigationItem;
import com.srots.presentation.navigation.model.NavigationRouteId;
import com.srots.presentation.navigation.model.NavigationVisibility;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Applies authentication / permission / role rules to navigation visibility.
 * Hidden items are excluded; disabled items remain visible as disabled.
 */
public final class NavigationVisibilityService {

    public List<NavigationItem> filterVisible(Collection<NavigationItem> items, UserAccessContext ctx) {
        Objects.requireNonNull(items, "items");
        Objects.requireNonNull(ctx, "ctx");

        List<NavigationItem> visible = new ArrayList<>();
        for (NavigationItem item : items) {
            if (item == null) {
                continue;
            }
            NavigationVisibility effective = resolveVisibility(item, ctx);
            if (effective == NavigationVisibility.HIDDEN) {
                continue;
            }
            visible.add(item);
        }
        return List.copyOf(visible);
    }

    private static NavigationVisibility resolveVisibility(NavigationItem item, UserAccessContext ctx) {
        if (item.getVisibility() == NavigationVisibility.HIDDEN) {
            return NavigationVisibility.HIDDEN;
        }

        NavigationRouteId route = item.getRoute();
        if (!ctx.isAuthenticated() && route != NavigationRouteId.LOGIN) {
            return NavigationVisibility.HIDDEN;
        }

        String permission = item.getRequiredPermission();
        if (permission != null && !permission.isBlank() && !ctx.hasPermission(permission)) {
            return NavigationVisibility.HIDDEN;
        }

        String role = item.getRequiredRole();
        if (role != null && !role.isBlank() && !ctx.hasRole(role)) {
            return NavigationVisibility.HIDDEN;
        }

        if (item.getVisibility() == NavigationVisibility.DISABLED) {
            return NavigationVisibility.DISABLED;
        }

        return NavigationVisibility.VISIBLE;
    }
}
