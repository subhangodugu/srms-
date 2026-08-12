# Table Design System

Tables are a core SROTS pattern for operational work. Prefer clarity and density over decorative chrome.

## Structure

```text
Header
────────────────────────
Row
────────────────────────
Row
────────────────────────
Row
```

Avoid unnecessary vertical grid lines. Use horizontal separators via row borders (`-srots-border`).

## Style classes

| Element | Class / selector |
|---------|------------------|
| Table | `.srots-table` |
| Header background | `.column-header-background` |
| Header label | secondary, 12 px, bold |
| Row | `.table-row-cell` |
| Cell | `.table-cell` — primary text, 13 px |

Chrome: elevated surface, 1 px border, 8 px radius.

## Row density

| Density | Row height | When |
|---------|------------|------|
| Compact | **36 px** | Dense ops lists |
| **Standard** (default) | **44 px** | Default (`-fx-cell-size: 44px`) |
| Comfortable | **52 px** | Review / accessibility preference |

Allow future user preference; ship Standard by default.

Header height ≈ 40 px.

## Interaction states

| State | Token / behavior |
|-------|------------------|
| Hover | `-srots-surface-hover` |
| Selected | `-srots-surface-selected` |
| Focused | Visible focus on active cell/row |
| Disabled row | Muted text; non-selectable if policy requires |

## Required table capabilities

- Clear header
- Consistent row height
- Hover & selection
- Status badges
- Sorting
- Filtering (via FilterBar)
- Pagination where needed
- Empty / loading / error states

## Row actions

Do not overcrowd rows with buttons.

```text
John Doe · Backend Developer · Active                    •••
```

- Overflow menu (`•••`) for secondary actions
- At most one obvious primary inline action when justified
- Destructive actions confirm via dialog

## Status badges

One reusable badge system for all modules:

```text
● Active · ● Pending · ● Approved · ● Blocked · ● Failed
● Production · ● Development · ● Staging
```

| Class | Meaning |
|-------|---------|
| `.srots-badge-success` / `.badge-success` | Active, Approved, Passed |
| `.srots-badge-warning` / `.badge-warning` | Pending, At risk |
| `.srots-badge-danger` / `.badge-danger` | Blocked, Failed |
| `.srots-badge-info` / `.badge-info` | Informational / staging |

Soft background + semantic text; pill radius. Always include text (and preferably a symbol), not color alone.

## Empty / loading / error

Never show a blank table:

```text
No releases yet
Create your first product release to begin tracking versions…
[Create Release]
```

```text
Rows loading...
```

```text
Unable to load releases
The release service could not be reached.
[Retry]
```

## Pagination & sort

- Pagination below the table; keep control density Standard.
- Sort indicators in headers; do not invent per-module header styles.

## CSS source

`tables.css`, badge styles in `components.css`
