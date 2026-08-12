# Charts

## Purpose

Lightweight JavaFX chart wrappers for dashboards and analytics widgets. Charts visualize series already prepared by the ViewModel — no aggregation, filtering, or API calls inside chart classes.

## Usage

Package: `com.srots.presentation.components.charts`

Style with Prompt 05/06 chart / series CSS (`srots-*`). Prefer looked-up color tokens for series paints when CSS alone is insufficient.

## Key classes

| Class | Role |
|-------|------|
| `SrotsBarChart` | Category comparisons |
| `SrotsLineChart` | Trends over time |
| `SrotsAreaChart` | Cumulative / volume trends |
| `SrotsDonutChart` | Part-to-whole |
| `SrotsProgressChart` | Single progress / completion |

## Properties

Typical pattern (see each class):

| API | Notes |
|-----|--------|
| Title / legend visibility | Chart chrome |
| Categories / series data | JavaFX `XYChart.Series` or domain-specific setters |
| Progress value | `SrotsProgressChart` determinate value |
| Size constraints | Fit dashboard tile / section |

Exact setters vary by chart type; keep data binding in the ViewModel.

## States

`Empty data · Loading (parent) · Populated · Disabled interaction`

Empty charts should show a short empty hint or defer to `SrotsEmptyState` in the parent card.

## Events

Optional point/series click for drill-down — host handles navigation. Charts themselves do not open modules or fetch details.

## Accessibility

- Provide a text summary or data table alternative for critical metrics when feasible.
- Legends and value labels required; **do not rely on color alone**.
- Tooltips on points should include category + value text.
- Don’t flash high-frequency animations; keep motion calm per design-system animation rules.

## Do / Don't

| Do | Don't |
|----|-------|
| Pass already-aggregated series | Query repositories from chart code |
| Place charts inside `SrotsCard` / KPI layouts | Use charts as the only status for critical alerts |
| One chart component per chart kind | Wrap JavaFX charts with one-off styling per screen |

## Example

```java
SrotsBarChart chart = new SrotsBarChart();
chart.setTitle("Tickets by status");
chart.setData(viewModel.getTicketStatusSeries());

SrotsCard tile = new SrotsCard();
tile.setTitle("Support volume");
tile.setContent(chart);
```
