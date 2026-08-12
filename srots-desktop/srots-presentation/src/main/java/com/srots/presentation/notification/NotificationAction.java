package com.srots.presentation.notification;

import com.srots.presentation.navigation.model.NavigationRouteId;

/**
 * Presentation action attached to a notification. Execution uses application services.
 */
public record NotificationAction(
        NotificationActionType type,
        NavigationRouteId route,
        String entityType,
        String entityId) {

    public static NotificationAction none() {
        return new NotificationAction(NotificationActionType.NONE, null, null, null);
    }

    public static NotificationAction navigate(NavigationRouteId route) {
        return new NotificationAction(NotificationActionType.NAVIGATE, route, null, null);
    }

    public NotificationAction {
        type = type == null ? NotificationActionType.NONE : type;
        entityType = entityType == null ? "" : entityType.trim();
        entityId = entityId == null ? "" : entityId.trim();
    }

    public boolean hasNavigation() {
        return type == NotificationActionType.NAVIGATE && route != null;
    }
}
