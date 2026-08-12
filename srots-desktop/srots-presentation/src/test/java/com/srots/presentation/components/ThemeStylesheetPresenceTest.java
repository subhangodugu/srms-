package com.srots.presentation.components;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ThemeStylesheetPresenceTest {

    @Test
    void designSystemStylesheetsAreOnClasspath() {
        Class<?> anchor = com.srots.presentation.components.layout.ThemeLoader.class;
        assertNotNull(anchor.getResource("/css/tokens.css"));
        assertNotNull(anchor.getResource("/css/buttons.css"));
        assertNotNull(anchor.getResource("/css/tables.css"));
        assertNotNull(anchor.getResource("/css/theme.css"));
        assertNotNull(anchor.getResource("/css/srots-search.css"));
    }
}
