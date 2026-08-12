# Component Rules

## Reuse first

Before creating UI:

```text
Does SROTS already have this component?
```

If yes — reuse it.

Do not create `EmployeeButton` / `ProjectButton` / `ComptyButton` when `SrotsButton` exists.

## When to create a new reusable component

Only when it:

- Appears in multiple places, **or**
- Represents an important reusable interaction, **or**
- Solves a consistent design-system requirement

Do not wrap every `VBox` as a named component.

## Preferred building blocks

| Need | Use |
|------|-----|
| Page chrome | `SrotsPageHeader`, shell layout |
| Tables | `SrotsDataTable` |
| KPIs | `SrotsKpiCard` |
| Empty / error / loading | `SrotsEmptyState`, `SrotsErrorState`, `SrotsLoadingState` |
| Forms / search / filters | existing `Srots*` form/search/filter components |
| Dialogs / toasts | existing overlay/feedback components |

## New component checklist

- [ ] Existing SROTS component already solves this?
- [ ] Reusable across modules?
- [ ] Belongs in the design system?
- [ ] Clear single responsibility?
- [ ] Custom control needed, or standard JavaFX enough?
- [ ] FXML needed?
- [ ] ViewModel needed?
- [ ] Tests required?

## No lazy placeholders

Do not ship fake buttons/tables/hard-coded KPIs as “feature complete”
unless they belong to the design-system showcase or mock-data architecture.

If a feature is unfinished, represent its state honestly (empty / disabled / coming soon).
