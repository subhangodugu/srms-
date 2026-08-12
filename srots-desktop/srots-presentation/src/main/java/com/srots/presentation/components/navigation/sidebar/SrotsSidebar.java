package com.srots.presentation.components.navigation.sidebar;

import com.srots.presentation.navigation.model.NavigationGroup;
import com.srots.presentation.navigation.model.NavigationRouteId;
import com.srots.presentation.window.SrotsWindowConfiguration;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Objects;

/**
 * Persistent application sidebar: brand, scrollable nav, footer, expand/collapse.
 * Navigation rules remain outside this component — presentation only.
 */
public class SrotsSidebar extends VBox {

    private static final Logger log = LoggerFactory.getLogger(SrotsSidebar.class);
    private static final String LOGO_RESOURCE = "/images/srots-logo.png";
    private static final Duration COLLAPSE_DURATION = Duration.millis(180);

    private final BooleanProperty collapsed = new SimpleBooleanProperty(false);
    private final ObjectProperty<SrotsSidebarViewModel> viewModel = new SimpleObjectProperty<>();

    private final VBox brandBlock = new VBox(2);
    private final Label brandTitle = new Label("SROTS");
    private final Label brandSubtitle = new Label("Enterprise Platform");
    private final ImageView brandLogo = new ImageView();
    private final VBox navigationHost = new VBox(4);
    private final ScrollPane navigationScroll = new ScrollPane();
    private final VBox footer = new VBox(6);
    private final Button collapseButton = new Button();
    private final Button settingsButton = new Button();
    private final Label emptyStateLabel = new Label("No available modules.");

    private Timeline widthTimeline;
    private Runnable onSettingsRequested = () -> {
    };
    private ChangeListener<Number> revisionListener;
    private ChangeListener<NavigationRouteId> routeListener;
    private ChangeListener<Boolean> collapsedVmListener;
    private ChangeListener<Boolean> collapsedLocalListener;
    private boolean bindingCollapsed;

    public SrotsSidebar() {
        getStyleClass().addAll("srots-sidebar");
        setSpacing(0);
        setFillWidth(true);
        applyWidth(false, false);

        buildBrandHeader();
        buildNavigationScroll();
        buildFooter();

        getChildren().addAll(brandBlock, navigationScroll, footer);
        VBox.setVgrow(navigationScroll, Priority.ALWAYS);

        collapsed.addListener((obs, was, isCollapsed) -> applyCollapsed(isCollapsed, true));
        emptyStateLabel.getStyleClass().add("srots-sidebar-empty");
        emptyStateLabel.setVisible(false);
        emptyStateLabel.setManaged(false);
        navigationHost.getChildren().add(emptyStateLabel);

        setAccessibleText("Application navigation");
    }

    public BooleanProperty collapsedProperty() {
        return collapsed;
    }

    public boolean isCollapsed() {
        return collapsed.get();
    }

    public void setCollapsed(boolean value) {
        collapsed.set(value);
    }

    public void toggleCollapsed() {
        setCollapsed(!isCollapsed());
    }

    public ObjectProperty<SrotsSidebarViewModel> viewModelProperty() {
        return viewModel;
    }

    public SrotsSidebarViewModel getViewModel() {
        return viewModel.get();
    }

    public void setViewModel(SrotsSidebarViewModel model) {
        detachViewModel();
        viewModel.set(model);
        if (model == null) {
            return;
        }

        bindingCollapsed = true;
        collapsed.set(model.isCollapsed());
        bindingCollapsed = false;

        collapsedVmListener = (obs, o, n) -> {
            if (!Objects.equals(collapsed.get(), n)) {
                bindingCollapsed = true;
                collapsed.set(n);
                bindingCollapsed = false;
            }
        };
        model.collapsedProperty().addListener(collapsedVmListener);
        collapsedLocalListener = (obs, o, n) -> {
            if (!bindingCollapsed && model.isCollapsed() != n) {
                model.setCollapsed(n);
            }
        };
        collapsed.addListener(collapsedLocalListener);

        revisionListener = (obs, o, n) -> renderFromViewModel(model);
        model.revisionProperty().addListener(revisionListener);

        routeListener = (obs, o, n) -> syncActiveStates(model);
        model.activeSidebarRouteProperty().addListener(routeListener);

        settingsButton.setOnAction(e -> {
            if (onSettingsRequested != null) {
                onSettingsRequested.run();
            }
            model.requestNavigate(NavigationRouteId.SETTINGS);
        });

        renderFromViewModel(model);
        syncActiveStates(model);
    }

    public VBox getNavigationHost() {
        return navigationHost;
    }

    public void setOnSettingsRequested(Runnable action) {
        this.onSettingsRequested = action == null ? () -> {
        } : action;
    }

    public void clearNavigation() {
        navigationHost.getChildren().removeIf(n -> n != emptyStateLabel);
        showEmpty(true);
    }

