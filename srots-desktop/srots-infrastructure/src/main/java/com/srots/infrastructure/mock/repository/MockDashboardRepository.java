package com.srots.infrastructure.mock.repository;

import com.srots.domain.dashboard.DashboardMetrics;
import com.srots.domain.employee.Employee;
import com.srots.domain.issue.Issue;
import com.srots.domain.model.enums.EmployeeStatus;
import com.srots.domain.model.enums.GatePhaseStatus;
import com.srots.domain.model.enums.IssueStatus;
import com.srots.domain.model.enums.ProjectStatus;
import com.srots.domain.model.enums.TaskStatus;
import com.srots.domain.project.Project;
import com.srots.domain.release.Release;
import com.srots.domain.release.ReleasePipelineGate;
import com.srots.domain.repository.DashboardRepository;
import com.srots.domain.task.Task;
import com.srots.infrastructure.mock.state.MockStateStore;
import com.srots.infrastructure.mock.support.MockRepositoryBehavior;

import java.util.List;

public final class MockDashboardRepository implements DashboardRepository {

    private final MockStateStore store;
    private final MockRepositoryBehavior behavior;

    public MockDashboardRepository(MockStateStore store, MockRepositoryBehavior behavior) {
        this.store = store;
        this.behavior = behavior;
    }

    @Override
    public DashboardMetrics getMetrics() {
        behavior.beforeRead();
        long activeProjects = store.projects().stream().filter(p -> p.getStatus() == ProjectStatus.ACTIVE).count();
        long openTasks = store.tasks().stream()
                .filter(t -> t.getStatus() != TaskStatus.DONE && t.getStatus() != TaskStatus.CANCELLED)
                .count();
        long openIssues = store.issues().stream()
                .filter(i -> i.getStatus() == IssueStatus.OPEN || i.getStatus() == IssueStatus.IN_PROGRESS || i.getStatus() == IssueStatus.REOPENED)
                .count();
        long activeEmployees = store.employees().stream().filter(e -> e.getStatus() == EmployeeStatus.ACTIVE).count();

        List<Release> releases = store.releases();
        int readiness = 0;
        int qaCoverage = 0;
        if (!releases.isEmpty()) {
            Release primary = releases.stream()
                    .filter(r -> "REL-001".equals(r.getId()))
                    .findFirst()
                    .orElse(releases.get(0));
            readiness = primary.getProgressPercent();
            List<ReleasePipelineGate> gates = primary.getPipelineGates();
            if (!gates.isEmpty()) {
                long passed = gates.stream().filter(g -> g.getStatus() == GatePhaseStatus.PASSED).count();
                qaCoverage = (int) Math.round((passed * 100.0) / gates.size());
            } else {
                qaCoverage = readiness;
            }
        }
        return new DashboardMetrics(activeProjects, openTasks, openIssues, activeEmployees, readiness, qaCoverage);
    }
}
