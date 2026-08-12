# SROTS Notification State

`NotificationState` is the single source of truth for:

- notification list (newest first)
- read/unread flags
- unread count
- load status / error message

TopBar badge reads unread count via `TopBarApplicationState`, synced by `NotificationService`.

Do not maintain a second independent unread counter in the panel.
