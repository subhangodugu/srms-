package com.srots.presentation.components.information.badge;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class SrotsStatusTest {

    @Test
    void statusProvidesIconAndLabel() {
        assertEquals("Active", SrotsStatus.ACTIVE.getLabel());
        assertFalse(SrotsStatus.FAILED.getIcon().isBlank());
        assertEquals("Production", SrotsStatus.PRODUCTION.getLabel());
    }
}
