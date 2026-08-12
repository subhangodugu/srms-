# Accessibility

## Purpose

Cross-cutting rules for every Srots component: keyboard operation, visible focus, non-color status, and accessible names. Components ship presentation hooks; screens must not strip them for aesthetics.

## Usage

Applies to all packages under `com.srots.presentation.components.*`, especially:

- Actions: `SrotsButton`, `SrotsIconButton`
- Forms: `SrotsFormField` + inputs
- Navigation: sidebar, tabs, breadcrumbs
- Overlays: dialogs, drawers, tooltips, command palette
- Feedback & badges: alerts, toasts, status chips
- Data: tables, pagination, search

Utilities: `SrotsTooltip`, `SrotsIcon`, `SrotsStyleClasses` (focus/state classes from Prompt 06).

## Key classes

| Class | A11y role |
|-------|-----------|
| `SrotsButton` | Sets accessible text from label; loading disables duplicate activation |
| `SrotsIconButton` | Requires tooltip + accessible name |
| `SrotsFormField` | Label, required, error text beside control |
| `SrotsStatusBadge` | Text (+ optional icon), not color-only |
| `SrotsTooltip` | Explains icon-only and truncated UI |
| `SrotsDialog` / confirmations | Focus trap patterns via show/close lifecycle |
| `SrotsCommandPalette` | Keyboard-first command entry |

## Properties

| Concern | Expectation |
|---------|-------------|
| Accessible text / name | Set on icon-only and graphic-heavy controls |
| Focus style classes | Keep Prompt focus tokens (`.srots-focused` / border-focus) |
| Error / helper text | Visible string via `SrotsFormField` |
| Status | Enum + label via `SrotsStatusBadge` |

## States

Interactive components must support, as applicable:

```text
Default · Hover · Pressed · Focused · Disabled · Selected · Loading · Error
```

Focus must remain visible in all themes. Disabled controls are skippable or clearly non-activatable.

## Events

Keyboard:

| Key | Action |
|-----|--------|
| Tab / Shift+Tab | Move focus |
| Enter | Default / activate |
| Space | Toggle / activate buttons |
| Escape | Close dialog, drawer, palette, dismissible overlay |
| Arrows | Lists, menus, tabs, table where implemented |
| Ctrl/Cmd+K | Command palette / global search (app wiring) |

## Accessibility

- Contrast: primary text on bg/surface per design tokens; don’t use muted text for critical actions.
- Non-color status: badge text, chart legends, sync labels.
- Don’t remove focus rings in module CSS.
- Modals restore focus to the invoker on close.
- Loading regions expose visible text (`SrotsLoadingState`), not spinner-only.
- RBAC: omit unauthorized nav items from the model; don’t leave silent dead controls.

## Do / Don't

| Do | Don't |
|----|-------|
| Install tooltips on icon-only actions | Ship unlabeled icon buttons |
| Keep one focus order per page | Create keyboard traps outside modals |
| Announce errors in form fields | Flash red borders with no message |
| Reuse component a11y behavior | Override `-fx-focus-color` to transparent |

## Example

```java
SrotsIconButton edit = new SrotsIconButton(SrotsIcon.of("edit"));
edit.setAccessibleText("Edit company");
SrotsTooltip.install(edit, "Edit company");

SrotsFormField email = new SrotsFormField("Email", new SrotsTextField());
email.setRequired(true);
email.setError("Enter a valid email address");

SrotsStatusBadge status = new SrotsStatusBadge();
status.setStatus(SrotsStatus.WARNING);
status.setShowIcon(true); // text + icon, not color alone
```

See also system-wide guidance: [../accessibility.md](../accessibility.md).
