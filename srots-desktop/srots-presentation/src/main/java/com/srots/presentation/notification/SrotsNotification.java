package com.srots.presentation.notification;

import java.time.Instant;
import java.util.Objects;

/**
 * Presentation notification model. No secrets; no employee/entity coupling.
 */
public final class SrotsNotification {

    private final String id;
    private final NotificationKind type;
    private final String title;
    private final String message;
    private final Instant timestamp;
    private final boolean read;
    private final NotificationPriority priority;
    private final NotificationAction action;

    public SrotsNotification(
            String id,
            NotificationKind type,
            String title,
            String message,
            Instant timestamp,
            boolean read,
            NotificationPriority priority,
            NotificationAction action) {
        this.id = Objects.requireNonNull(id, "id");
        this.type = type == null ? NotificationKind.GENERAL : type;
        this.title = title == null || title.isBlank() ? "Notification" : title.trim();
        this.message = message == null ? "" : message.trim();
        this.timestamp = timestamp == null ? Instant.EPOCH : timestamp;
        this.read = read;
        this.priority = priority == null ? NotificationPriority.NORMAL : priority;
        this.action = action == null ? NotificationAction.none() : action;
    }

    public String getId() {
        return id;
    }

    public NotificationKind getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public boolean isRead() {
        return read;
    }

    public NotificationPriority getPriority() {
        return priority;
    }

    public NotificationAction getAction() {
        return action;
    }

    public SrotsNotification withRead(boolean read) {
        return new SrotsNotification(id, type, title, message, timestamp, read, priority, action);
    }
}
