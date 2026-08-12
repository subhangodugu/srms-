package com.srots.presentation.components.overlays.command;

import com.srots.presentation.components.support.JavaFxTestSupport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Tag("testfx")
class SrotsCommandPaletteInteractionTest {

    @BeforeAll
    static void fx() throws Exception {
        JavaFxTestSupport.ensureToolkit();
    }

    @Test
    void paletteConstructsWithCommands() throws Exception {
        AtomicBoolean ran = new AtomicBoolean(false);
        JavaFxTestSupport.runOnFxThread(() -> {
            SrotsCommandPalette palette = new SrotsCommandPalette(null, List.of(
                    new SrotsCommandPalette.CommandItem("go-employees", "Go to Employees", () -> ran.set(true))
            ));
            assertNotNull(palette);
        });
    }
}
