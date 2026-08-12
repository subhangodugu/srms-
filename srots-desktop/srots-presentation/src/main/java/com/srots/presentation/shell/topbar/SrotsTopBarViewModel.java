package com.srots.presentation.shell.topbar;

import com.srots.presentation.components.navigation.breadcrumb.SrotsBreadcrumb;
import com.srots.presentation.components.navigation.topbar.SrotsConnectionState;
import com.srots.presentation.navigation.model.NavigationItem;
import com.srots.presentation.navigation.model.NavigationRouteId;
import com.srots.presentation.navigation.registry.NavigationRegistry;
import com.srots.presentation.navigation.service.NavigationService;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.value.ChangeListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Presentation state for {@link com.srots.presentation.components.navigation.topbar.SrotsTopBar}.
 * Observes navigation / session / connection / notification abstractions only.
 */
public final class SrotsTopBarViewModel {

    private final StringProperty pageTitle = new SimpleStringProperty("");
    private final StringProperty breadcrumbText = new SimpleStringProperty("");
    private final ObjectProperty<NavigationRouteId> currentRoute = new SimpleObjectProperty<>();
    private final ObjectProperty<TopBarUserInfo> currentUser =
            new SimpleObjectProperty<>(TopBarUserInfo.fallback());
    private final ObjectProperty<SrotsConnectionState> connectionState =
            new SimpleObjectProperty<>(SrotsConnectionState.ONLINE);
    private final IntegerProperty notificationCount = new SimpleIntegerProperty(0);
    private final BooleanProperty searchEnabled = new SimpleBooleanProperty(true);
    private final BooleanProperty compactMode = new SimpleBooleanProperty(false);
    private final ObjectProperty<SrotsTopBarState> uiState =
            new SimpleObjectProperty<>(SrotsTopBarState.READY);
    private final ObservableList<SrotsTopBarAction> actions = FXCollections.observableArrayList();
    private final ObservableList<SrotsBreadcrumb.Crumb> breadcrumbs = FXCollections.observableArrayList();

    private NavigationRegistry registry;
    private NavigationService navigationService;
    private TopBarApplicationState applicationState;
    private ChangeListener<NavigationRouteId> routeListener;
    private ChangeListener<TopBarUserInfo> userListener;
    private ChangeListener<SrotsConnectionState> connectionListener;
    private ChangeListener<Number> notificationListener;

    public void bind(NavigationRegistry registry, NavigationService navigationService) {
        bind(registry, navigationService, TopBarApplicationState.developmentDefaults());
    }

    public void bind(
            NavigationRegistry registry,
            NavigationService navigationService,
            TopBarApplicationState applicationState) {
        detach();
        this.registry = Objects.requireNonNull(registry, "registry");
        this.navigationService = Objects.requireNonNull(navigationService, "navigationService");
        this.applicationState = applicationState == null
                ? TopBarApplicationState.developmentDefaults()
                : applicationState;

        routeListener = (obs, o, route) -> applyRoute(route);
        this.navigationService.currentRouteProperty().addListener(routeListener);
        applyRoute(this.navigationService.currentRoute());

        userListener = (obs, o, user) -> currentUser.set(user == null ? TopBarUserInfo.fallback() : user);
        this.applicationState.currentUserProperty().addListener(userListener);
        currentUser.set(this.applicationState.getCurrentUser());

        connectionListener = (obs, o, state) -> applyConnection(state);
        this.applicationState.connectionStateProperty().addListener(connectionListener);
        applyConnection(this.applicationState.getConnectionState());

        notificationListener = (obs, o, count) ->
                notificationCount.set(count == null ? 0 : Math.max(0, count.intValue()));
        this.applicationState.notificationCountProperty().addListener(notificationListener);
        notificationCount.set(this.applicationState.getNotificationCount());
    }

    public void detach() {
        if (navigationService != null && routeListener != null) {
            navigationService.currentRouteProperty().removeListener(routeListener);
        }
        if (applicationState != null) {
            if (userListener != null) {
                applicationState.currentUserProperty().removeListener(userListener);
            }
            if (connectionListener != null) {
                applicationState.connectionStateProperty().removeListener(connectionListener);
            }
            if (notificationListener != null) {
                applicationState.notificationCountProperty().removeListener(notificationListener);
            }
        }
        routeListener = null;
        userListener = null;
        connectionListener = null;
        notificationListener = null;
    }

    public void setActions(List<SrotsTopBarAction> next) {
        actions.setAll(next == null ? List.of() : next.stream()
                .filter(SrotsTopBarAction::isVisible)
                .sorted((a, b) -> Integer.compare(a.getPriority(), b.getPriority()))
                .limit(3)
                .toList());
    }

    public void clearActions() {
        actions.clear();
    }

    public void requestNavigate(NavigationRouteId route) {
        if (navigationService != null && route != null) {
            navigationService.navigate(route);
        }
    }

    public StringProperty pageTitleProperty() {
        return pageTitle;
    }

    public String getPageTitle() {
        return pageTitle.get();
    }

    public StringProperty breadcrumbTextProperty() {
        return breadcrumbText;
    }

