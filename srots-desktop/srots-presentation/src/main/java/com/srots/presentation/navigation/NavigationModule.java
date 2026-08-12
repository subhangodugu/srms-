package com.srots.presentation.navigation;

import com.srots.presentation.navigation.guard.AuthenticationGuard;
import com.srots.presentation.navigation.guard.FeatureAvailabilityGuard;
import com.srots.presentation.navigation.guard.NavigationGuard;
import com.srots.presentation.navigation.guard.PermissionGuard;
import com.srots.presentation.navigation.guard.UnsavedChangesGuard;
import com.srots.presentation.navigation.host.SrotsContentHost;
import com.srots.presentation.navigation.registry.CoreNavigationProvider;
import com.srots.presentation.navigation.registry.DefaultNavigationRegistry;
import com.srots.presentation.navigation.registry.FeatureNavigationProvider;
import com.srots.presentation.navigation.registry.NavigationRegistry;
import com.srots.presentation.navigation.resolver.DefaultPlaceholderViewFactory;
import com.srots.presentation.navigation.resolver.ViewFactory;
import com.srots.presentation.navigation.service.DefaultNavigationService;
import com.srots.presentation.navigation.service.DevOpenAccessContext;
import com.srots.presentation.navigation.service.NavigationChromeBinder;
import com.srots.presentation.navigation.service.NavigationService;
import com.srots.presentation.navigation.service.NavigationVisibilityService;
import com.srots.presentation.navigation.service.UserAccessContext;
import com.srots.presentation.navigation.shortcut.NavigationShortcutRegistry;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javafx.scene.layout.StackPane;

/**
 * Wires presentation navigation for the desktop shell (no persistence / REST).
 */
public final class NavigationModule {

    private final NavigationRegistry registry;
    private final UserAccessContext accessContext;
    private final NavigationVisibilityService visibilityService;
    private final SrotsContentHost contentHost;
    private final NavigationService navigationService;
    private final NavigationChromeBinder chromeBinder;
    private final NavigationShortcutRegistry shortcutRegistry;

    private NavigationModule(
            NavigationRegistry registry,
            UserAccessContext accessContext,
            NavigationVisibilityService visibilityService,
            SrotsContentHost contentHost,
            NavigationService navigationService,
            NavigationChromeBinder chromeBinder,
            NavigationShortcutRegistry shortcutRegistry) {
        this.registry = registry;
        this.accessContext = accessContext;
        this.visibilityService = visibilityService;
        this.contentHost = contentHost;
        this.navigationService = navigationService;
        this.chromeBinder = chromeBinder;
        this.shortcutRegistry = shortcutRegistry;
    }

    public static NavigationModule createDefault() {
        return createDefault(new SrotsContentHost());
    }

    public static NavigationModule createDefault(StackPane contentArea) {
        return createDefault(new SrotsContentHost(contentArea));
    }

    public static NavigationModule createDefault(SrotsContentHost contentHost) {
        return create(
                List.of(new CoreNavigationProvider()),
                new DevOpenAccessContext(),
                new DefaultPlaceholderViewFactory(),
                contentHost,
                () -> false,
                () -> true);
    }

    public static NavigationModule create(
            List<? extends FeatureNavigationProvider> providers,
            UserAccessContext accessContext,
            ViewFactory viewFactory,
            SrotsContentHost contentHost,
            java.util.function.BooleanSupplier hasUnsavedChanges,
            java.util.function.Supplier<Boolean> confirmDiscard) {
        Objects.requireNonNull(providers, "providers");
        Objects.requireNonNull(accessContext, "accessContext");
        Objects.requireNonNull(viewFactory, "viewFactory");
        Objects.requireNonNull(contentHost, "contentHost");

        NavigationRegistry registry = new DefaultNavigationRegistry(providers);
        NavigationVisibilityService visibilityService = new NavigationVisibilityService();

        List<NavigationGuard> guards = new ArrayList<>();
        guards.add(new AuthenticationGuard(accessContext));
        guards.add(new PermissionGuard(registry, accessContext));
        guards.add(new FeatureAvailabilityGuard(registry));
        guards.add(new UnsavedChangesGuard(
                hasUnsavedChanges == null ? () -> false : hasUnsavedChanges,
                confirmDiscard == null ? () -> true : confirmDiscard));

        DefaultNavigationService navigationService =
                new DefaultNavigationService(registry, viewFactory, contentHost, guards);
        NavigationChromeBinder chromeBinder =
                new NavigationChromeBinder(navigationService, registry, visibilityService, accessContext);
        NavigationShortcutRegistry shortcuts = new NavigationShortcutRegistry();
        shortcuts.registerDefaults(
                navigationService::navigate,
                navigationService::back,
                navigationService::forward,
                null);

        return new NavigationModule(
                registry,
                accessContext,
                visibilityService,
                contentHost,
                navigationService,
                chromeBinder,
                shortcuts);
    }

    public NavigationRegistry registry() {
        return registry;
    }

    public UserAccessContext accessContext() {
        return accessContext;
    }

    public NavigationVisibilityService visibilityService() {
        return visibilityService;
    }

    public SrotsContentHost contentHost() {
        return contentHost;
    }

    public NavigationService navigationService() {
        return navigationService;
    }

    public NavigationChromeBinder chromeBinder() {
        return chromeBinder;
    }

    public NavigationShortcutRegistry shortcutRegistry() {
        return shortcutRegistry;
    }
}
