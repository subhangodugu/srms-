package com.srots.app.bootstrap;

import com.srots.application.usecase.dashboard.GetDashboardOverviewUseCase;
import com.srots.application.usecase.product.GetProductsUseCase;
import com.srots.domain.repository.ActivityRepository;
import com.srots.domain.repository.DashboardRepository;
import com.srots.domain.repository.EmployeeRepository;
import com.srots.domain.repository.IssueRepository;
import com.srots.domain.repository.ProductRepository;
import com.srots.domain.repository.ProjectRepository;
import com.srots.domain.repository.ReleaseRepository;
import com.srots.domain.repository.TaskRepository;
import com.srots.domain.repository.TeamRepository;
import com.srots.infrastructure.mock.MockInfrastructure;
import com.srots.infrastructure.mock.configuration.DataMode;
import com.srots.infrastructure.mock.configuration.MockConfiguration;
import com.srots.infrastructure.mock.configuration.ProductionMockGuard;
import com.srots.infrastructure.mock.diagnostics.MockDiagnosticsSnapshot;
import com.srots.presentation.app.MainViewModel;
import com.srots.presentation.navigation.NavigationModule;
import com.srots.presentation.navigation.host.SrotsContentHost;
import com.srots.presentation.navigation.registry.CoreNavigationProvider;
import com.srots.presentation.navigation.resolver.DefaultPlaceholderViewFactory;
import com.srots.presentation.navigation.resolver.FeatureAwareViewFactory;
import com.srots.presentation.navigation.service.DevOpenAccessContext;
import com.srots.presentation.navigation.service.NavigationService;
import javafx.scene.layout.StackPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Composition root for SROTS desktop. Created by {@link ApplicationBootstrap}.
 */
public final class AppContainer {

    private static final Logger log = LoggerFactory.getLogger(AppContainer.class);
    private static final AtomicReference<AppContainer> INSTANCE = new AtomicReference<>();

    private final ApplicationConfig config;
    private final MockInfrastructure mockInfrastructure;
    private final ProductRepository productRepository;
    private final EmployeeRepository employeeRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final ReleaseRepository releaseRepository;
    private final DashboardRepository dashboardRepository;
    private final IssueRepository issueRepository;
    private final TeamRepository teamRepository;
    private final ActivityRepository activityRepository;
    private final GetProductsUseCase getProductsUseCase;
    private final GetDashboardOverviewUseCase getDashboardOverviewUseCase;
    private final MainViewModel mainViewModel;

    private volatile NavigationModule navigationModule;
    private volatile boolean shutDown;

    private AppContainer(ApplicationConfig config) {
        this.config = Objects.requireNonNull(config, "config");

        if (config.dataMode() != DataMode.MOCK) {
            throw new StartupException(
                    "Data mode " + config.dataMode()
                            + " is not implemented yet. Use -Dsrots.data.mode=MOCK for development.");
        }

        MockConfiguration mockConfiguration = new MockConfiguration();
        mockConfiguration.setRuntimeEnvironment(config.environment());
        mockConfiguration.setDataMode(DataMode.MOCK);
        ProductionMockGuard.assertSafe(mockConfiguration);

        this.mockInfrastructure = new MockInfrastructure(mockConfiguration);
        this.productRepository = mockInfrastructure.productRepository();
        this.employeeRepository = mockInfrastructure.employeeRepository();
        this.projectRepository = mockInfrastructure.projectRepository();
        this.taskRepository = mockInfrastructure.taskRepository();
        this.releaseRepository = mockInfrastructure.releaseRepository();
        this.dashboardRepository = mockInfrastructure.dashboardRepository();
        this.issueRepository = mockInfrastructure.issueRepository();
        this.teamRepository = mockInfrastructure.teamRepository();
        this.activityRepository = mockInfrastructure.activityRepository();
        this.getProductsUseCase = new GetProductsUseCase(productRepository);
        this.getDashboardOverviewUseCase = new GetDashboardOverviewUseCase(
                dashboardRepository,
                projectRepository,
                taskRepository,
                issueRepository,
                teamRepository,
                activityRepository);
        this.mainViewModel = new MainViewModel(getProductsUseCase);

        MockDiagnosticsSnapshot snap = mockInfrastructure.diagnostics();
        log.info("Data mode={} scenario={} mockUser={} dataset={} counts={}",
                snap.dataMode(), snap.scenario(), snap.mockUserRole(), snap.datasetVersion(), snap.recordCounts());
    }

    public static AppContainer create(ApplicationConfig config) {
        AppContainer created = new AppContainer(config);
        INSTANCE.set(created);
        return created;
    }

    /**
     * Returns the bootstrapped instance. Prefer injecting {@link AppContainer} from bootstrap.
     */
    public static AppContainer getInstance() {
        AppContainer current = INSTANCE.get();
        if (current == null) {
            throw new IllegalStateException("AppContainer has not been bootstrapped yet.");
        }
        return current;
    }

    public synchronized NavigationModule ensureNavigation(StackPane contentArea) {
        ensureActive();
        if (navigationModule == null) {
            SrotsContentHost host = contentArea == null
                    ? new SrotsContentHost()
                    : new SrotsContentHost(contentArea);
            AtomicReference<NavigationService> navigationRef = new AtomicReference<>();
            FeatureAwareViewFactory viewFactory = new FeatureAwareViewFactory(
                    new DefaultPlaceholderViewFactory(),
                    () -> getDashboardOverviewUseCase,
                    route -> {
                        NavigationService navigation = navigationRef.get();
                        if (navigation != null) {
                            navigation.navigate(route);
                        }
                    });
            navigationModule = NavigationModule.create(
                    List.of(new CoreNavigationProvider()),
                    new DevOpenAccessContext(),
                    viewFactory,
                    host,
                    () -> false,
                    () -> true);
            navigationRef.set(navigationModule.navigationService());
        }
        return navigationModule;
    }

    public synchronized void shutdown() {
        if (shutDown) {
            return;
        }
        shutDown = true;
        navigationModule = null;
        log.info("AppContainer resources released.");
    }

    private void ensureActive() {
        if (shutDown) {
            throw new IllegalStateException("AppContainer has been shut down.");
        }
    }

    public ApplicationConfig getConfig() {
        return config;
    }

    public NavigationModule getNavigationModule() {
        return navigationModule;
    }

    public NavigationService getNavigationService() {
        return navigationModule == null ? null : navigationModule.navigationService();
    }

    public MockInfrastructure getMockInfrastructure() {
        return mockInfrastructure;
    }

    public ProductRepository getProductRepository() {
        return productRepository;
    }

    public EmployeeRepository getEmployeeRepository() {
        return employeeRepository;
    }

    public ProjectRepository getProjectRepository() {
        return projectRepository;
    }

    public TaskRepository getTaskRepository() {
        return taskRepository;
    }

    public ReleaseRepository getReleaseRepository() {
        return releaseRepository;
    }

    public DashboardRepository getDashboardRepository() {
        return dashboardRepository;
    }

    public GetProductsUseCase getGetProductsUseCase() {
        return getProductsUseCase;
    }

    public GetDashboardOverviewUseCase getGetDashboardOverviewUseCase() {
        return getDashboardOverviewUseCase;
    }

    public MainViewModel getMainViewModel() {
        return mainViewModel;
    }
}
