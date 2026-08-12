package com.srots.application.usecase.dashboard;

import com.srots.application.dto.ActivityItemDTO;
import com.srots.application.dto.DashboardOverviewDTO;
import com.srots.application.dto.NamedCountDTO;
import com.srots.domain.activity.ActivityEntry;
import com.srots.domain.dashboard.DashboardMetrics;
import com.srots.domain.issue.Issue;
import com.srots.domain.model.enums.IssueStatus;
import com.srots.domain.model.enums.ProjectStatus;
import com.srots.domain.model.enums.TaskStatus;
import com.srots.domain.project.Project;
import com.srots.domain.repository.ActivityRepository;
import com.srots.domain.repository.DashboardRepository;
import com.srots.domain.repository.IssueRepository;
import com.srots.domain.repository.ProjectRepository;
import com.srots.domain.repository.TaskRepository;
import com.srots.domain.repository.TeamRepository;
import com.srots.domain.task.Task;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Builds the Overview executive snapshot from repository aggregates.
 */
public final class GetDashboardOverviewUseCase {

    private static final int ACTIVITY_LIMIT = 8;

    private final DashboardRepository dashboardRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final IssueRepository issueRepository;
    private final TeamRepository teamRepository;
    private final ActivityRepository activityRepository;

    public GetDashboardOverviewUseCase(
            DashboardRepository dashboardRepository,
            ProjectRepository projectRepository,
            TaskRepository taskRepository,
            IssueRepository issueRepository,
            TeamRepository teamRepository,
            ActivityRepository activityRepository) {
        this.dashboardRepository = Objects.requireNonNull(dashboardRepository, "dashboardRepository");
        this.projectRepository = Objects.requireNonNull(projectRepository, "projectRepository");
        this.taskRepository = Objects.requireNonNull(taskRepository, "taskRepository");
        this.issueRepository = Objects.requireNonNull(issueRepository, "issueRepository");
        this.teamRepository = Objects.requireNonNull(teamRepository, "teamRepository");
        this.activityRepository = Objects.requireNonNull(activityRepository, "activityRepository");
    }

    public DashboardOverviewDTO execute() {
        DashboardMetrics metrics = dashboardRepository.getMetrics();
        List<Project> projects = projectRepository.findAll();
        List<Task> tasks = taskRepository.findAll();
        List<Issue> issues = issueRepository.findAll();

        long completedTasks = tasks.stream().filter(t -> t.getStatus() == TaskStatus.DONE).count();
        long activeTeams = teamRepository.findAll().size();

        Map<ProjectStatus, Long> healthCounts = new EnumMap<>(ProjectStatus.class);
        for (ProjectStatus status : ProjectStatus.values()) {
            healthCounts.put(status, 0L);
        }
        for (Project project : projects) {
            ProjectStatus status = project.getStatus() == null ? ProjectStatus.PLANNED : project.getStatus();
            healthCounts.merge(status, 1L, Long::sum);
        }

        List<NamedCountDTO> projectHealth = List.of(
                new NamedCountDTO("Healthy", healthCounts.getOrDefault(ProjectStatus.ACTIVE, 0L)
                        + healthCounts.getOrDefault(ProjectStatus.PLANNED, 0L)),
                new NamedCountDTO("At Risk", healthCounts.getOrDefault(ProjectStatus.AT_RISK, 0L)),
                new NamedCountDTO("Blocked", healthCounts.getOrDefault(ProjectStatus.BLOCKED, 0L)
                        + healthCounts.getOrDefault(ProjectStatus.ON_HOLD, 0L)),
                new NamedCountDTO("Completed", healthCounts.getOrDefault(ProjectStatus.COMPLETED, 0L)));

        List<NamedCountDTO> workload = List.of(
                new NamedCountDTO("Projects", projects.size()),
                new NamedCountDTO("Tasks", tasks.size()),
                new NamedCountDTO("Issues", issues.size()));

        Map<IssueStatus, Long> issueCounts = issues.stream()
                .collect(Collectors.groupingBy(
                        i -> i.getStatus() == null ? IssueStatus.OPEN : i.getStatus(),
                        () -> new EnumMap<>(IssueStatus.class),
                        Collectors.counting()));

        List<NamedCountDTO> issueBreakdown = List.of(
                new NamedCountDTO("Open", issueCounts.getOrDefault(IssueStatus.OPEN, 0L)
                        + issueCounts.getOrDefault(IssueStatus.REOPENED, 0L)),
                new NamedCountDTO("In Progress", issueCounts.getOrDefault(IssueStatus.IN_PROGRESS, 0L)),
                new NamedCountDTO("Resolved", issueCounts.getOrDefault(IssueStatus.RESOLVED, 0L)
                        + issueCounts.getOrDefault(IssueStatus.CLOSED, 0L)));

        List<ActivityItemDTO> recentActivity = activityRepository.findAll().stream()
                .sorted(Comparator.comparing(ActivityEntry::getTimestamp,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(ACTIVITY_LIMIT)
                .map(entry -> new ActivityItemDTO(
                        entry.getId(),
                        entry.getSummary(),
                        entry.getType() == null ? "" : entry.getType().name(),
                        entry.getTimestamp()))
                .toList();

        return new DashboardOverviewDTO(
                metrics.getActiveProjects(),
                metrics.getOpenTasks(),
                metrics.getOpenIssues(),
                metrics.getActiveEmployees(),
                activeTeams,
                completedTasks,
                metrics.getReleaseReadinessPercent(),
                metrics.getQaCoveragePercent(),
                projectHealth,
                workload,
                issueBreakdown,
                recentActivity);
    }
}
