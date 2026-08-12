# Navigation Model

## Sources of truth

| Concept | Type | Owner |
|---------|------|-------|
| Route id | `NavigationRouteId` | Prompt 08 |
| Catalog entry | `NavigationItem` | `NavigationRegistry` |
| Group | `NavigationGroup` | Catalog / sidebar grouping |
| Visibility | `NavigationVisibility` | Catalog + `NavigationVisibilityService` |

## Sidebar presentation rules

1. Registry items are filtered by `UserAccessContext` (permissions / roles / auth).
2. Deep grandchildren (parent has a parent) are omitted from the sidebar flatten.
3. `SETTINGS` is shown in the sidebar footer, not the scrollable list.
4. `OVERVIEW` / `WORKSPACE` groups render as standalone items (no group header).
5. Product children (e.g. COMPTY Releases) remain selectable via their sidebar ancestor (`COMPTY`) for active highlight (`findSidebarSelection`).

## Extending navigation

Register items through `FeatureNavigationProvider` — do not edit `SrotsSidebar` for new modules.
