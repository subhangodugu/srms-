package com.srots.presentation.window;

import com.srots.presentation.navigation.NavigationModule;
import javafx.scene.Parent;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

/**
 * Owns creation and lifecycle of the primary SROTS main window.
 */
public final class SrotsWindowManager {

    private static final Logger log = LoggerFactory.getLogger(SrotsWindowManager.class);

    private final SrotsWindowConfiguration configuration;
    private final SrotsWindowStateStore stateStore;
    private SrotsMainWindow mainWindow;

    public SrotsWindowManager() {
        this(new SrotsWindowConfiguration(), new SrotsWindowStateStore());
    }

    public SrotsWindowManager(SrotsWindowConfiguration configuration, SrotsWindowStateStore stateStore) {
        this.configuration = Objects.requireNonNullElseGet(configuration, SrotsWindowConfiguration::new);
        this.stateStore = Objects.requireNonNullElseGet(stateStore, SrotsWindowStateStore::new);
    }

    public SrotsMainWindow createMainWindow(
            Stage stage,
            Parent root,
            NavigationModule navigationModule,
            SrotsWindowTitleService titleService,
            BooleanSupplier sidebarCollapsedSupplier,
            Consumer<Boolean> sidebarCollapsedApplier) {
        Objects.requireNonNull(stage, "stage");
        Objects.requireNonNull(root, "root");
        Objects.requireNonNull(navigationModule, "navigationModule");
        Objects.requireNonNull(titleService, "titleService");

        mainWindow = new SrotsMainWindow(stage, configuration, titleService, stateStore);
        mainWindow.setSidebarCollapsedHooks(sidebarCollapsedSupplier, sidebarCollapsedApplier);
        mainWindow.configure(root, navigationModule);
        mainWindow.restoreState();
        log.info("Main window created");
        return mainWindow;
    }

    public void showMainWindow() {
        requireMain().show();
    }

    public void hideMainWindow() {
        if (mainWindow != null) {
            mainWindow.hide();
        }
    }

    public void closeMainWindow() {
        if (mainWindow != null) {
            mainWindow.close();
            mainWindow = null;
        }
    }

    public void saveWindowState() {
        if (mainWindow != null) {
            mainWindow.saveState();
        }
    }

    public void restoreWindowState() {
        if (mainWindow != null) {
            mainWindow.restoreState();
        }
    }

    public SrotsMainWindow getMainWindow() {
        return mainWindow;
    }

    public SrotsWindowConfiguration getConfiguration() {
        return configuration;
    }

    private SrotsMainWindow requireMain() {
        if (mainWindow == null) {
            throw new IllegalStateException("Main window has not been created.");
        }
        return mainWindow;
    }
}
