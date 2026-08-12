package com.srots.presentation.search;

import com.srots.application.search.SearchResult;
import com.srots.presentation.components.utility.icons.SrotsIcon;
import javafx.beans.InvalidationListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Window;

import java.util.Objects;

/**
 * Global search overlay popup (not a Stage).
 */
public final class SrotsSearchOverlay {

    public static final double WIDTH = 720;
    public static final double HEIGHT = 520;

    private final Popup popup = new Popup();
    private final BorderPane root = new BorderPane();
    private final TextField searchField = new TextField();
    private final Button clearButton = new Button(SrotsIcon.CLOSE.getGlyph());
    private final Label shortcutHint = new Label("Ctrl+K");
    private final HBox filters = new HBox(6);
    private final ToggleGroup filterGroup = new ToggleGroup();
    private final ListView<SrotsSearchListEntry> listView = new ListView<>();
    private final Label footerHint = new Label("↑ ↓ Navigate    Enter Open    Esc Close");
    private final Label statusLabel = new Label();

    private SrotsGlobalSearchViewModel viewModel;
    private InvalidationListener refreshListener;
    private Runnable onCloseRequest = () -> {
    };
    private Runnable onHidden = () -> {
    };

    public SrotsSearchOverlay() {
        popup.setAutoHide(true);
        popup.setAutoFix(true);
        popup.setHideOnEscape(true);

        root.getStyleClass().addAll("srots-search", "srots-search-overlay");
        root.setPrefSize(WIDTH, HEIGHT);
        root.setMaxSize(WIDTH, HEIGHT);
        root.setPadding(new Insets(14));
        root.setAccessibleText("Global search");

        Label searchIcon = new Label(SrotsIcon.SEARCH.getGlyph());
        searchIcon.getStyleClass().add("srots-search-icon");

        searchField.getStyleClass().addAll("srots-text-field", "srots-search-field-input");
        searchField.setPromptText("Search SROTS...");
        searchField.setAccessibleText("Search SROTS");
        HBox.setHgrow(searchField, Priority.ALWAYS);

        clearButton.getStyleClass().addAll("srots-icon-button", "srots-search-clear");
        clearButton.setAccessibleText("Clear search");
        clearButton.setVisible(false);
        clearButton.setManaged(false);

        shortcutHint.getStyleClass().add("srots-caption");

        HBox fieldRow = new HBox(10, searchIcon, searchField, clearButton, shortcutHint);
        fieldRow.setAlignment(Pos.CENTER_LEFT);
        fieldRow.getStyleClass().add("srots-search-field");

        filters.getStyleClass().add("srots-search-filters");
        filters.setPadding(new Insets(10, 0, 8, 0));
        for (SrotsSearchFilter filter : SrotsSearchFilter.values()) {
            ToggleButton chip = new ToggleButton(filter.label());
            chip.setToggleGroup(filterGroup);
            chip.getStyleClass().addAll("srots-secondary-button", "srots-search-filter");
            chip.setUserData(filter);
            chip.setAccessibleText(filter.label() + " filter");
            if (filter == SrotsSearchFilter.ALL) {
                chip.setSelected(true);
            }
            chip.setOnAction(e -> {
                if (viewModel != null) {
                    viewModel.setFilter(filter);
                }
            });
            filters.getChildren().add(chip);
        }

        listView.getStyleClass().add("srots-search-results");
        listView.setFocusTraversable(true);
        VBox.setVgrow(listView, Priority.ALWAYS);

        statusLabel.getStyleClass().add("srots-search-status");
        footerHint.getStyleClass().addAll("srots-caption", "srots-search-footer");

        VBox top = new VBox(fieldRow, filters, statusLabel);
        root.setTop(top);
        root.setCenter(listView);
        root.setBottom(footerHint);
        BorderPane.setAlignment(footerHint, Pos.CENTER_LEFT);
        BorderPane.setMargin(footerHint, new Insets(8, 0, 0, 0));

        popup.getContent().add(root);
        popup.setOnHidden(e -> onHidden.run());

        root.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeys);
        searchField.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeys);
        listView.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeys);
    }

    public void bind(SrotsGlobalSearchViewModel viewModel, Runnable onCloseRequest, Runnable onHidden) {
        unbind();
        this.viewModel = Objects.requireNonNull(viewModel, "viewModel");
        this.onCloseRequest = onCloseRequest == null ? () -> {
        } : onCloseRequest;
        this.onHidden = onHidden == null ? () -> {
        } : onHidden;

        searchField.textProperty().bindBidirectional(viewModel.queryTextProperty());
        listView.setItems(viewModel.getEntries());
        listView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(SrotsSearchListEntry item, boolean empty) {
                super.updateItem(item, empty);
                getStyleClass().removeAll(
                        "srots-search-group",
                        "srots-search-result",
                        "srots-search-command",
                        "srots-search-history",
                        "srots-search-empty",
                        "srots-search-result-selected");
                setGraphic(null);
                if (empty || item == null) {
                    setText(null);
                    setAccessibleText(null);
                    setDisable(false);
                    return;
                }
                switch (item.getKind()) {
                    case GROUP -> {
                        setText(item.getLabel());
                        getStyleClass().add("srots-search-group");
                        setDisable(true);
                    }
                    case HINT -> {
                        setText(item.getLabel());
                        getStyleClass().add("srots-search-empty");
                        setDisable(true);
                    }
                    case RECENT -> {
                        setText(item.getLabel());
                        getStyleClass().add("srots-search-history");
                        setDisable(false);
                        setAccessibleText("Recent search " + item.getLabel());
                    }
                    case COMMAND -> {
                        setText(SrotsIcon.FORWARD.getGlyph() + "  " + item.getLabel());
                        getStyleClass().add("srots-search-command");
                        setDisable(false);
                        setAccessibleText(item.getLabel());
                    }
                    case RESULT -> {
                        SearchResult result = item.getResult();
                        String glyph = SearchResultIconResolver.glyph(result);
                        String subtitle = result == null ? "" : result.getSubtitle();
                        Label title = new Label(glyph + "  " + item.getLabel());
                        title.getStyleClass().add("srots-search-result-title");
                        Label sub = new Label(subtitle);
                        sub.getStyleClass().add("srots-search-result-subtitle");
                        setGraphic(new VBox(2, title, sub));
                        setText(null);
                        getStyleClass().add("srots-search-result");
                        setDisable(false);
                        setAccessibleText(item.getLabel() + (subtitle.isBlank() ? "" : ", " + subtitle));
                    }
                }
                if (getIndex() == viewModel.selectedIndexProperty().get() && item.isSelectable()) {
                    getStyleClass().add("srots-search-result-selected");
                }
            }
        });

        listView.setOnMouseClicked(e -> {
            SrotsSearchListEntry selected = listView.getSelectionModel().getSelectedItem();
            if (selected != null && selected.isSelectable()) {
                viewModel.selectedIndexProperty().set(listView.getSelectionModel().getSelectedIndex());
                if (e.getClickCount() >= 2) {
                    viewModel.activate(selected);
                }
            }
        });

        clearButton.setOnAction(e -> viewModel.clearQuery());

        refreshListener = obs -> refreshChrome();
        viewModel.queryTextProperty().addListener(refreshListener);
        viewModel.searchingProperty().addListener(refreshListener);
        viewModel.statusMessageProperty().addListener(refreshListener);
        viewModel.stateProperty().addListener(refreshListener);
        viewModel.selectedIndexProperty().addListener((obs, o, n) -> {
            if (n != null && n.intValue() >= 0) {
                listView.getSelectionModel().select(n.intValue());
                listView.scrollTo(n.intValue());
            }
            listView.refresh();
        });
        viewModel.filterProperty().addListener((obs, o, n) -> syncFilterChips());
        refreshChrome();
    }

    public void unbind() {
        if (viewModel != null) {
            searchField.textProperty().unbindBidirectional(viewModel.queryTextProperty());
            if (refreshListener != null) {
                viewModel.queryTextProperty().removeListener(refreshListener);
                viewModel.searchingProperty().removeListener(refreshListener);
                viewModel.statusMessageProperty().removeListener(refreshListener);
                viewModel.stateProperty().removeListener(refreshListener);
            }
        }
        refreshListener = null;
        viewModel = null;
        listView.setItems(null);
    }

    public boolean isShowing() {
        return popup.isShowing();
    }

    public void show(Window owner) {
        if (owner == null) {
            return;
        }
        double x = owner.getX() + Math.max(24, (owner.getWidth() - WIDTH) / 2);
        double y = owner.getY() + Math.max(72, owner.getHeight() * 0.12);
        popup.show(owner, x, y);
        searchField.requestFocus();
        searchField.selectAll();
    }

    public void hide() {
        if (popup.isShowing()) {
            popup.hide();
        }
    }

    public TextField getSearchField() {
        return searchField;
    }

    public ListView<SrotsSearchListEntry> getListView() {
        return listView;
    }

    public BorderPane getRoot() {
        return root;
    }

    private void handleKeys(KeyEvent e) {
        if (viewModel == null) {
            return;
        }
        if (e.getCode() == KeyCode.ESCAPE) {
            onCloseRequest.run();
            e.consume();
        } else if (e.getCode() == KeyCode.ENTER) {
            viewModel.activateSelected();
            e.consume();
        } else if (e.getCode() == KeyCode.DOWN) {
            viewModel.moveSelection(1);
            e.consume();
        } else if (e.getCode() == KeyCode.UP) {
            viewModel.moveSelection(-1);
            e.consume();
        }
    }

    private void refreshChrome() {
        if (viewModel == null) {
            return;
        }
        boolean hasText = viewModel.queryTextProperty().get() != null
                && !viewModel.queryTextProperty().get().isBlank();
        clearButton.setVisible(hasText);
        clearButton.setManaged(hasText);
        String status = viewModel.statusMessageProperty().get();
        statusLabel.setText(status == null ? "" : status);
        statusLabel.setVisible(status != null && !status.isBlank());
        statusLabel.setManaged(statusLabel.isVisible());
        if (viewModel.searchingProperty().get()) {
            if (!root.getStyleClass().contains("srots-search-loading")) {
                root.getStyleClass().add("srots-search-loading");
            }
        } else {
            root.getStyleClass().remove("srots-search-loading");
        }
        if (viewModel.stateProperty().get() == SrotsGlobalSearchState.ERROR) {
            if (!root.getStyleClass().contains("srots-search-error")) {
                root.getStyleClass().add("srots-search-error");
            }
        } else {
            root.getStyleClass().remove("srots-search-error");
        }
    }

    private void syncFilterChips() {
        if (viewModel == null) {
            return;
        }
        SrotsSearchFilter current = viewModel.filterProperty().get();
        for (var node : filters.getChildren()) {
            if (node instanceof ToggleButton button && button.getUserData() instanceof SrotsSearchFilter filter) {
                button.setSelected(filter == current);
            }
        }
    }
}
