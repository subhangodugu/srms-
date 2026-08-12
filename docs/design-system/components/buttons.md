# Buttons

## Purpose

Primary action controls: text buttons, icon buttons, and menus. Presentation only — handlers live in screens/ViewModels.

## Usage

| Area | Package |
|------|---------|
| Button | `com.srots.presentation.components.actions.button` |
| Icon button | `com.srots.presentation.components.actions.icon` |
| Menu | `com.srots.presentation.components.actions.menu` |

CSS via `SrotsStyleClasses`: `srots-button`, `srots-primary-button`, `srots-secondary-button`, `srots-tertiary-button`, `srots-danger-button`, `srots-icon-button`.

**Authoritative:** `SrotsButton` / `SrotsIconButton`. Prefer them over legacy `buttons.SrotsButtons`.

## Key classes

| Class | Role |
|-------|------|
| `SrotsButton` | Themed `Button` with variant, size, loading |
| `SrotsButtonVariant` | `PRIMARY`, `SECONDARY`, `TERTIARY`, `DANGER` |
| `SrotsIconButton` | Compact icon-only action |
| `SrotsMenu` | Themed menu / overflow actions |
| `SrotsSize` (`utility`) | `SMALL`, `STANDARD`, `LARGE` |

## Properties

| Property | Notes |
|----------|--------|
| `variant` / `setVariant` | Maps to CSS variant classes |
| `size` / `setSize` | `srots-size-small` / `standard` / `large` |
| `loading` / `setLoading` | Disables interaction; loading presentation |
| Factories | `primary`, `secondary`, `tertiary`, `danger` |
| Icon button | graphic + optional tooltip text |

## States

`Default · Hover · Pressed · Focused · Disabled · Loading`

Use design-system state tokens; do not invent private palettes.

## Events

Standard JavaFX `onAction`. For async work: set `loading=true` before the call, clear in `finally` on the FX thread.

## Accessibility

- Visible focus ring required.
- Icon-only controls need accessible text and a tooltip.
- Loading buttons should remain in tab order but not fire duplicate actions.
- Danger actions should also appear in a confirmation dialog when destructive.

## Do / Don't

| Do | Don't |
|----|-------|
| One primary button per page header/toolbar cluster | Multiple competing primary buttons |
| Use `DANGER` only for destructive actions | Paint danger styles inline |
| Prefer `SrotsButton.primary("Save")` | Subclass `Button` with ad-hoc CSS per screen |

## Example

```java
SrotsButton save = SrotsButton.primary("Save");
save.setSize(SrotsSize.STANDARD);
save.setOnAction(e -> {
    save.setLoading(true);
    viewModel.save(() -> save.setLoading(false));
});

SrotsIconButton more = new SrotsIconButton(SrotsIcon.of("more"));
SrotsTooltip.install(more, "More actions");
```
