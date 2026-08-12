# Tables

## Purpose

Dense data presentation: themed tables, row actions, pagination, filter bars, and search. Components render and collect UI state; querying and sorting rules live in ViewModels.

## Usage

| Area | Package |
|------|---------|
| Table | `com.srots.presentation.components.data.table` |
| Pagination | `com.srots.presentation.components.data.pagination` |
| Filter | `com.srots.presentation.components.data.filter` |
| Search | `com.srots.presentation.components.data.search` |

CSS: `.srots-table` (`SrotsStyleClasses.TABLE`). Densities: Compact 36 / Standard 44 / Comfortable 52.

## Key classes

| Class | Role |
|-------|------|
| `SrotsDataTable<T>` | Themed `TableView` host with loading/empty/multi-select |
| `SrotsRowActions` | Per-row action cluster |
| `SrotsTableDensity` | Row density enum |
| `SrotsPagination` | Page controls |
| `PaginationState` | Page index / size / total model |
| `SrotsFilterBar` | Filter toolbar host |
| `SrotsFilterChip` | Active filter chip |
| `SrotsFilterDropdown` | Filter menu |
| `FilterState` | Selected filter values |
| `SrotsSearchField` | Scoped search input |

## Properties

| API | Notes |
|-----|--------|
| `setItems(ObservableList<T>)` | Row data |
| `setDensity(SrotsTableDensity)` | Row height class |
| `setLoading(boolean)` | Overlay / busy presentation |
| `setEmptyMessage(String)` | Empty copy when no rows |
| `enableMultiSelect(boolean)` | Multi selection mode |
| Pagination / filter / search | Bind state objects; refresh via ViewModel |

## States

`Default · Loading · Empty · Populated · Row hover/selected · Disabled controls`

Combine with [feedback](./feedback.md) empty/error panels when the whole page fails.

## Events

- Selection: `getSelectionModel()`
- Sort: column comparators / sort listeners → ViewModel
- Page change / filter / search text: listen and reload data outside the component

## Accessibility

- Column headers remain focusable for sort where enabled.
- Row actions: icon buttons need tooltips and accessible names.
- Announce empty and loading via visible text, not spinner alone.
- Keep horizontal scroll minimal; prefer priority columns.

## Do / Don't

| Do | Don't |
|----|-------|
| Use `SrotsDataTable` as the only table pattern | Theme raw `TableView` per screen |
| Drive filters through `FilterState` | Embed repository queries in the table class |
| Pair table + pagination + filter bar | Infinite ad-hoc toolbars with mixed controls |

## Example

```java
SrotsDataTable<CompanyRow> table = new SrotsDataTable<>();
table.setDensity(SrotsTableDensity.STANDARD);
table.setEmptyMessage("No companies found");
table.setItems(viewModel.getRows());

SrotsSearchField search = new SrotsSearchField();
search.textProperty().addListener((o, a, q) -> viewModel.setQuery(q));

SrotsPagination pager = new SrotsPagination();
pager.stateProperty().bindBidirectional(viewModel.paginationStateProperty());
```
