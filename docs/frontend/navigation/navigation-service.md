# Navigation Service

`NavigationService` / `DefaultNavigationService` is the single navigation system.

## API

- `navigate(route)` / `navigate(route, parameters)` / `navigate(context)` / `navigate(request)`
- `replace(...)` — change destination without pushing history
- `refresh()` — re-run current route (no Stage/Scene recreate)
- `back()` / `forward()` / `home()`
- `state()` / `history()` / observable `currentRouteProperty()`

## Flow

1. Publish `NAVIGATION_REQUESTED`
2. Same-route + same parameters → no-op (no view recreate)
3. Unavailable / unknown route → ContentHost status page
4. Guards (auth, permission, feature, unsaved)
5. Push history (when appropriate)
6. `ViewFactory.create` → `ContentHost.setView`
7. Update `NavigationState` → chrome observers update

## Must not

- Database / JDBC / JPA
- REST / WebSocket clients
- Feature business calculations
- Creating Stage / Scene

## Request model

`NavigationRequest` carries:

- `route` (`NavigationRouteId`)
- `parameters` (`RouteParameters`)
- `source` (`NavigationSource`: SIDEBAR, TOPBAR, SEARCH, …)
- `replaceHistory`

All chrome entry points should converge here.
