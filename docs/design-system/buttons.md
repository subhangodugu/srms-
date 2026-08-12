# Button System

Reusable action controls for SROTS Desktop. Prefer style classes over inline styles.

## Variants

| Variant | Class | Use |
|---------|-------|-----|
| Primary | `.srots-primary-button` | Single most important action on a view |
| Secondary | `.srots-secondary-button` | Cancel, alternate, companion actions |
| Tertiary | `.srots-tertiary-button` | Low-emphasis text/button |
| Destructive | `.srots-danger-button` | Delete / irreversible |
| Icon | `.srots-icon-button` | Search, notifications, more, refresh, settings |

Shared base: `.srots-button` (13 px, bold, 6 px radius, padding 8×16, hand cursor).

Legacy aliases in `components.css`: `.btn-primary`, `.btn-secondary` — prefer `.srots-*` in new code.

### Examples

```text
[ Create Release ]     Primary
[ Cancel ]             Secondary
Delete                 Tertiary / or Danger when destructive
[ ⟳ ]                  Icon
```

## Visual tokens

| Variant | Background | Text | Border |
|---------|------------|------|--------|
| Primary | `-srots-primary` | white | none |
| Primary hover | `-srots-primary-hover` | white | — |
| Primary pressed | `-srots-primary-pressed` | white | — |
| Secondary | `-srots-surface-elevated` | primary text | `-srots-border` |
| Secondary hover | `-srots-surface-hover` | — | — |
| Tertiary | transparent | `-srots-primary` | none |
| Danger | `-srots-danger` | white | none |
| Icon | elevated + border | primary text | padding 6×10 |

## Required states

Every button must support:

```text
Default · Hover · Pressed · Focused · Disabled · Loading
```

| State | Behavior |
|-------|----------|
| Focused | Visible focus using `-srots-border-focus` / focus ring |
| Disabled | Muted text (`-srots-text-disabled`), non-interactive, reduced emphasis |
| Loading | Show spinner + label (e.g. `◌ Creating...`); **disable further clicks** |

```text
[ ◌ Creating... ]
```

Do not allow repeated submits while processing.

## Hierarchy rules

1. **One** primary button in a page header or dialog footer by default.
2. Destructive actions require confirmation dialog when irreversible.
3. Do not crowd table rows with many buttons — use overflow `•••` (see [tables.md](./tables.md)).
4. Icon-only buttons **require** tooltips.

## Sizing

| Size | Padding | Font |
|------|---------|------|
| Default | 8 × 16 | 13 |
| Icon | 6 × 10 | icon 16–20 |

Stay on the spacing scale.

## Split actions

`SplitButton` (catalog) = primary action + menu for secondary variants. Use the same primary/secondary colors; menu items follow tertiary/list styles.

## CSS source

`buttons.css`, `components.css`
