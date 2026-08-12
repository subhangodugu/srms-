package com.srots.presentation.components.actions.button;

import com.srots.presentation.components.support.JavaFxTestSupport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("testfx")
class SrotsButtonInteractionTest {

    @BeforeAll
    static void fx() throws Exception {
        JavaFxTestSupport.ensureToolkit();
    }

    @Test
    void primaryVariantAndAccessibleText() throws Exception {
        SrotsButton button = JavaFxTestSupport.onFxThread(() -> SrotsButton.primary("Create Release"));
        assertEquals(SrotsButtonVariant.PRIMARY, button.getVariant());
        assertEquals("Create Release", button.getText());
        assertEquals("Create Release", button.getAccessibleText());
    }

    @Test
    void loadingDisablesButton() throws Exception {
        SrotsButton button = JavaFxTestSupport.onFxThread(() -> {
            SrotsButton b = SrotsButton.primary("Save");
            b.setLoading(true);
            return b;
        });
        assertTrue(button.isLoading());
        assertTrue(button.isDisable());
    }

    @Test
    void actionCallbackFires() throws Exception {
        AtomicBoolean clicked = new AtomicBoolean(false);
        JavaFxTestSupport.runOnFxThread(() -> {
            SrotsButton button = SrotsButton.secondary("Cancel");
            button.setOnAction(e -> clicked.set(true));
            button.fire();
        });
        assertTrue(clicked.get());
    }
}
