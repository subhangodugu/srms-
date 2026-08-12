# Dialogs & Overlays

## Purpose

Modal and transient overlays: dialogs, detail drawers, tooltips, and the command palette. Overlays focus attention; they must not own business workflows beyond collecting UI confirmation/input.

## Usage

| Area | Package |
|------|---------|
| Dialogs | `com.srots.presentation.components.overlays.dialog` |
| Drawer | `com.srots.presentation.components.overlays.drawer` |
| Tooltip | `com.srots.presentation.components.overlays.tooltip` |
| Command | `com.srots.presentation.components.overlays.command` |

CSS: `.srots-dialog`, `.srots-detail-panel`.

**Authoritative:** `overlays.*`. Prefer over legacy `dialogs.ConfirmationDialog`.

## Key classes

| Class | Role |
|-------|------|
| `SrotsDialog` | Base themed dialog shell (`show` / `showAndWait` / `close`) |
| `SrotsConfirmationDialog` | Confirm / cancel destructive or important actions |
| `SrotsFormDialog` | Dialog hosting a form body |
| `SrotsInformationDialog` | Read-only notice |
| `SrotsDetailPanel` | Side drawer / detail pane |
| `SrotsTooltip` | Accessible tooltip helper |
| `SrotsCommandPalette` | Quick command / navigation overlay |

## Properties

| API | Notes |
|-----|--------|
| `setContent(Node)` | Dialog body |
| `getRoot()` | Root container for advanced composition |
| `show` / `showAndWait` / `close` | Lifecycle |
| `applyTheme(Scene)` | Ensure Prompt CSS on dialog scene |
| Confirmation | title, message, confirm/cancel labels + callbacks |
| Detail panel | open/close, content node |
| Command palette | query string, command list, selection callback |

## States

`Hidden · Open · Busy (optional via button loading) · Invalid form (form dialog)`

Escape closes dismissible overlays unless a blocking confirm is required.

## Events

Confirm / cancel / close runnables or `onAction` on footer buttons. Command palette fires selection to the host. Do not call services from dialog classes.

## Accessibility

- Move focus into the dialog on open; restore on close.
- Escape closes when allowed; Enter activates the default button.
- Tooltips required for icon-only triggers.
- Modals: underlying UI not interactable while open.
- Command palette: typeahead results keyboard-navigable.

## Do / Don't

| Do | Don't |
|----|-------|
| Use confirmation for destructive actions | Delete on a single click with no confirm |
| Host forms in `SrotsFormDialog` | Stack nested dialogs deeply |
| One dialog pattern family | Mix legacy and `Srots*` dialogs on new screens |

## Example

```java
SrotsConfirmationDialog confirm = new SrotsConfirmationDialog(
        "Delete company?",
        "This cannot be undone.",
        () -> viewModel.deleteSelected()
);
confirm.showAndWait();

SrotsDetailPanel detail = new SrotsDetailPanel();
detail.setContent(companyDetailNode);
detail.open();

SrotsTooltip.install(iconButton, "Edit company");
```
