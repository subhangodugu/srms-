package com.srots.presentation.navigation.service;

import com.srots.presentation.navigation.event.NavigationEvent;
import com.srots.presentation.navigation.model.NavigationContext;
import com.srots.presentation.navigation.model.NavigationRequest;
import com.srots.presentation.navigation.model.NavigationRouteId;
import com.srots.presentation.navigation.model.RouteParameters;
import com.srots.presentation.navigation.state.NavigationHistory;
import com.srots.presentation.navigation.state.NavigationState;
import javafx.beans.property.ObjectProperty;

import java.util.function.Consumer;

/**
 * Controls in-app navigation without touching Stage/Scene or persistence.
 * Single application navigation system — chrome must not create feature views.
 */
public interface NavigationService {

    void navigate(NavigationRouteId route);

    void navigate(NavigationRouteId route, RouteParameters parameters);

    void navigate(NavigationContext context);

    void navigate(NavigationRequest request);

    void replace(NavigationRouteId route);

    void replace(NavigationContext context);

    /** Refresh current route data/view without recreating Stage/Scene/AppShell. */
    void refresh();

    void back();

    void forward();

    void home();

    NavigationRouteId currentRoute();

    boolean canGoBack();

    boolean canGoForward();

    NavigationState state();

    NavigationHistory history();

    void addListener(Consumer<NavigationEvent> listener);

    void removeListener(Consumer<NavigationEvent> listener);

    ObjectProperty<NavigationRouteId> currentRouteProperty();
}
