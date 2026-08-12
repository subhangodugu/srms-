package com.srots.presentation.components.navigation.topbar;

import com.srots.presentation.components.data.search.SrotsSearchField;
import com.srots.presentation.components.information.avatar.SrotsUserProfile;
import com.srots.presentation.components.navigation.breadcrumb.SrotsBreadcrumb;
import com.srots.presentation.components.utility.icons.SrotsIcon;
import com.srots.presentation.shell.topbar.SrotsTopBarAction;
import com.srots.presentation.shell.topbar.SrotsTopBarViewModel;
import com.srots.presentation.shell.topbar.TopBarNotificationBadgeFormatter;
import com.srots.presentation.shell.topbar.TopBarUserInfo;
import com.srots.presentation.window.SrotsWindowControls;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Persistent application top bar: context, search, notifications, connection, profile, window controls.
 * Presentation only — no DB / REST / auth implementation.
 */
public class SrotsTopBar extends HBox {

    private static final double COMPACT_BREAKPOINT = 860;
    private static final double MID_BREAKPOINT = 1180;

    private final ObjectProperty<SrotsTopBarViewModel> viewModel = new SimpleObjectProperty<>();
    private final ObjectProperty<Runnable> onNotifications = new SimpleObjectProperty<>();
    private final ObjectProperty<Runnable> onProfile = new SimpleObjectProperty<>();
    private final ObjectProperty<Runnable> onCommandPalette = new SimpleObjectProperty<>();

    private final Label brandLabel = new Label("SROTS");
    private final Region brandDivider = new Region();
    private final Label pageTitleLabel = new Label();
    private final SrotsBreadcrumb breadcrumb = new SrotsBreadcrumb();
    private final VBox contextBlock = new VBox(2);
    private final HBox leftCluster = new HBox(12);
    private final SrotsSearchField searchField = new SrotsSearchField("Search SROTS...");
    private final Button compactSearchButton = new Button(SrotsIcon.SEARCH.getGlyph());
    private final HBox actionsHost = new HBox(6);
    private final StackPane notificationSlot = new StackPane();
    private final Button notificationsButton = new Button(SrotsIcon.BELL.getGlyph());
    private final Label notificationBadge = new Label();
    private final SrotsConnectionIndicator connectionIndicator = new SrotsConnectionIndicator();
    private final SrotsUserProfile userProfile = new SrotsUserProfile("User", "");
    private final Label envLabel = new Label();
    private final SrotsWindowControls windowControls = new SrotsWindowControls();
    private final Region spacer = new Region();
    private final HBox rightCluster = new HBox(8);

    private ChangeListener<Number> widthListener;
    private ListChangeListener<SrotsBreadcrumb.Crumb> breadcrumbListener;
    private ListChangeListener<SrotsTopBarAction> actionsListener;
    private InvalidationListener titleListener;
    private InvalidationListener userListener;
    private InvalidationListener connectionListener;
    private InvalidationListener notificationListener;
    private InvalidationListener compactListener;
    private boolean midCompact;

