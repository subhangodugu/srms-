package com.srots.presentation.notification;

import com.srots.presentation.app.MainViewController;
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

class SrotsNotificationPanelSmokeTest {

    @BeforeAll
    static void bootFx() throws Exception {
        JavaFxTestSupport.ensureToolkit();
    }

    @Test
    void topBarNotifications_wirePanelAndBadge() throws Exception {
        JavaFxTestSupport.runOnFxThread(() -> {
            try {
                URL fxml = MainViewController.class.getResource("/fxml/app/MainView.fxml");
                assertNotNull(fxml);
                FXMLLoader loader = new FXMLLoader(fxml);
                Parent root = loader.load();
                MainViewController controller = loader.getController();
                NavigationModule module = NavigationModule.createDefault(controller.getContentArea());
                controller.attachNavigation(module);

                assertNotNull(controller.getNotificationService());
                assertNotNull(controller.getNotificationPanelViewModel());
                assertNotNull(controller.getNotificationController());
                assertEquals(3, controller.getNotificationPanelViewModel().getUnreadCount());
                assertEquals(3, controller.getTopBarApplicationState().getNotificationCount());
                assertNotNull(controller.getTopBar().getNotificationsButton());
                assertTrue(controller.getTopBar().getNotificationsButton()
                        .getAccessibleText().toLowerCase().contains("notification"));

                controller.getNotificationController().toggle();
                assertFalse(controller.getNotificationPanelViewModel().isPanelOpen()
                        && controller.getNotificationController().getPanel().isShowing());

                controller.getNotificationService().markAllAsRead();
                assertEquals(0, controller.getNotificationPanelViewModel().getUnreadCount());
                assertEquals(0, controller.getTopBarApplicationState().getNotificationCount());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }
}
