package com.srots.presentation.navigation.guard;

import com.srots.presentation.navigation.model.NavigationContext;

/**
 * Decides whether a navigation transition may proceed.
 */
public interface NavigationGuard {

    GuardDecision evaluate(NavigationContext from, NavigationContext to);
}
