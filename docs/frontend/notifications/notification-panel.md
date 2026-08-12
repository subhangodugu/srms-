# SROTS Notification Panel

## Role

TopBar attention center for application events.

```text
NotificationService → NotificationState → ViewModel → SrotsNotificationPanel (Popup)
                                         ↘ TopBar badge
```

## Package

`com.srots.presentation.notification`

| Type | Responsibility |
|------|----------------|
| `SrotsNotificationPanel` | Popup UI (not a Stage) |
| `SrotsNotificationController` | Button/popup lifecycle |
| `SrotsNotificationPanelViewModel` | Filter / empty / error / open state |
| `NotificationState` | Single source of truth |
| `NotificationService` | Refresh / mark read / updates |
| `SrotsNotificationItemView` | List cell graphic |

## Behavior

| Input | Result |
|-------|--------|
| Click notification button | Toggle panel |
| Outside click / Escape | Close |
| Window resize / minimize | Close |
| Another TopBar popup opens | Close via `SrotsPopupManager` |

## Non-goals

No REST, WebSocket, SQLite, JWT, or business logic in the panel.
