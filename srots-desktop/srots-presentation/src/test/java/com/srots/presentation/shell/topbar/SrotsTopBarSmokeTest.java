package com.srots.presentation.shell.topbar;

import com.srots.presentation.app.MainViewController;
import com.srots.presentation.components.navigation.topbar.SrotsConnectionState;
import com.srots.presentation.components.navigation.topbar.SrotsTopBar;
import com.srots.presentation.components.support.JavaFxTestSupport;
import com.srots.presentation.components.utility.icons.SrotsIcon;
import com.srots.presentation.navigation.NavigationModule;
import com.srots.presentation.navigation.model.NavigationRouteId;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * TopBar smoke coverage (toolkit-based, headless-safe).
 */
class SrotsTopBarSmokeTest {

    @BeforeAll
    static void bootFx() throws Exception {
        JavaFxTestSupport.ensureToolkit();
    }

    @Test
    void topBar_visibleWithSearchNotificationsAndProfile() throws Exception {
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

                SrotsTopBar topBar = controller.getTopBar();
                assertNotNull(topBar);
                assertTrue(topBar.getStyleClass().contains("srots-topbar"));
                assertEquals(60, topBar.getPrefHeight(), 0.1);
                assertEquals("SROTS", topBar.getBrandLabel().getText());
                assertTrue(topBar.getBrandLabel().getStyleClass().contains("srots-topbar-brand"));
                assertNotNull(topBar.getBrandDivider());
                assertTrue(topBar.getBrandDivider().getStyleClass().contains("srots-topbar-divider"));
                assertNotNull(topBar.getSearchField());
                assertTrue(topBar.getSearchField().getStyleClass().contains("srots-topbar-search"));
                assertEquals("Ctrl K", topBar.getSearchField().getShortcutChip().getText());
                assertTrue(topBar.getSearchField().getShortcutChip().isVisible());
                assertNotNull(topBar.getUserProfile());
                assertNotNull(topBar.getConnectionIndicator());
                assertEquals("Connected", topBar.getConnectionIndicator().getText());
                assertNotNull(topBar.lookup(".srots-topbar-notifications"));
                assertNotNull(topBar.lookup(".srots-window-controls"));
                assertEquals("Minimize", topBar.getWindowControls().getMinimizeButton().getAccessibleText());
                assertEquals("Close", topBar.getWindowControls().getCloseButton().getAccessibleText());
                assertEquals("X", topBar.getWindowControls().getCloseButton().getText());
                assertTrue(topBar.getWindowControls().getCloseButton().getStyleClass()
                        .contains("srots-window-control-close"));

                Label env = topBar.getEnvLabel();
                assertTrue(env.isVisible());
                assertEquals("DEVELOPMENT", env.getText());
                assertTrue(env.getStyleClass().contains("srots-env-dev"));

                HBox right = topBar.getRightCluster();
                List<Node> rightChildren = right.getChildren();
                assertTrue(rightChildren.indexOf(topBar.getConnectionIndicator())
                        < rightChildren.indexOf(topBar.getNotificationSlot()));
                assertTrue(rightChildren.indexOf(topBar.getNotificationSlot())
                        < rightChildren.indexOf(topBar.getUserProfile()));
                assertTrue(rightChildren.indexOf(topBar.getUserProfile())
                        < rightChildren.indexOf(env));
                assertTrue(rightChildren.indexOf(env)
                        < rightChildren.indexOf(topBar.getWindowControls()));

                module.navigationService().navigate(NavigationRouteId.OVERVIEW);
                assertEquals("Overview", controller.getTopBarViewModel().getPageTitle());

                module.navigationService().navigate(NavigationRouteId.COMPANY_EMPLOYEES);
                assertEquals("Employees", controller.getTopBarViewModel().getPageTitle());

                module.navigationService().navigate(NavigationRouteId.COMPTY_RELEASES);
                assertEquals("COMPTY Releases", controller.getTopBarViewModel().getPageTitle());
                assertFalse(controller.getTopBarViewModel().getBreadcrumbs().isEmpty());

                controller.getTopBarApplicationState().setNotificationCount(3);
                assertEquals(3, controller.getTopBarViewModel().getNotificationCount());
                assertEquals(SrotsIcon.BELL.getGlyph(), topBar.getNotificationsButton().getText());
                assertTrue(topBar.getNotificationsButton().getStyleClass()
                        .contains("srots-topbar-notifications-unread"));
                Label badge = (Label) topBar.lookup(".srots-topbar-notification-badge");
                assertNotNull(badge);
                assertEquals("3", badge.getText());
                assertTrue(badge.isVisible());
                javafx.scene.layout.Region slot =
                        (javafx.scene.layout.Region) topBar.lookup(".srots-topbar-notification-slot");
                assertNotNull(slot);
                assertEquals(36, slot.getMaxHeight(), 0.1);

                controller.getTopBarApplicationState().setNotificationCount(0);
                assertFalse(topBar.getNotificationsButton().getStyleClass()
                        .contains("srots-topbar-notifications-unread"));
                assertFalse(badge.isVisible());

                controller.getTopBarApplicationState().setCurrentUser(new TopBarUserInfo("Ada", "Administrator"));
                assertEquals("Administrator", controller.getTopBarViewModel().getCurrentUser().roleLabel());

                controller.getTopBarApplicationState().setConnectionState(SrotsConnectionState.OFFLINE);
                assertEquals(SrotsConnectionState.OFFLINE, topBar.getConnectionIndicator().getState());
                assertEquals("Disconnected", topBar.getConnectionIndicator().getText());
                assertTrue(topBar.getStyleClass().contains("srots-topbar"));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    @Test
    void commandCatalog_includesNavigationTargets() {
        NavigationModule module = NavigationModule.createDefault();
        var commands = NavigationCommandCatalog.fromNavigation(
                module.registry(),
                module.navigationService(),
                module.visibilityService(),
                module.accessContext());
        assertTrue(commands.stream().anyMatch(c -> c.label().contains("Overview")));
        assertTrue(commands.stream().anyMatch(c -> "Open Settings".equals(c.label())));
    }
}
