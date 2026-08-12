package com.srots.application.dto;

import java.util.List;
import java.util.Objects;

/**
 * Application snapshot for the Overview executive workspace.
 * Derived from repository aggregates — not presentation-hardcoded.
 */
public final class DashboardOverviewDTO {

    private final long activeProjects;
    private final long openTasks;
    private final long openIssues;
    private final long activeEmployees;
    private final long activeTeams;
    private final long completedTasks;
    private final int releaseReadinessPercent;
    private final int qaCoveragePercent;
    private final List<NamedCountDTO> projectHealth;
    private final List<NamedCountDTO> workload;
    private final List<NamedCountDTO> issueBreakdown;
    private final List<ActivityItemDTO> recentActivity;

    public DashboardOverviewDTO(
            long activeProjects,
            long openTasks,
            long openIssues,
            long activeEmployees,
            long activeTeams,
            long completedTasks,
            int releaseReadinessPercent,
            int qaCoveragePercent,
            List<NamedCountDTO> projectHealth,
            List<NamedCountDTO> workload,
            List<NamedCountDTO> issueBreakdown,
            List<ActivityItemDTO> recentActivity) {
        this.activeProjects = activeProjects;
        this.openTasks = openTasks;
        this.openIssues = openIssues;
        this.activeEmployees = activeEmployees;
        this.activeTeams = activeTeams;
        this.completedTasks = completedTasks;
        this.releaseReadinessPercent = releaseReadinessPercent;
        this.qaCoveragePercent = qaCoveragePercent;
        this.projectHealth = List.copyOf(Objects.requireNonNull(projectHealth, "projectHealth"));
        this.workload = List.copyOf(Objects.requireNonNull(workload, "workload"));
        this.issueBreakdown = List.copyOf(Objects.requireNonNull(issueBreakdown, "issueBreakdown"));
        this.recentActivity = List.copyOf(Objects.requireNonNull(recentActivity, "recentActivity"));
    }

    public long getActiveProjects() {
        return activeProjects;
    }

    public long getOpenTasks() {
        return openTasks;
    }

    public long getOpenIssues() {
        return openIssues;
    }

    public long getActiveEmployees() {
        return activeEmployees;
    }

    public long getActiveTeams() {
        return activeTeams;
    }

    public long getCompletedTasks() {
        return completedTasks;
    }

    public int getReleaseReadinessPercent() {
        return releaseReadinessPercent;
    }

    public int getQaCoveragePercent() {
        return qaCoveragePercent;
    }

    public List<NamedCountDTO> getProjectHealth() {
        return projectHealth;
    }

    public List<NamedCountDTO> getWorkload() {
        return workload;
    }

    public List<NamedCountDTO> getIssueBreakdown() {
        return issueBreakdown;
    }

    public List<ActivityItemDTO> getRecentActivity() {
        return recentActivity;
    }
}
