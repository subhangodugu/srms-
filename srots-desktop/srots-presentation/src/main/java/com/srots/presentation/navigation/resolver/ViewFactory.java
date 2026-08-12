package com.srots.presentation.navigation.resolver;

import com.srots.presentation.navigation.model.NavigationContext;
import javafx.scene.Node;

/**
 * Creates the JavaFX node for a navigation destination.
 */
public interface ViewFactory {

    Node create(NavigationContext context);
}
