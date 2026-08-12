package com.srots.presentation.window;

import com.srots.presentation.app.MainViewController;
import com.srots.presentation.components.support.JavaFxTestSupport;
import com.srots.presentation.navigation.NavigationModule;
import com.srots.presentation.navigation.model.NavigationRouteId;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Main-window smoke coverage (headless-safe: Stage configured, not required to show).
 */
class SrotsMainWindowSmokeTest {

    @BeforeAll
    static void bootFx() throws Exception {
        JavaFxTestSupport.ensureToolkit();
    }

    @Test
    void mainWindow_configuresShellChromeAndTitles() throws Exception {
        JavaFxTestSupport.runOnFxThread(() -> {
            try {
                URL fxml = MainViewController.class.getResource("/fxml/app/MainView.fxml");
                assertNotNull(fxml);
                FXMLLoader loader = new FXMLLoader(fxml);
                Parent root = loader.load();
                MainViewController controller = loader.getController();
                assertNotNull(controller);

                NavigationModule navigation = NavigationModule.createDefault(controller.getContentArea());
                controller.attachNavigation(navigation);

                Stage stage = new Stage();
                AtomicBoolean sidebarCollapsed = new AtomicBoolean(false);
                SrotsWindowManager manager = new SrotsWindowManager();
                SrotsWindowTitleService titles = new SrotsWindowTitleService("SROTS", navigation.registry());

                SrotsMainWindow window = manager.createMainWindow(
                        stage,
                        root,
                        navigation,
                        titles,
                        sidebarCollapsed::get,
                        value -> {
                            sidebarCollapsed.set(value);
                            controller.setSidebarCollapsed(value);
                        });

                assertTrue(window.isConfigured());
                assertNotNull(window.getScene());
                assertTrue(root.getStyleClass().contains("srots-main-window"));
                assertTrue(root.getStyleClass().contains("srots-app-shell"));
                assertNotNull(root.lookup(".srots-topbar"));
                assertNotNull(root.lookup(".srots-sidebar"));
                assertNotNull(root.lookup(".srots-status-bar"));
                assertNotNull(root.lookup(".srots-content-host"));

                assertEquals(1024, stage.getMinWidth(), 0.001);
                assertEquals(700, stage.getMinHeight(), 0.001);
                assertTrue(stage.isResizable());
                assertFalse(stage.isMaximized());
                assertFalse(stage.isFullScreen());
                assertNotNull(root.lookup(".srots-window-controls"));
                assertNotNull(controller.getTopBar().getWindowControls().getMinimizeButton());
                assertNotNull(controller.getTopBar().getWindowControls().getMaximizeButton());
                assertNotNull(controller.getTopBar().getWindowControls().getCloseButton());

                assertNotNull(controller.getTopBar());
                assertEquals("Overview", controller.getTopBarViewModel().getPageTitle());

                navigation.navigationService().navigate(NavigationRouteId.OVERVIEW);
                assertEquals("SROTS — Overview", stage.getTitle());
                assertEquals("Overview", controller.getTopBarViewModel().getPageTitle());

                Scene scene = window.getScene();
                navigation.navigationService().navigate(NavigationRouteId.COMPANY_EMPLOYEES);
                assertEquals("SROTS — Employees", stage.getTitle());
                assertEquals("Employees", controller.getTopBarViewModel().getPageTitle());
                assertSame(stage, window.getStage());
                assertSame(scene, window.getScene());

                navigation.navigationService().navigate(NavigationRouteId.PROJECTS);
                assertEquals("SROTS — Projects", stage.getTitle());
                assertEquals("Projects", controller.getTopBarViewModel().getPageTitle());
                assertSame(scene, window.getScene());

                navigation.navigationService().navigate(NavigationRouteId.COMPTY);
                assertEquals("SROTS — COMPTY", stage.getTitle());
                assertEquals("COMPTY", controller.getTopBarViewModel().getPageTitle());

                navigation.navigationService().navigate(NavigationRouteId.COMPTY_RELEASES);
                assertEquals("SROTS — COMPTY Releases", stage.getTitle());
                assertEquals("COMPTY Releases", controller.getTopBarViewModel().getPageTitle());
                assertSame(stage, window.getStage());
                assertSame(scene, window.getScene());
                assertSame(root, window.getRoot());

                controller.setSidebarCollapsed(true);
                assertTrue(controller.isSidebarCollapsed());
                assertTrue(controller.getSidebar().getStyleClass().contains("srots-sidebar-collapsed"));
                controller.setSidebarCollapsed(false);
                assertFalse(controller.isSidebarCollapsed());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }
}
