# Dashboard Grid System

Dashboards use a predictable grid — not arbitrary absolute positioning. Prefer slightly more breathing room than operational tables, while staying on the spacing scale.

## Standard dashboard layout

```text
┌────────┐ ┌────────┐ ┌────────┐ ┌────────┐
│ KPI    │ │ KPI    │ │ KPI    │ │ KPI    │
└────────┘ └────────┘ └────────┘ └────────┘

┌──────────────────────┐ ┌─────────────────┐
│ Main Chart           │ │ Activity        │
│                      │ │                 │
└──────────────────────┘ └─────────────────┘

┌────────────────────────────────────────────┐
│ Recent Work / Table                       │
└────────────────────────────────────────────┘
```

## Grid rules

| Region | Guidance |
|--------|----------|
| KPI row | Prefer **4 equal columns**; equal height (see [cards.md](./cards.md)) |
| Main chart | ~2/3 width on large screens |
| Activity feed | ~1/3 width; timeline / activity items |
| Bottom table | Full width recent work |

Gaps: **16–24** between tiles; page padding **24**.

On narrower widths:

1. KPI row wraps to 2×2
2. Chart stacks above activity
3. Table remains full width with horizontal scroll or fewer columns

## Content principles

- Every chart answers a **business question** (see [charts.md](./charts.md)).
- Activity items use `ActivityItem` / `Timeline` components — not freeform cards.
- Empty / loading / error / offline states apply to each region independently when possible (avoid blocking the whole app).

## Module reuse

The same grid pattern serves Admin overview, Engineering health, Sales snapshot, Support queue summary, and COMPTY analytics **without** inventing unique dashboard chrome.

## Density

Dashboards may use Comfortable spacing; nested tables inside the bottom region still default to Standard row height (44 px).

## CSS / layout

Compose with JavaFX `GridPane` / `HBox` / `VBox`. Chart styles live in `charts.css`; cards in `cards.css`. A dedicated `dashboard.css` may be added later for dashboard-only helpers — until then, reuse layout + cards + charts tokens.
