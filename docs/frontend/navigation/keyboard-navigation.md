# Keyboard Navigation

Global shortcuts are registered once via `NavigationShortcutRegistry` (install on the primary Scene). Do not register the same shortcut independently in five components.

## Defaults

| Shortcut | Action |
|----------|--------|
| Ctrl/Cmd + K | Global Search (command mode via `>` prefix) |
| Alt + Left | Navigate back |
| Alt + Right | Navigate forward |
| Escape | Close active popup / dialog |
| Enter / Space | Activate focused control |
| Tab / Arrows | Focus traversal (sidebar where appropriate) |

## Context rules

- Shortcuts respect focused text inputs where appropriate
- After navigation, ContentHost requests initial focus on the feature view
- Sidebar active item follows `NavigationState`, not the click handler alone

## Accessibility

Navigation controls expose accessible labels; dialogs and popups manage focus ownership; current route is indicated in Sidebar and TopBar from the same state.
