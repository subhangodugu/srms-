package com.srots.presentation.splash;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Presentation-only splash content. Does not run bootstrap.
 */
public final class SrotsSplashView extends StackPane {

    private static final Logger log = LoggerFactory.getLogger(SrotsSplashView.class);
    private static final String LOGO_RESOURCE = "/images/srots-logo.png";

    private final SrotsSplashViewModel viewModel;
    private final ProgressBar progressBar = new ProgressBar();
    private final Label statusLabel = new Label();
    private final Label errorLabel = new Label();
    private final HBox failureActions = new HBox(12);
    private final Button retryButton = new Button("Retry");
    private final Button exitButton = new Button("Exit");

    private Runnable onRetry = () -> {
    };
    private Runnable onExit = () -> {
    };

    public SrotsSplashView(SrotsSplashViewModel viewModel) {
        this.viewModel = Objects.requireNonNull(viewModel, "viewModel");
        getStyleClass().add("srots-splash");
        setPrefSize(520, 320);
        setMinSize(520, 320);
        setMaxSize(520, 320);

        VBox container = new VBox(12);
        container.getStyleClass().add("srots-splash-container");
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(32, 40, 28, 40));

        Node logo = createLogo();
        Label title = new Label("SROTS");
        title.getStyleClass().add("srots-splash-title");
        title.setAccessibleText("SROTS");

        Label subtitle = new Label();
        subtitle.getStyleClass().add("srots-splash-subtitle");
        subtitle.textProperty().bind(viewModel.subtitleProperty());

        Label badge = new Label();
        badge.getStyleClass().add("srots-splash-badge");
        badge.textProperty().bind(viewModel.environmentBadgeProperty());
        badge.visibleProperty().bind(viewModel.environmentBadgeProperty().isNotEmpty());
        badge.managedProperty().bind(badge.visibleProperty());

        statusLabel.getStyleClass().add("srots-splash-status");
        statusLabel.textProperty().bind(viewModel.statusMessageProperty());
        statusLabel.setWrapText(true);
        statusLabel.setMaxWidth(400);

        progressBar.getStyleClass().add("srots-splash-progress");
        progressBar.setPrefWidth(360);
        progressBar.progressProperty().bind(
                Bindings.when(viewModel.indeterminateProperty())
                        .then(-1.0)
                        .otherwise(viewModel.progressProperty()));
        progressBar.setAccessibleText("Startup progress");

        errorLabel.getStyleClass().add("srots-splash-error");
        errorLabel.textProperty().bind(viewModel.errorMessageProperty());
        errorLabel.setWrapText(true);
        errorLabel.setMaxWidth(400);
        errorLabel.visibleProperty().bind(viewModel.failedProperty());
        errorLabel.managedProperty().bind(errorLabel.visibleProperty());

        retryButton.getStyleClass().addAll("srots-button", "srots-button-primary", "srots-splash-retry");
        exitButton.getStyleClass().addAll("srots-button", "srots-button-secondary", "srots-splash-exit");
        retryButton.setOnAction(e -> onRetry.run());
        exitButton.setOnAction(e -> onExit.run());
        failureActions.setAlignment(Pos.CENTER);
        failureActions.getChildren().addAll(retryButton, exitButton);
        failureActions.visibleProperty().bind(viewModel.failedProperty());
        failureActions.managedProperty().bind(failureActions.visibleProperty());

        Label version = new Label();
        version.getStyleClass().add("srots-splash-version");
        version.textProperty().bind(Bindings.concat("Version ", viewModel.versionProperty()));

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        container.getChildren().addAll(
                logo,
                title,
                subtitle,
                badge,
                statusLabel,
                progressBar,
                errorLabel,
                failureActions,
                spacer,
                version);
        getChildren().add(container);
    }

    public void setOnRetry(Runnable onRetry) {
        this.onRetry = onRetry == null ? () -> {
        } : onRetry;
    }

    public void setOnExit(Runnable onExit) {
        this.onExit = onExit == null ? () -> {
        } : onExit;
    }

    public SrotsSplashViewModel getViewModel() {
        return viewModel;
    }

    private Node createLogo() {
        try (var stream = SrotsSplashView.class.getResourceAsStream(LOGO_RESOURCE)) {
            if (stream != null) {
                Image image = new Image(stream, 72, 72, true, true);
                if (!image.isError()) {
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(72);
                    imageView.setFitHeight(72);
                    imageView.setPreserveRatio(true);
                    imageView.getStyleClass().add("srots-splash-logo");
                    imageView.setAccessibleText("SROTS logo");
                    return imageView;
                }
            }
            log.warn("SROTS splash logo missing or failed to load: {}", LOGO_RESOURCE);
        } catch (Exception ex) {
            log.warn("Unable to load SROTS splash logo from {}", LOGO_RESOURCE, ex);
        }
        Label fallback = new Label("SROTS");
        fallback.getStyleClass().addAll("srots-splash-logo", "srots-splash-logo-fallback");
        fallback.setAccessibleText("SROTS");
        return fallback;
    }
}
