package com.srots.presentation.navigation.state;

import com.srots.presentation.navigation.model.NavigationRouteId;
import com.srots.presentation.navigation.model.RouteParameters;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NavigationHistoryTest {

    @Test
    void backAndForward_restoreRouteParameters() {
        NavigationHistory history = new NavigationHistory();
        history.push(NavigationRouteId.OVERVIEW, RouteParameters.empty());
        history.push(NavigationRouteId.PROJECTS, RouteParameters.empty());
        history.push(NavigationRouteId.PROJECT_DETAILS, RouteParameters.of("projectId", "123"));

        assertTrue(history.canGoBack());
        assertFalse(history.canGoForward());

        NavigationHistoryEntry current = new NavigationHistoryEntry(
                NavigationRouteId.RELEASE_DETAILS,
                RouteParameters.of("releaseId", "9"),
                System.currentTimeMillis());

        NavigationHistoryEntry previous = history.popBack(current);
        assertEquals(NavigationRouteId.PROJECT_DETAILS, previous.route());
        assertEquals("123", previous.parameters().get("projectId"));
        assertTrue(history.canGoForward());

        NavigationHistoryEntry next = history.popForward(previous);
        assertEquals(NavigationRouteId.RELEASE_DETAILS, next.route());
        assertEquals("9", next.parameters().get("releaseId"));
    }
}
