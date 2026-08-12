package com.srots.presentation.profile;

import com.srots.presentation.components.information.avatar.UserInitials;
import com.srots.presentation.components.utility.icons.SrotsIcon;
import com.srots.presentation.navigation.service.NavigationService;
import com.srots.presentation.navigation.service.UserAccessContext;
import com.srots.presentation.shell.topbar.TopBarApplicationState;
import com.srots.presentation.shell.topbar.TopBarUserInfo;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Window;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Presentation state for the TopBar user/profile control and menu.
 */
public final class SrotsUserProfileViewModel {

    private final ObjectProperty<CurrentUser> currentUser = new SimpleObjectProperty<>(CurrentUser.fallback());
    private final ObjectProperty<SessionState> sessionState =
            new SimpleObjectProperty<>(SessionState.AUTHENTICATED);
    private final ObjectProperty<SrotsUserProfileMenuState> menuState =
            new SimpleObjectProperty<>(SrotsUserProfileMenuState.CLOSED);
    private final BooleanProperty menuOpen = new SimpleBooleanProperty(false);
    private final BooleanProperty profileVisible = new SimpleBooleanProperty(true);
    private final ObservableList<SrotsUserProfileAction> actions = FXCollections.observableArrayList();

    private SessionService sessionService;
    private AuthenticationService authenticationService;
    private NavigationService navigationService;
    private DialogService dialogService = new DialogService.Default();
    private UserAccessContext accessContext;
    private Supplier<Window> ownerSupplier = () -> null;
    private Supplier<AboutInfo> aboutInfoSupplier = () -> AboutInfo.of("SROTS", "0.0.0", "desktop");
    private TopBarApplicationState topBarApplicationState;

    private javafx.beans.value.ChangeListener<CurrentUser> userListener;
    private javafx.beans.value.ChangeListener<SessionState> sessionListener;

    public void bind(
            SessionService sessionService,
            AuthenticationService authenticationService,
            NavigationService navigationService,
            UserAccessContext accessContext,
            TopBarApplicationState topBarApplicationState,
            Supplier<Window> ownerSupplier,
            Supplier<AboutInfo> aboutInfoSupplier) {
        detach();
        this.sessionService = Objects.requireNonNull(sessionService, "sessionService");
        this.authenticationService = Objects.requireNonNull(authenticationService, "authenticationService");
        this.navigationService = Objects.requireNonNull(navigationService, "navigationService");
        this.accessContext = accessContext;
        this.topBarApplicationState = topBarApplicationState;
        this.ownerSupplier = ownerSupplier == null ? () -> null : ownerSupplier;
        this.aboutInfoSupplier = aboutInfoSupplier == null
                ? () -> AboutInfo.of("SROTS", "0.0.0", "desktop")
                : aboutInfoSupplier;
        this.dialogService = new DialogService.Default();

        userListener = (obs, o, n) -> applyUser(n);
        sessionListener = (obs, o, n) -> applySession(n);
        sessionService.currentUserProperty().addListener(userListener);
        sessionService.sessionStateProperty().addListener(sessionListener);
        applyUser(sessionService.getCurrentUser());
        applySession(sessionService.getSessionState());
        rebuildActions();
    }

    public void detach() {
        if (sessionService != null) {
            if (userListener != null) {
                sessionService.currentUserProperty().removeListener(userListener);
            }
            if (sessionListener != null) {
                sessionService.sessionStateProperty().removeListener(sessionListener);
            }
        }
        userListener = null;
        sessionListener = null;
    }

    public ReadOnlyObjectProperty<CurrentUser> currentUserProperty() {
        return currentUser;
    }

    public CurrentUser getCurrentUser() {
        CurrentUser value = currentUser.get();
        return value == null ? CurrentUser.fallback() : value;
    }

    public ReadOnlyObjectProperty<SessionState> sessionStateProperty() {
        return sessionState;
    }

    public SessionState getSessionState() {
        SessionState value = sessionState.get();
        return value == null ? SessionState.SIGNED_OUT : value;
    }

