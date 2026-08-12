package com.srots.presentation.splash;

/**
 * Thin controller seam for splash presentation (FXML-compatible).
 * Does not run bootstrap or business logic.
 */
public final class SrotsSplashController {

    private SrotsSplashViewModel viewModel;

    public void setViewModel(SrotsSplashViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public SrotsSplashViewModel getViewModel() {
        return viewModel;
    }
}