    public String getBreadcrumbText() {
        return breadcrumbText.get();
    }

    public ObjectProperty<NavigationRouteId> currentRouteProperty() {
        return currentRoute;
    }

    public NavigationRouteId getCurrentRoute() {
        return currentRoute.get();
    }

    public ObjectProperty<TopBarUserInfo> currentUserProperty() {
        return currentUser;
    }

    public TopBarUserInfo getCurrentUser() {
        return currentUser.get() == null ? TopBarUserInfo.fallback() : currentUser.get();
    }

    public ObjectProperty<SrotsConnectionState> connectionStateProperty() {
        return connectionState;
    }

    public SrotsConnectionState getConnectionState() {
        return connectionState.get() == null ? SrotsConnectionState.UNKNOWN : connectionState.get();
    }

    public IntegerProperty notificationCountProperty() {
        return notificationCount;
    }

    public int getNotificationCount() {
        return notificationCount.get();
    }

    public BooleanProperty searchEnabledProperty() {
        return searchEnabled;
    }

    public boolean isSearchEnabled() {
        return searchEnabled.get();
    }

    public BooleanProperty compactModeProperty() {
        return compactMode;
    }

    public void setCompactMode(boolean compact) {
        compactMode.set(compact);
    }

    public ObjectProperty<SrotsTopBarState> uiStateProperty() {
        return uiState;
    }

    public SrotsTopBarState getUiState() {
        return uiState.get();
    }

    public void setUiState(SrotsTopBarState state) {
        uiState.set(state == null ? SrotsTopBarState.READY : state);
    }

    public ObservableList<SrotsTopBarAction> getActions() {
        return actions;
    }

    public ObservableList<SrotsBreadcrumb.Crumb> getBreadcrumbs() {
        return breadcrumbs;
    }

    private void applyConnection(SrotsConnectionState state) {
        SrotsConnectionState safe = state == null ? SrotsConnectionState.UNKNOWN : state;
        connectionState.set(safe);
        if (safe == SrotsConnectionState.OFFLINE) {
            uiState.set(SrotsTopBarState.OFFLINE);
        } else if (uiState.get() == SrotsTopBarState.OFFLINE) {
            uiState.set(SrotsTopBarState.READY);
        }
    }

    private void applyRoute(NavigationRouteId route) {
        currentRoute.set(route);
        if (route == null || registry == null) {
            pageTitle.set("");
            breadcrumbText.set("");
            breadcrumbs.setAll(List.of());
            return;
        }

        List<NavigationItem> path = registry.breadcrumbPath(route);
        if (path.isEmpty()) {
            pageTitle.set(humanize(route.name()));
            breadcrumbText.set(pageTitle.get());
            breadcrumbs.setAll(List.of(new SrotsBreadcrumb.Crumb(pageTitle.get(), null)));
            return;
        }

        NavigationItem leaf = path.get(path.size() - 1);
        pageTitle.set(composeTitle(leaf, path));

        List<SrotsBreadcrumb.Crumb> crumbs = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        for (int i = 0; i < path.size(); i++) {
            NavigationItem item = path.get(i);
            boolean last = i == path.size() - 1;
            NavigationRouteId target = item.getRoute();
            labels.add(item.getTitle());
            crumbs.add(new SrotsBreadcrumb.Crumb(
                    item.getTitle(),
                    last ? null : () -> requestNavigate(target)));
        }
        breadcrumbs.setAll(crumbs);
        breadcrumbText.set(formatBreadcrumb(labels, compactMode.get()));
    }

    static String composeTitle(NavigationItem leaf, List<NavigationItem> path) {
        if (leaf == null) {
            return "";
        }
        if (path != null && path.size() >= 2) {
            NavigationItem parent = path.get(path.size() - 2);
            if (parent.getRoute() == NavigationRouteId.COMPTY
                    || parent.getRoute() == NavigationRouteId.SROTS_PRODUCT) {
                String parentTitle = parent.getTitle();
                String leafTitle = leaf.getTitle();
                if (leafTitle != null && !leafTitle.regionMatches(true, 0, parentTitle, 0, parentTitle.length())) {
                    return parentTitle + " " + leafTitle;
                }
            }
        }
        return leaf.getTitle();
    }

    static String formatBreadcrumb(List<String> labels, boolean compact) {
        if (labels == null || labels.isEmpty()) {
            return "";
        }
        if (!compact && labels.size() <= 4) {
            return String.join(" / ", labels);
        }
        if (labels.size() == 1) {
            return labels.get(0);
        }
        if (labels.size() == 2) {
            return labels.get(0) + " / " + labels.get(1);
        }
        return labels.get(0) + " / … / " + labels.get(labels.size() - 1);
    }

    static String humanize(String routeName) {
        if (routeName == null || routeName.isBlank()) {
            return "";
        }
        String[] parts = routeName.toLowerCase().split("_");
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            if (part.isBlank()) {
                continue;
            }
            if (!sb.isEmpty()) {
                sb.append(' ');
            }
            sb.append(Character.toUpperCase(part.charAt(0)));
            if (part.length() > 1) {
                sb.append(part.substring(1));
            }
        }
        return sb.toString();
    }
}
