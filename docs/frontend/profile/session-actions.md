# SROTS Profile Session Actions

## Action model

`SrotsUserProfileAction` with type:

| Type | Service |
|------|---------|
| NAVIGATION | `NavigationService` (PROFILE, PREFERENCES, SETTINGS) |
| DIALOG | `DialogService` / `SrotsAboutDialog` |
| AUTHENTICATION | `AuthenticationService.signOut()` |
| COMMAND | Application command façade (future) |

Visibility may use `UserAccessContext.hasPermission(...)`. Hiding an item is **not** security.

## Routes added

- `NavigationRouteId.PROFILE`
- `NavigationRouteId.PREFERENCES`

## About

Shows application name, version, environment, optional Java version. Never tokens, passwords, or credentials.
