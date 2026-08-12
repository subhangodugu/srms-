package com.srots.presentation.profile;

import javafx.beans.property.ReadOnlyObjectProperty;

/**
 * Supplies the current presentation user to profile chrome.
 */
public interface CurrentUserProvider {

    ReadOnlyObjectProperty<CurrentUser> currentUserProperty();

    CurrentUser getCurrentUser();
}
