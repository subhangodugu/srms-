# Global Search

## Entry points

1. TopBar `SrotsSearchField` — opens `SrotsGlobalSearch` overlay
2. Shortcut `Ctrl/Cmd + K` via `NavigationShortcutRegistry`
3. Compact TopBar search icon → same overlay

## Responsibility

TopBar only opens search. Entity search runs through `GlobalSearchService` + providers.

Command mode (`>` prefix) consumes `NavigationCommandCatalog`. Dedicated command-palette expansion continues in a later prompt; Ctrl/Cmd + K remains the shared entry point.