    public SrotsTopBar() {
        getStyleClass().add("srots-topbar");
        setAlignment(Pos.CENTER_LEFT);
        setSpacing(12);
        setPadding(new Insets(0, 14, 0, 14));
        setMinHeight(60);
        setPrefHeight(60);
        setMaxHeight(60);
        setAccessibleText("Application toolbar");

        brandLabel.getStyleClass().addAll("srots-brand", "srots-topbar-brand");

        brandDivider.getStyleClass().add("srots-topbar-divider");
        brandDivider.setMinWidth(1);
        brandDivider.setPrefWidth(1);
        brandDivider.setMaxWidth(1);
        brandDivider.setMinHeight(22);
        brandDivider.setPrefHeight(22);
        brandDivider.setMaxHeight(22);

        pageTitleLabel.getStyleClass().add("srots-topbar-title");
        breadcrumb.getStyleClass().add("srots-topbar-breadcrumb");
        contextBlock.getStyleClass().add("srots-topbar-context");
        contextBlock.getChildren().addAll(pageTitleLabel, breadcrumb);
        contextBlock.setAlignment(Pos.CENTER_LEFT);
        contextBlock.setSpacing(1);

        leftCluster.getStyleClass().add("srots-topbar-left");
        leftCluster.setAlignment(Pos.CENTER_LEFT);
        leftCluster.getChildren().addAll(brandLabel, brandDivider, contextBlock);

        searchField.getStyleClass().add("srots-topbar-search");
        searchField.setTopBarChromeEnabled(true);
        searchField.getField().setAccessibleText("Global Search");
        searchField.getField().setPromptText("Search SROTS...");
        searchField.setPrefWidth(340);
        searchField.setMaxWidth(380);
        searchField.setMinHeight(38);
        searchField.setPrefHeight(38);
        searchField.setMaxHeight(40);
        searchField.getField().setOnMouseClicked(e -> openGlobalSearchEntry());
        searchField.getField().setOnKeyPressed(e -> {
            if (e.getCode() == javafx.scene.input.KeyCode.ENTER
                    || e.getCode() == javafx.scene.input.KeyCode.DOWN) {
                openGlobalSearchEntry();
                e.consume();
            }
        });

        compactSearchButton.getStyleClass().addAll(
                "srots-icon-button", "srots-topbar-action", "srots-topbar-compact-search");
        compactSearchButton.setTooltip(new Tooltip("Search SROTS (Ctrl+K)"));
        compactSearchButton.setAccessibleText("Global Search");
        compactSearchButton.setVisible(false);
        compactSearchButton.setManaged(false);
        compactSearchButton.setOnAction(e -> openGlobalSearchEntry());

        actionsHost.getStyleClass().add("srots-topbar-actions");
        actionsHost.setAlignment(Pos.CENTER_LEFT);

        notificationsButton.getStyleClass().addAll(
                "srots-icon-button", "srots-topbar-action", "srots-topbar-notifications");
        notificationsButton.setTooltip(new Tooltip("Notifications"));
        notificationsButton.setAccessibleText("Notifications");
        notificationsButton.setOnAction(e -> {
            Runnable callback = onNotifications.get();
            if (callback != null) {
                callback.run();
            }
        });

        notificationSlot.getStyleClass().add("srots-topbar-notification-slot");
        notificationSlot.setAlignment(Pos.CENTER);
        notificationSlot.setMinSize(36, 36);
        notificationSlot.setPrefSize(36, 36);
        notificationSlot.setMaxSize(36, 36);
        notificationBadge.getStyleClass().add("srots-topbar-notification-badge");
        notificationBadge.setVisible(false);
        notificationBadge.setManaged(false);
        notificationBadge.setMouseTransparent(true);
        StackPane.setAlignment(notificationsButton, Pos.CENTER);
        StackPane.setAlignment(notificationBadge, Pos.TOP_RIGHT);
        notificationSlot.getChildren().addAll(notificationsButton, notificationBadge);

        connectionIndicator.getStyleClass().add("srots-topbar-connection");

        userProfile.getStyleClass().add("srots-topbar-profile");
        userProfile.setAccessibleText("Open user profile menu");
        userProfile.setOnClick(() -> {
            Runnable callback = onProfile.get();
            if (callback != null) {
                callback.run();
            }
        });

        envLabel.getStyleClass().add("srots-topbar-env");
        envLabel.setVisible(false);
        envLabel.setManaged(false);

        rightCluster.getStyleClass().add("srots-topbar-right");
        rightCluster.setAlignment(Pos.CENTER_RIGHT);
        rightCluster.getChildren().addAll(
                actionsHost,
                connectionIndicator,
                notificationSlot,
                userProfile,
                envLabel,
                windowControls);

        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox.setHgrow(contextBlock, Priority.SOMETIMES);

        getChildren().addAll(
                leftCluster,
                searchField,
                compactSearchButton,
                spacer,
                rightCluster);

        widthListener = (obs, o, width) -> {
            double w = width == null ? 0 : width.doubleValue();
            applyDensity(w);
        };
        widthProperty().addListener(widthListener);
    }

    public void setViewModel(SrotsTopBarViewModel model) {
        detachViewModel();
        viewModel.set(model);
        if (model == null) {
            return;
        }

        titleListener = obs -> applyContextLabels(model);
        model.pageTitleProperty().addListener(titleListener);
        breadcrumbListener = change -> applyContextLabels(model);
        model.getBreadcrumbs().addListener(breadcrumbListener);
        applyContextLabels(model);

        actionsListener = change -> renderActions(model);
        model.getActions().addListener(actionsListener);
        renderActions(model);

        userListener = obs -> applyUser(model.getCurrentUser());
        model.currentUserProperty().addListener(userListener);
        applyUser(model.getCurrentUser());

        connectionListener = obs -> connectionIndicator.setState(model.getConnectionState());
        model.connectionStateProperty().addListener(connectionListener);
        connectionIndicator.setState(model.getConnectionState());

        notificationListener = obs -> applyNotificationCount(model.getNotificationCount());
        model.notificationCountProperty().addListener(notificationListener);
        applyNotificationCount(model.getNotificationCount());

        compactListener = obs -> applyCompact(model.compactModeProperty().get());
        model.compactModeProperty().addListener(compactListener);

        searchField.visibleProperty().bind(model.searchEnabledProperty().and(model.compactModeProperty().not()));
        searchField.managedProperty().bind(searchField.visibleProperty());
    }

