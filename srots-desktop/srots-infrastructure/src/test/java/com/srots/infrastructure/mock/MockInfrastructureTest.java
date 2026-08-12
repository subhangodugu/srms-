package com.srots.infrastructure.mock;

import com.srots.domain.employee.Employee;
import com.srots.domain.model.enums.EmployeeStatus;
import com.srots.domain.model.enums.ProjectStatus;
import com.srots.domain.project.Project;
import com.srots.domain.repository.EmployeeRepository;
import com.srots.domain.repository.ProductRepository;
import com.srots.domain.repository.ProjectRepository;
import com.srots.domain.repository.ReleaseRepository;
import com.srots.domain.repository.TaskRepository;
import com.srots.infrastructure.mock.configuration.MockConfiguration;
import com.srots.infrastructure.mock.configuration.MockFailurePolicy;
import com.srots.infrastructure.mock.configuration.MockScenarioType;
import com.srots.infrastructure.mock.configuration.ProductionMockGuard;
import com.srots.infrastructure.mock.factory.MockEmployeeFactory;
import com.srots.shared.query.PageRequest;
import com.srots.shared.query.PageResult;
import com.srots.shared.query.SortDirection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MockInfrastructureTest {

    private MockInfrastructure mock;

    @BeforeEach
    void setUp() {
        MockConfiguration configuration = new MockConfiguration();
        configuration.setRuntimeEnvironment("development");
        configuration.setScenario(MockScenarioType.NORMAL);
        configuration.setFailurePolicy(MockFailurePolicy.NEVER);
        mock = new MockInfrastructure(configuration);
    }

    @Test
    void normalScenario_seedsRelatedDatasets() {
        assertEquals(30, mock.employeeRepository().findAll().size());
        assertEquals(10, mock.departmentRepository().findAll().size());
        assertEquals(10, mock.teamRepository().findAll().size());
        assertEquals(15, mock.projectRepository().findAll().size());
        assertEquals(75, mock.taskRepository().findAll().size());
        assertEquals(45, mock.issueRepository().findAll().size());
        assertEquals(2, mock.productRepository().findAll().size());
        assertEquals(7, mock.productVersionRepository().findAll().size());
        assertEquals(5, mock.releaseRepository().findAll().size());
        assertEquals(15, mock.customerRepository().findAll().size());
        assertFalse(mock.notificationRepository().findAll().isEmpty());
        assertFalse(mock.activityRepository().findAll().isEmpty());
        assertTrue(mock.productRepository().findAll().stream().anyMatch(p -> "SROTS".equals(p.getCode())));
        assertTrue(mock.productRepository().findAll().stream().anyMatch(p -> "COMPTY".equals(p.getCode())));
    }

    @Test
    void employeeRepository_findSearchPageCrud() {
        EmployeeRepository repo = mock.employeeRepository();
        assertTrue(repo.findById("EMP-001").isPresent());
        assertFalse(repo.search("rahman").isEmpty());

        PageResult<Employee> page = repo.findPage(
                PageRequest.of(0, 10, "fullName", SortDirection.ASC),
                null,
                Map.of("status", "ACTIVE"));
        assertEquals(10, page.items().size());
        assertTrue(page.totalCount() >= 10);

        Employee created = MockEmployeeFactory.create(
                "EMP-999", "Zara Ahmed", "zara.ahmed@srots.example", "Software Engineer",
                "DEPT-ENG", "TEAM-BACKEND", "EMP-001", "Bengaluru");
        repo.save(created);
        assertTrue(repo.findById("EMP-999").isPresent());
        assertTrue(repo.deleteById("EMP-999"));
        assertTrue(repo.findById("EMP-999").isEmpty());
    }

    @Test
    void projectTaskRelease_queryAndRelationships() {
        ProjectRepository projects = mock.projectRepository();
        TaskRepository tasks = mock.taskRepository();
        ReleaseRepository releases = mock.releaseRepository();
        ProductRepository products = mock.productRepository();

        Project project = projects.findById("PRJ-001").orElseThrow();
        assertTrue(products.findById(new com.srots.domain.valueobject.ProductId(project.getProductId())).isPresent());
        assertFalse(tasks.search("PRJ-001").isEmpty());
        assertTrue(releases.findById("REL-001").isPresent());
        assertFalse(releases.findById("REL-001").orElseThrow().getPipelineGates().isEmpty());
    }

    @Test
    void dashboardMetrics_areDerivedFromRecords() {
        long activeProjects = mock.projectRepository().findAll().stream()
                .filter(p -> p.getStatus() == ProjectStatus.ACTIVE)
                .count();
        var metrics = mock.dashboardRepository().getMetrics();
        assertEquals(activeProjects, metrics.getActiveProjects());
        assertTrue(metrics.getOpenTasks() > 0);
        assertTrue(metrics.getActiveEmployees() > 0);
        assertEquals(87, metrics.getReleaseReadinessPercent());
    }

    @Test
    void emptyScenario_clearsRecords() {
        mock.applyScenario(MockScenarioType.EMPTY);
        assertTrue(mock.employeeRepository().findAll().isEmpty());
        assertTrue(mock.projectRepository().findAll().isEmpty());
        assertTrue(mock.taskRepository().findAll().isEmpty());
        assertTrue(mock.releaseRepository().findAll().isEmpty());
    }

    @Test
    void errorScenario_throwsOnRead() {
        mock.applyScenario(MockScenarioType.ERROR);
        assertThrows(IllegalStateException.class, () -> mock.employeeRepository().findAll());
    }

    @Test
    void offlineScenario_throwsUnavailable() {
        mock.applyScenario(MockScenarioType.OFFLINE);
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> mock.projectRepository().findAll());
        assertTrue(ex.getMessage().toLowerCase().contains("offline"));
    }

    @Test
    void reset_restoresNormalDataset() {
        mock.applyScenario(MockScenarioType.EMPTY);
        mock.configuration().setScenario(MockScenarioType.NORMAL);
        mock.reset();
        assertEquals(30, mock.employeeRepository().findAll().size());
    }

    @Test
    void productionGuard_rejectsMockMode() {
        MockConfiguration configuration = new MockConfiguration();
        configuration.setRuntimeEnvironment("production");
        configuration.setDataMode(com.srots.infrastructure.mock.configuration.DataMode.MOCK);
        assertThrows(IllegalStateException.class, () -> ProductionMockGuard.assertSafe(configuration));
    }

    @Test
    void mockUserSwitch_updatesDiagnostics() {
        mock.authenticationProvider().switchUser("QA");
        assertEquals("QA", mock.diagnostics().mockUserRole());
        assertEquals(EmployeeStatus.ACTIVE, mock.employeeRepository().findById("EMP-001").orElseThrow().getStatus());
    }
}
