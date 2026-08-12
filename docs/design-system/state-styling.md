# SROTS State Styling

## Interaction pseudo-classes

JavaFX: `:hover`, `:pressed`, `:focused`, `:disabled`, `:selected`

Applied consistently for buttons, fields, nav, tables, tabs.

## Application state classes

| Class | Meaning |
|-------|---------|
| `srots-loading` | Dimmed loading overlay |
| `srots-spinner` | Progress indicator accent |
| `srots-skeleton` | Placeholder block |
| `srots-empty-state` | No data panel |
| `srots-error-state` | Recoverable error panel |
| `srots-online` / `srots-offline` / `srots-syncing` / `srots-sync-error` | Connectivity |
| `srots-invalid` / `srots-valid` / `srots-warning` | Field validation borders |

## Status communication

Never rely on color alone — badges and connection indicators include text/icons (`✓`, `⚠`, `✕`, `●`).

## Toast variants

`srots-toast-success`, `srots-toast-warning`, `srots-toast-error`, `srots-toast-info`
