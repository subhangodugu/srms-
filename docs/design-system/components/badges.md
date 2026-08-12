# Badges

## Purpose

Compact status indicators with text (and optional icon). Badges communicate state at a glance without replacing full labels in tables or forms.

## Usage

Package: `com.srots.presentation.components.information.badge`

**Authoritative:** `SrotsStatusBadge` + `SrotsStatus`. Prefer over legacy `badges.StatusBadge`.

## Key classes

| Class | Role |
|-------|------|
| `SrotsStatusBadge` | Status chip (text + optional icon) |
| `SrotsStatus` | Semantic status enum mapped to `srots-*` classes |

## Properties

| API | Notes |
|-----|--------|
| `setStatus(SrotsStatus)` / `getStatus()` | Applies semantic style class |
| `setShowIcon(boolean)` / `isShowIcon()` | Toggle status glyph |
| Text | Label from status or explicit text setter if provided |

## States

Maps to semantic statuses (examples: success, warning, danger, info, neutral / custom enum values in `SrotsStatus`). Hover is usually unnecessary; disabled follows parent row/control.

## Events

None required. Badges are typically non-interactive. If clickable, treat as a button and supply accessible name + keyboard activation.

## Accessibility

- Always show text (or text + icon) — **never color alone**.
- Keep contrast against table/card surfaces.
- Don’t animate badges in ways that obscure the label.

## Do / Don't

| Do | Don't |
|----|-------|
| Use `SrotsStatusBadge` everywhere status appears | Invent per-module chip CSS |
| Pair with plain language (“Approved”) | Use ambiguous single-letter codes without tooltip |
| Keep one badge pattern | Mix legacy `StatusBadge` and `SrotsStatusBadge` on the same screen |

## Example

```java
SrotsStatusBadge badge = new SrotsStatusBadge();
badge.setStatus(SrotsStatus.SUCCESS); // e.g. Active / Approved mapping
badge.setShowIcon(true);

// In a table cell factory:
column.setCellFactory(col -> new TableCell<>() {
    private final SrotsStatusBadge chip = new SrotsStatusBadge();
    @Override
    protected void updateItem(SrotsStatus status, boolean empty) {
        super.updateItem(status, empty);
        if (empty || status == null) {
            setGraphic(null);
        } else {
            chip.setStatus(status);
            setGraphic(chip);
        }
    }
});
```
