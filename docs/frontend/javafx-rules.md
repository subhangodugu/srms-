# JavaFX Rules

## Product shape

SROTS is a **native desktop** app (Windows / Linux / macOS via JavaFX).

Use native windows, menus, dialogs, keyboard shortcuts, and desktop navigation.
Do not imitate browser SPA behavior without need.

## Window management

- One primary application window (`AppShell`)
- Controlled modal / non-modal / detail dialogs only when appropriate
- Do not open a new primary window per screen

## Properties

Use JavaFX properties for observable UI state and prefer binding over manual refresh.

```java
private final StringProperty searchText =
        new SimpleStringProperty(this, "searchText", "");

public StringProperty searchTextProperty() { return searchText; }
public String getSearchText() { return searchText.get(); }
public void setSearchText(String value) { searchText.set(value); }
```

Common property types: `StringProperty`, `BooleanProperty`, `ObjectProperty`, `ObservableList`.

## Controllers are thin

Controllers: FXML wiring, event forwarding, lifecycle, binding.

Not: SQL, REST, business calculations, authorization authority, multi-step workflows.

```java
@FXML
private void handleCreateEmployee() {
    viewModel.createEmployee();
}
```

## Views have no business rules

Bad:

```java
if (employee.getRole().equals("ADMIN")) {
    deleteButton.setVisible(true);
}
```

Expose presentation flags/state from the ViewModel instead.

## Navigation

- Always use `NavigationService`
- Strongly typed `NavigationRouteId` (no scattered string routes)
- History stores lightweight route + parameters — never Scene graphs
- Views resolve via registry / factory — feature controllers do not construct other feature graphs

## Layout standard for major screens

```text
PageContainer
├── SrotsPageHeader
├── Toolbar / FilterBar
└── Content (SrotsDataTable, detail, etc.)
```

Use `SrotsEmptyState`, `SrotsErrorState`, `SrotsLoadingState` for data screens.
Use `SrotsKpiCard` for dashboard metrics (consistent size/typography within a group).

## Memory and lifecycle

Avoid static Nodes / ViewModels / Controllers / global `ObservableList`s without strong reason.

For complex screens: `initialize` / `activate` / `deactivate` / `dispose` as needed —
remove listeners, cancel tasks, unsubscribe events on dispose.

Do not force trivial views to implement full lifecycle.

## Feedback

Meaningful actions need feedback (toast, dialog, inline validation).
Prevent duplicate submit while saving / approving / deploying.

## Animation

Optional, subtle, typically 120–180ms. Prefer productivity over spectacle.

## Performance

Remain responsive for large tables, filtering, search, navigation, file ops.
Use pagination, virtualization, filtering, lazy loading — never dump unbounded lists into every screen.
Refresh only affected state — not the entire application.
