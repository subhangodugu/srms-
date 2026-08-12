package com.srots.presentation.components.navigation.tabs;

import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

/**
 * Styled tab host for in-page sections.
 */
public class SrotsTabView extends TabPane {

    public SrotsTabView() {
        getStyleClass().add("srots-tabs");
        setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
    }

    public Tab addTab(String title, Node content) {
        Tab tab = new Tab(title == null ? "" : title, content);
        tab.setClosable(false);
        getTabs().add(tab);
        return tab;
    }
}
