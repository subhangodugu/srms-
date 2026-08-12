package com.srots.presentation.profile;

import com.srots.presentation.components.information.avatar.SrotsAvatar;
import com.srots.presentation.components.utility.SrotsSize;
import javafx.collections.ListChangeListener;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Screen;
import javafx.stage.Window;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Lightweight profile popup (not a Stage). Hosted next to the TopBar profile control.
 */
public final class SrotsUserProfileMenu {

    public static final double MENU_WIDTH = 300;

    private final Popup popup = new Popup();
    private final VBox root = new VBox();
    private final VBox header = new VBox(4);
    private final SrotsAvatar avatar = new SrotsAvatar("", SrotsSize.LARGE);
    private final Label nameLabel = new Label();
    private final Label roleLabel = new Label();
    private final Label emailLabel = new Label();
    private final VBox itemsHost = new VBox(2);

    private SrotsUserProfileViewModel viewModel;
    private Consumer<String> actionHandler = id -> {
    };
    private Runnable onHidden = () -> {
    };
    private ListChangeListener<SrotsUserProfileAction> actionsListener;
    private final List<Node> focusableItems = new ArrayList<>();

    public SrotsUserProfileMenu() {
        popup.setAutoHide(true);
        popup.setAutoFix(true);
        popup.setHideOnEscape(true);

        root.getStyleClass().add("srots-user-profile-menu");
        root.setPrefWidth(MENU_WIDTH);
        root.setMaxWidth(MENU_WIDTH);
        root.setPadding(new Insets(12));
        root.setSpacing(8);
        root.setFocusTraversable(true);

        avatar.getStyleClass().add("srots-user-avatar");
        nameLabel.getStyleClass().addAll("srots-state-title", "srots-user-name");
        roleLabel.getStyleClass().addAll("srots-caption", "srots-user-role");
        emailLabel.getStyleClass().addAll("srots-caption", "srots-user-email");

        header.getStyleClass().add("srots-user-profile-header");
        header.getChildren().addAll(avatar, nameLabel, roleLabel, emailLabel);

        itemsHost.getStyleClass().add("srots-user-menu-section");

        root.getChildren().addAll(header, divider(), itemsHost);
        popup.getContent().add(root);

        popup.setOnShown(e -> {
            root.requestFocus();
            if (!focusableItems.isEmpty()) {
                focusableItems.get(0).requestFocus();
            }
        });
        popup.setOnHidden(e -> onHidden.run());

        root.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                hide();
                e.consume();
            } else if (e.getCode() == KeyCode.TAB) {
                // allow default traversal within popup content
            }
        });
    }

    public void bind(SrotsUserProfileViewModel viewModel, Consumer<String> actionHandler, Runnable onHidden) {
        unbind();
        this.viewModel = Objects.requireNonNull(viewModel, "viewModel");
        this.actionHandler = actionHandler == null ? id -> {
        } : actionHandler;
        this.onHidden = onHidden == null ? () -> {
        } : onHidden;

        actionsListener = change -> renderActions();
        viewModel.getActions().addListener(actionsListener);
        viewModel.currentUserProperty().addListener((obs, o, n) -> applyUser(n));
        applyUser(viewModel.getCurrentUser());
        renderActions();
    }

    public void unbind() {
        if (viewModel != null && actionsListener != null) {
            viewModel.getActions().removeListener(actionsListener);
        }
        actionsListener = null;
        viewModel = null;
    }

    public boolean isShowing() {
        return popup.isShowing();
    }

    public void show(Node anchor) {
        if (anchor == null || anchor.getScene() == null || anchor.getScene().getWindow() == null) {
            return;
        }
        if (viewModel != null) {
            applyUser(viewModel.getCurrentUser());
            renderActions();
        }
        Window window = anchor.getScene().getWindow();
        Bounds bounds = anchor.localToScreen(anchor.getBoundsInLocal());
        if (bounds == null) {
            return;
        }

        root.applyCss();
        root.layout();
        double menuHeight = Math.max(root.prefHeight(MENU_WIDTH), 220);
        double x = bounds.getMaxX() - MENU_WIDTH;
        double y = bounds.getMaxY() + 4;

        Rectangle2D visual = resolveVisualBounds(bounds);
        if (x < visual.getMinX() + 8) {
            x = visual.getMinX() + 8;
        }
        if (x + MENU_WIDTH > visual.getMaxX() - 8) {
            x = visual.getMaxX() - MENU_WIDTH - 8;
        }
        if (y + menuHeight > visual.getMaxY() - 8) {
            y = bounds.getMinY() - menuHeight - 4;
        }
        if (y < visual.getMinY() + 8) {
            y = visual.getMinY() + 8;
        }

        // Keep within owning window when possible (multi-monitor safe via screen visual bounds above).
        double windowMaxX = window.getX() + window.getWidth() - 8;
        double windowMinX = window.getX() + 8;
        if (x + MENU_WIDTH > windowMaxX) {
            x = windowMaxX - MENU_WIDTH;
        }
        if (x < windowMinX) {
            x = windowMinX;
        }

        popup.show(window, x, y);
    }

    public void hide() {
        if (popup.isShowing()) {
            popup.hide();
        }
    }

    public Popup getPopup() {
        return popup;
    }

    public VBox getRoot() {
        return root;
    }

    private void applyUser(CurrentUser user) {
        CurrentUser safe = user == null ? CurrentUser.fallback() : user;
        avatar.setInitialsFrom(safe.displayName());
        nameLabel.setText(safe.displayName());
        roleLabel.setText(safe.role());
        roleLabel.setVisible(safe.hasRole());
        roleLabel.setManaged(safe.hasRole());
        emailLabel.setText(safe.email());
        emailLabel.setVisible(safe.hasEmail());
        emailLabel.setManaged(safe.hasEmail());
    }

    private void renderActions() {
        itemsHost.getChildren().clear();
        focusableItems.clear();
        if (viewModel == null) {
            return;
        }

        List<SrotsUserProfileAction> all = new ArrayList<>(viewModel.getActions());
        List<SrotsUserProfileAction> primary = new ArrayList<>();
        List<SrotsUserProfileAction> secondary = new ArrayList<>();
        List<SrotsUserProfileAction> auth = new ArrayList<>();

        for (SrotsUserProfileAction action : all) {
            if (!action.isVisible()) {
                continue;
            }
            switch (action.getType()) {
                case AUTHENTICATION -> auth.add(action);
                case DIALOG -> secondary.add(action);
                default -> primary.add(action);
            }
        }

        addSection(primary);
        if (!secondary.isEmpty()) {
            itemsHost.getChildren().add(divider());
            addSection(secondary);
        }
        if (!auth.isEmpty()) {
            itemsHost.getChildren().add(divider());
            addSection(auth);
        }
    }

    private void addSection(List<SrotsUserProfileAction> section) {
        for (SrotsUserProfileAction action : section) {
            itemsHost.getChildren().add(createItem(action));
        }
    }

    private HBox createItem(SrotsUserProfileAction action) {
        Label icon = new Label(action.getIconGlyph());
        icon.getStyleClass().add("srots-user-menu-icon");

        Label label = new Label(action.getLabel());
        label.getStyleClass().add("srots-user-menu-label");
        HBox.setHgrow(label, Priority.ALWAYS);

        HBox row = new HBox(10, icon, label);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(8, 10, 8, 10));
        row.setPrefHeight(40);
        row.getStyleClass().add("srots-user-menu-item");
        if (UserProfileActionCatalog.ACTION_SIGN_OUT.equals(action.getId())) {
            row.getStyleClass().add("srots-user-menu-signout");
            if (viewModel != null && viewModel.getSessionState() == SessionState.SIGNING_OUT) {
                label.setText("Signing out...");
            }
        }
        row.setDisable(!action.isEnabled());
        row.setFocusTraversable(true);
        row.setAccessibleText(action.getLabel());
        row.setOnMouseClicked(e -> {
            if (action.isEnabled()) {
                actionHandler.accept(action.getId());
            }
        });
        row.setOnKeyPressed(e -> {
            if ((e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.SPACE) && action.isEnabled()) {
                actionHandler.accept(action.getId());
                e.consume();
            }
        });
        focusableItems.add(row);
        return row;
    }

    private static Separator divider() {
        Separator separator = new Separator();
        separator.getStyleClass().add("srots-user-menu-divider");
        return separator;
    }

    private static Rectangle2D resolveVisualBounds(Bounds bounds) {
        List<Screen> screens = Screen.getScreensForRectangle(
                bounds.getMinX(), bounds.getMinY(), Math.max(bounds.getWidth(), 1), Math.max(bounds.getHeight(), 1));
        if (screens == null || screens.isEmpty()) {
            return Screen.getPrimary().getVisualBounds();
        }
        return screens.get(0).getVisualBounds();
    }
}
