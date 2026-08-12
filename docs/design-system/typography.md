# Typography

Clean, professional sans-serif typography for an information-dense enterprise desktop UI.

## Font family

**Preferred:** Inter  

**Fallbacks (JavaFX):**

```css
.root {
    -fx-font-family: "Inter", "Segoe UI", "Helvetica Neue", Arial, sans-serif;
    -fx-font-size: 13px;
}
```

Defined in `base.css`. Do not introduce competing display fonts for product screens.

## Type scale

| Role | Size | Weight | Class | Typical use |
|------|------|--------|-------|-------------|
| Display | 32 px | 700 | `.srots-display` | Rare hero / showcase only |
| Page Title | 24 px | 600–700 | `.srots-page-title` | Page header title |
| Section Title | 18 px | 600 | `.srots-section-title` | Section headings |
| Card Title | 15–16 px | 600 | `.srots-card-title` | Card / panel titles |
| Body | 13–14 px | 400 | `.srots-body` | Primary reading text (default 13) |
| Secondary / Label | 12–13 px | 500 | `.srots-label` | Field labels, secondary copy |
| Caption | 11–12 px | 400 | `.srots-caption` | Helper, timestamps, meta |

Do **not** use oversized headings. SROTS is dense by design.

## Font weights

| Name | Value | Use |
|------|-------|-----|
| Regular | 400 | Body, captions |
| Medium | 500 | Labels, secondary emphasis |
| Semibold | 600 | Page / section / card titles, key values |
| Bold | 700 | Display, KPI primary values when needed |

Recommended mapping:

```text
Page titles     → 600
Section titles  → 600
Card titles     → 600
Body            → 400
Labels          → 500
Primary values  → 600 / 700
```

Avoid bold everywhere — reserve weight for hierarchy.

## Text color classes

| Class | Token |
|-------|--------|
| (default body) | `-srots-text-primary` |
| `.srots-secondary-text` | `-srots-text-secondary` |
| `.srots-muted` | `-srots-text-muted` |
| `.srots-disabled-text` | `-srots-text-disabled` |

## KPI value typography

KPI primary numbers use larger emphasis (implementation: `.srots-kpi-value` ≈ 28 px / bold) while labels stay at secondary 12 px. Keep KPI rows visually aligned (see [cards.md](./cards.md)).

## Rules

1. Prefer style classes over per-control `-fx-font-size` in FXML/controllers.
2. Do not invent ad-hoc sizes (e.g. 17 px, 19 px) without updating this scale.
3. Truncate long text with ellipsis + tooltip when needed; do not shrink fonts below caption.
4. Charts and tables inherit body/caption sizes — keep legends readable (≥ 11 px).

## CSS source

`srots-presentation/src/main/resources/css/typography.css`
