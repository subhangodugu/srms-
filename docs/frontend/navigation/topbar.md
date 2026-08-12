# SROTS TopBar

## Role

Persistent global context + action frame inside the AppShell.

```text
NavigationService → TopBarViewModel → SrotsTopBar
Session/Connection/Notifications → TopBarApplicationState → TopBarViewModel
```

## Layout

Compact enterprise chrome (**60px** height).

```text
[SROTS] │ [Title / Breadcrumb] [⌕ Search…  Ctrl K]   …spacer…   [⋯] [● Connected] [🔔] [Avatar] [ENV] [— □ X]
```

| Zone | Content |
|------|---------|
| Left | Brand + vertical divider + page title + breadcrumb (title above crumbs) |
| Center | `SrotsSearchField` with search glyph + `Ctrl K` chip (compact → icon opens command palette / global search) |
| Right | Contextual actions (max 3), connection status, notifications badge, profile, optional ENV pill, window controls |

Right-cluster order is fixed: **actions → connection → notifications → profile → env → window controls**.

## Density breakpoints

| Width | Behavior |
|-------|----------|
| ≥1180 | Full chrome |
| 860–1180 | Mid density: hide breadcrumb when title present; tighter spacing |
| &lt;860 | Compact: icon search, avatar-only profile, hide connection label cluster |

## Packages

| Class | Location |
|-------|----------|
| `SrotsTopBar` | `components.navigation.topbar` |
| `SrotsTopBarViewModel` | `shell.topbar` |
| `SrotsTopBarAction` / `SrotsTopBarState` | `shell.topbar` |
| `TopBarApplicationState` | `shell.topbar` |
| `NavigationCommandCatalog` | `shell.topbar` |

## Shortcuts

`Ctrl/Cmd + K` → focus search (expanded) or open `SrotsCommandPalette` / global search (compact).

## Profile menu

TopBar profile opens `SrotsUserProfileMenu` via `SrotsUserProfileController` (Prompt 17). See `docs/frontend/profile/`.

## Notifications

TopBar notification button opens `SrotsNotificationPanel` via `SrotsNotificationController` (Prompt 18). Badge unread count comes from `NotificationState` through `TopBarApplicationState`. See `docs/frontend/notifications/`.

## Global Search

TopBar search / Ctrl+K opens `SrotsGlobalSearch` (Prompt 19). See `docs/frontend/search/`.

## Non-goals

No DB/REST/auth/business logic inside the TopBar.
