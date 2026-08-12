package com.srots.presentation.search;

/**
 * Public façade for the Global Search system.
 */
public final class SrotsGlobalSearch {

    private final SrotsGlobalSearchViewModel viewModel;
    private final SrotsGlobalSearchController controller;

    public SrotsGlobalSearch(SrotsGlobalSearchViewModel viewModel, SrotsGlobalSearchController controller) {
        this.viewModel = viewModel;
        this.controller = controller;
    }

    public SrotsGlobalSearchViewModel getViewModel() {
        return viewModel;
    }

    public SrotsGlobalSearchController getController() {
        return controller;
    }

    public void open() {
        controller.open();
    }

    public void close() {
        controller.close();
    }

    public void toggle() {
        controller.toggle();
    }

    public boolean isOpen() {
        return viewModel.isOpen();
    }
}
