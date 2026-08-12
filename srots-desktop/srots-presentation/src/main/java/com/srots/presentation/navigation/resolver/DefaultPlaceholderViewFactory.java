package com.srots.presentation.navigation.resolver;

import com.srots.presentation.components.layout.page.SrotsPageHeader;
import com.srots.presentation.designsystem.DesignSystemShowcaseView;
import com.srots.presentation.navigation.model.NavigationContext;
import com.srots.presentation.navigation.model.NavigationRouteId;
import java.util.Objects;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * Placeholder views until feature modules supply real screens.
 * Special-cases {@link NavigationRouteId#DESIGN_SYSTEM} and {@link NavigationRouteId#OVERVIEW}.
 */
public final class DefaultPlaceholderViewFactory implements ViewFactory {

    private static final String PLACEHOLDER_CAPTION =
            "Navigation destination ready for feature implementation.";

    private final ViewResolver viewResolver;

    public DefaultPlaceholderViewFactory(ViewResolver viewResolver) {
        this.viewResolver = Objects.requireNonNull(viewResolver, "viewResolver");
    }

    public DefaultPlaceholderViewFactory() {
        this(new DefaultViewResolver());
    }

    @Override
    public Node create(NavigationContext context) {
        Objects.requireNonNull(context, "context");
        NavigationRouteId route = context.route();

        if (route == NavigationRouteId.DESIGN_SYSTEM) {
            return new DesignSystemShowcaseView();
        }
        if (route == NavigationRouteId.OVERVIEW) {
            return createOverviewWelcome();
        }

        ViewDefinition definition = viewResolver.resolve(route);
        return createPlaceholderPage(definition);
    }

    private static VBox createOverviewWelcome() {
        VBox page = new VBox(16);
        page.getStyleClass().addAll("srots-page", "srots-overview-welcome");
        page.setPadding(new Insets(24));
        page.setFillWidth(true);

        SrotsPageHeader header = new SrotsPageHeader(
                "SROTS",
                "Welcome to SROTS",
                "Executive overview and platform operating home.");
        Label body = new Label("Select a destination from the sidebar to open a module workspace.");
        body.getStyleClass().add("srots-text-secondary");
        body.setWrapText(true);

        page.getChildren().addAll(header, body);
        VBox.setVgrow(body, Priority.NEVER);
        return page;
    }

    private static VBox createPlaceholderPage(ViewDefinition definition) {
        VBox page = new VBox(16);
        page.getStyleClass().addAll("srots-page", "srots-placeholder-view");
        page.setPadding(new Insets(24));
        page.setFillWidth(true);

        SrotsPageHeader header = new SrotsPageHeader(
                "",
                definition.title(),
                definition.description());

        Label caption = new Label(PLACEHOLDER_CAPTION);
        caption.getStyleClass().add("srots-text-muted");
        caption.setWrapText(true);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        page.getChildren().addAll(header, caption, spacer);
        page.setAlignment(Pos.TOP_LEFT);
        return page;
    }
}
