# Navigation Architecture (Prompt 20)

SROTS navigation is **application navigation**, not web routing.

```text
Sidebar / TopBar / Search / Notifications / Profile / Commands / Breadcrumbs
        ↓
NavigationRequest (typed route + parameters + source)
        ↓
NavigationService
        ├── Authorization / feature guards
        ├── Route + parameter validation
        └── NavigationHistory (route state only)
        ↓
NavigationState  (single source of truth)
        ↓
ContentHost → ViewFactory → Feature View → ViewModel
```

## Persistent shell

```text
SrotsMainWindow → Stage → Scene → SrotsAppShell
  ├── Sidebar (fixed chrome)
  ├── TopBar (fixed; does not scroll with page)
  ├── ContentHost (only area that swaps / scrolls page content)
  └── StatusBar (fixed)
```

Navigation never recreates Stage, Scene, or AppShell.

## Packages

| Area | Package |
|------|---------|
| Window | `com.srots.presentation.window` |
| Navigation | `com.srots.presentation.navigation.*` |
| Lifecycle close | `com.srots.presentation.lifecycle` |

## Rules

1. One `NavigationService` for the whole application.
2. Typed `NavigationRouteId` — no string path routing.
3. Chrome must not `new FeatureView()`.
4. History stores route + parameters, never JavaFX nodes.
5. UI hiding is not security — guards still block unauthorized routes.
6. Feature failures stay inside ContentHost (Access Denied / unavailable / retry).

See also: [navigation-service.md](navigation-service.md), [route-registry.md](route-registry.md), [navigation-history.md](navigation-history.md), [window-management.md](window-management.md), [keyboard-navigation.md](keyboard-navigation.md).
