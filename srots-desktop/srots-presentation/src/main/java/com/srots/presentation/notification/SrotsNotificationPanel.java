package com.srots.presentation.notification;

import com.srots.presentation.components.utility.icons.SrotsIcon;
import javafx.beans.InvalidationListener;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Screen;
import javafx.stage.Window;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Lightweight notification panel popup (not a Stage).
 */
public final class SrotsNotificationPanel {

    public static final double PANEL_WIDTH = 420;
    public static final double PANEL_HEIGHT = 560;

    private final Popup popup = new Popup();
    private final BorderPane root = new BorderPane();
    private final Label titleLabel = new Label("Notifications");
    private final Label unreadLabel = new Label();
    private final Button closeButton = new Button(SrotsIcon.CLOSE.getGlyph());
    private final ToggleButton allFilter = new ToggleButton("All");
    private final ToggleButton unreadFilter = new ToggleButton("Unread");
    private final ToggleGroup filterGroup = new ToggleGroup();
    private final ListView<SrotsNotification> listView = new ListView<>();
    private final Button retryButton = new Button("Retry");
    private final Button markAllButton = new Button("Mark all as read");
    private final StackPane body = new StackPane();
    private final VBox emptyBox = new VBox(8);
    private final VBox errorBox = new VBox(8);
    private final Label emptyLabel = new Label();
    private final Label errorLabel = new Label();
    private final Label loadingLabel = new Label("Loading notifications...");

    private SrotsNotificationPanelViewModel viewModel;
    private Consumer<SrotsNotification> onActivate = n -> {
    };
    private Runnable onCloseRequest = () -> {
    };
    private Runnable onHidden = () -> {
    };
    private InvalidationListener refreshListener;

