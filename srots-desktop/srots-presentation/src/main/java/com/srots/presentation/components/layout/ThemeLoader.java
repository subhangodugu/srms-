package com.srots.presentation.components.layout;

import javafx.collections.ObservableList;
import javafx.scene.Scene;

/**
 * Loads SROTS design-system stylesheets in deterministic order.
 * Prefer this over relying solely on CSS {@code @import} for JavaFX reliability.
 */
public final class ThemeLoader {

    private static final String[] STYLESHEETS = {
            "/css/tokens.css",
            "/css/base.css",
            "/css/typography.css",
            "/css/layout.css",
            "/css/navigation.css",
            "/css/buttons.css",
            "/css/forms.css",
            "/css/cards.css",
            "/css/tables.css",
            "/css/tabs.css",
            "/css/dialogs.css",
            "/css/feedback.css",
            "/css/charts.css",
            "/css/states.css",
            "/css/components.css",
            "/css/dashboard.css",
            "/css/srots-search.css"
    };

    private ThemeLoader() {}

    public static void apply(Scene scene) {
        if (scene == null) {
            return;
        }
        ObservableList<String> sheets = scene.getStylesheets();
        sheets.clear();
        Class<?> anchor = ThemeLoader.class;
        for (String path : STYLESHEETS) {
            var url = anchor.getResource(path);
            if (url != null) {
                sheets.add(url.toExternalForm());
            }
        }
        // #region agent log
        try {
            boolean hasTokens = sheets.stream().anyMatch(s -> s.contains("tokens.css"));
            boolean hasNav = sheets.stream().anyMatch(s -> s.contains("navigation.css"));
            int tokensIdx = -1;
            int navIdx = -1;
            for (int i = 0; i < sheets.size(); i++) {
                if (sheets.get(i).contains("tokens.css")) {
                    tokensIdx = i;
                }
                if (sheets.get(i).contains("navigation.css")) {
                    navIdx = i;
                }
            }
            String radiusToken = "";
            var tokensUrl = anchor.getResource("/css/tokens.css");
            if (tokensUrl != null) {
                String body = new String(tokensUrl.openStream().readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
                int idx = body.indexOf("-srots-radius-sm:");
                if (idx >= 0) {
                    int end = body.indexOf(';', idx);
                    radiusToken = body.substring(idx, end > idx ? end : Math.min(idx + 40, body.length())).trim();
                }
            }
            String payload = "{\"sessionId\":\"dd362e\",\"runId\":\"post-fix\",\"hypothesisId\":\"B\","
                    + "\"location\":\"ThemeLoader.apply\",\"message\":\"stylesheets-applied\","
                    + "\"data\":{\"count\":" + sheets.size()
                    + ",\"hasTokens\":" + hasTokens
                    + ",\"hasNav\":" + hasNav
                    + ",\"tokensIdx\":" + tokensIdx
                    + ",\"navIdx\":" + navIdx
                    + ",\"radiusToken\":\"" + radiusToken.replace("\"", "\\\"")
                    + "\",\"resource\":\"" + String.valueOf(tokensUrl).replace("\\", "\\\\").replace("\"", "\\\"")
                    + "\"},\"timestamp\":" + System.currentTimeMillis() + "}\n";
            java.nio.file.Files.writeString(
                    java.nio.file.Path.of("c:/srms/debug-dd362e.log"),
                    payload,
                    java.nio.file.StandardOpenOption.CREATE,
                    java.nio.file.StandardOpenOption.APPEND);
        } catch (Exception ignored) {
        }
        // #endregion
    }

    /** Master theme file (imports all). Useful when a single stylesheet entry is preferred. */
    public static void applyThemeBundle(Scene scene) {
        if (scene == null) {
            return;
        }
        var theme = ThemeLoader.class.getResource("/css/theme.css");
        if (theme != null) {
            scene.getStylesheets().setAll(theme.toExternalForm());
        }
    }
}
