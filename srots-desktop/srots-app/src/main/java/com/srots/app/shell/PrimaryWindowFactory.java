package com.srots.app.shell;

import com.srots.app.bootstrap.AppContainer;
import com.srots.app.bootstrap.ApplicationConfig;
import com.srots.app.bootstrap.SrotsBuildInfo;
import com.srots.app.bootstrap.StartupException;
import com.srots.app.constants.AppConstants;
import com.srots.presentation.app.MainViewController;
import com.srots.presentation.navigation.NavigationModule;
import com.srots.presentation.navigation.model.NavigationRouteId;
import com.srots.presentation.window.SrotsMainWindow;
import com.srots.presentation.window.SrotsWindowConfiguration;
import com.srots.presentation.window.SrotsWindowManager;
import com.srots.presentation.window.SrotsWindowStateStore;
import com.srots.presentation.window.SrotsWindowTitleService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Objects;

/**
 * Loads the shell FXML and opens the primary window via {@link SrotsWindowManager}.
 * Owns Stage/Scene wiring only — no feature business logic.
 */
public final class PrimaryWindowFactory {

    private static final Logger log = LoggerFactory.getLogger(PrimaryWindowFactory.class);

    private final AppContainer container;
    private final ApplicationConfig config;
    private final SrotsWindowManager windowManager;

    public PrimaryWindowFactory(AppContainer container) {
        this(container, null);
    }

    public PrimaryWindowFactory(AppContainer container, ApplicationConfig config) {
        this.container = Objects.requireNonNull(container, "container");
        this.config = config;
        this.windowManager = new SrotsWindowManager(
                new SrotsWindowConfiguration(
                        AppConstants.DEFAULT_WINDOW_WIDTH,
                        AppConstants.DEFAULT_WINDOW_HEIGHT,
                        AppConstants.MIN_WINDOW_WIDTH,
                        AppConstants.MIN_WINDOW_HEIGHT),
                new SrotsWindowStateStore());
    }

    public SrotsMainWindow createAndShow(Stage primaryStage) {
        Objects.requireNonNull(primaryStage, "primaryStage");
        try {
            log.info("Loading application shell FXML...");
            URL fxml = PrimaryWindowFactory.class.getResource(AppConstants.FXML_MAIN_VIEW);
            if (fxml == null) {
                throw new StartupException(
                        "Unable to locate the SROTS main window layout. The installation may be incomplete.");
            }

            FXMLLoader loader = new FXMLLoader(fxml);
            Parent root = loader.load();
            MainViewController controller = loader.getController();
            if (controller == null) {
                throw new StartupException("Unable to initialize the SROTS application shell controller.");
            }

            controller.setViewModel(container.getMainViewModel());
            NavigationModule navigation = container.ensureNavigation(controller.getContentArea());
            controller.attachNavigation(navigation);
            if (config != null) {
                controller.applyEnvironmentChrome(
                        config.isProduction(),
                        config.environment(),
                        config.dataMode().name(),
                        SrotsBuildInfo.displayVersion());
            } else {
                controller.applyEnvironmentChrome(
                        false,
                        "development",
                        "MOCK",
                        SrotsBuildInfo.displayVersion());
            }

            SrotsWindowTitleService titleService = new SrotsWindowTitleService(
                    AppConstants.APP_NAME, navigation.registry());

            SrotsMainWindow mainWindow = windowManager.createMainWindow(
                    primaryStage,
                    root,
                    navigation,
                    titleService,
                    controller::isSidebarCollapsed,
                    controller::setSidebarCollapsed);

            ensureDefaultRoute(navigation);
            // Title sync after initial route
            mainWindow.updateTitle(navigation.navigationService().currentRoute());

            windowManager.showMainWindow();
            log.info("Primary window shown. Default route active: {}",
                    navigation.navigationService().currentRoute());
            return mainWindow;
        } catch (StartupException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new StartupException(
                    "Unable to open the SROTS desktop window. Please try again or check the logs.",
                    ex);
        }
    }

    public SrotsWindowManager getWindowManager() {
        return windowManager;
    }

    private static void ensureDefaultRoute(NavigationModule navigation) {
        if (navigation.navigationService().currentRoute() == null) {
            navigation.navigationService().navigate(NavigationRouteId.OVERVIEW);
        }
    }
}
