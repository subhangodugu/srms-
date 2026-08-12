# JavaFX CSS Rules for SROTS

## Supported approach

- Looked-up colors (`-srots-*`) on `.root` for **Paint** values only (`-fx-background-color`, `-fx-text-fill`, borders as colors, etc.)
- Style classes with `srots-` prefix
- JavaFX properties: `-fx-background-color`, `-fx-text-fill`, `-fx-border-*`, `-fx-background-radius`, `-fx-padding`, etc.
- **Radii / lengths:** use literal sizes (`6px`, `8px`, `12px`, `999px`) matching `tokens.css`. Do **not** reference `-srots-radius-*` in `-fx-background-radius` / `-fx-border-radius` — JavaFX resolves those lookups as `Double` and throws `ClassCastException` (Double → Size).

## Unsupported (do not use)

- Browser CSS variables (`--token`)
- CSS Grid / Flexbox
- `@media` queries
- `backdrop-filter` / web filters
- HTML/Tailwind/Bootstrap

## Theme loading

```java
ThemeLoader.apply(scene);
```

Loads all stylesheets once at the Scene root. Prefer this over per-node stylesheet attachments.

## Inline style ban

```java
// Forbidden for normal UI
node.setStyle("-fx-background-color: #6D5DFB;");

// Required
node.getStyleClass().add("srots-primary-button");
```

Geometric sizing that cannot be expressed cleanly in shared CSS may use layout constraints (`setPrefWidth`) sparingly; prefer shared classes like `srots-pref-width-sm`.

## Shape chart styling

Lightweight chart shapes use CSS `-fx-fill` / `-fx-stroke` via series classes (`srots-chart-bar`, `srots-chart-donut-value`) instead of `Color.web`.

## Cross-platform fonts

```text
Inter → Segoe UI → Helvetica Neue → Arial → sans-serif
```

## Security

CSS must never contain secrets, tokens, passwords, or credentials.