    public void addGroup(SrotsNavigationGroup group) {
        if (group != null) {
            showEmpty(false);
            navigationHost.getChildren().add(group);
            group.setSidebarCollapsed(isCollapsed());
        }
    }

    public void addItem(SrotsNavigationItem item) {
        if (item != null) {
            showEmpty(false);
            navigationHost.getChildren().add(item);
            item.setCollapsedChrome(isCollapsed());
        }
    }

    public void ensureActiveGroupExpanded(NavigationGroup groupId) {
        if (groupId == null || getViewModel() == null) {
            return;
        }
        getViewModel().setGroupExpanded(groupId, true);
        for (var node : navigationHost.getChildren()) {
            if (node instanceof SrotsNavigationGroup group
                    && groupId.displayTitle().equalsIgnoreCase(group.getTitle())) {
                group.setExpanded(true);
            }
        }
    }

    private void detachViewModel() {
        SrotsSidebarViewModel previous = viewModel.get();
        if (previous != null) {
            if (revisionListener != null) {
                previous.revisionProperty().removeListener(revisionListener);
            }
            if (routeListener != null) {
                previous.activeSidebarRouteProperty().removeListener(routeListener);
            }
            if (collapsedVmListener != null) {
                previous.collapsedProperty().removeListener(collapsedVmListener);
            }
        }
        if (collapsedLocalListener != null) {
            collapsed.removeListener(collapsedLocalListener);
        }
        revisionListener = null;
        routeListener = null;
        collapsedVmListener = null;
        collapsedLocalListener = null;
    }

    private void renderFromViewModel(SrotsSidebarViewModel model) {
        navigationHost.getChildren().removeIf(n -> n != emptyStateLabel);
        if (model.getGroups().isEmpty()) {
            showEmpty(true);
            return;
        }
        showEmpty(false);
        for (SrotsSidebarViewModel.GroupPresentation group : model.getGroups()) {
            if (group.standalone()) {
                for (SrotsSidebarViewModel.ItemPresentation item : group.items()) {
                    navigationHost.getChildren().add(createItemNode(model, item));
                }
            } else {
                SrotsNavigationGroup groupNode = new SrotsNavigationGroup(group.title());
                groupNode.setExpanded(model.isGroupExpanded(group.id()));
                groupNode.expandedProperty().addListener((obs, was, expanded) ->
                        model.setGroupExpanded(group.id(), expanded));
                groupNode.setSidebarCollapsed(isCollapsed());
                for (SrotsSidebarViewModel.ItemPresentation item : group.items()) {
                    groupNode.addItem(createItemNode(model, item));
                }
                navigationHost.getChildren().add(groupNode);
            }
        }
        syncCollapsedChrome();
        syncActiveStates(model);
    }

    private SrotsNavigationItem createItemNode(
            SrotsSidebarViewModel model,
            SrotsSidebarViewModel.ItemPresentation item) {
        SrotsNavigationItem button = new SrotsNavigationItem(item.title());
        button.setIconKey(item.iconKey());
        button.setBadgeCount(item.badgeCount());
        button.setDisable(!item.enabled());
        if (!item.enabled() && item.disabledReason() != null && !item.disabledReason().isBlank()) {
            button.setTooltip(new Tooltip(item.disabledReason()));
        }
        button.setOnAction(e -> model.requestNavigate(item.route()));
        button.setCollapsedChrome(isCollapsed());
        button.getProperties().put("srots.route", item.route());
        return button;
    }

    private void syncActiveStates(SrotsSidebarViewModel model) {
        NavigationRouteId active = model.getActiveSidebarRoute();
        for (var node : navigationHost.getChildren()) {
            if (node instanceof SrotsNavigationItem item) {
                item.setActive(isRoute(item, active));
            } else if (node instanceof SrotsNavigationGroup group) {
                boolean groupHasActive = false;
                for (var child : group.getItemsHost().getChildren()) {
                    if (child instanceof SrotsNavigationItem item) {
                        boolean match = isRoute(item, active);
                        item.setActive(match);
                        groupHasActive = groupHasActive || match;
                    }
                }
                if (groupHasActive) {
                    group.setExpanded(true);
                }
            }
        }
    }

    private static boolean isRoute(SrotsNavigationItem item, NavigationRouteId route) {
        Object value = item.getProperties().get("srots.route");
        return route != null && Objects.equals(value, route);
    }

    private void buildBrandHeader() {
        brandBlock.getStyleClass().addAll("srots-sidebar-header", "srots-sidebar-brand");
        brandBlock.setPadding(new Insets(16, 14, 12, 14));
        brandBlock.setAlignment(Pos.CENTER_LEFT);

        brandLogo.setFitWidth(28);
        brandLogo.setFitHeight(28);
        brandLogo.setPreserveRatio(true);
        loadLogo();

        brandTitle.getStyleClass().add("srots-sidebar-brand-title");
        brandSubtitle.getStyleClass().add("srots-sidebar-brand-subtitle");

        VBox text = new VBox(2, brandTitle, brandSubtitle);
        HBox row = new HBox(10, brandLogo, text);
        row.setAlignment(Pos.CENTER_LEFT);
        brandBlock.getChildren().add(row);
    }

