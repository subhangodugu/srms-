package com.srots.presentation.profile;

import com.srots.presentation.components.utility.icons.SrotsIcon;
import com.srots.presentation.navigation.model.NavigationRouteId;
import com.srots.presentation.navigation.service.NavigationService;
import com.srots.presentation.navigation.service.UserAccessContext;
import javafx.stage.Window;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Builds implemented profile-menu actions only (no dead placeholders).
 */
public final class UserProfileActionCatalog {

    public static final String ACTION_PROFILE = "profile";
    public static final String ACTION_PREFERENCES = "preferences";
    public static final String ACTION_SETTINGS = "settings";
    public static final String ACTION_ABOUT = "about";
    public static final String ACTION_SIGN_OUT = "sign-out";

    private UserProfileActionCatalog() {
    }

    public static List<SrotsUserProfileAction> createDefault(
            NavigationService navigation,
            AuthenticationService authentication,
            DialogService dialogs,
            Supplier<Window> ownerSupplier,
            Supplier<AboutInfo> aboutInfoSupplier,
            UserAccessContext access,
            boolean signingOut) {
        Objects.requireNonNull(navigation, "navigation");
        Objects.requireNonNull(authentication, "authentication");
        Objects.requireNonNull(dialogs, "dialogs");

        List<SrotsUserProfileAction> actions = new ArrayList<>();
        actions.add(SrotsUserProfileAction.builder(ACTION_PROFILE, "Profile")
                .iconGlyph(SrotsIcon.USER.getGlyph())
                .type(SrotsUserProfileActionType.NAVIGATION)
                .priority(10)
                .onAction(() -> navigation.navigate(NavigationRouteId.PROFILE))
                .build());

        actions.add(SrotsUserProfileAction.builder(ACTION_PREFERENCES, "Preferences")
                .iconGlyph(SrotsIcon.PREFERENCES.getGlyph())
                .type(SrotsUserProfileActionType.NAVIGATION)
                .priority(20)
                .onAction(() -> navigation.navigate(NavigationRouteId.PREFERENCES))
                .build());

        actions.add(SrotsUserProfileAction.builder(ACTION_SETTINGS, "Settings")
                .iconGlyph(SrotsIcon.SETTINGS.getGlyph())
                .type(SrotsUserProfileActionType.NAVIGATION)
                .priority(30)
                .onAction(() -> navigation.navigate(NavigationRouteId.SETTINGS))
                .build());

        // Help omitted until HelpService / HELP destination exists.

        actions.add(SrotsUserProfileAction.builder(ACTION_ABOUT, "About SROTS")
                .iconGlyph(SrotsIcon.INFO.getGlyph())
                .type(SrotsUserProfileActionType.DIALOG)
                .priority(80)
                .onAction(() -> dialogs.showAbout(
                        ownerSupplier == null ? null : ownerSupplier.get(),
                        aboutInfoSupplier == null ? AboutInfo.of("SROTS", "0.0.0", "desktop")
                                : aboutInfoSupplier.get()))
                .build());

        actions.add(SrotsUserProfileAction.builder(ACTION_SIGN_OUT, "Sign out")
                .iconGlyph(SrotsIcon.SIGN_OUT.getGlyph())
                .type(SrotsUserProfileActionType.AUTHENTICATION)
                .priority(100)
                .enabled(!signingOut)
                .onAction(() -> confirmAndSignOut(authentication, dialogs, ownerSupplier))
                .build());

        return filterByAccess(actions, access);
    }

    static List<SrotsUserProfileAction> filterByAccess(
            List<SrotsUserProfileAction> actions,
            UserAccessContext access) {
        if (actions == null || actions.isEmpty()) {
            return List.of();
        }
        List<SrotsUserProfileAction> filtered = new ArrayList<>();
        for (SrotsUserProfileAction action : actions) {
            if (action == null || !action.isVisible()) {
                continue;
            }
            String permission = action.getRequiredPermission();
            if (permission != null && access != null && !access.hasPermission(permission)
                    && !access.hasPermission("*")) {
                continue;
            }
            filtered.add(action);
        }
        filtered.sort((a, b) -> Integer.compare(a.getPriority(), b.getPriority()));
        return List.copyOf(filtered);
    }

    private static void confirmAndSignOut(
            AuthenticationService authentication,
            DialogService dialogs,
            Supplier<Window> ownerSupplier) {
        if (authentication == null || authentication.isSigningOut()) {
            return;
        }
        Window owner = ownerSupplier == null ? null : ownerSupplier.get();
        dialogs.confirmSignOut(owner).thenAccept(confirmed -> {
            if (Boolean.TRUE.equals(confirmed)) {
                authentication.signOut();
            }
        });
    }
}
