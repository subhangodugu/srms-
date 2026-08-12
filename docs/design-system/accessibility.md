# Accessibility & Keyboard Navigation

SROTS must remain readable and operable for keyboard users and for operators who cannot rely on color alone.

## Contrast & type

- Maintain sufficient contrast between text tokens and surfaces (primary text on bg/surface; muted only for secondary meta).
- Minimum body size **13 px** default; captions not below **11 px** for essential status.
- Do not remove focus rings for aesthetics.

## Visible focus

Every interactive control must show focus using `-srots-border-focus` / `.srots-focused`.

```text
Tab · Shift+Tab · Enter · Escape · Arrow keys
```

## Non-color status

Never communicate status with color alone:

| Prefer | Avoid |
|--------|-------|
| ✓ Approved | Green dot only |
| ⚠ Pending | Amber circle only |
| ✕ Failed | Red circle only |

Badges include text (+ symbol). Charts include legends and values.

## Tooltips & errors

- Icon-only controls need tooltips.
- Errors are specific and actionable (see [forms.md](./forms.md)).
- Never expose stack traces to end users.

## Keyboard shortcuts

| Shortcut | Action |
|----------|--------|
| Ctrl/Cmd + K | Global search |
| Ctrl/Cmd + F | Page / feature search |
| Esc | Close dialog / drawer / cancel |
| Ctrl/Cmd + R | Refresh (where safe) |
| Enter | Activate focused default action |
| Arrows | Navigate lists, menus, table where implemented |

Do not create conflicting shortcuts. Document module-specific shortcuts in the relevant feature docs when added.

## Role-based UI (RBAC)

Supported roles (examples):

```text
Admin · Manager · Employee · Product Manager · Developer
QA · DevOps · Sales · Support · Executive
```

Rules:

1. Navigation and actions are **permission-aware**.
2. Do not “hide everything” inconsistently — omit unauthorized destinations from the nav model.
3. If an action is visible but denied, show a clear permission-denied state (not a silent no-op).
4. Employee Portal vs Admin Portal may use different nav trees with the **same** visual system.

## Application states to support

Every major screen accounts for:

```text
Initial · Loading · Loaded · Empty · Error
Offline · Synchronizing · Permission Denied
```

## Offline / sync (UI)

| State | Presentation |
|-------|----------------|
| ONLINE | ● Online (success) |
| OFFLINE | ○ Offline + pending local changes count |
| SYNCING | ◐ Synchronizing (info) |
| SYNC ERROR | ⚠ Sync error (danger) |
| LOCAL CHANGES / CONFLICT | Warning alert + resolution CTA |

Data freshness:

```text
Last synchronized: 2 minutes ago
Data may be outdated  [Refresh]
```

## Desktop resize

Layouts must remain usable without overlapping controls. Collapse sidebar before crushing content. No browser responsive CSS.

## Testing checklist

- [ ] Tab order matches visual order
- [ ] Focus visible on all controls
- [ ] Status readable in grayscale
- [ ] Dialogs trap focus and restore it
- [ ] Disabled controls are not focus-activatable for forbidden actions
- [ ] Screen states (empty/error/offline) include text + action
