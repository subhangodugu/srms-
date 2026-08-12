# Notification & Alert System

Consistent feedback for success, information, warning, and error — without notification spam.

## Types

| Type | Token | Example |
|------|-------|---------|
| Success | `-srots-success` | ✓ Release created successfully. |
| Information | `-srots-info` | System maintenance window tonight. |
| Warning | `-srots-warning` | Data may be outdated. |
| Error | `-srots-danger` | Unable to save changes. |

Always include an icon/symbol + text — never color alone.

## Toasts

Class: `.srots-toast`

| Property | Value |
|----------|--------|
| Background | elevated surface |
| Border | `-srots-border` |
| Radius | 8 px |
| Padding | 12 × 16 |

- Appear near a consistent corner (typically top-right under the topbar).
- Auto-dismiss for success/info; keep errors until dismissed or actioned when appropriate.
- Do not queue endless toasts for routine polling.

## Inline alerts

Class: `.srots-alert` + semantic modifier

| Class | Border / soft fill |
|-------|--------------------|
| `.srots-alert-success` | success + success-soft |
| `.srots-alert-warning` | warning + warning-soft |
| `.srots-alert-danger` | danger + danger-soft |
| `.srots-alert-info` | info + info-soft |

Use alerts inside pages for persistent contextual messages (offline banner, permission notice, validation summary).

## Progress

`.srots-progress` — bar uses `-srots-primary`; track uses elevated surface. Pair indeterminate progress with a short status label.

## Tooltips

Class: `.srots-tooltip`

Use for:

- Icon-only buttons
- Abbreviations
- Truncated table/cell content
- Technical terminology

Do **not** hide essential instructions only in tooltips.

## Application / connectivity messaging

Coordinate with status bar states:

```text
● Online
○ Offline — 3 local changes pending
◐ Synchronizing
⚠ Sync error
Data may be outdated  [Refresh]
```

See [state-styling.md](./state-styling.md).

## Icon rule

Use one professional vector icon family. **No emoji** as primary notification icons (🔥🚀 etc.).

Recommended icon sizes: 12 / 16 / 20 / 24 px.

## CSS source

`feedback.css`, `states.css`
