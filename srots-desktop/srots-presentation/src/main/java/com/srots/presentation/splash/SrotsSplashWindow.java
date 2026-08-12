package com.srots.presentation.splash;

import com.srots.presentation.components.layout.ThemeLoader;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Owns the dedicated borderless splash {@link Stage}. Presentation only.
 */
public final class SrotsSplashWindow {

    private static final Logger log = LoggerFactory.getLogger(SrotsSplashWindow.class);
    private static final Duration FADE_OUT = Duration.millis(150);

    private final Stage stage;
    private final SrotsSplashView view;
    private final SrotsSplashViewModel viewModel;
    private boolean closed;

    public SrotsSplashWindow(SrotsSplashViewModel viewModel) {
        this.viewModel = Objects.requireNonNull(viewModel, "viewModel");
        this.view = new SrotsSplashView(viewModel);
        this.stage = new Stage(StageStyle.UNDECORATED);
        this.stage.setTitle("SROTS");
        this.stage.setResizable(false);
        this.stage.setAlwaysOnTop(true);
        this.stage.centerOnScreen();

        Scene scene = new Scene(view, 520, 320);
        ThemeLoader.apply(scene);
        var splashCss = SrotsSplashWindow.class.getResource("/css/srots-splash.css");
        if (splashCss != null) {
            scene.getStylesheets().add(splashCss.toExternalForm());
        }
        stage.setScene(scene);
        viewModel.setLifecycleState(SplashLifecycleState.CREATED);
    }

    public void show() {
        closed = false;
        stage.centerOnScreen();
        stage.show();
        stage.toFront();
        viewModel.setLifecycleState(SplashLifecycleState.VISIBLE);
        log.info("Splash screen displayed");
    }

    public void closeImmediately() {
        if (closed) {
            return;
        }
        closed = true;
        viewModel.setLifecycleState(SplashLifecycleState.CLOSING);
        stage.close();
        viewModel.setLifecycleState(SplashLifecycleState.CLOSED);
        log.info("Splash screen closed");
    }

    public void closeWithFade(Runnable afterClose) {
        if (closed) {
            if (afterClose != null) {
                afterClose.run();
            }
            return;
        }
        closed = true;
        viewModel.setLifecycleState(SplashLifecycleState.CLOSING);
        FadeTransition fade = new FadeTransition(FADE_OUT, view);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setOnFinished(e -> {
            stage.close();
            view.setOpacity(1.0);
            viewModel.setLifecycleState(SplashLifecycleState.CLOSED);
            log.info("Splash screen closed");
            if (afterClose != null) {
                afterClose.run();
            }
        });
        fade.play();
    }

    public void setOnRetry(Runnable onRetry) {
        view.setOnRetry(onRetry);
    }

    public void setOnExit(Runnable onExit) {
        view.setOnExit(onExit);
    }

    public SrotsSplashViewModel getViewModel() {
        return viewModel;
    }

    public SrotsSplashView getView() {
        return view;
    }

    public Stage getStage() {
        return stage;
    }

    public boolean isShowing() {
        return stage.isShowing();
    }

    public void dispose() {
        Platform.runLater(() -> {
            if (stage.isShowing()) {
                stage.close();
            }
            viewModel.setLifecycleState(SplashLifecycleState.CLOSED);
        });
    }
}
