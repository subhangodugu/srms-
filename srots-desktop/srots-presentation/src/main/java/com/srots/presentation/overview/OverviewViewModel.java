package com.srots.presentation.overview;

import com.srots.application.dto.ActivityItemDTO;
import com.srots.application.dto.DashboardOverviewDTO;
import com.srots.application.dto.NamedCountDTO;
import com.srots.application.usecase.dashboard.GetDashboardOverviewUseCase;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Presentation state for the Overview executive workspace.
 * Loads metrics off the FX thread; binds results on the FX thread.
 */
public final class OverviewViewModel {

    public enum LoadState {
        IDLE,
        LOADING,
        READY,
        EMPTY,
        ERROR
    }

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

    private final GetDashboardOverviewUseCase useCase;
    private final AtomicInteger loadGeneration = new AtomicInteger();

    private final ObjectProperty<LoadState> loadState = new SimpleObjectProperty<>(LoadState.IDLE);
    private final StringProperty greeting = new SimpleStringProperty("");
    private final StringProperty subtitle = new SimpleStringProperty("");
    private final StringProperty metaLine = new SimpleStringProperty("");
    private final StringProperty errorMessage = new SimpleStringProperty("");
    private final ObjectProperty<DashboardOverviewDTO> overview =
            new SimpleObjectProperty<>();
    private final ObservableList<NamedCountDTO> workload = FXCollections.observableArrayList();
    private final ObservableList<NamedCountDTO> projectHealth = FXCollections.observableArrayList();
    private final ObservableList<NamedCountDTO> issueBreakdown = FXCollections.observableArrayList();
    private final ObservableList<ActivityItemDTO> recentActivity = FXCollections.observableArrayList();

    public OverviewViewModel(GetDashboardOverviewUseCase useCase) {
        this.useCase = Objects.requireNonNull(useCase, "useCase");
        refreshChromeMeta();
    }

    public void load() {
        int generation = loadGeneration.incrementAndGet();
        loadState.set(LoadState.LOADING);
        errorMessage.set("");
        CompletableFuture.supplyAsync(useCase::execute)
                .whenComplete((dto, error) -> Platform.runLater(() -> {
                    if (generation != loadGeneration.get()) {
                        return;
                    }
                    if (error != null) {
                        overview.set(null);
                        workload.clear();
                        projectHealth.clear();
                        issueBreakdown.clear();
                        recentActivity.clear();
                        errorMessage.set(userFacingError(error));
                        loadState.set(LoadState.ERROR);
                        return;
                    }
                    apply(dto);
                }));
    }

    public void retry() {
        load();
    }

    private void apply(DashboardOverviewDTO dto) {
        refreshChromeMeta();
        if (dto == null) {
            overview.set(null);
            workload.clear();
            projectHealth.clear();
            issueBreakdown.clear();
            recentActivity.clear();
            loadState.set(LoadState.EMPTY);
            return;
        }
        overview.set(dto);
        workload.setAll(dto.getWorkload());
        projectHealth.setAll(dto.getProjectHealth());
        issueBreakdown.setAll(dto.getIssueBreakdown());
        recentActivity.setAll(dto.getRecentActivity());
        boolean empty = dto.getActiveProjects() == 0
                && dto.getOpenTasks() == 0
                && dto.getOpenIssues() == 0
                && dto.getActiveEmployees() == 0;
        loadState.set(empty ? LoadState.EMPTY : LoadState.READY);
    }

    private void refreshChromeMeta() {
        LocalTime now = LocalTime.now();
        String period;
        if (now.getHour() < 12) {
            period = "Good morning";
        } else if (now.getHour() < 17) {
            period = "Good afternoon";
        } else {
            period = "Good evening";
        }
        greeting.set(period + ". Here's what's happening across your organization.");
        subtitle.set("Executive command center");
        metaLine.set(LocalDate.now().format(DATE_FMT)
                + "  ·  "
                + now.format(TIME_FMT)
                + "  ·  Organization status: Operational");
    }

    private static String userFacingError(Throwable error) {
        Throwable root = error;
        while (root.getCause() != null && root.getCause() != root) {
            root = root.getCause();
        }
        String message = root.getMessage();
        if (message == null || message.isBlank()) {
            return "Unable to load the executive overview.";
        }
        if (message.toLowerCase().contains("offline")) {
            return "Connection unavailable. Reconnect and try again.";
        }
        return "Unable to load the executive overview.";
    }

    public ObjectProperty<LoadState> loadStateProperty() {
        return loadState;
    }

    public LoadState getLoadState() {
        return loadState.get();
    }

    public StringProperty greetingProperty() {
        return greeting;
    }

    public StringProperty subtitleProperty() {
        return subtitle;
    }

    public StringProperty metaLineProperty() {
        return metaLine;
    }

    public StringProperty errorMessageProperty() {
        return errorMessage;
    }

    public ObjectProperty<DashboardOverviewDTO> overviewProperty() {
        return overview;
    }

    public DashboardOverviewDTO getOverview() {
        return overview.get();
    }

    public ObservableList<NamedCountDTO> getWorkload() {
        return workload;
    }

    public ObservableList<NamedCountDTO> getProjectHealth() {
        return projectHealth;
    }

    public ObservableList<NamedCountDTO> getIssueBreakdown() {
        return issueBreakdown;
    }

    public ObservableList<ActivityItemDTO> getRecentActivity() {
        return recentActivity;
    }
}
