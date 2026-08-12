package com.srots.application.search;

/**
 * Stable entity reference for navigation without UI coupling.
 */
public record EntityReference(SearchEntityType entityType, String entityId) {

    public EntityReference {
        entityType = entityType == null ? SearchEntityType.SETTINGS : entityType;
        entityId = entityId == null ? "" : entityId.trim();
    }
}
