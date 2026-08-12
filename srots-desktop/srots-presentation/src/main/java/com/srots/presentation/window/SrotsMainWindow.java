package com.srots.presentation.window;

import com.srots.presentation.components.layout.ThemeLoader;
import com.srots.presentation.components.navigation.topbar.SrotsTopBar;
import com.srots.presentation.navigation.NavigationModule;
import com.srots.presentation.navigation.model.NavigationRouteId;
import com.srots.presentation.navigation.service.NavigationService;
import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

/**
 * Owns the primary SROTS Stage/Scene configuration. No feature business logic.
 */
public final class SrotsMainWindow {

    private static final Logger log = LoggerFactory.getLogger(SrotsMainWindow.class);
    private static final String[] ICON_RESOURCES = {
            "/icons/srots-64.png",
            "/icons/srots-128.png",
            "/icons/srots-256.png"
    };

    private final Stage stage;
    private final SrotsWindowConfiguration configuration;
    private final SrotsWindowTitleService titleService;
    private final SrotsWindowStateStore stateStore;

    private Scene scene;
    private Parent root;
    private NavigationModule navigationModule;
    private ChangeListener<NavigationRouteId> titleListener;
    private BooleanSupplier sidebarCollapsedSupplier = () -> false;
    private Consumer<Boolean> sidebarCollapsedApplier = collapsed -> {
    };
    private boolean configured;
    private double windowedWidth;
    private double windowedHeight;
    private double windowedX = Double.NaN;
    private double windowedY = Double.NaN;

    public SrotsMainWindow(
            Stage stage,
            SrotsWindowConfiguration configuration,
            SrotsWindowTitleService titleService,
            SrotsWindowStateStore stateStore) {
        this.stage = Objects.requireNonNull(stage, "stage");
        this.configuration = Objects.requireNonNullElseGet(configuration, SrotsWindowConfiguration::new);
        this.titleService = Objects.requireNonNull(titleService, "titleService");
        this.stateStore = Objects.requireNonNullElseGet(stateStore, SrotsWindowStateStore::new);
        this.windowedWidth = this.configuration.defaultWidth();
        this.windowedHeight = this.configuration.defaultHeight();
    }

    public void configure(Parent root, NavigationModule navigationModule) {
        this.root = Objects.requireNonNull(root, "root");
        this.navigationModule = Objects.requireNonNull(navigationModule, "navigationModule");

        if (root.getStyleClass() != null) {
            if (!root.getStyleClass().contains("srots-main-window")) {
                root.getStyleClass().add("srots-main-window");
            }
            if (!root.getStyleClass().contains("srots-app-shell")) {
                root.getStyleClass().add("srots-app-shell");
            }
        }

        scene = new Scene(root, configuration.defaultWidth(), configuration.defaultHeight());
        ThemeLoader.apply(scene);
        navigationModule.shortcutRegistry().install(scene);

        applyUndecoratedChrome();
        stage.setScene(scene);
        stage.setMinWidth(configuration.minWidth());
        stage.setMinHeight(configuration.minHeight());
        stage.setResizable(true);
        stage.setMaximized(false);
        stage.setFullScreen(false);
        stage.setTitle(titleService.defaultTitle());
        applyIcons();
        attachWindowControls();

        bindTitle(navigationModule.navigationService());
        installCloseHandler();
        configured = true;
    }

    public void restoreState() {
        SrotsWindowState state = stateStore.load(configuration);
        applyState(state);
    }

    public void applyState(SrotsWindowState state) {
        SrotsWindowState safe = SrotsWindowStateStore.sanitize(state, configuration);
        stage.setWidth(safe.getWidth());
        stage.setHeight(safe.getHeight());
        if (safe.hasPosition()) {
            stage.setX(safe.getX());
            stage.setY(safe.getY());
        }
        rememberWindowedBounds(safe);
        // Always open windowed so minimize / maximize / close stay visible.
        stage.setMaximized(false);
        stage.setFullScreen(false);
        sidebarCollapsedApplier.accept(safe.isSidebarCollapsed());
    }

