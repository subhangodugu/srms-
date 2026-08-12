package com.srots.infrastructure.mock;

import com.srots.domain.repository.ActivityRepository;
import com.srots.domain.repository.CustomerRepository;
import com.srots.domain.repository.DashboardRepository;
import com.srots.domain.repository.DepartmentRepository;
import com.srots.domain.repository.EmployeeRepository;
import com.srots.domain.repository.IssueRepository;
import com.srots.domain.repository.NotificationRepository;
import com.srots.domain.repository.ProductRepository;
import com.srots.domain.repository.ProductVersionRepository;
import com.srots.domain.repository.ProjectRepository;
import com.srots.domain.repository.ReleaseRepository;
import com.srots.domain.repository.SalesDealRepository;
import com.srots.domain.repository.SalesLeadRepository;
import com.srots.domain.repository.SalesOpportunityRepository;
import com.srots.domain.repository.TaskRepository;
import com.srots.domain.repository.TeamRepository;
import com.srots.infrastructure.mock.auth.MockAuthenticationProvider;
import com.srots.infrastructure.mock.configuration.DataMode;
import com.srots.infrastructure.mock.configuration.MockConfiguration;
import com.srots.infrastructure.mock.configuration.MockScenarioType;
import com.srots.infrastructure.mock.configuration.ProductionMockGuard;
import com.srots.infrastructure.mock.datasource.MockEmployeeDataSource;
import com.srots.infrastructure.mock.datasource.MockProductDataSource;
import com.srots.infrastructure.mock.datasource.MockProjectDataSource;
import com.srots.infrastructure.mock.datasource.MockReleaseDataSource;
import com.srots.infrastructure.mock.datasource.MockTaskDataSource;
import com.srots.infrastructure.mock.diagnostics.MockDiagnosticsSnapshot;
import com.srots.infrastructure.mock.events.MockEventPublisher;
import com.srots.infrastructure.mock.repository.MockActivityRepository;
import com.srots.infrastructure.mock.repository.MockCustomerRepository;
import com.srots.infrastructure.mock.repository.MockDashboardRepository;
import com.srots.infrastructure.mock.repository.MockDepartmentRepository;
import com.srots.infrastructure.mock.repository.MockEmployeeRepository;
import com.srots.infrastructure.mock.repository.MockIssueRepository;
import com.srots.infrastructure.mock.repository.MockNotificationRepository;
import com.srots.infrastructure.mock.repository.MockProductRepository;
import com.srots.infrastructure.mock.repository.MockProductVersionRepository;
import com.srots.infrastructure.mock.repository.MockProjectRepository;
import com.srots.infrastructure.mock.repository.MockReleaseRepository;
import com.srots.infrastructure.mock.repository.MockSalesDealRepository;
import com.srots.infrastructure.mock.repository.MockSalesLeadRepository;
import com.srots.infrastructure.mock.repository.MockSalesOpportunityRepository;
import com.srots.infrastructure.mock.repository.MockTaskRepository;
import com.srots.infrastructure.mock.repository.MockTeamRepository;
import com.srots.infrastructure.mock.scenario.EmptyScenario;
import com.srots.infrastructure.mock.scenario.ErrorScenario;
import com.srots.infrastructure.mock.scenario.LargeDatasetScenario;
import com.srots.infrastructure.mock.scenario.LoadingScenario;
import com.srots.infrastructure.mock.scenario.MockScenario;
import com.srots.infrastructure.mock.scenario.NormalScenario;
import com.srots.infrastructure.mock.scenario.OfflineScenario;
import com.srots.infrastructure.mock.seed.MockDataSeeder;
import com.srots.infrastructure.mock.state.MockStateStore;
import com.srots.infrastructure.mock.support.MockRepositoryBehavior;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumMap;
import java.util.Map;

/**
 * Composition root for development mock infrastructure.
 */
public final class MockInfrastructure {

    private static final Logger log = LoggerFactory.getLogger(MockInfrastructure.class);

    private final MockConfiguration configuration;
    private final MockStateStore stateStore;
    private final MockDataSeeder seeder;
    private final MockRepositoryBehavior behavior;
    private final MockAuthenticationProvider authenticationProvider;
    private final MockEventPublisher eventPublisher;
    private final Map<MockScenarioType, MockScenario> scenarios;

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final TeamRepository teamRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final IssueRepository issueRepository;
    private final ProductRepository productRepository;
    private final ProductVersionRepository productVersionRepository;
    private final ReleaseRepository releaseRepository;
    private final CustomerRepository customerRepository;
    private final SalesLeadRepository salesLeadRepository;
    private final SalesOpportunityRepository salesOpportunityRepository;
    private final SalesDealRepository salesDealRepository;
    private final NotificationRepository notificationRepository;
    private final ActivityRepository activityRepository;
    private final DashboardRepository dashboardRepository;

    private final MockEmployeeDataSource employeeDataSource;
    private final MockProjectDataSource projectDataSource;
    private final MockTaskDataSource taskDataSource;
    private final MockProductDataSource productDataSource;
    private final MockReleaseDataSource releaseDataSource;

