# Card System

Cards group summary content. Do **not** wrap every block of UI in a card — use them for KPIs, summaries, product/release/team status, activity, and alerts.

## Card chrome

| Property | Value |
|----------|--------|
| Class | `.srots-card` |
| Background | `-srots-surface-elevated` |
| Border | 1 px `-srots-border` |
| Radius | **8 px** (8–12 allowed) |
| Padding | **16–20 px** |
| Shadow | Prefer none; borders for separation |

### Parts

| Part | Class |
|------|-------|
| Header | `.srots-card-header` (bottom border, padding below) |
| Title | `.srots-card-title` (15 px, bold, primary text) |
| Body | default body typography |

## KPI cards

Class: `.srots-kpi-card` (min-height ≈ 110 px)

```text
┌─────────────────────────────┐
│ Active Projects             │  ← .srots-kpi-label (12 px secondary)
│                             │
│ 18                          │  ← .srots-kpi-value (~28 px bold)
│                             │
│ ↑ 12% vs previous period    │  ← .srots-kpi-trend (11 px semantic)
└─────────────────────────────┘
```

Contents:

```text
Label · Primary value · Optional trend · Optional supporting info
```

Trend colors: success for positive / healthy; warning/danger when the metric implies risk (do not color “up” green if “up” means worse).

## KPI row consistency

When multiple KPIs share a row:

```text
┌────────┐ ┌────────┐ ┌────────┐ ┌────────┐
│        │ │        │ │        │ │        │
│        │ │        │ │        │ │        │
└────────┘ └────────┘ └────────┘ └────────┘
```

They **must** share:

- Equal height
- Same padding
- Same title position
- Same value position
- Same footer/trend position

Do not let content stretch one card taller than siblings. Truncate or wrap consistently.

## Info cards

`InfoCard` follows `.srots-card` with optional status strip or badge. Use for release health, team summary, COMPTY overview tiles — still SROTS tokens only.

## Anti-patterns

- Giant dashboard cards with sparse content
- Nested cards more than one level deep
- Per-module unique radii/shadows
- Decorative glassmorphism stacks

## CSS source

`cards.css`
