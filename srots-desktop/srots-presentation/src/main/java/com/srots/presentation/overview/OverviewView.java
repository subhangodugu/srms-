package com.srots.presentation.overview;

import com.srots.application.dto.ActivityItemDTO;
import com.srots.application.dto.DashboardOverviewDTO;
import com.srots.application.dto.NamedCountDTO;
import com.srots.presentation.components.charts.SrotsBarChart;
import com.srots.presentation.components.charts.SrotsProgressChart;
import com.srots.presentation.components.feedback.empty.SrotsEmptyState;
import com.srots.presentation.components.information.activity.SrotsActivityFeed;
import com.srots.presentation.components.information.activity.SrotsActivityItem;
import com.srots.presentation.components.information.kpi.SrotsKpiCard;
import com.srots.presentation.navigation.model.NavigationRouteId;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Executive Overview workspace — KPI command center, analytics, activity, quick actions.
 * Presentation only; data via {@link OverviewViewModel}.
 */
public final class OverviewView extends StackPane {

    private static final DateTimeFormatter STAMP = DateTimeFormatter.ofPattern("MMM d, HH:mm");

    private final OverviewViewModel viewModel;
    private final Consumer<NavigationRouteId> navigator;

    private final Label greetingLabel = new Label();
    private final Label subtitleLabel = new Label();
    private final Label metaLabel = new Label();
    private final GridPane kpiGrid = new GridPane();
    private final SrotsBarChart workloadChart = new SrotsBarChart("Current workload");
    private final SrotsBarChart healthChart = new SrotsBarChart("Project health");
    private final SrotsBarChart issueChart = new SrotsBarChart("Issue status");
    private final SrotsProgressChart readinessChart = new SrotsProgressChart("Release readiness");
    private final SrotsProgressChart qaChart = new SrotsProgressChart("Pipeline quality");
    private final SrotsActivityFeed activityFeed = new SrotsActivityFeed();
    private final VBox contentRoot = new VBox(16);
    private final VBox loadingPane = new VBox(8);
    private final VBox errorPane = new VBox(12);
    private final Label errorLabel = new Label();
    private final SrotsEmptyState emptyState = new SrotsEmptyState(
            "No operational data yet",
            "Seed mock data or connect a live source to populate the executive overview.");

    private final SrotsKpiCard projectsKpi = new SrotsKpiCard("Active projects", "—");
    private final SrotsKpiCard tasksKpi = new SrotsKpiCard("Open tasks", "—");
    private final SrotsKpiCard issuesKpi = new SrotsKpiCard("Open issues", "—");
    private final SrotsKpiCard employeesKpi = new SrotsKpiCard("Employees", "—");
    private final SrotsKpiCard teamsKpi = new SrotsKpiCard("Active teams", "—");
    private final SrotsKpiCard completedKpi = new SrotsKpiCard("Completed tasks", "—");
    private final SrotsKpiCard readinessKpi = new SrotsKpiCard("Release readiness", "—");
    private final SrotsKpiCard qaKpi = new SrotsKpiCard("QA coverage", "—");

    public OverviewView(OverviewViewModel viewModel, Consumer<NavigationRouteId> navigator) {
        this.viewModel = Objects.requireNonNull(viewModel, "viewModel");
        this.navigator = navigator == null ? route -> { } : navigator;

        getStyleClass().addAll("srots-page", "srots-overview");
        setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        buildChrome();
        buildLoading();
        buildError();

        ScrollPane scroll = new ScrollPane(contentRoot);
        scroll.getStyleClass().add("srots-overview-scroll");
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        getChildren().addAll(scroll, loadingPane, errorPane, emptyState);
        StackPane.setAlignment(loadingPane, Pos.CENTER);
        StackPane.setAlignment(errorPane, Pos.CENTER);
        StackPane.setAlignment(emptyState, Pos.CENTER);

        ChangeListener<OverviewViewModel.LoadState> stateListener =
                (obs, o, state) -> applyState(state == null ? OverviewViewModel.LoadState.IDLE : state);
        viewModel.loadStateProperty().addListener(stateListener);
        viewModel.overviewProperty().addListener((obs, o, dto) -> bindOverview(dto));
        viewModel.greetingProperty().addListener((obs, o, v) -> greetingLabel.setText(v == null ? "" : v));
        viewModel.subtitleProperty().addListener((obs, o, v) -> subtitleLabel.setText(v == null ? "" : v));
        viewModel.metaLineProperty().addListener((obs, o, v) -> metaLabel.setText(v == null ? "" : v));
        viewModel.errorMessageProperty().addListener((obs, o, v) -> errorLabel.setText(v == null ? "" : v));

        greetingLabel.setText(viewModel.greetingProperty().get());
        subtitleLabel.setText(viewModel.subtitleProperty().get());
        metaLabel.setText(viewModel.metaLineProperty().get());
        applyState(viewModel.getLoadState());
    }

    public void load() {
        viewModel.load();
    }

    public OverviewViewModel getViewModel() {
        return viewModel;
    }

