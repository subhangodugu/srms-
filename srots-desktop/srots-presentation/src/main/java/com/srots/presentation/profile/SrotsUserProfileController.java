package com.srots.presentation.profile;

import com.srots.presentation.components.information.avatar.SrotsUserProfile;
import com.srots.presentation.components.information.avatar.UserInitials;
import javafx.beans.value.ChangeListener;
import javafx.scene.Scene;
import javafx.stage.Window;

import java.util.Objects;

/**
 * Binds profile button + popup lifecycle. No authentication logic.
 */
public final class SrotsUserProfileController {

    private final SrotsUserProfile profileButton;
    private final SrotsUserProfileMenu menu = new SrotsUserProfileMenu();
    private final SrotsUserProfileViewModel viewModel;
    private final SrotsPopupManager popupManager;

    private ChangeListener<Number> widthListener;
    private ChangeListener<Number> heightListener;
    private ChangeListener<Boolean> menuOpenListener;
    private Window boundWindow;
    private boolean suppressToggle;

    public SrotsUserProfileController(
            SrotsUserProfile profileButton,
            SrotsUserProfileViewModel viewModel,
            SrotsPopupManager popupManager) {
        this.profileButton = Objects.requireNonNull(profileButton, "profileButton");
        this.viewModel = Objects.requireNonNull(viewModel, "viewModel");
        this.popupManager = popupManager == null ? new SrotsPopupManager() : popupManager;
    }

    public void attach() {
        detach();
        profileButton.setOnClick(this::toggle);
        menu.bind(viewModel, this::onAction, this::onMenuHidden);

        menuOpenListener = (obs, wasOpen, open) -> {
            profileButton.setMenuOpen(Boolean.TRUE.equals(open));
            if (Boolean.TRUE.equals(open)) {
                showMenu();
            } else if (menu.isShowing()) {
                suppressToggle = true;
                try {
                    menu.hide();
                } finally {
                    suppressToggle = false;
                }
            }
        };
        viewModel.menuOpenProperty().addListener(menuOpenListener);

        viewModel.currentUserProperty().addListener((obs, o, n) -> applyUser(n));
        viewModel.profileVisibleProperty().addListener((obs, o, visible) -> {
            profileButton.setVisible(Boolean.TRUE.equals(visible));
            profileButton.setManaged(Boolean.TRUE.equals(visible));
            if (!Boolean.TRUE.equals(visible)) {
                viewModel.closeMenu();
            }
        });
        applyUser(viewModel.getCurrentUser());
        profileButton.setVisible(viewModel.isProfileVisible());
        profileButton.setManaged(viewModel.isProfileVisible());
    }

    public void detach() {
        if (menuOpenListener != null) {
            viewModel.menuOpenProperty().removeListener(menuOpenListener);
            menuOpenListener = null;
        }
        unbindWindow();
        menu.unbind();
        menu.hide();
        profileButton.setOnClick(null);
        profileButton.setMenuOpen(false);
    }

    public void toggle() {
        if (suppressToggle) {
            return;
        }
        if (viewModel.isMenuOpen()) {
            close();
        } else {
            open();
        }
    }

    public void open() {
        popupManager.requestOpen(SrotsPopupManager.PopupKind.PROFILE, this::close);
        viewModel.openMenu();
    }

    public void close() {
        viewModel.closeMenu();
        if (menu.isShowing()) {
            menu.hide();
        }
        popupManager.notifyClosed(SrotsPopupManager.PopupKind.PROFILE);
        profileButton.requestFocus();
    }

    public SrotsUserProfileMenu getMenu() {
        return menu;
    }

    public SrotsUserProfileViewModel getViewModel() {
        return viewModel;
    }

    private void showMenu() {
        Scene scene = profileButton.getScene();
        if (scene == null) {
            viewModel.closeMenu();
            return;
        }
        bindWindow(scene.getWindow());
        menu.show(profileButton);
        if (!menu.isShowing()) {
            viewModel.closeMenu();
            popupManager.notifyClosed(SrotsPopupManager.PopupKind.PROFILE);
        }
    }

    private void onMenuHidden() {
        if (!suppressToggle) {
            viewModel.closeMenu();
            popupManager.notifyClosed(SrotsPopupManager.PopupKind.PROFILE);
            profileButton.requestFocus();
        }
    }

    private void onAction(String actionId) {
        viewModel.executeAction(actionId);
        close();
    }

    private void applyUser(CurrentUser user) {
        CurrentUser safe = user == null ? CurrentUser.fallback() : user;
        profileButton.setName(safe.displayName());
        profileButton.setRole(safe.role());
        String initials = UserInitials.fromDisplayName(safe.displayName());
        profileButton.getAvatar().setInitials(initials.isBlank() ? "?" : initials);
    }

    private void bindWindow(Window window) {
        unbindWindow();
        boundWindow = window;
        if (window == null) {
            return;
        }
        widthListener = (obs, o, n) -> viewModel.closeMenu();
        heightListener = (obs, o, n) -> viewModel.closeMenu();
        window.widthProperty().addListener(widthListener);
        window.heightProperty().addListener(heightListener);
        window.xProperty().addListener(widthListener);
        window.yProperty().addListener(heightListener);
    }

    private void unbindWindow() {
        if (boundWindow != null && widthListener != null) {
            boundWindow.widthProperty().removeListener(widthListener);
            boundWindow.heightProperty().removeListener(heightListener);
            boundWindow.xProperty().removeListener(widthListener);
            boundWindow.yProperty().removeListener(heightListener);
        }
        boundWindow = null;
        widthListener = null;
        heightListener = null;
    }
}
