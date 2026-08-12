package com.srots.domain.dashboard;

/**
 * Derived dashboard KPI snapshot. Prefer computing from repository aggregates.
 */
public final class DashboardMetrics {
    private final long activeProjects;
    private final long openTasks;
    private final long openIssues;
    private final long activeEmployees;
    private final int releaseReadinessPercent;
    private final int qaCoveragePercent;

    public DashboardMetrics(long activeProjects, long openTasks, long openIssues,
                            long activeEmployees, int releaseReadinessPercent, int qaCoveragePercent) {
        this.activeProjects = activeProjects;
        this.openTasks = openTasks;
        this.openIssues = openIssues;
        this.activeEmployees = activeEmployees;
        this.releaseReadinessPercent = releaseReadinessPercent;
        this.qaCoveragePercent = qaCoveragePercent;
    }

    public long getActiveProjects() { return activeProjects; }
    public long getOpenTasks() { return openTasks; }
    public long getOpenIssues() { return openIssues; }
    public long getActiveEmployees() { return activeEmployees; }
    public int getReleaseReadinessPercent() { return releaseReadinessPercent; }
    public int getQaCoveragePercent() { return qaCoveragePercent; }
}