    public SrotsTopBarViewModel getViewModel() {
        return viewModel.get();
    }

    public SrotsSearchField getSearchField() {
        return searchField;
    }

    public void focusSearch() {
        openGlobalSearchEntry();
        if (searchField.isVisible()) {
            searchField.getField().requestFocus();
            searchField.getField().selectAll();
        }
    }

    private void openGlobalSearchEntry() {
        Runnable open = onCommandPalette.get();
        if (open != null) {
            open.run();
        }
    }

    public SrotsConnectionIndicator getConnectionIndicator() {
        return connectionIndicator;
    }

    public SrotsUserProfile getUserProfile() {
        return userProfile;
    }

    public Button getNotificationsButton() {
        return notificationsButton;
    }

    public StackPane getNotificationSlot() {
        return notificationSlot;
    }

    public SrotsBreadcrumb getBreadcrumb() {
        return breadcrumb;
    }

    public Label getBrandLabel() {
        return brandLabel;
    }

    public Region getBrandDivider() {
        return brandDivider;
    }

    public HBox getRightCluster() {
        return rightCluster;
    }

    public Label getEnvLabel() {
        return envLabel;
    }

    public SrotsWindowControls getWindowControls() {
        return windowControls;
    }

    public void setEnvironmentBadge(String text, boolean visible) {
        setEnvironmentBadge(text, visible, null);
    }

    public void setEnvironmentBadge(String text, boolean visible, String environmentKey) {
        envLabel.setText(text == null ? "" : text);
        envLabel.setVisible(visible);
        envLabel.setManaged(visible);
        envLabel.getStyleClass().removeAll("srots-env-dev", "srots-env-staging", "srots-env-prod", "srots-env-other");
        if (!visible) {
            return;
        }
        String key = environmentKey == null ? text : environmentKey;
        String normalized = key == null ? "" : key.trim().toLowerCase();
        if (normalized.contains("prod")) {
            envLabel.getStyleClass().add("srots-env-prod");
        } else if (normalized.contains("stag")) {
            envLabel.getStyleClass().add("srots-env-staging");
        } else if (normalized.contains("dev") || normalized.contains("local")) {
            envLabel.getStyleClass().add("srots-env-dev");
        } else {
            envLabel.getStyleClass().add("srots-env-other");
        }
    }

    public void setOnNotifications(Runnable callback) {
        onNotifications.set(callback);
    }

    public void setOnProfile(Runnable callback) {
        onProfile.set(callback);
    }

    public void setOnCommandPalette(Runnable callback) {
        onCommandPalette.set(callback);
    }

    public ObjectProperty<Runnable> onNotificationsProperty() {
        return onNotifications;
    }

    public ObjectProperty<Runnable> onProfileProperty() {
        return onProfile;
    }

    public ObjectProperty<Runnable> onCommandPaletteProperty() {
        return onCommandPalette;
    }

    public void dispose() {
        detachViewModel();
        if (widthListener != null) {
            widthProperty().removeListener(widthListener);
            widthListener = null;
        }
    }

    private void detachViewModel() {
        SrotsTopBarViewModel model = viewModel.get();
        if (model == null) {
            return;
        }
        if (titleListener != null) {
            model.pageTitleProperty().removeListener(titleListener);
        }
        if (breadcrumbListener != null) {
            model.getBreadcrumbs().removeListener(breadcrumbListener);
        }
        if (actionsListener != null) {
            model.getActions().removeListener(actionsListener);
        }
        if (userListener != null) {
            model.currentUserProperty().removeListener(userListener);
        }
        if (connectionListener != null) {
            model.connectionStateProperty().removeListener(connectionListener);
        }
        if (notificationListener != null) {
            model.notificationCountProperty().removeListener(notificationListener);
        }
        if (compactListener != null) {
            model.compactModeProperty().removeListener(compactListener);
        }
        searchField.visibleProperty().unbind();
        searchField.managedProperty().unbind();
        titleListener = null;
        breadcrumbListener = null;
        actionsListener = null;
        userListener = null;
        connectionListener = null;
        notificationListener = null;
        compactListener = null;
    }

