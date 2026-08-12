package com.srots.domain.notification;

import com.srots.domain.model.enums.NotificationType;
import java.time.LocalDateTime;
import java.util.Objects;

public final class AppNotification {
    private final String id;
    private final String title;
    private final String description;
    private final NotificationType type;
    private final boolean read;
    private final LocalDateTime timestamp;
    private final String relatedEntityType;
    private final String relatedEntityId;

    public AppNotification(String id, String title, String description, NotificationType type,
                           boolean read, LocalDateTime timestamp, String relatedEntityType,
                           String relatedEntityId) {
        this.id = Objects.requireNonNull(id);
        this.title = Objects.requireNonNull(title);
        this.description = description;
        this.type = Objects.requireNonNull(type);
        this.read = read;
        this.timestamp = timestamp;
        this.relatedEntityType = relatedEntityType;
        this.relatedEntityId = relatedEntityId;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public NotificationType getType() { return type; }
    public boolean isRead() { return read; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getRelatedEntityType() { return relatedEntityType; }
    public String getRelatedEntityId() { return relatedEntityId; }
}
