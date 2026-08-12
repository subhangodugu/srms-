package com.srots.presentation.components.navigation.sidebar;

import java.util.Locale;
import java.util.Map;

/**
 * Maps navigation catalog {@code iconKey} values to centralized glyph tokens.
 * Presentation-only — not an icon font package.
 */
public final class NavigationIconResolver {

    private static final Map<String, String> GLYPHS = Map.ofEntries(
            Map.entry("overview", "⌂"),
            Map.entry("design", "▦"),
            Map.entry("workspace", "▥"),
            Map.entry("tasks", "☑"),
            Map.entry("projects", "⧉"),
            Map.entry("issues", "\u2691"),
            Map.entry("activity", "↻"),
            Map.entry("company", "▣"),
            Map.entry("users", "U"),
            Map.entry("teams", "T"),
            Map.entry("departments", "D"),
            Map.entry("organization", "O"),
            Map.entry("products", "P"),
            Map.entry("srots", "S"),
            Map.entry("compty", "C"),
            Map.entry("engineering", "E"),
            Map.entry("versions", "V"),
            Map.entry("releases", "R"),
            Map.entry("deployments", "^"),
            Map.entry("requirements", "="),
            Map.entry("development", "</>"),
            Map.entry("builds", "B"),
            Map.entry("sales", "$"),
            Map.entry("leads", "L"),
            Map.entry("opportunities", "*"),
            Map.entry("customers", "K"),
            Map.entry("deals", "#"),
            Map.entry("support", "?"),
            Map.entry("knowledge", "i"),
            Map.entry("analytics", "A"),
            Map.entry("settings", "⚙"),
            Map.entry("default", "•")
    );

    private NavigationIconResolver() {
    }

    public static String glyphFor(String iconKey) {
        if (iconKey == null || iconKey.isBlank()) {
            return GLYPHS.get("default");
        }
        String key = iconKey.trim().toLowerCase(Locale.ROOT);
        return GLYPHS.getOrDefault(key, GLYPHS.get("default"));
    }
}