    public SrotsWindowState captureState() {
        SrotsWindowState state = new SrotsWindowState();
        state.setMaximized(stage.isMaximized());
        if (!stage.isMaximized()) {
            rememberWindowedBounds(stage.getX(), stage.getY(), stage.getWidth(), stage.getHeight());
        }
        state.setX(windowedX);
        state.setY(windowedY);
        state.setWidth(windowedWidth > 0 ? windowedWidth : configuration.defaultWidth());
        state.setHeight(windowedHeight > 0 ? windowedHeight : configuration.defaultHeight());
        state.setSidebarCollapsed(sidebarCollapsedSupplier.getAsBoolean());
        return state;
    }

    public void saveState() {
        stateStore.save(captureState());
    }

    public void show() {
        if (!configured) {
            throw new IllegalStateException("Main window has not been configured.");
        }
        stage.show();
        stage.toFront();
    }

    public void hide() {
        stage.hide();
    }

    public void close() {
        saveState();
        detachTitleListener();
        if (root != null && root.lookup(".srots-topbar") instanceof SrotsTopBar topBar) {
            topBar.getWindowControls().detach();
        }
        stage.close();
    }

    public void setSidebarCollapsedHooks(BooleanSupplier supplier, Consumer<Boolean> applier) {
        this.sidebarCollapsedSupplier = supplier == null ? () -> false : supplier;
        this.sidebarCollapsedApplier = applier == null ? collapsed -> {
        } : applier;
    }

    public void updateTitle(NavigationRouteId route) {
        stage.setTitle(titleService.titleFor(route));
    }

    public Stage getStage() {
        return stage;
    }

    public Scene getScene() {
        return scene;
    }

    public Parent getRoot() {
        return root;
    }

    public NavigationModule getNavigationModule() {
        return navigationModule;
    }

    public SrotsWindowTitleService getTitleService() {
        return titleService;
    }

    public boolean isConfigured() {
        return configured;
    }

    private void bindTitle(NavigationService navigation) {
        detachTitleListener();
        titleListener = (obs, oldRoute, newRoute) -> updateTitle(newRoute);
        navigation.currentRouteProperty().addListener(titleListener);
        updateTitle(navigation.currentRoute());
    }

    private void detachTitleListener() {
        if (titleListener != null && navigationModule != null) {
            navigationModule.navigationService().currentRouteProperty().removeListener(titleListener);
            titleListener = null;
        }
    }

    private void rememberWindowedBounds(SrotsWindowState state) {
        rememberWindowedBounds(state.getX(), state.getY(), state.getWidth(), state.getHeight());
    }

    private void rememberWindowedBounds(double x, double y, double width, double height) {
        windowedX = x;
        windowedY = y;
        windowedWidth = width;
        windowedHeight = height;
    }

    private void applyUndecoratedChrome() {
        if (stage.isShowing()) {
            return;
        }
        try {
            stage.initStyle(StageStyle.UNDECORATED);
        } catch (IllegalArgumentException | IllegalStateException ex) {
            log.debug("Primary stage already has a style; keeping native decorations", ex);
        }
    }

