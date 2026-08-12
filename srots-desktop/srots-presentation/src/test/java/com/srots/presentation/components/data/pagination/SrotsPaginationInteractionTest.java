package com.srots.presentation.components.data.pagination;

import com.srots.presentation.components.support.JavaFxTestSupport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("testfx")
class SrotsPaginationInteractionTest {

    @BeforeAll
    static void fx() throws Exception {
        JavaFxTestSupport.ensureToolkit();
    }

    @Test
    void pageChangeCallbackEmitsNextPage() throws Exception {
        AtomicInteger page = new AtomicInteger(-1);
        JavaFxTestSupport.runOnFxThread(() -> {
            SrotsPagination pagination = new SrotsPagination();
            pagination.bind(new PaginationState(0, 10, 40));
            pagination.setOnPageChange(page::set);
            pagination.getChildren().stream()
                    .filter(n -> n instanceof javafx.scene.control.Button b && "Next".equals(b.getText()))
                    .map(n -> (javafx.scene.control.Button) n)
                    .findFirst()
                    .orElseThrow()
                    .fire();
        });
        assertEquals(1, page.get());
    }
}
