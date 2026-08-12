package com.srots.presentation.profile;

import com.srots.presentation.shell.topbar.TopBarApplicationState;
import com.srots.presentation.shell.topbar.TopBarUserInfo;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * In-memory session state shared with TopBar chrome. No secrets / REST / DB.
 */
public final class DefaultSessionService implements SessionService {

    private final ObjectProperty<SessionState> sessionState =
            new SimpleObjectProperty<>(SessionState.AUTHENTICATED);
    private final ObjectProperty<CurrentUser> currentUser =
            new SimpleObjectProperty<>(CurrentUser.fallback());
    private final TopBarApplicationState topBarApplicationState;

    public DefaultSessionService(TopBarApplicationState topBarApplicationState) {
        this.topBarApplicationState = topBarApplicationState == null
                ? new TopBarApplicationState()
                : topBarApplicationState;
        syncTopBar();
    }

    public static DefaultSessionService developmentDefaults(TopBarApplicationState topBarState) {
        DefaultSessionService service = new DefaultSessionService(topBarState);
        service.setCurrentUser(new CurrentUser(
                "USER-OPERATOR",
                "Operator",
                "operator@srots.local",
                "Administrator",
                "Platform",
                null));
        return service;
    }

    @Override
    public ReadOnlyObjectProperty<SessionState> sessionStateProperty() {
        return sessionState;
    }

    @Override
    public SessionState getSessionState() {
        SessionState value = sessionState.get();
        return value == null ? SessionState.SIGNED_OUT : value;
    }

    @Override
    public ReadOnlyObjectProperty<CurrentUser> currentUserProperty() {
        return currentUser;
    }

    @Override
    public CurrentUser getCurrentUser() {
        CurrentUser value = currentUser.get();
        return value == null ? CurrentUser.fallback() : value;
    }

    @Override
    public void setCurrentUser(CurrentUser user) {
        CurrentUser next = user == null ? CurrentUser.fallback() : user;
        currentUser.set(next);
        if (getSessionState() == SessionState.SIGNED_OUT
                || getSessionState() == SessionState.SESSION_EXPIRED) {
            sessionState.set(SessionState.AUTHENTICATED);
        }
        syncTopBar();
    }

    @Override
    public void beginSignOut() {
        sessionState.set(SessionState.SIGNING_OUT);
    }

    @Override
    public void completeSignOut() {
        currentUser.set(CurrentUser.fallback());
        sessionState.set(SessionState.SIGNED_OUT);
        syncTopBar();
    }

    @Override
    public void markSessionExpired() {
        currentUser.set(CurrentUser.fallback());
        sessionState.set(SessionState.SESSION_EXPIRED);
        syncTopBar();
    }

    @Override
    public void restoreAuthenticated(CurrentUser user) {
        setCurrentUser(user);
        sessionState.set(SessionState.AUTHENTICATED);
        syncTopBar();
    }

    private void syncTopBar() {
        CurrentUser user = getCurrentUser();
        if (getSessionState() == SessionState.SIGNED_OUT
                || getSessionState() == SessionState.SESSION_EXPIRED) {
            topBarApplicationState.setCurrentUser(TopBarUserInfo.fallback());
            return;
        }
        topBarApplicationState.setCurrentUser(new TopBarUserInfo(user.displayName(), user.role()));
    }
}
