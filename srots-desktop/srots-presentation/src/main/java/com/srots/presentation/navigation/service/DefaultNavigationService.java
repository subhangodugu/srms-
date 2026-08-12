package com.srots.presentation.navigation.service;

import com.srots.presentation.navigation.error.NavigationErrorHandler;
import com.srots.presentation.navigation.event.NavigationEvent;
import com.srots.presentation.navigation.event.NavigationEventType;
import com.srots.presentation.navigation.guard.FeatureAvailabilityGuard;
import com.srots.presentation.navigation.guard.GuardDecision;
import com.srots.presentation.navigation.guard.NavigationGuard;
import com.srots.presentation.navigation.host.SrotsContentHost;
import com.srots.presentation.navigation.model.NavigationContext;
import com.srots.presentation.navigation.model.NavigationRequest;
import com.srots.presentation.navigation.model.NavigationRouteId;
import com.srots.presentation.navigation.model.RouteParameters;
import com.srots.presentation.navigation.registry.NavigationRegistry;
import com.srots.presentation.navigation.resolver.ViewFactory;
import com.srots.presentation.navigation.state.NavigationHistory;
import com.srots.presentation.navigation.state.NavigationHistoryEntry;
import com.srots.presentation.navigation.state.NavigationState;
import com.srots.presentation.navigation.state.NavigationStatus;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * Default navigation orchestrator: guards → history → view factory → content host.
 * No database, REST, or feature business logic.
 */
public final class DefaultNavigationService implements NavigationService {

    private static final Logger log = LoggerFactory.getLogger(DefaultNavigationService.class);

    private final NavigationState state = new NavigationState();
    private final NavigationHistory history = new NavigationHistory();
    private final NavigationRegistry registry;
    private final ViewFactory viewFactory;
    private final SrotsContentHost contentHost;
    private final List<NavigationGuard> guards;
    private final NavigationErrorHandler errorHandler;
    private final List<Consumer<NavigationEvent>> listeners = new CopyOnWriteArrayList<>();

    private boolean suppressHistory;

    public DefaultNavigationService(
            NavigationRegistry registry,
            ViewFactory viewFactory,
            SrotsContentHost contentHost,
            List<NavigationGuard> guards) {
        this(registry, viewFactory, contentHost, guards, new NavigationErrorHandler());
    }

    public DefaultNavigationService(
            NavigationRegistry registry,
            ViewFactory viewFactory,
            SrotsContentHost contentHost,
            List<NavigationGuard> guards,
            NavigationErrorHandler errorHandler) {
        this.registry = Objects.requireNonNull(registry, "registry");
        this.viewFactory = Objects.requireNonNull(viewFactory, "viewFactory");
        this.contentHost = Objects.requireNonNull(contentHost, "contentHost");
        this.guards = guards == null ? List.of() : List.copyOf(guards);
        this.errorHandler = Objects.requireNonNullElseGet(errorHandler, NavigationErrorHandler::new);
    }

    @Override
    public void navigate(NavigationRouteId route) {
        navigate(NavigationRequest.of(normalize(route)));
    }

    @Override
    public void navigate(NavigationRouteId route, RouteParameters parameters) {
        navigate(new NavigationRequest(
                normalize(route),
                parameters == null ? RouteParameters.empty() : parameters,
                com.srots.presentation.navigation.model.NavigationSource.SYSTEM,
                false));
    }

    @Override
    public void navigate(NavigationContext context) {
        Objects.requireNonNull(context, "context");
        navigate(new NavigationRequest(
                normalize(context.route()),
                context.parameters(),
                com.srots.presentation.navigation.model.NavigationSource.SYSTEM,
                false));
    }

    @Override
    public void navigate(NavigationRequest request) {
        Objects.requireNonNull(request, "request");
        NavigationRequest target = new NavigationRequest(
                normalize(request.route()),
                request.parameters(),
                request.source(),
                request.replaceHistory());
        publish(NavigationEventType.NAVIGATION_REQUESTED, target.toContext(), null);
        if (target.replaceHistory()) {
            performNavigation(target.toContext(), false);
        } else {
            performNavigation(target.toContext(), true);
        }
    }

