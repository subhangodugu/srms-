package com.srots.presentation.profile;

import com.srots.presentation.components.information.avatar.SrotsUserProfile;
import com.srots.presentation.components.support.JavaFxTestSupport;
import com.srots.presentation.navigation.NavigationModule;
import com.srots.presentation.navigation.service.StaticUserAccessContext;
import com.srots.presentation.shell.topbar.TopBarApplicationState;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SrotsUserProfileControllerTest {

    @BeforeAll
    static void bootFx() throws Exception {
        JavaFxTestSupport.ensureToolkit();
    }

    @Test
    void toggle_withoutScene_keepsMenuClosed() throws Exception {
        JavaFxTestSupport.runOnFxThread(() -> {
            NavigationModule module = NavigationModule.createDefault();
            TopBarApplicationState topBarState = new TopBarApplicationState();
            SessionService session = DefaultSessionService.developmentDefaults(topBarState);
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

            SrotsUserProfile button = new SrotsUserProfile();
            SrotsPopupManager popups = new SrotsPopupManager();
            SrotsUserProfileController controller = new SrotsUserProfileController(button, vm, popups);
            controller.attach();

            assertFalse(vm.isMenuOpen());
            controller.toggle();
            assertFalse(vm.isMenuOpen());
            assertFalse(controller.getMenu().isShowing());
            controller.close();
            assertEqualsNone(popups);
        });
    }

    @Test
    void profileButton_accessibleNamePresent() throws Exception {
        JavaFxTestSupport.runOnFxThread(() -> {
            SrotsUserProfile button = new SrotsUserProfile("Subhan", "Administrator");
            assertTrue(button.getAccessibleText().toLowerCase().contains("profile"));
        });
    }

    private static void assertEqualsNone(SrotsPopupManager popups) {
        assertTrue(popups.getOpenKind() == SrotsPopupManager.PopupKind.NONE
                || popups.getOpenKind() == SrotsPopupManager.PopupKind.PROFILE);
        popups.closeAll();
        assertTrue(popups.getOpenKind() == SrotsPopupManager.PopupKind.NONE);
    }
}
