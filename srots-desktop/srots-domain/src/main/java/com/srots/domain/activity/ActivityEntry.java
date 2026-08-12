package com.srots.domain.activity;

import com.srots.domain.model.enums.ActivityType;
import java.time.LocalDateTime;
import java.util.Objects;

public final class ActivityEntry {
    private final String id;
    private final ActivityType type;
    private final String summary;
    private final String actorEmployeeId;
    private final String relatedEntityType;
    private final String relatedEntityId;
    private final LocalDateTime timestamp;

    public ActivityEntry(String id, ActivityType type, String summary, String actorEmployeeId,
                         String relatedEntityType, String relatedEntityId, LocalDateTime timestamp) {
        this.id = Objects.requireNonNull(id);
        this.type = Objects.requireNonNull(type);
        this.summary = Objects.requireNonNull(summary);
        this.actorEmployeeId = actorEmployeeId;
        this.relatedEntityType = relatedEntityType;
        this.relatedEntityId = relatedEntityId;
        this.timestamp = timestamp;
    }

    public String getId() { return id; }
    public ActivityType getType() { return type; }
    public String getSummary() { return summary; }
    public String getActorEmployeeId() { return actorEmployeeId; }
    public String getRelatedEntityType() { return relatedEntityType; }
    public String getRelatedEntityId() { return relatedEntityId; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
