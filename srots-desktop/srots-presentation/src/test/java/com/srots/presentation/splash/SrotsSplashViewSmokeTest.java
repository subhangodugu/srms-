package com.srots.presentation.splash;

import com.srots.presentation.components.support.JavaFxTestSupport;
import javafx.scene.control.Label;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Splash UI smoke coverage without showing a Stage (headless-safe).
 */
class SrotsSplashViewSmokeTest {

    @BeforeAll
    static void bootFx() throws Exception {
        JavaFxTestSupport.ensureToolkit();
    }

    @Test
    void splashView_showsBrandingAndStatus() throws Exception {
        JavaFxTestSupport.runOnFxThread(() -> {
            SrotsSplashViewModel viewModel = new SrotsSplashViewModel();
            viewModel.setVersion("0.1.0");
            viewModel.apply(StartupProgress.of(StartupPhase.CONFIGURATION));

            SrotsSplashView view = new SrotsSplashView(viewModel);
            assertTrue(view.getStyleClass().contains("srots-splash"));
            assertEquals("Loading configuration...", viewModel.getStatusMessage());

            boolean hasTitle = view.lookupAll(".srots-splash-title").stream()
                    .anyMatch(n -> n instanceof Label label && "SROTS".equals(label.getText()));
            assertTrue(hasTitle);
        });
    }

    @Test
    void splashView_showsFailureActions() throws Exception {
        JavaFxTestSupport.runOnFxThread(() -> {
            SrotsSplashViewModel viewModel = new SrotsSplashViewModel();
            SrotsSplashView view = new SrotsSplashView(viewModel);
            viewModel.apply(StartupProgress.failed("Unable to initialize the application."));
            assertTrue(viewModel.isFailed());
            assertTrue(view.lookup(".srots-splash-retry").isVisible());
            assertTrue(view.lookup(".srots-splash-exit").isVisible());
        });
    }

    @Test
    void splashWindow_constructsSceneWithoutShowing() throws Exception {
        JavaFxTestSupport.runOnFxThread(() -> {
            SrotsSplashViewModel viewModel = new SrotsSplashViewModel();
            viewModel.setVersion("0.1.0");
            SrotsSplashWindow window = new SrotsSplashWindow(viewModel);
            assertEquals(SplashLifecycleState.CREATED, viewModel.getLifecycleState());
            assertEquals(520, window.getStage().getScene().getWidth(), 0.1);
        });
    }
}
