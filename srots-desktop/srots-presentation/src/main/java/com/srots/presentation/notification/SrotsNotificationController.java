package com.srots.presentation.notification;

import com.srots.presentation.profile.SrotsPopupManager;
import javafx.beans.value.ChangeListener;
import javafx.css.PseudoClass;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Window;

import java.util.Objects;

/**
 * Binds TopBar notification button to panel popup lifecycle.
 */
public final class SrotsNotificationController {

    private static final PseudoClass OPEN = PseudoClass.getPseudoClass("open");

    private final Node anchor;
    private final Button notificationsButton;
    private final SrotsNotificationPanel panel = new SrotsNotificationPanel();
    private final SrotsNotificationPanelViewModel viewModel;
    private final SrotsPopupManager popupManager;

    private ChangeListener<Number> geometryListener;
    private ChangeListener<Boolean> openListener;
    private Window boundWindow;
    private boolean suppressToggle;

    public SrotsNotificationController(
            Node anchor,
            Button notificationsButton,
            SrotsNotificationPanelViewModel viewModel,
            SrotsPopupManager popupManager) {
        this.anchor = Objects.requireNonNull(anchor, "anchor");
        this.notificationsButton = Objects.requireNonNull(notificationsButton, "notificationsButton");
        this.viewModel = Objects.requireNonNull(viewModel, "viewModel");
        this.popupManager = popupManager == null ? new SrotsPopupManager() : popupManager;
    }

    public void attach() {
        detach();
        panel.bind(viewModel, this::onActivate, this::close, this::onPanelHidden);

        openListener = (obs, wasOpen, open) -> {
            notificationsButton.pseudoClassStateChanged(OPEN, Boolean.TRUE.equals(open));
            if (Boolean.TRUE.equals(open)) {
                showPanel();
            } else if (panel.isShowing()) {
                suppressToggle = true;
                try {
                    panel.hide();
                } finally {
                    suppressToggle = false;
                }
            }
        };
        viewModel.panelOpenProperty().addListener(openListener);
    }

    public void detach() {
        if (openListener != null) {
            viewModel.panelOpenProperty().removeListener(openListener);
            openListener = null;
        }
        unbindWindow();
        panel.unbind();
        panel.hide();
        notificationsButton.pseudoClassStateChanged(OPEN, false);
    }

    public void toggle() {
        if (suppressToggle) {
            return;
        }
        if (viewModel.isPanelOpen()) {
            close();
        } else {
            open();
        }
    }

    public void open() {
        popupManager.requestOpen(SrotsPopupManager.PopupKind.NOTIFICATIONS, this::close);
        viewModel.openPanel();
    }

    public void close() {
        viewModel.closePanel();
        if (panel.isShowing()) {
            panel.hide();
        }
        popupManager.notifyClosed(SrotsPopupManager.PopupKind.NOTIFICATIONS);
        notificationsButton.requestFocus();
    }

    public SrotsNotificationPanel getPanel() {
        return panel;
    }

    public SrotsNotificationPanelViewModel getViewModel() {
        return viewModel;
    }

    private void showPanel() {
        Scene scene = anchor.getScene();
        if (scene == null) {
            viewModel.closePanel();
            return;
        }
        bindWindow(scene.getWindow());
        panel.show(anchor);
        if (!panel.isShowing()) {
            viewModel.closePanel();
            popupManager.notifyClosed(SrotsPopupManager.PopupKind.NOTIFICATIONS);
        }
    }

    private void onPanelHidden() {
        if (!suppressToggle) {
            viewModel.closePanel();
            popupManager.notifyClosed(SrotsPopupManager.PopupKind.NOTIFICATIONS);
            notificationsButton.requestFocus();
        }
    }

    private void onActivate(SrotsNotification notification) {
        viewModel.activate(notification);
        close();
    }

    private void bindWindow(Window window) {
        unbindWindow();
        boundWindow = window;
        if (window == null) {
            return;
        }
        geometryListener = (obs, o, n) -> viewModel.closePanel();
        window.widthProperty().addListener(geometryListener);
        window.heightProperty().addListener(geometryListener);
        window.xProperty().addListener(geometryListener);
        window.yProperty().addListener(geometryListener);
        if (window instanceof javafx.stage.Stage stage) {
            stage.iconifiedProperty().addListener((obs, o, iconified) -> {
                if (Boolean.TRUE.equals(iconified)) {
                    close();
                }
            });
        }
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
