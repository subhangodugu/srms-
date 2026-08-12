package com.srots.presentation.designsystem;

import com.srots.presentation.components.actions.button.SrotsButton;
import com.srots.presentation.components.actions.icon.SrotsIconButton;
import com.srots.presentation.components.charts.SrotsBarChart;
import com.srots.presentation.components.charts.SrotsProgressChart;
import com.srots.presentation.components.data.filter.SrotsFilterBar;
import com.srots.presentation.components.data.filter.SrotsFilterChip;
import com.srots.presentation.components.data.filter.SrotsFilterDropdown;
import com.srots.presentation.components.data.pagination.PaginationState;
import com.srots.presentation.components.data.pagination.SrotsPagination;
import com.srots.presentation.components.data.search.SrotsSearchField;
import com.srots.presentation.components.data.table.SrotsDataTable;
import com.srots.presentation.components.data.table.SrotsTableDensity;
import com.srots.presentation.components.feedback.alert.SrotsAlert;
import com.srots.presentation.components.feedback.empty.SrotsEmptyState;
import com.srots.presentation.components.feedback.error.SrotsErrorState;
import com.srots.presentation.components.feedback.loading.SrotsLoadingState;
import com.srots.presentation.components.feedback.offline.SrotsOfflineState;
import com.srots.presentation.components.feedback.toast.SrotsToastManager;
import com.srots.presentation.components.forms.field.SrotsFormField;
import com.srots.presentation.components.forms.input.SrotsTextField;
import com.srots.presentation.components.information.activity.SrotsActivityFeed;
import com.srots.presentation.components.information.activity.SrotsActivityItem;
import com.srots.presentation.components.information.avatar.SrotsAvatar;
import com.srots.presentation.components.information.avatar.SrotsUserProfile;
import com.srots.presentation.components.information.badge.SrotsStatus;
import com.srots.presentation.components.information.badge.SrotsStatusBadge;
import com.srots.presentation.components.information.card.SrotsCard;
import com.srots.presentation.components.information.kpi.SrotsKpiCard;
import com.srots.presentation.components.information.metric.SrotsMetric;
import com.srots.presentation.components.information.timeline.SrotsTimeline;
import com.srots.presentation.components.information.timeline.SrotsTimelineItem;
import com.srots.presentation.components.layout.page.SrotsPageHeader;
import com.srots.presentation.components.layout.page.SrotsSection;
import com.srots.presentation.components.navigation.tabs.SrotsTabView;
import com.srots.presentation.components.overlays.dialog.SrotsConfirmationDialog;
import com.srots.presentation.components.overlays.drawer.SrotsDetailPanel;
import com.srots.presentation.components.utility.icons.SrotsIcon;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * Internal showcase for the reusable SROTS component library (Prompt 07).
 * Demo/mock data only — not an end-user business screen.
 */
public class DesignSystemShowcaseView extends StackPane {

    private final SrotsToastManager toastManager = new SrotsToastManager();

    public DesignSystemShowcaseView() {
        VBox scrollContent = new VBox(8);
        scrollContent.setPadding(new Insets(0, 8, 24, 0));

        SrotsPageHeader header = new SrotsPageHeader(
                "SROTS / Design System",
                "SROTS JavaFX CSS Theme",
                "Prompt 06 theme tokens and component styles — the visual source of truth for all screens."
        ).withPrimaryAction("Show Toast", () -> toastManager.showSuccess("Theme loaded successfully."));

        scrollContent.getChildren().addAll(
                header,
                section("Colors", colorSwatches()),
                section("Typography", typographySample()),
                section("Buttons", buttonsSample()),
                section("Forms", formsSample()),
                section("Search & Filters", filterSample()),
                section("KPI & Metrics", kpiSample()),
                section("Badges", badgesSample()),
                section("Table & Pagination", tableSample()),
                section("Tabs", tabsSample()),
                section("Timeline & Activity", timelineSample()),
                section("Avatar & Profile", avatarSample()),
                section("Cards & Detail", cardSample()),
                section("Charts", chartSample()),
                section("Alerts & States", statesSample())
        );

        javafx.scene.control.ScrollPane scroll = new javafx.scene.control.ScrollPane(scrollContent);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(javafx.scene.control.ScrollPane.ScrollBarPolicy.NEVER);
        scroll.getStyleClass().add("srots-content");

        getChildren().add(scroll);
        toastManager.attach(this);
        getStyleClass().add("srots-page");
    }

