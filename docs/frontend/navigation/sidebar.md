# SROTS Sidebar

## Role

Permanent native JavaFX navigation frame inside `SrotsAppShell` / `MainView`.

```text
User → SrotsSidebar → NavigationService → NavigationState → ContentHost → Feature View
```

The sidebar does **not** own business logic, databases, REST, or authorization decisions.

## Components

| Class | Package | Role |
|-------|---------|------|
| `SrotsSidebar` | `components.navigation.sidebar` | Layout: brand, scroll, footer, collapse |
| `SrotsSidebarViewModel` | same | Observable groups, collapse, active route |
| `SrotsNavigationItem` | same | Icon / label / badge / active / tooltip |
| `SrotsNavigationGroup` | same | Collapsible group (separate from sidebar collapse) |
| `NavigationIconResolver` | same | Catalog `iconKey` → glyph |
| `NavigationBadgeFormatter` | same | `12` / `999+` |

## Integration

```text
MainView.fxml
  └── SrotsSidebar
        └── NavigationChromeBinder.bindSidebar(sidebar)
              └── SrotsSidebarViewModel.bind(registry, nav, visibility, access)
```

Collapse state is persisted via Prompt 13 `SrotsWindowState.sidebarCollapsed`.

## States

- Sidebar: expanded (260px) / collapsed (68px)
- Group: expanded / collapsed (local UI)
- Item: default / hover / focused / active / disabled

Active route always comes from `NavigationService.currentRoute` (via ViewModel), never a private sidebar route store.
