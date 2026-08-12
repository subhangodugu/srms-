package com.srots.presentation.navigation.error;

import com.srots.presentation.navigation.model.NavigationContext;
import com.srots.presentation.navigation.model.NavigationRouteId;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Centralizes navigation failure UX. Isolates errors inside ContentHost.
 */
public final class NavigationErrorHandler {

    private static final Logger log = LoggerFactory.getLogger(NavigationErrorHandler.class);

    public Node accessDenied(NavigationRouteId attempted, Runnable onGoHome) {
        log.info("Access denied for route {}", attempted);
        return statusPage(
                "Access Denied",
                "You do not have permission to open this section.",
                "Go to Overview",
                onGoHome);
    }

    public Node pageUnavailable(NavigationRouteId attempted) {
        log.warn("Page unavailable for route {}", attempted);
        return statusPage(
                "Page unavailable",
                "This destination is not registered or is no longer available.",
                null,
                null);
    }

    public Node featureUnavailable(NavigationRouteId attempted) {
        log.info("Feature unavailable for route {}", attempted);
        return statusPage(
                "Feature unavailable",
                "This feature is currently disabled.",
                null,
                null);
    }

    public Node loadFailed(NavigationContext context, Throwable error, Consumer<NavigationContext> onRetry) {
        log.error("Unable to load module for {}", context == null ? null : context.route(), error);
        Runnable retry = null;
        if (onRetry != null && context != null) {
            retry = () -> onRetry.accept(context);
        }
        return statusPage(
                "Unable to load this module",
                "Something went wrong while opening this section.",
                retry == null ? null : "Retry",
                retry);
    }

    private static Node statusPage(String title, String body, String actionLabel, Runnable action) {
        VBox page = new VBox(16);
        page.getStyleClass().addAll("srots-page", "srots-navigation-status");
        page.setPadding(new Insets(24));
        page.setAlignment(Pos.TOP_LEFT);

        Label titleLabel = new Label(Objects.requireNonNullElse(title, "Unavailable"));
        titleLabel.getStyleClass().add("srots-section-title");

        Label bodyLabel = new Label(Objects.requireNonNullElse(body, ""));
        bodyLabel.getStyleClass().add("srots-text-secondary");
        bodyLabel.setWrapText(true);
        bodyLabel.setMaxWidth(520);

        page.getChildren().addAll(titleLabel, bodyLabel);
        if (actionLabel != null && !actionLabel.isBlank() && action != null) {
            Button button = new Button(actionLabel);
            button.getStyleClass().addAll("srots-button", "srots-primary-button");
            button.setOnAction(e -> action.run());
            page.getChildren().add(button);
        }
        return page;
    }
}
