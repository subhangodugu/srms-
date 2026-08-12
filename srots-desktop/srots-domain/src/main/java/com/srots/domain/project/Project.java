package com.srots.domain.project;

import com.srots.domain.model.enums.ProjectPriority;
import com.srots.domain.model.enums.ProjectStatus;
import java.time.LocalDate;
import java.util.Objects;

public final class Project {
    private final String id;
    private final String name;
    private final String description;
    private final String productId;
    private final String ownerEmployeeId;
    private final String teamId;
    private final ProjectStatus status;
    private final ProjectPriority priority;
    private final LocalDate startDate;
    private final LocalDate targetDate;
    private final int progressPercent;

    public Project(String id, String name, String description, String productId, String ownerEmployeeId,
                   String teamId, ProjectStatus status, ProjectPriority priority,
                   LocalDate startDate, LocalDate targetDate, int progressPercent) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.description = description;
        this.productId = productId;
        this.ownerEmployeeId = ownerEmployeeId;
        this.teamId = teamId;
        this.status = Objects.requireNonNull(status);
        this.priority = Objects.requireNonNull(priority);
        this.startDate = startDate;
        this.targetDate = targetDate;
        this.progressPercent = progressPercent;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getProductId() { return productId; }
    public String getOwnerEmployeeId() { return ownerEmployeeId; }
    public String getTeamId() { return teamId; }
    public ProjectStatus getStatus() { return status; }
    public ProjectPriority getPriority() { return priority; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getTargetDate() { return targetDate; }
    public int getProgressPercent() { return progressPercent; }
}