    private VBox section(String title, Node content) {
        return new SrotsSection(title, content);
    }

    private FlowPane colorSwatches() {
        FlowPane pane = new FlowPane(12, 12);
        pane.getChildren().addAll(
                swatch("Background", "srots-swatch-bg"),
                swatch("Surface", "srots-swatch-surface"),
                swatch("Elevated", "srots-swatch-elevated"),
                swatch("Border", "srots-swatch-border"),
                swatch("Primary", "srots-swatch-primary"),
                swatch("Success", "srots-swatch-success"),
                swatch("Warning", "srots-swatch-warning"),
                swatch("Danger", "srots-swatch-danger"),
                swatch("Info", "srots-swatch-info")
        );
        return pane;
    }

    private StackPane swatch(String name, String swatchClass) {
        Region rect = new Region();
        rect.getStyleClass().addAll("srots-showcase-swatch", swatchClass);
        rect.setMinSize(110, 72);
        rect.setPrefSize(110, 72);
        Label label = new Label(name);
        label.getStyleClass().add("srots-swatch-label");
        StackPane stack = new StackPane(rect, label);
        StackPane.setAlignment(label, javafx.geometry.Pos.BOTTOM_LEFT);
        StackPane.setMargin(label, new Insets(8));
        return stack;
    }

    private VBox typographySample() {
        VBox box = new VBox(8);
        Label display = new Label("Display 32 — Enterprise Control");
        display.getStyleClass().add("srots-display");
        Label page = new Label("Page Title 24");
        page.getStyleClass().add("srots-page-title");
        Label sectionTitle = new Label("Section Title 18");
        sectionTitle.getStyleClass().add("srots-section-title");
        Label card = new Label("Card Title 15");
        card.getStyleClass().add("srots-card-title");
        Label body = new Label("Body 13 — Information-dense enterprise content.");
        body.getStyleClass().add("srots-body");
        Label caption = new Label("Caption 11 — Supporting metadata");
        caption.getStyleClass().add("srots-caption");
        box.getChildren().addAll(display, page, sectionTitle, card, body, caption);
        return box;
    }

    private HBox buttonsSample() {
        SrotsButton primary = SrotsButton.primary("Create Release");
        SrotsButton secondary = SrotsButton.secondary("Cancel");
        SrotsButton tertiary = SrotsButton.tertiary("Learn more");
        SrotsButton danger = SrotsButton.danger("Delete");
        danger.setOnAction(e -> SrotsConfirmationDialog.show(
                getScene() == null ? null : getScene().getWindow(),
                "Delete Release?",
                "This action cannot be undone.",
                "Delete",
                true,
                () -> toastManager.showWarning("Delete confirmed (demo)."),
                () -> {}
        ));
        SrotsButton loading = SrotsButton.primary("Creating...");
        loading.setLoading(true);

        SrotsIconButton refresh = new SrotsIconButton(SrotsIcon.REFRESH.getGlyph(), "Refresh", "Refresh data");
        refresh.setTooltip(new Tooltip("Refresh"));

        SrotsButton disabled = SrotsButton.secondary("Disabled");
        disabled.setDisable(true);

        return new HBox(12, primary, secondary, tertiary, danger, loading, disabled, refresh);
    }

