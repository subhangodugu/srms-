package com.srots.presentation.components.feedback.notification;

import com.srots.presentation.components.support.JavaFxTestSupport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("testfx")
class SrotsNotificationCenterInteractionTest {

    @BeforeAll
    static void fx() throws Exception {
        JavaFxTestSupport.ensureToolkit();
    }

    @Test
    void acceptsNotificationItems() throws Exception {
        JavaFxTestSupport.runOnFxThread(() -> {
            SrotsNotificationCenter center = new SrotsNotificationCenter();
            center.setItems(List.of(
                    new SrotsNotificationItem("Release created", "COMPTY v1.9.0", false, "2m ago", SrotsNotificationItem.Variant.SUCCESS)
            ));
            assertEquals(1, center.getItems().size());
        });
    }
}
