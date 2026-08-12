package com.srots.presentation.window;

import com.srots.presentation.navigation.model.NavigationItem;
import com.srots.presentation.navigation.model.NavigationRouteId;
import com.srots.presentation.navigation.registry.NavigationRegistry;

import java.util.Objects;
import java.util.Optional;

/**
 * Centralized Stage title generation from navigation metadata.
 */
public final class SrotsWindowTitleService {

    private final String applicationName;
    private final NavigationRegistry registry;

    public SrotsWindowTitleService(String applicationName, NavigationRegistry registry) {
        this.applicationName = Objects.requireNonNullElse(applicationName, "SROTS");
        this.registry = registry;
    }

    public String defaultTitle() {
        return applicationName;
    }

    public String titleFor(NavigationRouteId route) {
        if (route == null) {
            return applicationName;
        }
        if (registry != null) {
            Optional<NavigationItem> item = registry.find(route);
            if (item.isPresent()) {
                return applicationName + " — " + displayTitle(item.get());
            }
        }
        return applicationName + " — " + humanize(route.name());
    }

    /**
     * Product children (SROTS / COMPTY) compose with their parent brand for Stage titles.
     */
    private String displayTitle(NavigationItem item) {
        String title = item.getTitle();
        NavigationRouteId parent = item.getParentRoute();
        if (parent == null || registry == null) {
            return title;
        }
        if (parent != NavigationRouteId.COMPTY && parent != NavigationRouteId.SROTS_PRODUCT) {
            return title;
        }
        Optional<NavigationItem> parentItem = registry.find(parent);
        if (parentItem.isEmpty()) {
            return title;
        }
        String parentTitle = parentItem.get().getTitle();
        if (title == null || title.isBlank()) {
            return parentTitle;
        }
        if (title.regionMatches(true, 0, parentTitle, 0, parentTitle.length())) {
            return title;
        }
        return parentTitle + " " + title;
    }

    static String humanize(String routeName) {
        if (routeName == null || routeName.isBlank()) {
            return "";
        }
        String[] parts = routeName.toLowerCase().split("_");
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            if (part.isBlank()) {
                continue;
            }
            if (!sb.isEmpty()) {
                sb.append(' ');
            }
            sb.append(Character.toUpperCase(part.charAt(0)));
            if (part.length() > 1) {
                sb.append(part.substring(1));
            }
        }
        return sb.toString();
    }
}