    @Override
    public void replace(NavigationRouteId route) {
        replace(NavigationContext.of(normalize(route)));
    }

    @Override
    public void replace(NavigationContext context) {
        Objects.requireNonNull(context, "context");
        performNavigation(withNormalizedRoute(context), false);
    }

    @Override
    public void refresh() {
        NavigationRouteId current = state.getCurrentRoute();
        if (current == null) {
            return;
        }
        performNavigation(
                NavigationContext.of(current, state.getCurrentParameters()),
                false,
                true);
    }

    @Override
    public void back() {
        if (!history.canGoBack()) {
            return;
        }
        NavigationHistoryEntry previous = history.popBack(snapshotCurrent());
        if (previous == null) {
            return;
        }
        suppressHistory = true;
        try {
            performNavigation(NavigationContext.of(previous.route(), previous.parameters()), false);
        } finally {
            suppressHistory = false;
            refreshHistoryFlags();
        }
    }

    @Override
    public void forward() {
        if (!history.canGoForward()) {
            return;
        }
        NavigationHistoryEntry next = history.popForward(snapshotCurrent());
        if (next == null) {
            return;
        }
        suppressHistory = true;
        try {
            performNavigation(NavigationContext.of(next.route(), next.parameters()), false);
        } finally {
            suppressHistory = false;
            refreshHistoryFlags();
        }
    }

    @Override
    public void home() {
        navigate(NavigationRouteId.OVERVIEW);
    }

    @Override
    public NavigationRouteId currentRoute() {
        return state.getCurrentRoute();
    }

    @Override
    public boolean canGoBack() {
        return state.isCanGoBack();
    }

    @Override
    public boolean canGoForward() {
        return state.isCanGoForward();
    }

    @Override
    public NavigationState state() {
        return state;
    }

    @Override
    public NavigationHistory history() {
        return history;
    }

