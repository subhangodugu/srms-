package com.srots.presentation.app;

import com.srots.application.search.GlobalSearchService;
import com.srots.application.search.SearchAccessContext;
import com.srots.application.search.mock.MockSearchProviders;
import com.srots.presentation.components.navigation.sidebar.SrotsSidebar;
import com.srots.presentation.components.navigation.topbar.SrotsStatusBar;
import com.srots.presentation.components.navigation.topbar.SrotsTopBar;
import com.srots.presentation.components.overlays.command.SrotsCommandPalette;
import com.srots.presentation.navigation.NavigationModule;
import com.srots.presentation.navigation.service.NavigationService;
import com.srots.presentation.notification.DefaultNotificationService;
import com.srots.presentation.notification.NotificationService;
import com.srots.presentation.notification.SrotsNotificationController;
import com.srots.presentation.notification.SrotsNotificationPanelViewModel;
import com.srots.presentation.profile.AboutInfo;
import com.srots.presentation.profile.AuthenticationService;
import com.srots.presentation.profile.DefaultAuthenticationService;
import com.srots.presentation.profile.DefaultSessionService;
import com.srots.presentation.profile.DialogService;
import com.srots.presentation.profile.SessionService;
import com.srots.presentation.profile.SrotsPopupManager;
import com.srots.presentation.profile.SrotsUserProfileController;
import com.srots.presentation.profile.SrotsUserProfileViewModel;
import com.srots.presentation.search.SrotsGlobalSearch;
import com.srots.presentation.search.SrotsGlobalSearchController;
import com.srots.presentation.search.SrotsGlobalSearchViewModel;
import com.srots.presentation.shell.statusbar.ApplicationActivityService;
import com.srots.presentation.shell.statusbar.SrotsStatusBarViewModel;
import com.srots.presentation.shell.statusbar.StatusBarEnvironmentInfo;
import com.srots.presentation.shell.topbar.NavigationCommandCatalog;
import com.srots.presentation.shell.topbar.SrotsTopBarViewModel;
import com.srots.presentation.shell.topbar.TopBarApplicationState;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.StackPane;
import javafx.stage.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * App shell controller. Navigation is delegated to {@link NavigationService} — never setScene.
 */
public class MainViewController {

    private static final Logger log = LoggerFactory.getLogger(MainViewController.class);

    @FXML
    private SrotsTopBar topBar;

    @FXML
    private SrotsSidebar sidebar;

    @FXML
    private SrotsStatusBar statusBar;

    @FXML
    private StackPane contentArea;

    private MainViewModel viewModel;
    private NavigationModule navigationModule;
    private SrotsTopBarViewModel topBarViewModel;
    private TopBarApplicationState topBarApplicationState;
    private SrotsStatusBarViewModel statusBarViewModel;
    private ApplicationActivityService activityService;
    private SrotsCommandPalette commandPalette;
    private SessionService sessionService;
    private AuthenticationService authenticationService;
    private SrotsUserProfileViewModel userProfileViewModel;
    private SrotsUserProfileController userProfileController;
    private NotificationService notificationService;
    private SrotsNotificationPanelViewModel notificationPanelViewModel;
    private SrotsNotificationController notificationController;
    private GlobalSearchService globalSearchService;
    private SrotsGlobalSearchViewModel globalSearchViewModel;
    private SrotsGlobalSearchController globalSearchController;
    private SrotsGlobalSearch globalSearch;
    private final SrotsPopupManager popupManager = new SrotsPopupManager();
    private StatusBarEnvironmentInfo environmentInfo =
            StatusBarEnvironmentInfo.of("development", "MOCK", "0.1.0", false);

    @FXML
    private void initialize() {
        // Navigation / chrome bound after NavigationModule injection.
    }

    public void setViewModel(MainViewModel viewModel) {
        this.viewModel = viewModel;
    }

