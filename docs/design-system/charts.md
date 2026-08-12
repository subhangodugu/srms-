# Chart Standards

Charts follow the SROTS enterprise dark palette. Every chart must answer a business question — no decorative charts.

## Supported chart types

```text
Line · Bar · Area · Donut · Stacked Bar · Progress · Trend
```

## Color series (order)

| Series index | Token |
|--------------|-------|
| 0 | `-srots-primary` |
| 1 | `-srots-success` |
| 2 | `-srots-warning` |
| 3 | `-srots-danger` |
| 4 | `-srots-info` |

Mapped in `charts.css` via JavaFX `.default-colorN.chart-series-line` / `.chart-bar`.

## Visual rules

| Element | Rule |
|---------|------|
| Plot background | Transparent (`.chart-plot-background`) |
| Legend | Transparent background; caption/body type |
| Line stroke | ~2 px |
| Labels | Clear axis labels; readable ≥ 11–12 px |
| Tooltips | Show concrete values on hover |
| Semantics | Use success/warning/danger only when meaning matches |

## Accessibility

- Provide numeric values in tooltips and, where feasible, data tables or summaries beside complex charts.
- Do not rely on color alone to distinguish critical vs healthy series — use legend text and patterns/markers when needed.
- Ensure contrast of series against `#0B0F14` / `#172033` backgrounds.

## Dashboard placement

Main chart occupies the primary dashboard region; sparklines/trends may sit inside KPI footers when compact. See [dashboards.md](./dashboards.md).

## Anti-patterns

- Rainbow decorative palettes unrelated to tokens
- 3D effects, heavy gradients, chartjunk
- Unlabeled axes
- Mixing chart libraries with conflicting themes

## CSS source

`charts.css`
