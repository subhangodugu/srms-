# Color System

Centralized enterprise dark palette for SROTS Desktop. **Never hard-code hex values in screens** — use looked-up color tokens from `tokens.css`.

## JavaFX looked-up colors (not CSS variables)

JavaFX CSS does **not** support web-style custom properties (`--token` / `var(--token)`).

Define tokens on `.root` and consume them as **looked-up colors**:

```css
.root {
    -srots-primary: #6D5DFB;
}

.srots-primary-button {
    -fx-background-color: -srots-primary;
}
```

| Web (do not use) | SROTS JavaFX (required) |
|------------------|-------------------------|
| `--srots-primary` | `-srots-primary` |
| `var(--srots-bg)` | `-srots-bg` |

## Base surfaces

| Role | Hex | Token | Use |
|------|-----|-------|-----|
| Background | `#0B0F14` | `-srots-bg` | App shell / content canvas |
| Surface | `#111827` | `-srots-surface` | Sidebar, topbar, status bar |
| Surface Elevated | `#172033` | `-srots-surface-elevated` | Cards, dialogs, tables, panels |
| Surface Hover | `#1D2738` | `-srots-surface-hover` | Row / nav / control hover |
| Surface Selected | `#243044` | `-srots-surface-selected` | Selected rows / items |
| Border | `#273244` | `-srots-border` | Default dividers & outlines |
| Border Strong | `#344155` | `-srots-border-strong` | Emphasis borders, dialogs |
| Border Focus | `#6D5DFB` | `-srots-border-focus` | Keyboard focus ring |

## Text

| Role | Hex | Token |
|------|-----|-------|
| Primary | `#F8FAFC` | `-srots-text-primary` |
| Secondary | `#CBD5E1` | `-srots-text-secondary` |
| Muted | `#94A3B8` | `-srots-text-muted` |
| Disabled | `#64748B` | `-srots-text-disabled` |

## Brand (primary accent)

| State | Hex | Token |
|-------|-----|-------|
| Default | `#6D5DFB` | `-srots-primary` |
| Hover | `#7C6CFF` | `-srots-primary-hover` |
| Pressed | `#5848D6` | `-srots-primary-pressed` |
| Soft fill | `#241F54` | `-srots-primary-soft` |

Use primary mainly for:

- Active navigation
- Primary actions
- Focus indicators
- Selected emphasis
- Important indicators

Do **not** flood the UI with primary purple.

## Semantic colors

| Role | Hex | Token | Soft fill | Typical meaning |
|------|-----|-------|-----------|-----------------|
| Success | `#22C55E` | `-srots-success` | `-srots-success-soft` `#14532D` | Active, Passed, Approved, Healthy, Completed, Production Ready |
| Warning | `#F59E0B` | `-srots-warning` | `-srots-warning-soft` `#78350F` | Pending, Attention, Degraded, At Risk |
| Danger | `#EF4444` | `-srots-danger` | `-srots-danger-soft` `#7F1D1D` | Failed, Critical, Blocked, Error, Destructive |
| Info | `#38BDF8` | `-srots-info` | `-srots-info-soft` `#0C4A6E` | Informational status / system messages |

Do not use semantic colors as decoration. Pair color with **text and/or icon** (see [accessibility.md](./accessibility.md)).

## Canonical looked-up names (must exist)

```text
-srots-bg
-srots-surface
-srots-surface-elevated
-srots-border
-srots-text-primary
-srots-text-secondary
-srots-text-muted
-srots-primary
-srots-success
-srots-warning
-srots-danger
-srots-info
```

Extended tokens (also defined in `tokens.css`) include hover/selected surfaces, border-strong/focus, primary hover/pressed/soft, text-disabled, and semantic soft fills.

## Usage rules

1. One semantic role → one token. Do not invent a new hex per feature.
2. Prefer surface elevation + border over heavy shadows for hierarchy.
3. Status badges always map to success / warning / danger / info (or primary for “in progress” when documented).
4. Never mix legacy SRMS palette (`#0F172A`, `#3B82F6`, etc.) into new SROTS screens.

## Example

```css
.srots-card {
    -fx-background-color: -srots-surface-elevated;
    -fx-border-color: -srots-border;
}

.srots-badge-success {
    -fx-background-color: -srots-success-soft;
    -fx-text-fill: -srots-success;
}
```

See also: [color-tokens.md](./color-tokens.md) for the full inventory.
