package com.srots.presentation.shell.topbar;

import com.srots.presentation.components.navigation.topbar.SrotsConnectionState;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Presentation providers for TopBar connection / notifications / user.
 * Mock-friendly; no database or REST access.
 */
public final class TopBarApplicationState {

    private final ObjectProperty<TopBarUserInfo> currentUser =
            new SimpleObjectProperty<>(TopBarUserInfo.fallback());
    private final ObjectProperty<SrotsConnectionState> connectionState =
            new SimpleObjectProperty<>(SrotsConnectionState.ONLINE);
    private final IntegerProperty notificationCount = new SimpleIntegerProperty(0);

    public static TopBarApplicationState developmentDefaults() {
        TopBarApplicationState state = new TopBarApplicationState();
        state.setCurrentUser(new TopBarUserInfo("Operator", "Administrator"));
        state.setConnectionState(SrotsConnectionState.ONLINE);
        state.setNotificationCount(3);
        return state;
    }

    public ReadOnlyObjectProperty<TopBarUserInfo> currentUserProperty() {
        return currentUser;
    }

    public TopBarUserInfo getCurrentUser() {
        TopBarUserInfo value = currentUser.get();
        return value == null ? TopBarUserInfo.fallback() : value;
    }

    public void setCurrentUser(TopBarUserInfo user) {
        currentUser.set(user == null ? TopBarUserInfo.fallback() : user);
    }

    public ReadOnlyObjectProperty<SrotsConnectionState> connectionStateProperty() {
        return connectionState;
    }

    public SrotsConnectionState getConnectionState() {
        SrotsConnectionState value = connectionState.get();
        return value == null ? SrotsConnectionState.UNKNOWN : value;
    }

    public void setConnectionState(SrotsConnectionState state) {
        connectionState.set(state == null ? SrotsConnectionState.UNKNOWN : state);
    }

    public ReadOnlyIntegerProperty notificationCountProperty() {
        return notificationCount;
    }

    public int getNotificationCount() {
        return notificationCount.get();
    }

    public void setNotificationCount(int count) {
        notificationCount.set(Math.max(0, count));
    }
}
