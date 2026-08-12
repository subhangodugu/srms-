package com.srots.domain.issue;

import com.srots.domain.model.enums.IssuePriority;
import com.srots.domain.model.enums.IssueSeverity;
import com.srots.domain.model.enums.IssueStatus;
import java.time.LocalDate;
import java.util.Objects;

public final class Issue {
    private final String id;
    private final String title;
    private final String description;
    private final String projectId;
    private final String productId;
    private final String reporterEmployeeId;
    private final String assigneeEmployeeId;
    private final IssuePriority priority;
    private final IssueSeverity severity;
    private final IssueStatus status;
    private final LocalDate createdDate;
    private final LocalDate updatedDate;

    public Issue(String id, String title, String description, String projectId, String productId,
                 String reporterEmployeeId, String assigneeEmployeeId, IssuePriority priority,
                 IssueSeverity severity, IssueStatus status, LocalDate createdDate, LocalDate updatedDate) {
        this.id = Objects.requireNonNull(id);
        this.title = Objects.requireNonNull(title);
        this.description = description;
        this.projectId = projectId;
        this.productId = productId;
        this.reporterEmployeeId = reporterEmployeeId;
        this.assigneeEmployeeId = assigneeEmployeeId;
        this.priority = Objects.requireNonNull(priority);
        this.severity = Objects.requireNonNull(severity);
        this.status = Objects.requireNonNull(status);
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getProjectId() { return projectId; }
    public String getProductId() { return productId; }
    public String getReporterEmployeeId() { return reporterEmployeeId; }
    public String getAssigneeEmployeeId() { return assigneeEmployeeId; }
    public IssuePriority getPriority() { return priority; }
    public IssueSeverity getSeverity() { return severity; }
    public IssueStatus getStatus() { return status; }
    public LocalDate getCreatedDate() { return createdDate; }
    public LocalDate getUpdatedDate() { return updatedDate; }
}
