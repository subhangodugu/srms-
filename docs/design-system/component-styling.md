# SROTS Component Styling

Components apply theme classes; they do not invent colors.

## Examples

| Component | Style classes |
|-----------|---------------|
| `SrotsButton` primary | `srots-button`, `srots-primary-button` |
| `SrotsCard` | `srots-card`, `srots-card-title`, … |
| `SrotsKpiCard` | `srots-kpi-card`, `srots-kpi-label`, `srots-kpi-value` |
| `SrotsDataTable` | `srots-table`, density modifiers |
| `SrotsStatusBadge` | `srots-badge-success` / warning / danger / info / neutral |
| `SrotsFormField` | `srots-form-field`, `srots-form-label`, `srots-form-error` |
| Navigation | `srots-sidebar`, `srots-nav-item`, `srots-nav-item-active` |

## Pattern

```java
node.getStyleClass().add("srots-card");
```

## Do

- Consume Prompt 06 CSS tokens via classes
- Keep one authoritative style per pattern

## Don't

- `setStyle` with hex colors
- Per-module button/card/table themes
- Browser CSS frameworks
