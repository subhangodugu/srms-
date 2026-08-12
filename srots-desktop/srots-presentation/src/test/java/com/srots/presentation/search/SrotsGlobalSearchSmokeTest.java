package com.srots.presentation.search;

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

class SrotsGlobalSearchSmokeTest {

    @BeforeAll
    static void bootFx() throws Exception {
        JavaFxTestSupport.ensureToolkit();
    }

    @Test
    void topBarSearch_wiresGlobalSearch() throws Exception {
        JavaFxTestSupport.runOnFxThread(() -> {
            try {
                URL fxml = MainViewController.class.getResource("/fxml/app/MainView.fxml");
                assertNotNull(fxml);
                FXMLLoader loader = new FXMLLoader(fxml);
                Parent root = loader.load();
                MainViewController controller = loader.getController();
                NavigationModule module = NavigationModule.createDefault(controller.getContentArea());
                controller.attachNavigation(module);

                assertNotNull(controller.getGlobalSearch());
                assertNotNull(controller.getGlobalSearchService());
                assertNotNull(controller.getGlobalSearchViewModel());

                controller.getGlobalSearch().open();
                assertTrue(controller.getGlobalSearchViewModel().isOpen());
                assertEquals(SrotsGlobalSearchState.IDLE, controller.getGlobalSearchViewModel().stateProperty().get());

                controller.getGlobalSearch().close();
                assertFalse(controller.getGlobalSearchViewModel().isOpen());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }
}
