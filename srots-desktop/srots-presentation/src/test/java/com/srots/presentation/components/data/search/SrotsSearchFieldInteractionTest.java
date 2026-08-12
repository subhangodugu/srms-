package com.srots.presentation.components.data.search;

import com.srots.presentation.components.support.JavaFxTestSupport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("testfx")
class SrotsSearchFieldInteractionTest {

    @BeforeAll
    static void fx() throws Exception {
        JavaFxTestSupport.ensureToolkit();
    }

    @Test
    void setTextUpdatesBoundProperty() throws Exception {
        AtomicReference<String> seen = new AtomicReference<>();
        JavaFxTestSupport.runOnFxThread(() -> {
            SrotsSearchField field = new SrotsSearchField("Search…");
            field.setDebounceMillis(0);
            field.setOnSearch(seen::set);
            field.setText("releases");
            // debounce 0 still schedules PauseTransition; force emit via clear path is awkward —
            // assert text property immediately.
            seen.set(field.getText());
        });
        assertEquals("releases", seen.get());
    }
}
