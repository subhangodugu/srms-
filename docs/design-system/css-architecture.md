# SROTS CSS Architecture

## Location

```text
srots-desktop/srots-presentation/src/main/resources/css/
```

## Token strategy

JavaFX does **not** support browser CSS custom properties (`--var`). SROTS uses **looked-up colors** on `.root`:

```css
.root {
    -srots-bg: #0B0F14;
    -srots-primary: #6D5DFB;
}
```

Referenced as:

```css
.srots-card {
    -fx-background-color: -srots-bg;
}
```

Never repeat raw hex values outside `tokens.css`.

## Deterministic load order

`ThemeLoader.apply(Scene)` loads stylesheets in this order (preferred over `@import` alone):

1. `tokens.css`
2. `base.css`
3. `typography.css`
4. `layout.css`
5. `navigation.css`
6. `buttons.css`
7. `forms.css`
8. `cards.css`
9. `tables.css`
10. `tabs.css`
11. `dialogs.css`
12. `feedback.css`
13. `charts.css`
14. `states.css`
15. `components.css` (aliases)
16. `dashboard.css`

`theme.css` mirrors the same order via `@import` for single-bundle loading.

## File responsibilities

| File | Responsibility |
|------|----------------|
| `tokens.css` | Looked-up colors, radii, spacing, shell metrics |
| `base.css` | Root/app defaults, scrollbars, split-pane, focus |
| `typography.css` | Display → caption type scale |
| `layout.css` | Shell surfaces, page structure, list/tree |
| `navigation.css` | Sidebar, topbar, statusbar, nav items |
| `buttons.css` | Primary/secondary/tertiary/danger/icon |
| `forms.css` | Fields, combo, checkbox, toggle, validation |
| `cards.css` | Card + KPI |
| `tables.css` | TableView + density + pagination |
| `tabs.css` | TabPane |
| `dialogs.css` | Dialog + detail panel |
| `feedback.css` | Toast, alert, badge, tooltip, progress |
| `charts.css` | Chart series + lightweight bar/donut classes |
| `states.css` | Loading/empty/error/offline/sync |
| `theme.css` | Assembly hub |

## Naming

All application classes use the `srots-` prefix (`srots-card`, `srots-primary-button`).

## Layout vs CSS

- **Layout**: `HBox`, `VBox`, `BorderPane`, `GridPane`, `StackPane`, `SplitPane`, `ScrollPane`
- **CSS**: color, typography, border, background, radius, states

Do not use unsupported browser features (CSS Grid, Flexbox, `var()`, media queries, `backdrop-filter`).

## Forbidden practices

- `node.setStyle("-fx-background-color: #...")` for normal UI
- `Color.web("#...")` for normal theming (use semantic style classes)
- Screen-specific one-off colors
- HTML/Tailwind/Bootstrap/React styling

## Cross-platform

Font stack: `"Inter", "Segoe UI", "Helvetica Neue", Arial, sans-serif`. Do not assume Windows-only fonts or controls.
