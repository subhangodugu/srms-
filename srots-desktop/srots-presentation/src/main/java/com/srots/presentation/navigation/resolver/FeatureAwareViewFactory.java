package com.srots.presentation.navigation.resolver;

import com.srots.application.usecase.dashboard.GetDashboardOverviewUseCase;
import com.srots.presentation.navigation.model.NavigationContext;
import com.srots.presentation.navigation.model.NavigationRouteId;
import com.srots.presentation.overview.OverviewView;
import com.srots.presentation.overview.OverviewViewModel;
import javafx.scene.Node;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Routes feature screens to real views; everything else falls through to placeholders.
 */
public final class FeatureAwareViewFactory implements ViewFactory {

    private final ViewFactory fallback;
    private final Supplier<GetDashboardOverviewUseCase> overviewUseCase;
    private final Consumer<NavigationRouteId> navigator;

    public FeatureAwareViewFactory(
            ViewFactory fallback,
            Supplier<GetDashboardOverviewUseCase> overviewUseCase,
            Consumer<NavigationRouteId> navigator) {
        this.fallback = Objects.requireNonNull(fallback, "fallback");
        this.overviewUseCase = Objects.requireNonNull(overviewUseCase, "overviewUseCase");
        this.navigator = navigator == null ? route -> { } : navigator;
    }

    @Override
    public Node create(NavigationContext context) {
        Objects.requireNonNull(context, "context");
        if (context.route() == NavigationRouteId.OVERVIEW) {
            OverviewViewModel viewModel = new OverviewViewModel(overviewUseCase.get());
            OverviewView view = new OverviewView(viewModel, navigator);
            view.load();
            return view;
        }
        return fallback.create(context);
    }
}
