package com.srots.presentation.shell.statusbar;

import com.srots.presentation.app.MainViewController;
import com.srots.presentation.components.navigation.topbar.SrotsConnectionState;
import com.srots.presentation.components.navigation.topbar.SrotsStatusBar;
import com.srots.presentation.components.support.JavaFxTestSupport;
import com.srots.presentation.navigation.NavigationModule;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * StatusBar smoke coverage (toolkit-based, headless-safe).
 */
class SrotsStatusBarSmokeTest {

    @BeforeAll
    static void bootFx() throws Exception {
        JavaFxTestSupport.ensureToolkit();
    }

    @Test
    void statusBar_visibleWithConnectedReadyAndVersion() throws Exception {
        JavaFxTestSupport.runOnFxThread(() -> {
            try {
                URL fxml = MainViewController.class.getResource("/fxml/app/MainView.fxml");
                assertNotNull(fxml);
                FXMLLoader loader = new FXMLLoader(fxml);
                Parent root = loader.load();
                MainViewController controller = loader.getController();
                NavigationModule module = NavigationModule.createDefault(controller.getContentArea());
                controller.attachNavigation(module);
                controller.applyEnvironmentChrome(false, "development", "MOCK", "0.1.0");

                SrotsStatusBar statusBar = controller.getStatusBar();
                assertNotNull(statusBar);
                assertTrue(statusBar.getStyleClass().contains("srots-status-bar"));
                assertNotNull(root.lookup(".srots-status-bar"));
                assertEquals("Connected", statusBar.getConnectionIndicator().getText());
                assertEquals("Ready", controller.getStatusBarViewModel().getActivityText());
                assertTrue(controller.getStatusBarViewModel().getEnvironmentText().contains("v0.1.0"));

                controller.getTopBarApplicationState().setConnectionState(SrotsConnectionState.OFFLINE);
                assertEquals(SrotsStatusBarState.OFFLINE, controller.getStatusBarViewModel().getBarState());
                assertEquals("Offline", statusBar.getConnectionIndicator().getText());

                controller.getActivityService().publish(ApplicationActivity.builder(ApplicationActivityType.SYNCING)
                        .message("Synchronizing...")
                        .build());
                // Offline still wins over syncing in aggregate state priority
                assertEquals(SrotsStatusBarState.OFFLINE, controller.getStatusBarViewModel().getBarState());

                controller.getTopBarApplicationState().setConnectionState(SrotsConnectionState.ONLINE);
                assertEquals(SrotsStatusBarState.SYNCING, controller.getStatusBarViewModel().getBarState());
                assertEquals("Synchronizing...", controller.getStatusBarViewModel().getActivityText());

                controller.applyEnvironmentChrome(true, "production", "REMOTE", "1.0.0");
                assertFalse(controller.getStatusBarViewModel().getEnvironmentText().toLowerCase().contains("mock"));
                assertTrue(controller.getStatusBarViewModel().getEnvironmentText().contains("Production"));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }
}