    @Override
    public void addListener(Consumer<NavigationEvent> listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    @Override
    public void removeListener(Consumer<NavigationEvent> listener) {
        listeners.remove(listener);
    }

    @Override
    public ObjectProperty<NavigationRouteId> currentRouteProperty() {
        return state.currentRouteProperty();
    }

    public NavigationRegistry registry() {
        return registry;
    }

    public SrotsContentHost contentHost() {
        return contentHost;
    }

    private void performNavigation(NavigationContext target, boolean recordHistory) {
        performNavigation(target, recordHistory, false);
    }

    private void performNavigation(NavigationContext target, boolean recordHistory, boolean forceRefresh) {
        NavigationContext from = currentContext();

        if (!forceRefresh && isSameDestination(from, target)) {
            state.updateStatus(NavigationStatus.IDLE, "");
            publish(NavigationEventType.NAVIGATION_COMPLETED, target, "same-route");
            return;
        }

        state.updateStatus(NavigationStatus.NAVIGATING, "");
        publish(NavigationEventType.NAVIGATION_STARTED, target, null);

        if (isUnavailableRoute(target.route())) {
            Node unavailable = errorHandler.pageUnavailable(target.route());
            runOnFx(() -> contentHost.setView(unavailable));
            state.updateStatus(NavigationStatus.FAILED, "Page unavailable");
            publish(NavigationEventType.NAVIGATION_FAILED, target, "Page unavailable");
            return;
        }

        for (NavigationGuard guard : guards) {
            GuardDecision decision = guard.evaluate(from, target);
            if (decision == null || decision.allowed()) {
                continue;
            }
            if (decision.cancelled()) {
                state.updateStatus(NavigationStatus.IDLE, decision.message());
                publish(NavigationEventType.NAVIGATION_CANCELLED, target, decision.message());
                return;
            }
            handleBlocked(target, decision);
            return;
        }

        try {
            if (recordHistory && !suppressHistory) {
                pushHistory(from);
            }

            Node view = viewFactory.create(target);
            if (view == null) {
                throw new IllegalStateException("ViewFactory returned null for " + target.route());
            }

            runOnFx(() -> contentHost.setView(view));
            state.updateRoute(target.route(), target.parameters());
            state.updateStatus(NavigationStatus.IDLE, "");
            refreshHistoryFlags();
            publish(NavigationEventType.NAVIGATION_COMPLETED, target, null);
        } catch (Exception ex) {
            log.error("Navigation failed to {}", target.route(), ex);
            String message = "Unable to open this section.";
            state.updateStatus(NavigationStatus.FAILED, message);
            publish(NavigationEventType.NAVIGATION_FAILED, target, message);
            Node errorView = errorHandler.loadFailed(target, ex, ctx -> {
                suppressHistory = true;
                try {
                    performNavigation(ctx, false, true);
                } finally {
                    suppressHistory = false;
                }
            });
            runOnFx(() -> contentHost.setView(errorView));
            refreshHistoryFlags();
        }
    }

    private boolean isUnavailableRoute(NavigationRouteId route) {
        return route == null || route == NavigationRouteId.UNKNOWN;
    }

    private void handleBlocked(NavigationContext target, GuardDecision decision) {
        String message = decision.message() == null ? "Navigation blocked" : decision.message();
        state.updateStatus(NavigationStatus.BLOCKED, message);
        publish(NavigationEventType.NAVIGATION_BLOCKED, target, message);

        Node statusView;
        if (FeatureAvailabilityGuard.FEATURE_UNAVAILABLE.equals(message)) {
            statusView = errorHandler.featureUnavailable(target.route());
        } else {
            statusView = errorHandler.accessDenied(target.route(), this::home);
        }
        runOnFx(() -> contentHost.setView(statusView));
    }

    private boolean isSameDestination(NavigationContext from, NavigationContext target) {
        if (from == null || target == null || from.route() == null || target.route() == null) {
            return false;
        }
        RouteParameters fromParams = from.parameters() == null ? RouteParameters.empty() : from.parameters();
        RouteParameters toParams = target.parameters() == null ? RouteParameters.empty() : target.parameters();
        return from.route() == target.route() && fromParams.equals(toParams);
    }

    private void pushHistory(NavigationContext from) {
        if (from == null || from.route() == null || state.getCurrentRoute() == null) {
            return;
        }
        history.push(
                state.getCurrentRoute(),
                state.getCurrentParameters() == null ? RouteParameters.empty() : state.getCurrentParameters());
    }

    private NavigationHistoryEntry snapshotCurrent() {
        if (state.getCurrentRoute() == null) {
            return null;
        }
        return new NavigationHistoryEntry(
                state.getCurrentRoute(),
                state.getCurrentParameters() == null ? RouteParameters.empty() : state.getCurrentParameters(),
                System.currentTimeMillis());
    }

    private void refreshHistoryFlags() {
        state.updateHistoryFlags(history.canGoBack(), history.canGoForward());
    }

    private NavigationContext currentContext() {
        NavigationRouteId current = state.getCurrentRoute();
        if (current == null) {
            return null;
        }
        return NavigationContext.of(current, state.getCurrentParameters());
    }

    private NavigationRouteId normalize(NavigationRouteId route) {
        if (route == null) {
            return NavigationRouteId.OVERVIEW;
        }
        return route;
    }

    private NavigationContext withNormalizedRoute(NavigationContext context) {
        NavigationRouteId route = normalize(context.route());
        if (route == context.route()) {
            return context;
        }
        return new NavigationContext(route, context.parameters(), context.sourceRoute(), context.returnRoute());
    }

    private void publish(NavigationEventType type, NavigationContext context, String message) {
        NavigationEvent event = new NavigationEvent(
                type,
                context == null ? null : context.route(),
                context == null ? RouteParameters.empty() : context.parameters(),
                message);
        for (Consumer<NavigationEvent> listener : listeners) {
            try {
                listener.accept(event);
            } catch (Exception ex) {
                log.warn("Navigation listener failed", ex);
            }
        }
    }

    private static void runOnFx(Runnable action) {
        if (Platform.isFxApplicationThread()) {
            action.run();
        } else {
            Platform.runLater(action);
        }
    }
}
