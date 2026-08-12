# Accessibility and Desktop UX

## Keyboard-first

Important workflows must work with:

```text
Tab | Shift+Tab | Enter | Escape | Arrow keys | Ctrl/Cmd shortcuts
```

Centralize shortcuts (e.g. navigation shortcut registry). Avoid per-screen global key listeners.

## Accessible controls

Every important interactive element needs:

- Accessible role
- Accessible text / name
- Keyboard access
- Visible focus

Icon-only buttons **must** have accessible labels (not emoji as the sole affordance).

## Focus and contrast

Preserve design-system focus rings and semantic status colors from Prompt 06.
Do not remove focus indicators for aesthetics.

## Dialogs and escape

Modals should support Escape to cancel where safe.
Destructive actions require clear confirmation.

## Screen reader / AT notes

Prefer meaningful control text over decorative glyphs.
Status badges should convey meaning in text, not color alone.