    private void buildNavigationScroll() {
        navigationHost.getStyleClass().add("srots-sidebar-nav");
        navigationHost.setPadding(new Insets(4, 8, 8, 8));
        navigationScroll.setContent(navigationHost);
        navigationScroll.setFitToWidth(true);
        navigationScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        navigationScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        navigationScroll.getStyleClass().add("srots-sidebar-scroll");
    }

    private void buildFooter() {
        footer.getStyleClass().add("srots-sidebar-footer");
        footer.setPadding(new Insets(8, 10, 12, 10));

        settingsButton.getStyleClass().addAll(
                "srots-nav-item", "srots-sidebar-item", "srots-sidebar-footer-settings");
        settingsButton.setMaxWidth(Double.MAX_VALUE);
        settingsButton.setAlignment(Pos.CENTER_LEFT);
        settingsButton.setText("⚙  Settings");
        settingsButton.setAccessibleText("Settings");

        collapseButton.getStyleClass().addAll("srots-sidebar-collapse-button");
        collapseButton.setMaxWidth(Double.MAX_VALUE);
        collapseButton.setText("←  Collapse");
        collapseButton.setAccessibleText("Collapse navigation");
        collapseButton.setOnAction(e -> toggleCollapsed());

        footer.getChildren().addAll(settingsButton, collapseButton);
    }

    private void applyCollapsed(boolean isCollapsed, boolean animate) {
        if (isCollapsed) {
            if (!getStyleClass().contains("srots-sidebar-collapsed")) {
                getStyleClass().add("srots-sidebar-collapsed");
            }
        } else {
            getStyleClass().remove("srots-sidebar-collapsed");
        }

        brandSubtitle.setVisible(!isCollapsed);
        brandSubtitle.setManaged(!isCollapsed);
        brandTitle.setVisible(!isCollapsed);
        brandTitle.setManaged(!isCollapsed);
        settingsButton.setText(isCollapsed ? "⚙" : "⚙  Settings");
        collapseButton.setText(isCollapsed ? "→" : "←  Collapse");
        collapseButton.setAccessibleText(isCollapsed ? "Expand navigation" : "Collapse navigation");
        if (isCollapsed) {
            collapseButton.setTooltip(new Tooltip("Expand navigation"));
            settingsButton.setTooltip(new Tooltip("Settings"));
        } else {
            collapseButton.setTooltip(null);
            settingsButton.setTooltip(null);
        }
        setAccessibleText(isCollapsed ? "Navigation collapsed" : "Navigation expanded");

        syncCollapsedChrome();
        applyWidth(isCollapsed, animate);
    }

    private void syncCollapsedChrome() {
        boolean isCollapsed = isCollapsed();
        for (var node : navigationHost.getChildren()) {
            if (node instanceof SrotsNavigationItem item) {
                item.setCollapsedChrome(isCollapsed);
            } else if (node instanceof SrotsNavigationGroup group) {
                group.setSidebarCollapsed(isCollapsed);
                for (var child : group.getItemsHost().getChildren()) {
                    if (child instanceof SrotsNavigationItem item) {
                        item.setCollapsedChrome(isCollapsed);
                    }
                }
            }
        }
    }

    private void applyWidth(boolean isCollapsed, boolean animate) {
        double target = isCollapsed
                ? SrotsWindowConfiguration.SIDEBAR_COLLAPSED_WIDTH
                : SrotsWindowConfiguration.SIDEBAR_EXPANDED_WIDTH;
        setMinWidth(target);
        setMaxWidth(target);
        if (!animate) {
            setPrefWidth(target);
            return;
        }
        if (widthTimeline != null) {
            widthTimeline.stop();
        }
        widthTimeline = new Timeline(new KeyFrame(
                COLLAPSE_DURATION,
                new KeyValue(prefWidthProperty(), target)));
        widthTimeline.play();
    }

    private void showEmpty(boolean empty) {
        emptyStateLabel.setVisible(empty);
        emptyStateLabel.setManaged(empty);
    }

    private void loadLogo() {
        try (InputStream stream = SrotsSidebar.class.getResourceAsStream(LOGO_RESOURCE)) {
            if (stream == null) {
                log.warn("Sidebar logo missing: {}", LOGO_RESOURCE);
                brandLogo.setVisible(false);
                brandLogo.setManaged(false);
                return;
            }
            Image image = new Image(stream);
            if (image.isError()) {
                log.warn("Sidebar logo failed to load");
                brandLogo.setVisible(false);
                brandLogo.setManaged(false);
                return;
            }
            brandLogo.setImage(image);
        } catch (Exception ex) {
            log.warn("Unable to load sidebar logo", ex);
            brandLogo.setVisible(false);
            brandLogo.setManaged(false);
        }
    }
}
