package com.srots.presentation.notification;

import com.srots.presentation.navigation.service.NavigationService;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Presentation state for the notification panel.
 */
public final class SrotsNotificationPanelViewModel {

    private final ObservableList<SrotsNotification> visibleNotifications = FXCollections.observableArrayList();
    private final ObjectProperty<NotificationFilter> filter =
            new SimpleObjectProperty<>(NotificationFilter.ALL);
    private final IntegerProperty unreadCount = new SimpleIntegerProperty(0);
    private final BooleanProperty loading = new SimpleBooleanProperty(false);
    private final BooleanProperty refreshing = new SimpleBooleanProperty(false);
    private final BooleanProperty error = new SimpleBooleanProperty(false);
    private final StringProperty errorMessage = new SimpleStringProperty("");
    private final BooleanProperty panelOpen = new SimpleBooleanProperty(false);
    private final BooleanProperty empty = new SimpleBooleanProperty(true);
    private final StringProperty emptyMessage = new SimpleStringProperty("You're all caught up.");
    private final ObjectProperty<SrotsNotificationPanelState> panelState =
            new SimpleObjectProperty<>(SrotsNotificationPanelState.CLOSED);

    private NotificationService notificationService;
    private NavigationService navigationService;
    private NotificationState notificationState;
    private FilteredList<SrotsNotification> filtered;
    private ListChangeListener<SrotsNotification> listListener;
    private final AtomicBoolean actionGuard = new AtomicBoolean(false);
    private final NotificationTimestampFormatter timestampFormatter = new NotificationTimestampFormatter();

    public void bind(NotificationService notificationService, NavigationService navigationService) {
        detach();
        this.notificationService = Objects.requireNonNull(notificationService, "notificationService");
        this.navigationService = Objects.requireNonNull(navigationService, "navigationService");
        this.notificationState = notificationService.state();

        filtered = new FilteredList<>(notificationState.getNotifications(), this::matchesFilter);
        listListener = change -> visibleNotifications.setAll(filtered);
        filtered.addListener(listListener);
        visibleNotifications.setAll(filtered);

        unreadCount.bind(notificationState.unreadCountProperty());
        loading.bind(Bindings.createBooleanBinding(
                () -> notificationState.getLoadStatus() == NotificationState.LoadStatus.LOADING,
                notificationState.loadStatusProperty()));
        refreshing.bind(loading);
        error.bind(Bindings.createBooleanBinding(
                () -> notificationState.getLoadStatus() == NotificationState.LoadStatus.ERROR,
                notificationState.loadStatusProperty()));
        errorMessage.bind(notificationState.errorMessageProperty());
        empty.bind(Bindings.createBooleanBinding(
                () -> visibleNotifications.isEmpty() && !loading.get() && !error.get(),
                visibleNotifications, loading, error));
        emptyMessage.bind(Bindings.createStringBinding(
                () -> filter.get() == NotificationFilter.UNREAD
                        ? "No unread notifications."
                        : "You're all caught up.",
                filter));

        filter.addListener((obs, o, n) -> {
            if (filtered != null) {
                filtered.setPredicate(this::matchesFilter);
                visibleNotifications.setAll(filtered);
            }
        });
        updatePanelState();
    }

    public void detach() {
        if (filtered != null && listListener != null) {
            filtered.removeListener(listListener);
        }
        unreadCount.unbind();
        loading.unbind();
        refreshing.unbind();
        error.unbind();
        errorMessage.unbind();
        empty.unbind();
        emptyMessage.unbind();
        listListener = null;
        filtered = null;
        notificationState = null;
        notificationService = null;
        navigationService = null;
    }

    public ObservableList<SrotsNotification> getVisibleNotifications() {
        return visibleNotifications;
    }

    public ObjectProperty<NotificationFilter> filterProperty() {
        return filter;
    }

    public NotificationFilter getFilter() {
        return filter.get() == null ? NotificationFilter.ALL : filter.get();
    }

    public void setFilter(NotificationFilter value) {
        filter.set(value == null ? NotificationFilter.ALL : value);
    }

    public ReadOnlyIntegerProperty unreadCountProperty() {
        return unreadCount;
    }

    public int getUnreadCount() {
        return unreadCount.get();
    }

    public ReadOnlyBooleanProperty loadingProperty() {
        return loading;
    }

    public ReadOnlyBooleanProperty refreshingProperty() {
        return refreshing;
    }

    public ReadOnlyBooleanProperty errorProperty() {
        return error;
    }

    public ReadOnlyStringProperty errorMessageProperty() {
        return errorMessage;
    }

    public BooleanProperty panelOpenProperty() {
        return panelOpen;
    }

    public boolean isPanelOpen() {
        return panelOpen.get();
    }

    public void setPanelOpen(boolean open) {
        panelOpen.set(open);
        updatePanelState();
    }

    public void openPanel() {
        setPanelOpen(true);
        if (notificationService != null
                && notificationState != null
                && notificationState.getLoadStatus() == NotificationState.LoadStatus.IDLE) {
            notificationService.refresh();
        }
    }

    public void closePanel() {
        setPanelOpen(false);
    }

    public void togglePanel() {
        if (isPanelOpen()) {
            closePanel();
        } else {
            openPanel();
        }
    }

    public ReadOnlyBooleanProperty emptyProperty() {
        return empty;
    }

    public ReadOnlyStringProperty emptyMessageProperty() {
        return emptyMessage;
    }

    public ReadOnlyObjectProperty<SrotsNotificationPanelState> panelStateProperty() {
        return panelState;
    }

    public NotificationTimestampFormatter getTimestampFormatter() {
        return timestampFormatter;
    }

    public void refresh() {
        if (notificationService != null) {
            notificationService.refresh();
        }
    }

    public void markAllAsRead() {
        if (notificationService != null) {
            notificationService.markAllAsRead();
        }
    }

    public void activate(SrotsNotification notification) {
        if (notification == null || !actionGuard.compareAndSet(false, true)) {
            return;
        }
        try {
            if (!notification.isRead() && notificationService != null) {
                notificationService.markAsRead(notification.getId());
            }
            NotificationAction action = notification.getAction();
            if (action != null && action.hasNavigation() && navigationService != null) {
                navigationService.navigate(action.route());
            }
            closePanel();
        } finally {
            actionGuard.set(false);
        }
    }

    private boolean matchesFilter(SrotsNotification notification) {
        if (notification == null) {
            return false;
        }
        if (getFilter() == NotificationFilter.UNREAD) {
            return !notification.isRead();
        }
        return true;
    }

    private void updatePanelState() {
        if (!isPanelOpen()) {
            panelState.set(SrotsNotificationPanelState.CLOSED);
            return;
        }
        if (error.get()) {
            panelState.set(SrotsNotificationPanelState.ERROR);
        } else if (loading.get()) {
            panelState.set(SrotsNotificationPanelState.LOADING);
        } else if (empty.get()) {
            panelState.set(SrotsNotificationPanelState.EMPTY);
        } else {
            panelState.set(SrotsNotificationPanelState.READY);
        }
        // also OPEN marker for open chrome
        if (isPanelOpen() && panelState.get() != SrotsNotificationPanelState.ERROR) {
            // keep READY/EMPTY/LOADING while open
        }
    }
}
