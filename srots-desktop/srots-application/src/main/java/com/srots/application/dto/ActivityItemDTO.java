package com.srots.application.dto;

import java.time.LocalDateTime;
import java.util.Objects;

/** Activity feed row for presentation binding. */
public final class ActivityItemDTO {

    private final String id;
    private final String summary;
    private final String type;
    private final LocalDateTime timestamp;

    public ActivityItemDTO(String id, String summary, String type, LocalDateTime timestamp) {
        this.id = Objects.requireNonNull(id, "id");
        this.summary = Objects.requireNonNull(summary, "summary");
        this.type = type == null ? "" : type;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public String getSummary() {
        return summary;
    }

    public String getType() {
        return type;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