    private VBox formsSample() {
        SrotsTextField name = new SrotsTextField();
        name.setPromptText("COMPTY");
        SrotsFormField productName = new SrotsFormField("Product Name", name, true, "Official product display name.");

        SrotsTextField code = new SrotsTextField();
        code.setPromptText("COMPTY-ATE");
        SrotsFormField productCode = new SrotsFormField("Product Code", code, true, "3–30 characters.");
        productCode.setError("Product code must contain 3–30 characters.");

        return new VBox(16, productName, productCode);
    }

    private VBox filterSample() {
        SrotsSearchField search = new SrotsSearchField("Search components...");
        search.setOnSearch(q -> toastManager.showInfo("Search: " + q));

        SrotsFilterDropdown<String> status = new SrotsFilterDropdown<>("Status");
        status.setItems(FXCollections.observableArrayList("Active", "Pending", "Failed"));

        SrotsFilterBar bar = new SrotsFilterBar();
        bar.setSearchField(search);
        bar.addFilter(status);
        bar.setActiveFilters(List.of(
                new SrotsFilterChip("Backend", () -> {}),
                new SrotsFilterChip("Active", () -> {})
        ));
        bar.onClearAll(() -> toastManager.showInfo("Filters cleared"));
        return new VBox(bar);
    }

    private GridPane kpiSample() {
        GridPane grid = new GridPane();
        grid.setHgap(16);
        grid.setVgap(16);
        SrotsKpiCard a = new SrotsKpiCard("Active Projects", "18", "↑ 12% vs previous period", SrotsKpiCard.TrendDirection.UP);
        SrotsKpiCard b = new SrotsKpiCard("Open Issues", "42", "↓ 5% vs previous period", SrotsKpiCard.TrendDirection.DOWN);
        SrotsKpiCard c = new SrotsKpiCard("Release Gates", "6", "No change", SrotsKpiCard.TrendDirection.NEUTRAL);
        SrotsMetric metric = new SrotsMetric("QA Coverage", "94.8%");
        for (Node n : List.of(a, b, c)) {
            GridPane.setHgrow(n, Priority.ALWAYS);
            if (n instanceof SrotsKpiCard card) {
                card.setMaxWidth(Double.MAX_VALUE);
                card.setPrefWidth(180);
            }
        }
        grid.addRow(0, a, b, c, metric);
        return grid;
    }

    private FlowPane badgesSample() {
        FlowPane pane = new FlowPane(8, 8);
        for (SrotsStatus status : List.of(
                SrotsStatus.ACTIVE, SrotsStatus.PENDING, SrotsStatus.FAILED,
                SrotsStatus.APPROVED, SrotsStatus.PRODUCTION, SrotsStatus.STAGING,
                SrotsStatus.BLOCKED, SrotsStatus.WARNING)) {
            pane.getChildren().add(new SrotsStatusBadge(status));
        }
        return pane;
    }