    private void renderActions(SrotsTopBarViewModel model) {
        actionsHost.getChildren().clear();
        for (SrotsTopBarAction action : model.getActions()) {
            if (!action.isVisible()) {
                continue;
            }
            String glyph = resolveActionGlyph(action);
            Button button = new Button(glyph);
            button.getStyleClass().addAll("srots-icon-button", "srots-topbar-action", "srots-topbar-more");
            button.setDisable(!action.isEnabled());
            String tip = action.getTooltip() == null || action.getTooltip().isBlank()
                    ? action.getLabel()
                    : action.getTooltip();
            if (tip == null || tip.isBlank() || "…".equals(tip) || "...".equals(tip)) {
                tip = "More actions";
            }
            button.setTooltip(new Tooltip(tip));
            button.setAccessibleText(tip);
            button.setOnAction(e -> action.execute());
            actionsHost.getChildren().add(button);
        }
    }

    private static String resolveActionGlyph(SrotsTopBarAction action) {
        String iconKey = action.getIconKey();
        if (iconKey != null) {
            String key = iconKey.trim().toLowerCase();
            if (key.contains("more") || key.contains("ellipsis")) {
                return SrotsIcon.MORE.getGlyph();
            }
            if (key.contains("help")) {
                return SrotsIcon.HELP.getGlyph();
            }
            if (key.contains("settings")) {
                return SrotsIcon.SETTINGS.getGlyph();
            }
        }
        String label = action.getLabel() == null ? "" : action.getLabel().trim();
        if (label.isEmpty() || "…".equals(label) || "...".equals(label) || "More".equalsIgnoreCase(label)) {
            return SrotsIcon.MORE.getGlyph();
        }
        return label;
    }

    private void applyUser(TopBarUserInfo user) {
        TopBarUserInfo safe = user == null ? TopBarUserInfo.fallback() : user;
        userProfile.setName(safe.displayName());
        userProfile.setRole(safe.roleLabel());
        userProfile.setAccessibleText(
                safe.roleLabel().isBlank()
                        ? "User profile " + safe.displayName()
                        : "User profile " + safe.displayName() + ", " + safe.roleLabel());
        Tooltip.install(userProfile, new Tooltip(safe.displayName()
                + (safe.roleLabel().isBlank() ? "" : " (" + safe.roleLabel() + ")")));
    }

    private void applyNotificationCount(int count) {
        String badge = TopBarNotificationBadgeFormatter.format(count);
        boolean show = TopBarNotificationBadgeFormatter.shouldShow(count);
        notificationBadge.setText(badge);
        notificationBadge.setVisible(show);
        notificationBadge.setManaged(false);
        notificationsButton.getStyleClass().remove("srots-topbar-notifications-unread");
        if (show) {
            notificationsButton.getStyleClass().add("srots-topbar-notifications-unread");
        }
        notificationsButton.setAccessibleText(show ? "Notifications, " + badge + " unread" : "Notifications");
    }

    private void applyDensity(double width) {
        boolean compact = width < COMPACT_BREAKPOINT;
        midCompact = width > 0 && width < MID_BREAKPOINT && !compact;
        applyCompact(compact);
        getStyleClass().removeAll("srots-topbar-mid", "srots-topbar-compact");
        if (compact) {
            getStyleClass().add("srots-topbar-compact");
        } else if (midCompact) {
            getStyleClass().add("srots-topbar-mid");
        }
        SrotsTopBarViewModel model = viewModel.get();
        if (model != null) {
            applyContextLabels(model);
        }
    }

    private void applyCompact(boolean compact) {
        SrotsTopBarViewModel model = viewModel.get();
        if (model != null && model.compactModeProperty().get() != compact) {
            model.setCompactMode(compact);
        }
        compactSearchButton.setVisible(compact && (model == null || model.isSearchEnabled()));
        compactSearchButton.setManaged(compactSearchButton.isVisible());
        if (model != null) {
            applyContextLabels(model);
        }
        userProfile.setCompact(compact);
        connectionIndicator.setVisible(!compact);
        connectionIndicator.setManaged(!compact);
    }

    private void applyContextLabels(SrotsTopBarViewModel model) {
        if (model == null) {
            return;
        }
        pageTitleLabel.setText(nullToEmpty(model.getPageTitle()));
        breadcrumb.setItems(model.getBreadcrumbs());
        boolean redundant = model.getBreadcrumbs().size() <= 1;
        boolean hideCrumb = redundant || midCompact;
        breadcrumb.setVisible(!hideCrumb);
        breadcrumb.setManaged(!hideCrumb);
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}
