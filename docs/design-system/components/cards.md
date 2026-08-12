# Cards

## Purpose

Surface containers and summary widgets: general cards, KPI cards, metrics, timelines, activity feeds, and avatars. Display-only composition — no fetching or aggregation logic inside the component.

## Usage

| Area | Package |
|------|---------|
| Card | `com.srots.presentation.components.information.card` |
| KPI | `com.srots.presentation.components.information.kpi` |
| Metric | `com.srots.presentation.components.information.metric` |
| Timeline | `com.srots.presentation.components.information.timeline` |
| Activity | `com.srots.presentation.components.information.activity` |
| Avatar | `com.srots.presentation.components.information.avatar` |

CSS: `.srots-card`, `.srots-kpi-card`.

**Authoritative:** `information.*` types. Prefer over legacy `cards.InfoCard` / `cards.KpiCard`.

## Key classes

| Class | Role |
|-------|------|
| `SrotsCard` | Header / title / subtitle / content / footer slots |
| `SrotsKpiCard` | KPI label, value, delta/trend presentation |
| `SrotsMetric` | Compact metric readout |
| `SrotsTimeline` / `SrotsTimelineItem` | Ordered event timeline |
| `SrotsActivityFeed` / `SrotsActivityItem` | Recent activity list |
| `SrotsAvatar` | Initials / image avatar |
| `SrotsUserProfile` | Avatar + name + meta |

## Properties

| Component | Highlights |
|-----------|------------|
| `SrotsCard` | `setHeader`, `setTitle`, `setSubtitle`, `setContent`, `setFooter`, pane getters |
| `SrotsKpiCard` | title, value, secondary/trend text |
| `SrotsMetric` | label + value |
| Timeline / activity | item collections, timestamps as display strings |
| Avatar | size, initials/image |

## States

Default / hover (if clickable host) / selected (rare). Loading and empty belong to parent page or [feedback](./feedback.md), not inside every card.

## Events

Optional `setOnMouseClicked` / action buttons in footer. Prefer explicit buttons over making the entire card the only hit target when multiple actions exist.

## Accessibility

- KPI/metric values need visible labels (not color-only trend).
- Timeline/activity: time text + description; don’t rely on dot color alone.
- Avatars: accessible name = person/entity name.
- Decorative cards: don’t steal tab focus; interactive footers remain focusable.

## Do / Don't

| Do | Don't |
|----|-------|
| Use `SrotsCard` for grouped content | Nest cards inside cards without need |
| Feed formatted values from ViewModel | Compute business KPIs inside `SrotsKpiCard` |
| Pair status with [badges](./badges.md) | Encode status only as card border color |

## Example

```java
SrotsKpiCard openTickets = new SrotsKpiCard();
openTickets.setTitle("Open tickets");
openTickets.setValue("42");
openTickets.setTrend("+3 vs last week");

SrotsCard panel = new SrotsCard();
panel.setTitle("Account");
panel.setSubtitle("Primary company profile");
panel.setContent(formNode);
panel.setFooter(SrotsButton.secondary("Cancel"), SrotsButton.primary("Save"));
```
