package com.srots.presentation.notification;

/**
 * Application-facing notification operations. UI never talks to transport/DB.
 */
public interface NotificationService {

    NotificationState state();

    void refresh();

    void markAsRead(String id);

    void markAllAsRead();
}