    private void buildChrome() {
        contentRoot.getStyleClass().add("srots-dashboard");
        contentRoot.setPadding(new Insets(24));
        contentRoot.setFillWidth(true);

        subtitleLabel.getStyleClass().add("srots-overview-kicker");
        greetingLabel.getStyleClass().add("srots-overview-greeting");
        greetingLabel.setWrapText(true);
        metaLabel.getStyleClass().add("srots-overview-meta");
        metaLabel.setWrapText(true);

        VBox header = new VBox(6, subtitleLabel, greetingLabel, metaLabel);
        header.getStyleClass().add("srots-overview-header");

        kpiGrid.getStyleClass().add("srots-dashboard-kpi-row");
        kpiGrid.setHgap(12);
        kpiGrid.setVgap(12);
        for (int i = 0; i < 4; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(25);
            col.setHgrow(Priority.ALWAYS);
            kpiGrid.getColumnConstraints().add(col);
        }
        kpiGrid.add(projectsKpi, 0, 0);
        kpiGrid.add(tasksKpi, 1, 0);
        kpiGrid.add(issuesKpi, 2, 0);
        kpiGrid.add(employeesKpi, 3, 0);
        kpiGrid.add(teamsKpi, 0, 1);
        kpiGrid.add(completedKpi, 1, 1);
        kpiGrid.add(readinessKpi, 2, 1);
        kpiGrid.add(qaKpi, 3, 1);

        HBox analyticsRow = new HBox(12, workloadChart, healthChart, issueChart);
        analyticsRow.getStyleClass().add("srots-dashboard-main");
        HBox.setHgrow(workloadChart, Priority.ALWAYS);
        HBox.setHgrow(healthChart, Priority.ALWAYS);
        HBox.setHgrow(issueChart, Priority.ALWAYS);
        workloadChart.setMaxWidth(Double.MAX_VALUE);
        healthChart.setMaxWidth(Double.MAX_VALUE);
        issueChart.setMaxWidth(Double.MAX_VALUE);

        HBox progressRow = new HBox(12, readinessChart, qaChart);
        HBox.setHgrow(readinessChart, Priority.ALWAYS);
        HBox.setHgrow(qaChart, Priority.ALWAYS);

        Label activityTitle = new Label("Recent activity");
        activityTitle.getStyleClass().add("srots-section-title");
        VBox activityPanel = new VBox(12, activityTitle, activityFeed);
        activityPanel.getStyleClass().add("srots-dashboard-activity");
        HBox.setHgrow(activityPanel, Priority.ALWAYS);

        Label actionsTitle = new Label("Quick actions");
        actionsTitle.getStyleClass().add("srots-section-title");
        HBox actions = new HBox(8,
                actionButton("Create task", NavigationRouteId.WORKSPACE_TASKS),
                actionButton("Create project", NavigationRouteId.PROJECTS),
                actionButton("Create issue", NavigationRouteId.ISSUES),
                actionButton("Employees", NavigationRouteId.COMPANY_EMPLOYEES),
                actionButton("Teams", NavigationRouteId.COMPANY_TEAMS));
        actions.getStyleClass().add("srots-overview-actions");
        VBox actionsPanel = new VBox(12, actionsTitle, actions);
        actionsPanel.getStyleClass().add("srots-dashboard-chart-panel");

        HBox bottom = new HBox(12, activityPanel, actionsPanel);
        HBox.setHgrow(activityPanel, Priority.ALWAYS);
        HBox.setHgrow(actionsPanel, Priority.SOMETIMES);

        contentRoot.getChildren().addAll(header, kpiGrid, analyticsRow, progressRow, bottom);
    }

    private Button actionButton(String label, NavigationRouteId route) {
        Button button = new Button(label);
        button.getStyleClass().addAll("srots-secondary-button", "srots-overview-action");
        button.setOnAction(e -> navigator.accept(route));
        button.setAccessibleText(label);
        return button;
    }

    private void buildLoading() {
        loadingPane.getStyleClass().add("srots-loading");
        loadingPane.setAlignment(Pos.CENTER);
        Label title = new Label("Loading executive overview…");
        title.getStyleClass().add("srots-state-title");
        Label hint = new Label("Aggregating projects, tasks, issues, and activity.");
        hint.getStyleClass().add("srots-text-muted");
        loadingPane.getChildren().addAll(title, hint);
        loadingPane.setVisible(false);
        loadingPane.setManaged(false);
    }

    private void buildError() {
        errorPane.getStyleClass().add("srots-error-state");
        errorPane.setAlignment(Pos.CENTER);
        errorPane.setPadding(new Insets(24));
        Label title = new Label("Unable to load overview");
        title.getStyleClass().add("srots-error-title");
        errorLabel.getStyleClass().add("srots-error-description");
        errorLabel.setWrapText(true);
        Button retry = new Button("Retry");
        retry.getStyleClass().addAll("srots-primary-button", "srots-error-action");
        retry.setOnAction(e -> viewModel.retry());
        errorPane.getChildren().addAll(title, errorLabel, retry);
        errorPane.setVisible(false);
        errorPane.setManaged(false);
        emptyState.setVisible(false);
        emptyState.setManaged(false);
    }

