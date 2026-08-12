package com.srots.presentation.search;

import com.srots.presentation.profile.SrotsPopupManager;
import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Window;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Binds TopBar search entry to the global search overlay lifecycle.
 */
public final class SrotsGlobalSearchController {

    private final Supplier<Node> focusReturnSupplier;
    private final SrotsGlobalSearchViewModel viewModel;
    private final SrotsPopupManager popupManager;
    private final SrotsSearchOverlay overlay = new SrotsSearchOverlay();

    private ChangeListener<Boolean> openListener;
    private ChangeListener<Number> geometryListener;
    private Window boundWindow;
    private boolean suppressToggle;

    public SrotsGlobalSearchController(
            SrotsGlobalSearchViewModel viewModel,
            SrotsPopupManager popupManager,
            Supplier<Node> focusReturnSupplier) {
        this.viewModel = Objects.requireNonNull(viewModel, "viewModel");
        this.popupManager = popupManager == null ? new SrotsPopupManager() : popupManager;
        this.focusReturnSupplier = focusReturnSupplier == null ? () -> null : focusReturnSupplier;
    }

    public void attach() {
        detach();
        overlay.bind(viewModel, this::close, this::onHidden);
        openListener = (obs, wasOpen, open) -> {
            if (Boolean.TRUE.equals(open)) {
                show();
            } else if (overlay.isShowing()) {
                suppressToggle = true;
                try {
                    overlay.hide();
                } finally {
                    suppressToggle = false;
                }
            }
        };
        viewModel.openProperty().addListener(openListener);
    }

    public void detach() {
        if (openListener != null) {
            viewModel.openProperty().removeListener(openListener);
            openListener = null;
        }
        unbindWindow();
        overlay.unbind();
        overlay.hide();
    }

    public void toggle() {
        if (suppressToggle) {
            return;
        }
        if (viewModel.isOpen()) {
            close();
        } else {
            open();
        }
    }

    public void open() {
        popupManager.requestOpen(SrotsPopupManager.PopupKind.GLOBAL_SEARCH, this::close);
        viewModel.open();
    }

    public void close() {
        viewModel.close();
        if (overlay.isShowing()) {
            overlay.hide();
        }
        popupManager.notifyClosed(SrotsPopupManager.PopupKind.GLOBAL_SEARCH);
        Node focus = focusReturnSupplier.get();
        if (focus != null) {
            focus.requestFocus();
        }
    }

    public SrotsSearchOverlay getOverlay() {
        return overlay;
    }

    public SrotsGlobalSearchViewModel getViewModel() {
        return viewModel;
    }

    private void show() {
        Node anchor = focusReturnSupplier.get();
        Scene scene = anchor == null ? null : anchor.getScene();
        if (scene == null || scene.getWindow() == null) {
            // Keep ViewModel open; overlay shows once a window is available.
            return;
        }
        bindWindow(scene.getWindow());
        overlay.show(scene.getWindow());
    }

    private void onHidden() {
        if (!suppressToggle) {
            viewModel.close();
            popupManager.notifyClosed(SrotsPopupManager.PopupKind.GLOBAL_SEARCH);
            Node focus = focusReturnSupplier.get();
            if (focus != null) {
                focus.requestFocus();
            }
        }
    }

    private void bindWindow(Window window) {
        unbindWindow();
        boundWindow = window;
        if (window == null) {
            return;
        }
        geometryListener = (obs, o, n) -> {
            if (overlay.isShowing()) {
                overlay.show(window);
            }
        };
        window.widthProperty().addListener(geometryListener);
        window.heightProperty().addListener(geometryListener);
        window.xProperty().addListener(geometryListener);
        window.yProperty().addListener(geometryListener);
    }

    private void unbindWindow() {
        if (boundWindow != null && geometryListener != null) {
            boundWindow.widthProperty().removeListener(geometryListener);
            boundWindow.heightProperty().removeListener(geometryListener);
            boundWindow.xProperty().removeListener(geometryListener);
            boundWindow.yProperty().removeListener(geometryListener);
        }
        boundWindow = null;
        geometryListener = null;
    }
}
