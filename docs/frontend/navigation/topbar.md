# SROTS TopBar

## Role

Persistent global context + action frame inside the AppShell.

```text
NavigationService → TopBarViewModel → SrotsTopBar
Session/Connection/Notifications → TopBarApplicationState → TopBarViewModel
```

## Layout

| Zone | Content |
|------|---------|
| Left | Brand + page title + breadcrumb |
| Center | `SrotsSearchField` (compact → icon opens command palette) |
| Right | Contextual actions (max 3), notifications badge, connection, profile, optional ENV, window controls (minimize / maximize / close) |

## Packages

| Class | Location |
|-------|----------|
| `SrotsTopBar` | `components.navigation.topbar` |
| `SrotsTopBarViewModel` | `shell.topbar` |
| `SrotsTopBarAction` / `SrotsTopBarState` | `shell.topbar` |
| `TopBarApplicationState` | `shell.topbar` |
| `NavigationCommandCatalog` | `shell.topbar` |

## Shortcuts

`Ctrl/Cmd + K` → focus search (expanded) or open `SrotsCommandPalette` (compact).

## Profile menu

TopBar profile opens `SrotsUserProfileMenu` via `SrotsUserProfileController` (Prompt 17). See `docs/frontend/profile/`.

## Notifications

TopBar notification button opens `SrotsNotificationPanel` via `SrotsNotificationController` (Prompt 18). Badge unread count comes from `NotificationState` through `TopBarApplicationState`. See `docs/frontend/notifications/`.

## Global Search

TopBar search / Ctrl+K opens `SrotsGlobalSearch` (Prompt 19). See `docs/frontend/search/`.

## Non-goals

No DB/REST/auth/business logic inside the TopBar.