    /**
     * Wires registry-driven sidebar, topbar, status bar, content host, and default route.
     */
    public void attachNavigation(NavigationModule module) {
        this.navigationModule = module;
        if (module == null) {
            return;
        }
        if (sidebar != null) {
            module.chromeBinder().bindSidebar(sidebar);
        }

        topBarApplicationState = TopBarApplicationState.developmentDefaults();
        sessionService = DefaultSessionService.developmentDefaults(topBarApplicationState);
        notificationService = DefaultNotificationService.developmentDefaults(topBarApplicationState);
        authenticationService = new DefaultAuthenticationService(
                sessionService,
                module.navigationService(),
                message -> {
                    Window owner = topBar == null || topBar.getScene() == null
                            ? null
                            : topBar.getScene().getWindow();
                    new DialogService.Default().showError(owner, "Sign out", message);
                });

        topBarViewModel = new SrotsTopBarViewModel();
        topBarViewModel.bind(module.registry(), module.navigationService(), topBarApplicationState);

        if (topBar != null) {
            topBar.setViewModel(topBarViewModel);
            topBar.setOnCommandPalette(this::openGlobalSearch);

            notificationPanelViewModel = new SrotsNotificationPanelViewModel();
            notificationPanelViewModel.bind(notificationService, module.navigationService());
            notificationController = new SrotsNotificationController(
                    topBar.getNotificationSlot(),
                    topBar.getNotificationsButton(),
                    notificationPanelViewModel,
                    popupManager);
            notificationController.attach();
            topBar.setOnNotifications(notificationController::toggle);

            userProfileViewModel = new SrotsUserProfileViewModel();
            userProfileViewModel.bind(
                    sessionService,
                    authenticationService,
                    module.navigationService(),
                    module.accessContext(),
                    topBarApplicationState,
                    () -> topBar.getScene() == null ? null : topBar.getScene().getWindow(),
                    this::currentAboutInfo);
            userProfileController = new SrotsUserProfileController(
                    topBar.getUserProfile(),
                    userProfileViewModel,
                    popupManager);
            userProfileController.attach();
            topBar.setOnProfile(userProfileController::toggle);

            SearchAccessContext searchAccess = SearchAccessContext.of(true, Set.of("*"));
            // Development: MockSearchProviders. Production wiring must inject real providers.
            globalSearchService = MockSearchProviders.developmentService(searchAccess);
            globalSearchViewModel = new SrotsGlobalSearchViewModel();
            globalSearchViewModel.bind(
                    globalSearchService,
                    module.navigationService(),
                    () -> NavigationCommandCatalog.fromNavigation(
                            module.registry(),
                            module.navigationService(),
                            module.visibilityService(),
                            module.accessContext()));
            globalSearchController = new SrotsGlobalSearchController(
                    globalSearchViewModel,
                    popupManager,
                    () -> topBar.getSearchField().getField());
            globalSearchController.attach();
            globalSearch = new SrotsGlobalSearch(globalSearchViewModel, globalSearchController);

            topBar.getSearchField().setOnSearch(query -> {
                if (query != null && !query.isBlank()) {
                    openGlobalSearch();
                    globalSearchViewModel.queryTextProperty().set(query);
                }
            });
        }

        activityService = new ApplicationActivityService();
        statusBarViewModel = new SrotsStatusBarViewModel();
        statusBarViewModel.bind(topBarApplicationState, activityService, environmentInfo);
        if (statusBar != null) {
            statusBar.setViewModel(statusBarViewModel);
        }

        registerCommandShortcut(module);
        NavigationService navigation = module.navigationService();
        if (navigation.currentRoute() == null) {
            navigation.home();
        }
    }

    /**
     * Applies environment / build metadata to TopBar and StatusBar chrome.
     */
    public void applyEnvironmentChrome(
            boolean production,
            String environment,
            String dataMode,
            String displayVersion) {
        environmentInfo = StatusBarEnvironmentInfo.of(environment, dataMode, displayVersion, production);
        if (statusBarViewModel != null) {
            statusBarViewModel.setEnvironmentInfo(environmentInfo);
        }
        if (topBar == null) {
            return;
        }
        if (production) {
            topBar.setEnvironmentBadge("", false);
        } else {
            String env = environment == null || environment.isBlank() ? "DEVELOPMENT" : environment.trim().toUpperCase();
            if ("DESKTOP LOCAL".equalsIgnoreCase(env) || "LOCAL".equalsIgnoreCase(env)) {
                env = "DEVELOPMENT";
            }
            topBar.setEnvironmentBadge(env, true, env);
        }
    }

