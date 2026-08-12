package com.srots.presentation.components.data.filter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FilterStateTest {

    @Test
    void storesAndClearsFilters() {
        FilterState state = new FilterState();
        state.setSearchText("release");
        state.set("status", "Active");
        state.set("team", "Backend");

        assertEquals("release", state.getSearchText());
        assertEquals(2, state.getSelectedFilters().size());
        assertEquals("Active", state.getSelectedFilters().get("status"));

        state.clear();
        assertEquals("", state.getSearchText());
        assertTrue(state.getSelectedFilters().isEmpty());
    }

    @Test
    void copyIsIndependent() {
        FilterState original = new FilterState();
        original.set("priority", "High");
        FilterState copy = original.copy();
        copy.set("priority", "Low");
        assertEquals("High", original.getSelectedFilters().get("priority"));
        assertEquals("Low", copy.getSelectedFilters().get("priority"));
    }
}