    public MockInfrastructure(MockConfiguration configuration) {
        this.configuration = configuration == null ? new MockConfiguration() : configuration;
        ProductionMockGuard.assertSafe(this.configuration);

        this.stateStore = new MockStateStore();
        this.seeder = new MockDataSeeder(this.configuration, this.stateStore);
        this.behavior = new MockRepositoryBehavior(this.configuration);
        this.authenticationProvider = new MockAuthenticationProvider();
        this.eventPublisher = new MockEventPublisher();

        this.employeeDataSource = new MockEmployeeDataSource(stateStore);
        this.projectDataSource = new MockProjectDataSource(stateStore);
        this.taskDataSource = new MockTaskDataSource(stateStore);
        this.productDataSource = new MockProductDataSource(stateStore);
        this.releaseDataSource = new MockReleaseDataSource(stateStore);

        this.employeeRepository = new MockEmployeeRepository(stateStore, behavior);
        this.departmentRepository = new MockDepartmentRepository(stateStore, behavior);
        this.teamRepository = new MockTeamRepository(stateStore, behavior);
        this.projectRepository = new MockProjectRepository(stateStore, behavior);
        this.taskRepository = new MockTaskRepository(stateStore, behavior);
        this.issueRepository = new MockIssueRepository(stateStore, behavior);
        this.productRepository = new MockProductRepository(stateStore, behavior);
        this.productVersionRepository = new MockProductVersionRepository(stateStore, behavior);
        this.releaseRepository = new MockReleaseRepository(stateStore, behavior);
        this.customerRepository = new MockCustomerRepository(stateStore, behavior);
        this.salesLeadRepository = new MockSalesLeadRepository(stateStore, behavior);
        this.salesOpportunityRepository = new MockSalesOpportunityRepository(stateStore, behavior);
        this.salesDealRepository = new MockSalesDealRepository(stateStore, behavior);
        this.notificationRepository = new MockNotificationRepository(stateStore, behavior);
        this.activityRepository = new MockActivityRepository(stateStore, behavior);
        this.dashboardRepository = new MockDashboardRepository(stateStore, behavior);

        this.scenarios = new EnumMap<>(MockScenarioType.class);
        register(new NormalScenario());
        register(new EmptyScenario());
        register(new ErrorScenario());
        register(new OfflineScenario());
        register(new LoadingScenario());
        register(new LargeDatasetScenario());

        applyScenario(this.configuration.getScenario());
        log.info("Mock repository active scenario={} datasetVersion={}",
                this.configuration.getScenario(), this.configuration.getDatasetVersion());
    }

    public static MockInfrastructure forDevelopment() {
        MockConfiguration configuration = new MockConfiguration();
        configuration.setDataMode(DataMode.MOCK);
        configuration.setRuntimeEnvironment(System.getProperty("srots.env", "development"));
        String mode = System.getProperty("srots.data.mode", "MOCK");
        configuration.setDataMode(DataMode.valueOf(mode.toUpperCase()));
        return new MockInfrastructure(configuration);
    }

    private void register(MockScenario scenario) {
        scenarios.put(scenario.type(), scenario);
    }

    public void applyScenario(MockScenarioType type) {
        MockScenario scenario = scenarios.get(type == null ? MockScenarioType.NORMAL : type);
        if (scenario == null) {
            scenario = scenarios.get(MockScenarioType.NORMAL);
        }
        scenario.apply(configuration, seeder);
        eventPublisher.publish("MockScenarioApplied", "SYSTEM", type.name(), "Scenario applied: " + type);
    }

    public void reset() {
        seeder.reset();
        eventPublisher.publish("MockDataReset", "SYSTEM", "MOCK", "Mock dataset reset");
    }

    public void refresh() {
        applyScenario(configuration.getScenario());
    }

    public MockDiagnosticsSnapshot diagnostics() {
        return new MockDiagnosticsSnapshot(
                configuration.getDataMode(),
                configuration.getScenario(),
                authenticationProvider.currentUser().getRole(),
                authenticationProvider.currentUser().getDisplayName(),
                configuration.getLatency(),
                configuration.getFailurePolicy(),
                configuration.getDatasetVersion(),
                stateStore.recordCounts(),
                true);
    }

    public MockConfiguration configuration() { return configuration; }
    public MockStateStore stateStore() { return stateStore; }
    public MockAuthenticationProvider authenticationProvider() { return authenticationProvider; }
    public MockEventPublisher eventPublisher() { return eventPublisher; }

    public EmployeeRepository employeeRepository() { return employeeRepository; }
    public DepartmentRepository departmentRepository() { return departmentRepository; }
    public TeamRepository teamRepository() { return teamRepository; }
    public ProjectRepository projectRepository() { return projectRepository; }
    public TaskRepository taskRepository() { return taskRepository; }
    public IssueRepository issueRepository() { return issueRepository; }
    public ProductRepository productRepository() { return productRepository; }
    public ProductVersionRepository productVersionRepository() { return productVersionRepository; }
    public ReleaseRepository releaseRepository() { return releaseRepository; }
    public CustomerRepository customerRepository() { return customerRepository; }
    public SalesLeadRepository salesLeadRepository() { return salesLeadRepository; }
    public SalesOpportunityRepository salesOpportunityRepository() { return salesOpportunityRepository; }
    public SalesDealRepository salesDealRepository() { return salesDealRepository; }
    public NotificationRepository notificationRepository() { return notificationRepository; }
    public ActivityRepository activityRepository() { return activityRepository; }
    public DashboardRepository dashboardRepository() { return dashboardRepository; }

    public MockEmployeeDataSource employeeDataSource() { return employeeDataSource; }
    public MockProjectDataSource projectDataSource() { return projectDataSource; }
    public MockTaskDataSource taskDataSource() { return taskDataSource; }
    public MockProductDataSource productDataSource() { return productDataSource; }
    public MockReleaseDataSource releaseDataSource() { return releaseDataSource; }
}
