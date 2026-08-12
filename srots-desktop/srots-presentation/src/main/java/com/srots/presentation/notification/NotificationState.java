package com.srots.presentation.notification;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Single source of truth for notification list + unread count.
 */
public final class NotificationState {

    public enum LoadStatus {
        IDLE,
        LOADING,
        READY,
        ERROR
    }

    private final ObservableList<SrotsNotification> notifications = FXCollections.observableArrayList();
    private final IntegerProperty unreadCount = new SimpleIntegerProperty(0);
    private final ObjectProperty<LoadStatus> loadStatus = new SimpleObjectProperty<>(LoadStatus.IDLE);
    private final StringProperty errorMessage = new SimpleStringProperty("");

    public ObservableList<SrotsNotification> getNotifications() {
        return notifications;
    }

    public ReadOnlyIntegerProperty unreadCountProperty() {
        return unreadCount;
    }

    public int getUnreadCount() {
        return unreadCount.get();
    }

    public ReadOnlyObjectProperty<LoadStatus> loadStatusProperty() {
        return loadStatus;
    }

    public LoadStatus getLoadStatus() {
        LoadStatus value = loadStatus.get();
        return value == null ? LoadStatus.IDLE : value;
    }

    public StringProperty errorMessageProperty() {
        return errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage.get() == null ? "" : errorMessage.get();
    }

    public void setLoading() {
        loadStatus.set(LoadStatus.LOADING);
        errorMessage.set("");
    }

    public void setError(String message) {
        loadStatus.set(LoadStatus.ERROR);
        errorMessage.set(message == null || message.isBlank()
                ? "Unable to load notifications."
                : message.trim());
    }

    public void replaceAll(List<SrotsNotification> items) {
        List<SrotsNotification> sorted = sortNewestFirst(items);
        notifications.setAll(sorted);
        recalculateUnread();
        loadStatus.set(LoadStatus.READY);
        errorMessage.set("");
    }

    public void upsert(SrotsNotification notification) {
        if (notification == null) {
            return;
        }
        List<SrotsNotification> next = new ArrayList<>(notifications);
        next.removeIf(n -> Objects.equals(n.getId(), notification.getId()));
        next.add(notification);
        notifications.setAll(sortNewestFirst(next));
        recalculateUnread();
        if (getLoadStatus() != LoadStatus.LOADING) {
            loadStatus.set(LoadStatus.READY);
        }
    }

    public boolean markAsRead(String id) {
        if (id == null || id.isBlank()) {
            return false;
        }
        boolean changed = false;
        List<SrotsNotification> next = new ArrayList<>(notifications.size());
        for (SrotsNotification notification : notifications) {
            if (Objects.equals(notification.getId(), id) && !notification.isRead()) {
                next.add(notification.withRead(true));
                changed = true;
            } else {
                next.add(notification);
            }
        }
        if (changed) {
            notifications.setAll(next);
            recalculateUnread();
        }
        return changed;
    }

    public void markAllAsRead() {
        List<SrotsNotification> next = new ArrayList<>(notifications.size());
        boolean changed = false;
        for (SrotsNotification notification : notifications) {
            if (!notification.isRead()) {
                next.add(notification.withRead(true));
                changed = true;
            } else {
                next.add(notification);
            }
        }
        if (changed) {
            notifications.setAll(next);
            recalculateUnread();
        }
    }

    public void recalculateUnread() {
        int count = 0;
        for (SrotsNotification notification : notifications) {
            if (!notification.isRead()) {
                count++;
            }
        }
        unreadCount.set(count);
    }

    static List<SrotsNotification> sortNewestFirst(List<SrotsNotification> items) {
        if (items == null || items.isEmpty()) {
            return List.of();
        }
        List<SrotsNotification> copy = new ArrayList<>(items);
        copy.sort(Comparator.comparing(SrotsNotification::getTimestamp).reversed()
                .thenComparing(SrotsNotification::getId));
        return List.copyOf(copy);
    }
}
