package com.srots.presentation.shell.topbar;

import com.srots.presentation.components.overlays.command.SrotsCommandPalette;
import com.srots.presentation.navigation.model.NavigationItem;
import com.srots.presentation.navigation.model.NavigationRouteId;
import com.srots.presentation.navigation.registry.NavigationRegistry;
import com.srots.presentation.navigation.service.NavigationService;
import com.srots.presentation.navigation.service.NavigationVisibilityService;
import com.srots.presentation.navigation.service.UserAccessContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Builds command-palette items from navigation metadata (no business search).
 */
public final class NavigationCommandCatalog {

    private NavigationCommandCatalog() {
    }

    public static List<SrotsCommandPalette.CommandItem> fromNavigation(
            NavigationRegistry registry,
            NavigationService navigation,
            NavigationVisibilityService visibility,
            UserAccessContext access) {
        Objects.requireNonNull(registry, "registry");
        Objects.requireNonNull(navigation, "navigation");
        List<NavigationItem> items = visibility == null || access == null
                ? registry.allItems()
                : visibility.filterVisible(registry.allItems(), access);

        List<SrotsCommandPalette.CommandItem> commands = new ArrayList<>();
        for (NavigationItem item : items) {
            if (item == null || item.getRoute() == null) {
                continue;
            }
            if (!item.isEnabled()) {
                continue;
            }
            NavigationRouteId route = item.getRoute();
            commands.add(new SrotsCommandPalette.CommandItem(
                    route.id(),
                    "Go to " + item.getTitle(),
                    () -> navigation.navigate(route)));
        }
        commands.add(new SrotsCommandPalette.CommandItem(
                "settings",
                "Open Settings",
                () -> navigation.navigate(NavigationRouteId.SETTINGS)));
        return List.copyOf(commands);
    }
}
