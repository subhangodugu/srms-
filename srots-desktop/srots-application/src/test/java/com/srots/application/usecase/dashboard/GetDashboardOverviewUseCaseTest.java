package com.srots.application.usecase.dashboard;

import com.srots.application.dto.DashboardOverviewDTO;
import com.srots.domain.activity.ActivityEntry;
import com.srots.domain.dashboard.DashboardMetrics;
import com.srots.domain.issue.Issue;
import com.srots.domain.model.enums.ActivityType;
import com.srots.domain.model.enums.ProjectPriority;
import com.srots.domain.model.enums.ProjectStatus;
import com.srots.domain.model.enums.TaskPriority;
import com.srots.domain.model.enums.TaskStatus;
import com.srots.domain.project.Project;
import com.srots.domain.repository.ActivityRepository;
import com.srots.domain.repository.IssueRepository;
import com.srots.domain.repository.ProjectRepository;
import com.srots.domain.repository.TaskRepository;
import com.srots.domain.repository.TeamRepository;
import com.srots.domain.task.Task;
import com.srots.domain.team.Team;
import com.srots.shared.query.PageRequest;
import com.srots.shared.query.PageResult;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class GetDashboardOverviewUseCaseTest {

    @Test
    void execute_aggregatesRepositorySnapshots() {
        GetDashboardOverviewUseCase useCase = new GetDashboardOverviewUseCase(
                () -> new DashboardMetrics(2, 3, 1, 5, 80, 70),
                stubProjects(),
                stubTasks(),
                stubIssues(),
                stubTeams(),
                stubActivity());

        DashboardOverviewDTO dto = useCase.execute();
        assertEquals(2, dto.getActiveProjects());
        assertEquals(3, dto.getOpenTasks());
        assertEquals(2, dto.getCompletedTasks());
        assertEquals(1, dto.getActiveTeams());
        assertEquals(80, dto.getReleaseReadinessPercent());
        assertFalse(dto.getProjectHealth().isEmpty());
        assertEquals(1, dto.getRecentActivity().size());
        assertEquals(3, dto.getWorkload().size());
    }

    private static ProjectRepository stubProjects() {
        return new ProjectRepository() {
            @Override
            public Optional<Project> findById(String id) {
                return Optional.empty();
            }

            @Override
            public List<Project> findAll() {
                return List.of(
                        new Project("p1", "Alpha", null, null, null, null,
                                ProjectStatus.ACTIVE, ProjectPriority.MEDIUM, null, null, 40),
                        new Project("p2", "Beta", null, null, null, null,
                                ProjectStatus.AT_RISK, ProjectPriority.HIGH, null, null, 20),
                        new Project("p3", "Gamma", null, null, null, null,
                                ProjectStatus.COMPLETED, ProjectPriority.LOW, null, null, 100));
            }

            @Override
            public List<Project> search(String query) {
                return List.of();
            }

            @Override
            public PageResult<Project> findPage(PageRequest pageRequest, String search, Map<String, String> filters) {
                return PageResult.empty(0, 20);
            }

            @Override
            public Project save(Project entity) {
                return entity;
            }

            @Override
            public boolean deleteById(String id) {
                return false;
            }
        };
    }

    private static TaskRepository stubTasks() {
        return new TaskRepository() {
            @Override
            public Optional<Task> findById(String id) {
                return Optional.empty();
            }

            @Override
            public List<Task> findAll() {
                return List.of(
                        new Task("t1", "One", null, null, TaskStatus.TODO, TaskPriority.MEDIUM, null, 0, null),
                        new Task("t2", "Two", null, null, TaskStatus.DONE, TaskPriority.MEDIUM, null, 100, null),
                        new Task("t3", "Three", null, null, TaskStatus.DONE, TaskPriority.LOW, null, 100, null));
            }

            @Override
            public List<Task> search(String query) {
                return List.of();
            }

            @Override
            public PageResult<Task> findPage(PageRequest pageRequest, String search, Map<String, String> filters) {
                return PageResult.empty(0, 20);
            }

            @Override
            public Task save(Task entity) {
                return entity;
            }

            @Override
            public boolean deleteById(String id) {
                return false;
            }
        };
    }

    private static IssueRepository stubIssues() {
        return new IssueRepository() {
            @Override
            public Optional<Issue> findById(String id) {
                return Optional.empty();
            }

            @Override
            public List<Issue> findAll() {
                return List.of();
            }

            @Override
            public List<Issue> search(String query) {
                return List.of();
            }

            @Override
            public PageResult<Issue> findPage(PageRequest pageRequest, String search, Map<String, String> filters) {
                return PageResult.empty(0, 20);
            }

            @Override
            public Issue save(Issue entity) {
                return entity;
            }

            @Override
            public boolean deleteById(String id) {
                return false;
            }
        };
    }

    private static TeamRepository stubTeams() {
        return new TeamRepository() {
            @Override
            public Optional<Team> findById(String id) {
                return Optional.empty();
            }

            @Override
            public List<Team> findAll() {
                return List.of(new Team("t1", "Alpha", "d1", null));
            }

            @Override
            public List<Team> search(String query) {
                return List.of();
            }

            @Override
            public PageResult<Team> findPage(PageRequest pageRequest, String search, Map<String, String> filters) {
                return PageResult.empty(0, 20);
            }

            @Override
            public Team save(Team entity) {
                return entity;
            }

            @Override
            public boolean deleteById(String id) {
                return false;
            }
        };
    }

    private static ActivityRepository stubActivity() {
        return new ActivityRepository() {
            @Override
            public Optional<ActivityEntry> findById(String id) {
                return Optional.empty();
            }

            @Override
            public List<ActivityEntry> findAll() {
                return List.of(new ActivityEntry(
                        "a1",
                        ActivityType.SYSTEM,
                        "Sync completed",
                        null,
                        null,
                        null,
                        LocalDateTime.now()));
            }

            @Override
            public List<ActivityEntry> search(String query) {
                return List.of();
            }

            @Override
            public PageResult<ActivityEntry> findPage(
                    PageRequest pageRequest, String search, Map<String, String> filters) {
                return PageResult.empty(0, 20);
            }

            @Override
            public ActivityEntry save(ActivityEntry entity) {
                return entity;
            }

            @Override
            public boolean deleteById(String id) {
                return false;
            }
        };
    }
}
