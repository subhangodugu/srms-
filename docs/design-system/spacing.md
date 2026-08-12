# Spacing System

Consistent spacing keeps SROTS dense but readable. Use the scale below for padding, gaps, and margins in FXML layouts and CSS.

## Scale (px)

| Token concept | Value |
|---------------|-------|
| Micro | **4** |
| Small | **8** |
| Standard | **12** |
| Component | **16** |
| Comfort | **20** |
| Section | **24** |
| Large section | **32** |
| Major | **40** |
| XL | **48** |
| XXL | **64** |

```text
4 · 8 · 12 · 16 · 20 · 24 · 32 · 40 · 48 · 64
```

## Recommended usage

| Context | Spacing |
|---------|---------|
| Icon ↔ label gap | 4–8 |
| Tight control groups | 8 |
| Related fields / button groups | 12 |
| Card / control padding | 16 |
| Card header ↔ body | 12–16 |
| Content page padding | **24** (large screens may use 32) |
| Between major page sections | 24–32 |
| Empty-state vertical padding | 40–48 |
| Shell-level separation | 40–64 |

## Layout reminders

- **Content padding:** 24 px default inside main content area.
- **Card padding:** 16–20 px.
- **Dialog padding:** 24 px.
- **Drawer padding:** ~20 px.
- **Nav item padding:** 8 × 12.
- **Button padding:** 8 × 16 (icon buttons 6 × 10).

## Prohibited arbitrary values

Do not randomly use:

```text
7 · 13 · 19 · 27 · 33
```

unless a JavaFX control metric (e.g. row height) or icon alignment requires it and it is documented.

## Density vs spacing

| Density | Feel | Spacing bias |
|---------|------|--------------|
| Compact | Operational tables | Prefer 4–12 |
| Standard (default) | Most screens | Prefer 8–24 |
| Comfortable | Dashboards / executive | Prefer 12–32 |

Tables prioritize density; dashboards may breathe slightly more. See [layout.md](./layout.md) and [tables.md](./tables.md).

## Implementation notes

JavaFX CSS does not provide a full spacing token API like web design systems. Encode spacing as:

1. Shared style classes (`-fx-padding`, `-fx-spacing` on `VBox`/`HBox` via CSS where supported).
2. Consistent FXML `spacing` / `padding` attributes using this scale.
3. Documented constants in presentation helpers when needed.

Do not scatter magic numbers across controllers.