    private void attachWindowControls() {
        if (!(root.lookup(".srots-topbar") instanceof SrotsTopBar topBar)) {
            return;
        }
        topBar.getWindowControls().attach(stage);
        installMoveByTopBar(topBar);
        // #region agent log
        javafx.application.Platform.runLater(() -> {
            try {
                if (root != null) {
                    root.applyCss();
                    root.layout();
                }
                var close = topBar.getWindowControls().getCloseButton();
                var min = topBar.getWindowControls().getMinimizeButton();
                close.applyCss();
                min.applyCss();
                String closeBg = String.valueOf(close.getBackground());
                String minBg = String.valueOf(min.getBackground());
                boolean closeHasFills = close.getBackground() != null && !close.getBackground().getFills().isEmpty();
                double closeRadius = 0;
                if (closeHasFills) {
                    var radii = close.getBackground().getFills().get(0).getRadii();
                    closeRadius = radii.getTopLeftHorizontalRadius();
                }
                String payload = "{\"sessionId\":\"dd362e\",\"runId\":\"post-fix\",\"hypothesisId\":\"C\","
                        + "\"location\":\"SrotsMainWindow.attachWindowControls\",\"message\":\"window-control-styles\","
                        + "\"data\":{\"closeClasses\":\"" + String.join(",", close.getStyleClass())
                        + "\",\"closeRadius\":" + closeRadius
                        + ",\"closeHasFills\":" + closeHasFills
                        + ",\"closeBg\":\"" + closeBg.replace("\\", "\\\\").replace("\"", "\\\"")
                        + "\",\"minBg\":\"" + minBg.replace("\\", "\\\\").replace("\"", "\\\"")
                        + "\"},\"timestamp\":" + System.currentTimeMillis() + "}\n";
                java.nio.file.Files.writeString(
                        java.nio.file.Path.of("c:/srms/debug-dd362e.log"),
                        payload,
                        java.nio.file.StandardOpenOption.CREATE,
                        java.nio.file.StandardOpenOption.APPEND);
            } catch (Exception ignored) {
            }
        });
        // #endregion
    }

    private void installMoveByTopBar(SrotsTopBar topBar) {
        final double[] drag = new double[2];
        final boolean[] dragArmed = {false};
        topBar.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            if (shouldIgnoreWindowDrag(event) || event.getTarget() == null) {
                dragArmed[0] = false;
                return;
            }
            dragArmed[0] = true;
            drag[0] = event.getScreenX() - stage.getX();
            drag[1] = event.getScreenY() - stage.getY();
        });
        topBar.addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {
            if (!dragArmed[0]) {
                return;
            }
            if (stage.isMaximized()) {
                stage.setMaximized(false);
            }
            stage.setX(event.getScreenX() - drag[0]);
            stage.setY(event.getScreenY() - drag[1]);
        });
        topBar.addEventFilter(MouseEvent.MOUSE_RELEASED, event -> dragArmed[0] = false);
        topBar.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getClickCount() != 2 || shouldIgnoreWindowDrag(event)) {
                return;
            }
            stage.setMaximized(!stage.isMaximized());
            event.consume();
        });
    }

    private static boolean shouldIgnoreWindowDrag(MouseEvent event) {
        Node target = event.getTarget() instanceof Node node ? node : null;
        if (target == null && event.getPickResult() != null) {
            target = event.getPickResult().getIntersectedNode();
        }
        while (target != null) {
            if (target instanceof Control) {
                return true;
            }
            if (target.getStyleClass() != null) {
                if (target.getStyleClass().contains("srots-window-controls")
                        || target.getStyleClass().contains("srots-window-control")
                        || target.getStyleClass().contains("srots-topbar-search")
                        || target.getStyleClass().contains("srots-search-field")
                        || target.getStyleClass().contains("srots-topbar-profile")
                        || target.getStyleClass().contains("srots-user-profile")
                        || target.getStyleClass().contains("srots-topbar-action")
                        || target.getStyleClass().contains("srots-topbar-notifications")
                        || target.getStyleClass().contains("srots-topbar-env")) {
                    return true;
                }
            }
            target = target.getParent();
        }
        return false;
    }

    private void installCloseHandler() {
        stage.setOnCloseRequest(event -> {
            try {
                saveState();
            } catch (Exception ex) {
                log.warn("Failed to save window state on close", ex);
            }
            detachTitleListener();
        });
    }

    private void applyIcons() {
        List<Image> icons = new ArrayList<>();
        for (String path : ICON_RESOURCES) {
            try (InputStream stream = SrotsMainWindow.class.getResourceAsStream(path)) {
                if (stream == null) {
                    log.warn("Application icon missing: {}", path);
                    continue;
                }
                Image image = new Image(stream);
                if (!image.isError()) {
                    icons.add(image);
                }
            } catch (Exception ex) {
                log.warn("Unable to load application icon {}", path, ex);
            }
        }
        if (!icons.isEmpty()) {
            stage.getIcons().setAll(icons);
        }
    }
}
