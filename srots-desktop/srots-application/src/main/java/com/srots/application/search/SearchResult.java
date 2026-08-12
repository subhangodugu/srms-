package com.srots.application.search;

/**
 * Single search hit from a provider. No JavaFX types.
 */
public final class SearchResult {

    private final String id;
    private final SearchEntityType type;
    private final String title;
    private final String subtitle;
    private final String description;
    private final double score;
    private final String routeId;
    private final EntityReference entityReference;

    public SearchResult(
            String id,
            SearchEntityType type,
            String title,
            String subtitle,
            String description,
            double score,
            String routeId,
            EntityReference entityReference) {
        this.id = id == null || id.isBlank() ? title : id.trim();
        this.type = type == null ? SearchEntityType.SETTINGS : type;
        this.title = title == null || title.isBlank() ? "Untitled" : title.trim();
        this.subtitle = subtitle == null ? "" : subtitle.trim();
        this.description = description == null ? "" : description.trim();
        this.score = score;
        this.routeId = routeId == null ? "" : routeId.trim();
        this.entityReference = entityReference;
    }

    public String getId() {
        return id;
    }

    public SearchEntityType getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getDescription() {
        return description;
    }

    public double getScore() {
        return score;
    }

    public String getRouteId() {
        return routeId;
    }

    public EntityReference getEntityReference() {
        return entityReference;
    }

    public SearchResult withScore(double score) {
        return new SearchResult(id, type, title, subtitle, description, score, routeId, entityReference);
    }
}