    private void applyState(OverviewViewModel.LoadState state) {
        boolean loading = state == OverviewViewModel.LoadState.LOADING;
        boolean error = state == OverviewViewModel.LoadState.ERROR;
        boolean empty = state == OverviewViewModel.LoadState.EMPTY;
        boolean ready = state == OverviewViewModel.LoadState.READY;

        contentRoot.setVisible(ready || empty);
        contentRoot.setManaged(ready || empty);
        loadingPane.setVisible(loading);
        loadingPane.setManaged(loading);
        errorPane.setVisible(error);
        errorPane.setManaged(error);
        emptyState.setVisible(empty);
        emptyState.setManaged(empty);
        if (empty) {
            contentRoot.setVisible(false);
            contentRoot.setManaged(false);
        }
    }

    private void bindOverview(DashboardOverviewDTO dto) {
        if (dto == null) {
            return;
        }
        projectsKpi.setValue(formatCount(dto.getActiveProjects()));
        projectsKpi.setTrend("Current snapshot", SrotsKpiCard.TrendDirection.NEUTRAL);

        tasksKpi.setValue(formatCount(dto.getOpenTasks()));
        tasksKpi.setTrend("Open and in progress", SrotsKpiCard.TrendDirection.NEUTRAL);

        issuesKpi.setValue(formatCount(dto.getOpenIssues()));
        issuesKpi.setTrend("Requires attention", dto.getOpenIssues() > 0
                ? SrotsKpiCard.TrendDirection.DOWN
                : SrotsKpiCard.TrendDirection.NEUTRAL);

        employeesKpi.setValue(formatCount(dto.getActiveEmployees()));
        employeesKpi.setTrend("Active directory", SrotsKpiCard.TrendDirection.NEUTRAL);

        teamsKpi.setValue(formatCount(dto.getActiveTeams()));
        teamsKpi.setTrend("Registered teams", SrotsKpiCard.TrendDirection.NEUTRAL);

        completedKpi.setValue(formatCount(dto.getCompletedTasks()));
        completedKpi.setTrend("Marked done", SrotsKpiCard.TrendDirection.UP);

        readinessKpi.setValue(dto.getReleaseReadinessPercent() + "%");
        readinessKpi.setTrend("Primary release", SrotsKpiCard.TrendDirection.NEUTRAL);

        qaKpi.setValue(dto.getQaCoveragePercent() + "%");
        qaKpi.setTrend("Gate pass rate", SrotsKpiCard.TrendDirection.NEUTRAL);

        workloadChart.setData(toBars(dto.getWorkload(), List.of(
                SrotsBarChart.Series.PRIMARY,
                SrotsBarChart.Series.INFO,
                SrotsBarChart.Series.WARNING)));
        healthChart.setData(toBars(dto.getProjectHealth(), List.of(
                SrotsBarChart.Series.SUCCESS,
                SrotsBarChart.Series.WARNING,
                SrotsBarChart.Series.DANGER,
                SrotsBarChart.Series.NEUTRAL)));
        issueChart.setData(toBars(dto.getIssueBreakdown(), List.of(
                SrotsBarChart.Series.DANGER,
                SrotsBarChart.Series.WARNING,
                SrotsBarChart.Series.SUCCESS)));

        readinessChart.setPercentage(dto.getReleaseReadinessPercent());
        qaChart.setPercentage(dto.getQaCoveragePercent());

        List<SrotsActivityItem> items = new ArrayList<>();
        for (ActivityItemDTO activity : dto.getRecentActivity()) {
            items.add(new SrotsActivityItem(activity.getSummary(), relativeTime(activity.getTimestamp())));
        }
        activityFeed.setItems(items);
    }

    private static List<SrotsBarChart.BarPoint> toBars(
            List<NamedCountDTO> values,
            List<SrotsBarChart.Series> seriesCycle) {
        List<SrotsBarChart.BarPoint> points = new ArrayList<>();
        if (values == null) {
            return points;
        }
        for (int i = 0; i < values.size(); i++) {
            NamedCountDTO value = values.get(i);
            SrotsBarChart.Series series = seriesCycle.get(i % seriesCycle.size());
            points.add(new SrotsBarChart.BarPoint(value.getName(), value.getCount(), series));
        }
        return points;
    }

    private static String formatCount(long value) {
        return String.format("%,d", value);
    }

    private static String relativeTime(LocalDateTime timestamp) {
        if (timestamp == null) {
            return "";
        }
        Duration duration = Duration.between(timestamp, LocalDateTime.now());
        if (duration.isNegative()) {
            return timestamp.format(STAMP);
        }
        long minutes = duration.toMinutes();
        if (minutes < 1) {
            return "Just now";
        }
        if (minutes < 60) {
            return minutes + " min ago";
        }
        long hours = duration.toHours();
        if (hours < 24) {
            return hours + " hr ago";
        }
        long days = duration.toDays();
        if (days < 7) {
            return days + " day" + (days == 1 ? "" : "s") + " ago";
        }
        return timestamp.format(STAMP);
    }
}
