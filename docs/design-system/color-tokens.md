# SROTS Color Tokens

Centralized in `css/tokens.css` as JavaFX looked-up colors.

## Background

| Token | Value | Usage |
|-------|-------|-------|
| `-srots-bg` | `#0B0F14` | Application background |
| `-srots-surface` | `#111827` | Sidebar, topbar, cards |
| `-srots-surface-elevated` | `#172033` | Elevated panels, dialogs |
| `-srots-surface-hover` | `#1D2738` | Hover |
| `-srots-surface-selected` | `#243044` | Selection |

## Borders

| Token | Value |
|-------|-------|
| `-srots-border` | `#273244` |
| `-srots-border-strong` | `#344155` |
| `-srots-border-focus` | `#6D5DFB` |

## Text

| Token | Value |
|-------|-------|
| `-srots-text-primary` | `#F8FAFC` |
| `-srots-text-secondary` | `#CBD5E1` |
| `-srots-text-muted` | `#94A3B8` |
| `-srots-text-disabled` | `#64748B` |

## Brand

| Token | Value |
|-------|-------|
| `-srots-primary` | `#6D5DFB` |
| `-srots-primary-hover` | `#7C6CFF` |
| `-srots-primary-pressed` | `#5848D6` |
| `-srots-primary-soft` | `#241F54` |
| `-srots-primary-on` | `#FFFFFF` |

## Semantic (+ soft surfaces)

| Role | Strong | Soft |
|------|--------|------|
| Success | `#22C55E` | `#14532D` |
| Warning | `#F59E0B` | `#78350F` |
| Danger | `#EF4444` | `#7F1D1D` |
| Info | `#38BDF8` | `#0C4A6E` |

Soft colors are solid JavaFX-compatible values (not unsupported CSS alpha functions).

## Chart series

`-srots-chart-1` … `-srots-chart-5` and `-srots-chart-neutral` map to brand/semantic tokens.

## Rule

Feature screens choose **semantic classes** (`srots-badge-success`, `srots-primary-button`), never raw hex.
