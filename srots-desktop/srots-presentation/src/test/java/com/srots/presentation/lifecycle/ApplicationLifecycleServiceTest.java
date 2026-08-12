package com.srots.presentation.lifecycle;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ApplicationLifecycleServiceTest {

    @Test
    void confirmClose_allowsWhenNoUnsavedWork() {
        ApplicationLifecycleService service = new ApplicationLifecycleService();
        service.setUnsavedWorkDetector(() -> false);
        assertEquals(ApplicationLifecycleService.CloseDecision.ALLOW, service.confirmClose(null));
    }
}
