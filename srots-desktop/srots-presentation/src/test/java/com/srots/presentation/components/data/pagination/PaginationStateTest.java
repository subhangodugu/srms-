package com.srots.presentation.components.data.pagination;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PaginationStateTest {

    @Test
    void navigatesWithinBounds() {
        PaginationState state = new PaginationState(0, 10, 35);
        assertEquals(4, state.getTotalPages());
        assertTrue(state.canNext());
        assertFalse(state.canPrevious());

        state.next();
        assertEquals(1, state.getPage());
        state.last();
        assertEquals(3, state.getPage());
        assertFalse(state.canNext());
        state.first();
        assertEquals(0, state.getPage());
    }

    @Test
    void clampsInvalidPage() {
        PaginationState state = new PaginationState(99, 10, 25);
        assertEquals(2, state.getPage());
    }
}
