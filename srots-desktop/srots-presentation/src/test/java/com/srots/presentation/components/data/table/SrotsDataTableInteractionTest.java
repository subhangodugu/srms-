package com.srots.presentation.components.data.table;

import com.srots.presentation.components.support.JavaFxTestSupport;
import javafx.collections.FXCollections;
import javafx.scene.control.SelectionMode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("testfx")
class SrotsDataTableInteractionTest {

    @BeforeAll
    static void fx() throws Exception {
        JavaFxTestSupport.ensureToolkit();
    }

    @Test
    void densityAndSelectionModesApply() throws Exception {
        JavaFxTestSupport.runOnFxThread(() -> {
            SrotsDataTable<String> table = new SrotsDataTable<>();
            table.setItems(FXCollections.observableArrayList("a", "b"));
            table.setDensity(SrotsTableDensity.COMPACT);
            table.enableMultiSelect(true);
            assertTrue(table.getTableView().getStyleClass().contains("srots-density-compact"));
            assertEquals(SelectionMode.MULTIPLE, table.getTableView().getSelectionModel().getSelectionMode());
            assertEquals(2, table.getTableView().getItems().size());
        });
    }
}