    public SrotsNotificationPanel() {
        popup.setAutoHide(true);
        popup.setAutoFix(true);
        popup.setHideOnEscape(true);

        root.getStyleClass().add("srots-notification-panel");
        root.setPrefSize(PANEL_WIDTH, PANEL_HEIGHT);
        root.setMaxSize(PANEL_WIDTH, PANEL_HEIGHT);
        root.setPadding(new Insets(12));

        titleLabel.getStyleClass().add("srots-notification-panel-title");
        unreadLabel.getStyleClass().add("srots-notification-panel-unread");
        closeButton.getStyleClass().addAll("srots-icon-button", "srots-notification-close");
        closeButton.setAccessibleText("Close notifications");
        closeButton.setOnAction(e -> onCloseRequest.run());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox header = new HBox(8, titleLabel, unreadLabel, spacer, closeButton);
        header.setAlignment(Pos.CENTER_LEFT);
        header.getStyleClass().add("srots-notification-panel-header");

        allFilter.setToggleGroup(filterGroup);
        unreadFilter.setToggleGroup(filterGroup);
        allFilter.getStyleClass().addAll("srots-secondary-button", "srots-notification-filter");
        unreadFilter.getStyleClass().addAll("srots-secondary-button", "srots-notification-filter");
        allFilter.setSelected(true);
        allFilter.setAccessibleText("All notifications");
        unreadFilter.setAccessibleText("Unread notifications");
        HBox filters = new HBox(8, allFilter, unreadFilter);
        filters.getStyleClass().add("srots-notification-filters");
        filters.setPadding(new Insets(8, 0, 8, 0));

        listView.getStyleClass().add("srots-notification-list");
        listView.setFocusTraversable(true);
        VBox.setVgrow(listView, Priority.ALWAYS);

        emptyLabel.getStyleClass().add("srots-state-message");
        emptyBox.getStyleClass().add("srots-notification-empty");
        emptyBox.setAlignment(Pos.CENTER);
        emptyBox.getChildren().add(emptyLabel);

        errorLabel.getStyleClass().add("srots-state-message");
        retryButton.getStyleClass().addAll("srots-button", "srots-secondary-button");
        retryButton.setAccessibleText("Retry loading notifications");
        errorBox.getStyleClass().add("srots-notification-error");
        errorBox.setAlignment(Pos.CENTER);
        errorBox.getChildren().addAll(errorLabel, retryButton);

        loadingLabel.getStyleClass().add("srots-caption");
        loadingLabel.setAlignment(Pos.CENTER);

        body.getChildren().addAll(listView, emptyBox, errorBox, loadingLabel);
        StackPane.setAlignment(emptyBox, Pos.CENTER);
        StackPane.setAlignment(errorBox, Pos.CENTER);
        StackPane.setAlignment(loadingLabel, Pos.CENTER);

        markAllButton.getStyleClass().addAll("srots-button", "srots-tertiary-button", "srots-notification-mark-all");
        markAllButton.setMaxWidth(Double.MAX_VALUE);
        markAllButton.setAccessibleText("Mark all as read");
        HBox footer = new HBox(markAllButton);
        footer.setAlignment(Pos.CENTER);
        footer.getStyleClass().add("srots-notification-panel-footer");
        footer.setPadding(new Insets(8, 0, 0, 0));

        VBox top = new VBox(header, filters);
        root.setTop(top);
        root.setCenter(body);
        root.setBottom(footer);

        popup.getContent().add(root);
        popup.setOnHidden(e -> onHidden.run());

        root.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                onCloseRequest.run();
                e.consume();
            }
        });

        root.setAccessibleText("Notifications");
    }

    public void bind(
            SrotsNotificationPanelViewModel viewModel,
            Consumer<SrotsNotification> onActivate,
            Runnable onCloseRequest,
            Runnable onHidden) {
        unbind();
        this.viewModel = Objects.requireNonNull(viewModel, "viewModel");
        this.onActivate = onActivate == null ? n -> {
        } : onActivate;
        this.onCloseRequest = onCloseRequest == null ? () -> {
        } : onCloseRequest;
        this.onHidden = onHidden == null ? () -> {
        } : onHidden;

        listView.setItems(viewModel.getVisibleNotifications());
        listView.setCellFactory(lv -> new ListCell<>() {
            private final SrotsNotificationItemView itemView =
                    new SrotsNotificationItemView(viewModel.getTimestampFormatter());

            {
                setOnMouseClicked(e -> {
                    SrotsNotification item = getItem();
                    if (item != null && e.getClickCount() == 1) {
                        SrotsNotificationPanel.this.onActivate.accept(item);
                    }
                });
                setOnKeyPressed(e -> {
                    if ((e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.SPACE) && getItem() != null) {
                        SrotsNotificationPanel.this.onActivate.accept(getItem());
                        e.consume();
                    }
                });
            }

            @Override
            protected void updateItem(SrotsNotification item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                    setAccessibleText(null);
                    return;
                }
                itemView.setNotification(item);
                setGraphic(itemView);
                setText(null);
                setAccessibleText(itemView.getAccessibleText());
            }
        });

        allFilter.setOnAction(e -> viewModel.setFilter(NotificationFilter.ALL));
        unreadFilter.setOnAction(e -> viewModel.setFilter(NotificationFilter.UNREAD));
        markAllButton.setOnAction(e -> viewModel.markAllAsRead());
        retryButton.setOnAction(e -> viewModel.refresh());

        refreshListener = obs -> refreshChrome();
        viewModel.unreadCountProperty().addListener(refreshListener);
        viewModel.loadingProperty().addListener(refreshListener);
        viewModel.errorProperty().addListener(refreshListener);
        viewModel.emptyProperty().addListener(refreshListener);
        viewModel.emptyMessageProperty().addListener(refreshListener);
        viewModel.errorMessageProperty().addListener(refreshListener);
        viewModel.filterProperty().addListener(refreshListener);
        refreshChrome();
    }

    public void unbind() {
        if (viewModel != null && refreshListener != null) {
            viewModel.unreadCountProperty().removeListener(refreshListener);
            viewModel.loadingProperty().removeListener(refreshListener);
            viewModel.errorProperty().removeListener(refreshListener);
            viewModel.emptyProperty().removeListener(refreshListener);
            viewModel.emptyMessageProperty().removeListener(refreshListener);
            viewModel.errorMessageProperty().removeListener(refreshListener);
            viewModel.filterProperty().removeListener(refreshListener);
        }
        refreshListener = null;
        viewModel = null;
        listView.setItems(null);
    }

    public boolean isShowing() {
        return popup.isShowing();
    }

    public void show(Node anchor) {
        if (anchor == null || anchor.getScene() == null || anchor.getScene().getWindow() == null) {
            return;
        }
        Window window = anchor.getScene().getWindow();
        Bounds bounds = anchor.localToScreen(anchor.getBoundsInLocal());
        if (bounds == null) {
            return;
        }
        root.applyCss();
        root.layout();

        double x = bounds.getMaxX() - PANEL_WIDTH;
        double y = bounds.getMaxY() + 4;
        Rectangle2D visual = resolveVisualBounds(bounds);
        if (x < visual.getMinX() + 8) {
            x = visual.getMinX() + 8;
        }
        if (x + PANEL_WIDTH > visual.getMaxX() - 8) {
            x = visual.getMaxX() - PANEL_WIDTH - 8;
        }
        if (y + PANEL_HEIGHT > visual.getMaxY() - 8) {
            y = Math.max(visual.getMinY() + 8, visual.getMaxY() - PANEL_HEIGHT - 8);
        }

        double windowMinX = window.getX() + 8;
        double windowMaxX = window.getX() + window.getWidth() - 8;
        if (x + PANEL_WIDTH > windowMaxX) {
            x = windowMaxX - PANEL_WIDTH;
        }
        if (x < windowMinX) {
            x = windowMinX;
        }

        popup.show(window, x, y);
        listView.requestFocus();
    }

    public void hide() {
        if (popup.isShowing()) {
            popup.hide();
        }
    }

    public Popup getPopup() {
        return popup;
    }

    public BorderPane getRoot() {
        return root;
    }

    public ListView<SrotsNotification> getListView() {
        return listView;
    }

    public Button getMarkAllButton() {
        return markAllButton;
    }

    private void refreshChrome() {
        if (viewModel == null) {
            return;
        }
        int unread = viewModel.getUnreadCount();
        unreadLabel.setText(unread <= 0 ? "" : unread + " unread");
        unreadLabel.setVisible(unread > 0);
        unreadLabel.setManaged(unread > 0);
        markAllButton.setDisable(unread <= 0 || viewModel.loadingProperty().get());
        markAllButton.setVisible(unread > 0 && !viewModel.errorProperty().get());
        markAllButton.setManaged(markAllButton.isVisible());

        boolean loading = viewModel.loadingProperty().get();
        boolean err = viewModel.errorProperty().get();
        boolean empty = viewModel.emptyProperty().get();

        loadingLabel.setVisible(loading);
        loadingLabel.setManaged(loading);
        errorBox.setVisible(err);
        errorBox.setManaged(err);
        emptyBox.setVisible(empty && !loading && !err);
        emptyBox.setManaged(emptyBox.isVisible());
        listView.setVisible(!loading && !err && !empty);
        listView.setManaged(listView.isVisible());

        emptyLabel.setText(viewModel.emptyMessageProperty().get());
        errorLabel.setText(viewModel.errorMessageProperty().get().isBlank()
                ? "Unable to load notifications."
                : viewModel.errorMessageProperty().get());

        if (viewModel.getFilter() == NotificationFilter.UNREAD) {
            unreadFilter.setSelected(true);
        } else {
            allFilter.setSelected(true);
        }
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
