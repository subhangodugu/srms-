# SROTS Notification Actions

```text
Click item → markAsRead(id) → NotificationAction → NavigationService / DialogService
```

| Action type | Behavior |
|-------------|----------|
| NAVIGATE | `NavigationService.navigate(route)` |
| NONE | Mark read only |
| OPEN_* | Reserved for future command/dialog wiring |

Authorization remains outside the UI: navigating to a destination does not grant access.
