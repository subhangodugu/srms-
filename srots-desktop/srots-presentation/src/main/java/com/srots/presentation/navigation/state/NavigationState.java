package com.srots.presentation.navigation.state;

import com.srots.presentation.navigation.model.NavigationRouteId;
import com.srots.presentation.navigation.model.RouteParameters;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Observable presentation navigation state (no business data).
 */
public final class NavigationState {

    private final ObjectProperty<NavigationRouteId> currentRoute = new SimpleObjectProperty<>(this, "currentRoute");
    private final ObjectProperty<RouteParameters> currentParameters =
            new SimpleObjectProperty<>(this, "currentParameters", RouteParameters.empty());
    private final ObjectProperty<NavigationStatus> status =
            new SimpleObjectProperty<>(this, "status", NavigationStatus.IDLE);
    private final BooleanProperty canGoBack = new SimpleBooleanProperty(this, "canGoBack", false);
    private final BooleanProperty canGoForward = new SimpleBooleanProperty(this, "canGoForward", false);
    private final StringProperty statusMessage = new SimpleStringProperty(this, "statusMessage", "");

    public ObjectProperty<NavigationRouteId> currentRouteProperty() {
        return currentRoute;
    }

    public NavigationRouteId getCurrentRoute() {
        return currentRoute.get();
    }

    public void setCurrentRoute(NavigationRouteId route) {
        currentRoute.set(route);
    }

    public ObjectProperty<RouteParameters> currentParametersProperty() {
        return currentParameters;
    }

    public RouteParameters getCurrentParameters() {
        return currentParameters.get();
    }

    public void setCurrentParameters(RouteParameters parameters) {
        currentParameters.set(parameters == null ? RouteParameters.empty() : parameters);
    }

    public ObjectProperty<NavigationStatus> statusProperty() {
        return status;
    }

    public NavigationStatus getStatus() {
        return status.get();
    }

    public void setStatus(NavigationStatus value) {
        status.set(value == null ? NavigationStatus.IDLE : value);
    }

    public BooleanProperty canGoBackProperty() {
        return canGoBack;
    }

    public boolean isCanGoBack() {
        return canGoBack.get();
    }

    public void setCanGoBack(boolean value) {
        canGoBack.set(value);
    }

    public BooleanProperty canGoForwardProperty() {
        return canGoForward;
    }

    public boolean isCanGoForward() {
        return canGoForward.get();
    }

    public void setCanGoForward(boolean value) {
        canGoForward.set(value);
    }

    public StringProperty statusMessageProperty() {
        return statusMessage;
    }

    public String getStatusMessage() {
        return statusMessage.get();
    }

    public void setStatusMessage(String message) {
        statusMessage.set(message == null ? "" : message);
    }

    public void updateRoute(NavigationRouteId route, RouteParameters parameters) {
        setCurrentRoute(route);
        setCurrentParameters(parameters);
    }

    public void updateStatus(NavigationStatus value, String message) {
        setStatus(value);
        setStatusMessage(message);
    }

    public void updateHistoryFlags(boolean back, boolean forward) {
        setCanGoBack(back);
        setCanGoForward(forward);
    }

    public void reset() {
        setCurrentRoute(null);
        setCurrentParameters(RouteParameters.empty());
        setStatus(NavigationStatus.IDLE);
        setCanGoBack(false);
        setCanGoForward(false);
        setStatusMessage("");
    }
}
