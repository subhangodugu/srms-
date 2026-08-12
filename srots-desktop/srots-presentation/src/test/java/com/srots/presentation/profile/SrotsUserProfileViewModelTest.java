package com.srots.presentation.profile;

import com.srots.presentation.components.information.avatar.UserInitials;
import com.srots.presentation.components.support.JavaFxTestSupport;
import com.srots.presentation.navigation.NavigationModule;
import com.srots.presentation.navigation.model.NavigationRouteId;
import com.srots.presentation.navigation.service.StaticUserAccessContext;
import com.srots.presentation.shell.topbar.TopBarApplicationState;
import javafx.stage.Window;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SrotsUserProfileViewModelTest {

    @BeforeAll
    static void bootFx() throws Exception {
        JavaFxTestSupport.ensureToolkit();
    }

    @Test
    void initials_followDisplayNameRules() {
        assertEquals("SG", UserInitials.fromDisplayName("Subhan Godogu"));
        assertEquals("S", UserInitials.fromDisplayName("Subhan"));
        assertEquals("JMS", UserInitials.fromDisplayName("John Michael Smith"));
        assertEquals("", UserInitials.fromDisplayName(null));
        assertEquals("", UserInitials.fromDisplayName("   "));
    }

    @Test
    void authenticated_showsProfileIdentityAndActions() throws Exception {
        JavaFxTestSupport.runOnFxThread(() -> {
            Fixture fx = Fixture.create();
            assertTrue(fx.viewModel.isProfileVisible());
            assertEquals("Subhan", fx.viewModel.getDisplayName());
            assertEquals("Administrator", fx.viewModel.getRoleLabel());
            assertEquals("S", fx.viewModel.getInitials());
            assertTrue(fx.viewModel.getActions().stream()
                    .anyMatch(a -> UserProfileActionCatalog.ACTION_PROFILE.equals(a.getId())));
            assertTrue(fx.viewModel.getActions().stream()
                    .anyMatch(a -> UserProfileActionCatalog.ACTION_SIGN_OUT.equals(a.getId())));
        });
    }

    @Test
    void menuOpenClose_togglesState() throws Exception {
        JavaFxTestSupport.runOnFxThread(() -> {
            Fixture fx = Fixture.create();
            assertFalse(fx.viewModel.isMenuOpen());
            fx.viewModel.openMenu();
            assertTrue(fx.viewModel.isMenuOpen());
            assertEquals(SrotsUserProfileMenuState.OPEN, fx.viewModel.getMenuState());
            fx.viewModel.toggleMenu();
            assertFalse(fx.viewModel.isMenuOpen());
            assertEquals(SrotsUserProfileMenuState.CLOSED, fx.viewModel.getMenuState());
        });
    }

    @Test
    void sessionExpired_closesMenuAndHidesProfile() throws Exception {
        JavaFxTestSupport.runOnFxThread(() -> {
            Fixture fx = Fixture.create();
            fx.viewModel.openMenu();
            assertTrue(fx.viewModel.isMenuOpen());
            fx.session.markSessionExpired();
            assertFalse(fx.viewModel.isMenuOpen());
            assertFalse(fx.viewModel.isProfileVisible());
            assertEquals(SessionState.SESSION_EXPIRED, fx.viewModel.getSessionState());
        });
    }

    @Test
    void signedOut_hidesProfile() throws Exception {
        JavaFxTestSupport.runOnFxThread(() -> {
            Fixture fx = Fixture.create();
            fx.session.completeSignOut();
            assertFalse(fx.viewModel.isProfileVisible());
            assertEquals(SessionState.SIGNED_OUT, fx.viewModel.getSessionState());
        });
    }

    @Test
    void profileAction_navigatesToProfile() throws Exception {
        JavaFxTestSupport.runOnFxThread(() -> {
            Fixture fx = Fixture.create();
            fx.viewModel.executeAction(UserProfileActionCatalog.ACTION_PROFILE);
            assertEquals(NavigationRouteId.PROFILE, fx.module.navigationService().currentRoute());
        });
    }

    @Test
    void preferencesAndSettings_navigate() throws Exception {
        JavaFxTestSupport.runOnFxThread(() -> {
            Fixture fx = Fixture.create();
            fx.viewModel.executeAction(UserProfileActionCatalog.ACTION_PREFERENCES);
            assertEquals(NavigationRouteId.PREFERENCES, fx.module.navigationService().currentRoute());
            fx.viewModel.executeAction(UserProfileActionCatalog.ACTION_SETTINGS);
            assertEquals(NavigationRouteId.SETTINGS, fx.module.navigationService().currentRoute());
        });
    }

    @Test
    void signOut_invokesAuthenticationOnce() throws Exception {
        JavaFxTestSupport.runOnFxThread(() -> {
            Fixture fx = Fixture.create();
            AtomicInteger calls = new AtomicInteger();
            AuthenticationService counting = new AuthenticationService() {
                private final AuthenticationService delegate = fx.auth;

                @Override
                public void signOut() {
                    calls.incrementAndGet();
                    delegate.signOut();
                }

                @Override
                public boolean isSigningOut() {
                    return delegate.isSigningOut();
                }
            };
            DialogService immediate = new DialogService() {
                @Override
                public void showAbout(Window owner, AboutInfo info) {
                }

                @Override
                public void showError(Window owner, String title, String message) {
                }

                @Override
                public CompletableFuture<Boolean> confirmSignOut(Window owner) {
                    return CompletableFuture.completedFuture(true);
                }
            };
            fx.viewModel.setDialogService(immediate);
            fx.viewModel.bind(
                    fx.session,
                    counting,
                    fx.module.navigationService(),
                    StaticUserAccessContext.admin(),
                    fx.topBarState,
                    () -> null,
                    () -> AboutInfo.of("SROTS", "0.1.0", "test"));
            fx.viewModel.setDialogService(immediate);
            fx.viewModel.rebuildActions();

            fx.viewModel.executeAction(UserProfileActionCatalog.ACTION_SIGN_OUT);
            fx.viewModel.executeAction(UserProfileActionCatalog.ACTION_SIGN_OUT);
            assertEquals(1, calls.get());
            assertEquals(NavigationRouteId.LOGIN, fx.module.navigationService().currentRoute());
        });
    }

    @Test
    void signingOut_disablesSignOutAction() throws Exception {
        JavaFxTestSupport.runOnFxThread(() -> {
            Fixture fx = Fixture.create();
            fx.session.beginSignOut();
            assertEquals(SessionState.SIGNING_OUT, fx.viewModel.getSessionState());
            SrotsUserProfileAction signOut = fx.viewModel.getActions().stream()
                    .filter(a -> UserProfileActionCatalog.ACTION_SIGN_OUT.equals(a.getId()))
                    .findFirst()
                    .orElseThrow();
            assertFalse(signOut.isEnabled());
        });
    }

    @Test
    void permissionFiltering_hidesUnauthorizedActions() {
        List<SrotsUserProfileAction> actions = List.of(
                SrotsUserProfileAction.builder("profile", "Profile").priority(1).build(),
                SrotsUserProfileAction.builder("admin", "Administration")
                        .priority(2)
                        .requiredPermission("administration.access")
                        .build());
        List<SrotsUserProfileAction> employee = UserProfileActionCatalog.filterByAccess(
                actions, StaticUserAccessContext.employee());
        assertEquals(1, employee.size());
        assertEquals("profile", employee.get(0).getId());

        List<SrotsUserProfileAction> admin = UserProfileActionCatalog.filterByAccess(
                actions, StaticUserAccessContext.admin());
        assertEquals(2, admin.size());
    }

    @Test
    void defaultAuthService_ignoresSecondSignOutWhenAlreadySignedOut() throws Exception {
        JavaFxTestSupport.runOnFxThread(() -> {
            NavigationModule module = NavigationModule.createDefault();
            TopBarApplicationState state = new TopBarApplicationState();
            SessionService session = DefaultSessionService.developmentDefaults(state);
            AuthenticationService auth = new DefaultAuthenticationService(session, module.navigationService());
            auth.signOut();
            auth.signOut();
            assertEquals(NavigationRouteId.LOGIN, module.navigationService().currentRoute());
            assertEquals(SessionState.SIGNED_OUT, session.getSessionState());
        });
    }

    @Test
    void aboutInfo_neverContainsSecrets() {
        AboutInfo info = AboutInfo.of("SROTS", "1.2.3", "development");
        String message = info.formatMessage();
        assertTrue(message.contains("SROTS"));
        assertTrue(message.contains("1.2.3"));
        assertFalse(message.toLowerCase().contains("jwt"));
        assertFalse(message.toLowerCase().contains("password"));
        assertFalse(message.toLowerCase().contains("token"));
        assertFalse(message.toLowerCase().contains("api key"));
    }

    @Test
    void popupManager_closesPreviousPopup() {
        SrotsPopupManager manager = new SrotsPopupManager();
        AtomicInteger closed = new AtomicInteger();
        manager.requestOpen(SrotsPopupManager.PopupKind.NOTIFICATIONS, closed::incrementAndGet);
        manager.requestOpen(SrotsPopupManager.PopupKind.PROFILE, () -> {
        });
        assertEquals(1, closed.get());
        assertEquals(SrotsPopupManager.PopupKind.PROFILE, manager.getOpenKind());
    }

    private static final class Fixture {
        final NavigationModule module;
        final TopBarApplicationState topBarState;
        final SessionService session;
        final AuthenticationService auth;
        final SrotsUserProfileViewModel viewModel;

        private Fixture(
                NavigationModule module,
                TopBarApplicationState topBarState,
                SessionService session,
                AuthenticationService auth,
                SrotsUserProfileViewModel viewModel) {
            this.module = module;
            this.topBarState = topBarState;
            this.session = session;
            this.auth = auth;
            this.viewModel = viewModel;
        }

        static Fixture create() {
            NavigationModule module = NavigationModule.createDefault();
            TopBarApplicationState topBarState = new TopBarApplicationState();
            SessionService session = new DefaultSessionService(topBarState);
            session.setCurrentUser(new CurrentUser(
                    "u1", "Subhan", "subhan@example.com", "Administrator", "Platform", null));
            AuthenticationService auth = new DefaultAuthenticationService(session, module.navigationService());
            SrotsUserProfileViewModel vm = new SrotsUserProfileViewModel();
            vm.bind(
                    session,
                    auth,
                    module.navigationService(),
                    StaticUserAccessContext.admin(),
                    topBarState,
                    () -> null,
                    () -> AboutInfo.of("SROTS", "0.1.0", "test"));
            return new Fixture(module, topBarState, session, auth, vm);
        }
    }
}
