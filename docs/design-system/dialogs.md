# Dialog & Overlay System

Reusable modal and side-panel patterns. Prefer non-modal drawers for detail inspection; reserve dialogs for confirmation, short forms, and critical information.

## Dialog chrome

| Property | Value |
|----------|--------|
| Class | `.srots-dialog` |
| Background | `-srots-surface-elevated` |
| Border | `-srots-border-strong` |
| Radius | **10–12 px** (`-srots-radius-lg`) |
| Padding | **24 px** |

| Part | Class |
|------|-------|
| Header | `.srots-dialog-header` |
| Footer | `.srots-dialog-footer` |

Header/footer use 1 px border separators.

## Dialog types

### Confirmation

```text
Delete Product?

This action cannot be undone.

[Cancel] [Delete]
```

- Cancel = secondary
- Confirm destructive = danger button
- Esc closes (cancel)
- Focus moves into the dialog; restore on close

### Form dialog

```text
Create Team

Name        [................]
Department  [................]

[Cancel] [Create]
```

- Same field system as [forms.md](./forms.md)
- One primary submit
- Disable submit while loading

### Information dialog

Use sparingly for important information only. Prefer inline alerts / toasts for routine feedback.

## Drawer / detail panel

Class: `.srots-detail-panel`

```text
Main Table                    Detail
─────────────────────        ┌───────────────────────┐
Employee 1                    │ Employee Details      │
Employee 2                    │ Name / Role / Team…   │
Employee 3              →    │ Status                │
                              └───────────────────────┘
```

| Property | Value |
|----------|--------|
| Background | elevated surface |
| Border | left 1 px `-srots-border` |
| Padding | ~20 px |

Use drawers to reduce navigation hops. Close via Esc, explicit close control, or deselection policy defined by the view model.

## Overlay components (catalog)

| Component | Role |
|-----------|------|
| `Dialog` | General modal shell |
| `ConfirmationDialog` | Confirm / cancel |
| `Drawer` | Side detail |
| `Tooltip` | Icon / truncated help |
| `Popover` | Lightweight anchored content (filters, previews) |

## Motion

Dialog/drawer appearance: **120–220 ms**, subtle opacity/slide. See [animation.md](./animation.md).

## Rules

1. Avoid stacking multiple modals.
2. Do not use dialogs for every CRUD navigate — prefer pages or drawers for long forms.
3. Never put business logic in CSS; controllers/view-models own open/close and results.
4. Permission-denied overlays use Error/Empty patterns with clear copy — no stack traces.

## CSS source

`dialogs.css`, tooltips in `feedback.css`