    private VBox tableSample() {
        SrotsDataTable<DemoRow> table = new SrotsDataTable<>();
        table.setDensity(SrotsTableDensity.STANDARD);
        table.setPrefHeight(160);

        TableColumn<DemoRow, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().name()));
        TableColumn<DemoRow, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().role()));
        TableColumn<DemoRow, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().status()));
        table.getTableView().getColumns().addAll(nameCol, roleCol, statusCol);
        table.setItems(FXCollections.observableArrayList(
                new DemoRow("Ada Lovelace", "Product Manager", "Active"),
                new DemoRow("Alan Turing", "Backend Developer", "Active"),
                new DemoRow("Grace Hopper", "Release Engineer", "Pending")
        ));

        PaginationState state = new PaginationState(0, 10, 35);
        SrotsPagination pagination = new SrotsPagination();
        pagination.bind(state);
        pagination.setOnPageChange(page -> toastManager.showInfo("Page " + (page + 1)));

        return new VBox(12, table, pagination);
    }

    private SrotsTabView tabsSample() {
        SrotsTabView tabs = new SrotsTabView();
        tabs.addTab("Overview", new Label("Overview content uses shared components."));
        tabs.addTab("Requirements", new Label("Requirements tab"));
        tabs.addTab("Releases", new Label("Releases tab"));
        tabs.setPrefHeight(100);
        return tabs;
    }

    private HBox timelineSample() {
        SrotsTimeline timeline = new SrotsTimeline();
        timeline.setItems(List.of(
                new SrotsTimelineItem("Development", SrotsTimelineItem.State.DONE),
                new SrotsTimelineItem("QA", SrotsTimelineItem.State.DONE),
                new SrotsTimelineItem("Security", SrotsTimelineItem.State.DONE),
                new SrotsTimelineItem("Database", SrotsTimelineItem.State.WARN),
                new SrotsTimelineItem("Approval", SrotsTimelineItem.State.PENDING),
                new SrotsTimelineItem("Production", SrotsTimelineItem.State.PENDING)
        ));

        SrotsActivityFeed feed = new SrotsActivityFeed();
        feed.setItems(List.of(
                new SrotsActivityItem("John updated COMPTY v1.9.0", "5 minutes ago"),
                new SrotsActivityItem("QA approved release candidate", "18 minutes ago")
        ));
        feed.setPrefWidth(320);
        return new HBox(24, timeline, feed);
    }

    private HBox avatarSample() {
        SrotsAvatar avatar = new SrotsAvatar("Ada Lovelace");
        SrotsUserProfile profile = new SrotsUserProfile("Ada Lovelace", "Product Manager");
        return new HBox(16, avatar, profile);
    }

    private HBox cardSample() {
        SrotsCard card = new SrotsCard();
        card.setTitle("Component Card");
        card.setSubtitle("Header / content / footer pattern");
        card.setContent(new Label("Feature screens compose this card — they do not restyle it."));
        card.setFooter(SrotsButton.secondary("Action"));

        SrotsDetailPanel detail = new SrotsDetailPanel();
        detail.setTitle("Detail Panel");
        detail.setContent(new Label("Table → detail drawer pattern."));
        detail.setOnClose(() -> toastManager.showInfo("Detail closed"));
        detail.setPrefWidth(260);

        return new HBox(16, card, detail);
    }

    private HBox chartSample() {
        SrotsBarChart bars = new SrotsBarChart("Release throughput");
        bars.setData(List.of(
                new SrotsBarChart.BarPoint("Dev", 12, SrotsBarChart.Series.PRIMARY),
                new SrotsBarChart.BarPoint("QA", 9, SrotsBarChart.Series.SUCCESS),
                new SrotsBarChart.BarPoint("Prod", 7, SrotsBarChart.Series.WARNING)
        ));
        SrotsProgressChart progress = new SrotsProgressChart("Gate completion");
        progress.setProgress(0.62);
        return new HBox(16, bars, progress);
    }

    private VBox statesSample() {
        SrotsAlert info = SrotsAlert.of(
                SrotsAlert.Variant.INFO,
                "Design tokens are active.",
                "All components consume Prompt 06 CSS."
        );
        SrotsEmptyState empty = new SrotsEmptyState(
                "No projects found",
                "Create a project to begin organizing company work."
        );
        empty.setPrimaryAction("Create Project", () -> toastManager.showSuccess("Create Project (demo)"));
        SrotsErrorState error = new SrotsErrorState(
                "Unable to load projects",
                "The project service is currently unavailable.",
                () -> toastManager.showWarning("Retry (demo)")
        );
        SrotsLoadingState loading = new SrotsLoadingState("Loading product information...");
        SrotsOfflineState offline = new SrotsOfflineState(SrotsOfflineState.OfflineMode.LOCAL_CHANGES);

        HBox row = new HBox(12, empty, error, loading, offline);
        for (Node n : row.getChildren()) {
            if (!n.getStyleClass().contains("srots-pref-width-sm")) {
                n.getStyleClass().add("srots-pref-width-sm");
            }
        }
        return new VBox(12, info, row);
    }

    private record DemoRow(String name, String role, String status) {}
}
