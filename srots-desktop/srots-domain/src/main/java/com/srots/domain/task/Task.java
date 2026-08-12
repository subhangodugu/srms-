package com.srots.domain.task;

import com.srots.domain.model.enums.TaskPriority;
import com.srots.domain.model.enums.TaskStatus;
import java.time.LocalDate;
import java.util.Objects;

public final class Task {
    private final String id;
    private final String title;
    private final String projectId;
    private final String assigneeEmployeeId;
    private final TaskStatus status;
    private final TaskPriority priority;
    private final LocalDate dueDate;
    private final int progressPercent;
    private final LocalDate createdDate;

    public Task(String id, String title, String projectId, String assigneeEmployeeId,
                TaskStatus status, TaskPriority priority, LocalDate dueDate,
                int progressPercent, LocalDate createdDate) {
        this.id = Objects.requireNonNull(id);
        this.title = Objects.requireNonNull(title);
        this.projectId = projectId;
        this.assigneeEmployeeId = assigneeEmployeeId;
        this.status = Objects.requireNonNull(status);
        this.priority = Objects.requireNonNull(priority);
        this.dueDate = dueDate;
        this.progressPercent = progressPercent;
        this.createdDate = createdDate;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getProjectId() { return projectId; }
    public String getAssigneeEmployeeId() { return assigneeEmployeeId; }
    public TaskStatus getStatus() { return status; }
    public TaskPriority getPriority() { return priority; }
    public LocalDate getDueDate() { return dueDate; }
    public int getProgressPercent() { return progressPercent; }
    public LocalDate getCreatedDate() { return createdDate; }
}
