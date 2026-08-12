package com.srots.presentation.profile;

import javafx.beans.property.ReadOnlyObjectProperty;

/**
 * Session lifecycle façade for presentation. No JWT / token / DB access.
 */
public interface SessionService extends CurrentUserProvider {

    ReadOnlyObjectProperty<SessionState> sessionStateProperty();

    SessionState getSessionState();

    void setCurrentUser(CurrentUser user);

    void beginSignOut();

    void completeSignOut();

    void markSessionExpired();

    void restoreAuthenticated(CurrentUser user);
}