    /** Backward-compatible overload used by older call sites. */
    public void applyEnvironmentChrome(boolean production, String environment) {
        applyEnvironmentChrome(production, environment, production ? "" : "MOCK", "0.1.0");
    }

    public void setSidebarCollapsed(boolean collapsed) {
        if (sidebar != null) {
            sidebar.setCollapsed(collapsed);
        }
    }

    public boolean isSidebarCollapsed() {
        return sidebar != null && sidebar.isCollapsed();
    }

    public void toggleSidebarCollapsed() {
        if (sidebar != null) {
            sidebar.toggleCollapsed();
        }
    }

    public NavigationModule getNavigationModule() {
        return navigationModule;
    }

    public StackPane getContentArea() {
        return contentArea;
    }

    public SrotsSidebar getSidebar() {
        return sidebar;
    }

    public SrotsTopBar getTopBar() {
        return topBar;
    }

    public SrotsStatusBar getStatusBar() {
        return statusBar;
    }

    public SrotsTopBarViewModel getTopBarViewModel() {
        return topBarViewModel;
    }

    public TopBarApplicationState getTopBarApplicationState() {
        return topBarApplicationState;
    }

    public SrotsStatusBarViewModel getStatusBarViewModel() {
        return statusBarViewModel;
    }

    public ApplicationActivityService getActivityService() {
        return activityService;
    }

    public SessionService getSessionService() {
        return sessionService;
    }

    public AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    public SrotsUserProfileViewModel getUserProfileViewModel() {
        return userProfileViewModel;
    }

    public SrotsUserProfileController getUserProfileController() {
        return userProfileController;
    }

    public NotificationService getNotificationService() {
        return notificationService;
    }

    public SrotsNotificationPanelViewModel getNotificationPanelViewModel() {
        return notificationPanelViewModel;
    }

    public SrotsNotificationController getNotificationController() {
        return notificationController;
    }

    public SrotsGlobalSearch getGlobalSearch() {
        return globalSearch;
    }

    public SrotsGlobalSearchViewModel getGlobalSearchViewModel() {
        return globalSearchViewModel;
    }

    public GlobalSearchService getGlobalSearchService() {
        return globalSearchService;
    }

    public SrotsPopupManager getPopupManager() {
        return popupManager;
    }

    /** @deprecated use {@link #getSidebar()} */
    @Deprecated
    public SrotsSidebar getSidebarPane() {
        return sidebar;
    }

    private AboutInfo currentAboutInfo() {
        String version = environmentInfo.versionLabel();
        if (version.startsWith("v")) {
            version = version.substring(1);
        }
        return AboutInfo.of("SROTS", version.isBlank() ? "0.1.0" : version, environmentInfo.environmentLabel());
    }

    private void registerCommandShortcut(NavigationModule module) {
        module.shortcutRegistry().register(
                new KeyCodeCombination(KeyCode.K, KeyCombination.SHORTCUT_DOWN),
                this::openGlobalSearch);
    }

    private void openGlobalSearch() {
        if (globalSearch != null) {
            globalSearch.open();
            return;
        }
        if (topBar != null) {
            topBar.focusSearch();
        }
    }

    /** Retained for Prompt 20 command-palette separation; prefer {@link #openGlobalSearch()}. */
    private void openCommandPalette() {
        if (navigationModule == null || topBar == null) {
            return;
        }
        try {
            popupManager.closeAll();
            Window owner = topBar.getScene() == null ? null : topBar.getScene().getWindow();
            if (commandPalette == null) {
                commandPalette = new SrotsCommandPalette(owner, NavigationCommandCatalog.fromNavigation(
                        navigationModule.registry(),
                        navigationModule.navigationService(),
                        navigationModule.visibilityService(),
                        navigationModule.accessContext()));
            } else {
                commandPalette.setItems(NavigationCommandCatalog.fromNavigation(
                        navigationModule.registry(),
                        navigationModule.navigationService(),
                        navigationModule.visibilityService(),
                        navigationModule.accessContext()));
            }
            commandPalette.show();
        } catch (Exception ex) {
            log.warn("Unable to open command palette; opening global search instead", ex);
            openGlobalSearch();
        }
    }

    public MainViewModel getViewModel() {
        return viewModel;
    }
}