    public ReadOnlyObjectProperty<SrotsUserProfileMenuState> menuStateProperty() {
        return menuState;
    }

    public SrotsUserProfileMenuState getMenuState() {
        return menuState.get() == null ? SrotsUserProfileMenuState.CLOSED : menuState.get();
    }

    public ReadOnlyBooleanProperty menuOpenProperty() {
        return menuOpen;
    }

    public boolean isMenuOpen() {
        return menuOpen.get();
    }

    public ReadOnlyBooleanProperty profileVisibleProperty() {
        return profileVisible;
    }

    public boolean isProfileVisible() {
        return profileVisible.get();
    }

    public ObservableList<SrotsUserProfileAction> getActions() {
        return actions;
    }

    public String getDisplayName() {
        return getCurrentUser().displayName();
    }

    public String getRoleLabel() {
        return getCurrentUser().role();
    }

    public String getEmail() {
        return getCurrentUser().email();
    }

    public String getInitials() {
        return UserInitials.fromDisplayName(getDisplayName());
    }

    public void setMenuOpen(boolean open) {
        menuOpen.set(open);
        menuState.set(open ? SrotsUserProfileMenuState.OPEN : SrotsUserProfileMenuState.CLOSED);
        if (topBarApplicationState != null) {
            // TopBar chrome may observe PROFILE_OPEN via separate binders later.
        }
    }

    public void toggleMenu() {
        setMenuOpen(!isMenuOpen());
    }

    public void closeMenu() {
        setMenuOpen(false);
    }

    public void openMenu() {
        if (isProfileVisible() && getSessionState() == SessionState.AUTHENTICATED) {
            setMenuOpen(true);
            rebuildActions();
        }
    }

    public void executeAction(String actionId) {
        for (SrotsUserProfileAction action : actions) {
            if (action.getId().equals(actionId)) {
                action.execute();
                return;
            }
        }
    }

    public void rebuildActions() {
        if (navigationService == null || authenticationService == null) {
            actions.clear();
            return;
        }
        boolean signingOut = getSessionState() == SessionState.SIGNING_OUT
                || authenticationService.isSigningOut();
        List<SrotsUserProfileAction> next = UserProfileActionCatalog.createDefault(
                navigationService,
                authenticationService,
                dialogService,
                ownerSupplier,
                aboutInfoSupplier,
                accessContext,
                signingOut);
        actions.setAll(next);
    }

    private void applyUser(CurrentUser user) {
        CurrentUser safe = user == null ? CurrentUser.fallback() : user;
        currentUser.set(safe);
        if (topBarApplicationState != null
                && getSessionState() == SessionState.AUTHENTICATED) {
            topBarApplicationState.setCurrentUser(new TopBarUserInfo(safe.displayName(), safe.role()));
        }
    }

    private void applySession(SessionState state) {
        SessionState safe = state == null ? SessionState.SIGNED_OUT : state;
        sessionState.set(safe);
        boolean authenticated = safe == SessionState.AUTHENTICATED || safe == SessionState.SIGNING_OUT;
        profileVisible.set(authenticated);
        if (safe == SessionState.SESSION_EXPIRED
                || safe == SessionState.SIGNED_OUT) {
            closeMenu();
            if (topBarApplicationState != null) {
                topBarApplicationState.setCurrentUser(TopBarUserInfo.fallback());
            }
        }
        rebuildActions();
    }

    /** Test helper — inject a custom dialog service. */
    void setDialogService(DialogService dialogService) {
        this.dialogService = dialogService == null ? new DialogService.Default() : dialogService;
    }

    public ObjectProperty<SrotsUserProfileMenuState> menuStatePropertyWritable() {
        return menuState;
    }

    public BooleanProperty menuOpenPropertyWritable() {
        return menuOpen;
    }

    /** Glyph used by menu items for sign-out styling checks in tests. */
    public static String signOutGlyph() {
        return SrotsIcon.SIGN_OUT.getGlyph();
    }
}
