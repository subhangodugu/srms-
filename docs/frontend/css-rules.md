# CSS Rules (Frontend)

SROTS styling is defined by Prompt 05 (design system) → Prompt 06 (JavaFX CSS theme) → Prompt 07 (components).

## Forbidden for normal UI

```java
node.setStyle("-fx-background-color: #...");
Color.web("#6D5DFB");
```

## Required approach

```java
node.getStyleClass().add("srots-card");
```

All application CSS classes start with `srots-`:

```text
srots-card
srots-primary-button
srots-page-header
srots-data-table
srots-status-badge
```

## Responsibility split

| CSS | JavaFX layout |
|-----|----------------|
| Color, typography, borders, background, radius, states | HBox, VBox, BorderPane, GridPane, StackPane, SplitPane, ScrollPane |

Do not use CSS to paper over broken layout structure.

## Spacing scale

Prefer: `4, 8, 12, 16, 20, 24, 32, 40, 48`  
Avoid arbitrary: `13, 17, 19, 27, 31` unless technically justified.

## Dimensions

Use standardized component sizes (Small / Standard / Large).
Do not invent one-off widths per module button.

## Theme loading

Apply stylesheets through `ThemeLoader` / established theme entry points.
Preserve Prompt 06 CSS architecture when adding styles.
