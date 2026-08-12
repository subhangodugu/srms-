# SROTS Notification Realtime Architecture

```text
Transport (WebSocket / SSE / poll) → NotificationService → NotificationState → UI
```

Rules:

1. Panel never opens a WebSocket.
2. Panel never calls REST.
3. Panel never polls in a UI loop.
4. New notifications arrive by updating `NotificationState` (e.g. `DefaultNotificationService.publish`).
5. Offline: keep last known notifications; show safe refresh errors without wiping the list.
