# Window Management (Prompt 20)

Primary desktop window model:

```text
SrotsWindowManager
  → SrotsMainWindow
  → JavaFX Stage + Scene
  → SrotsAppShell
```

One primary application window. Secondary windows (reports/editors) go through `SrotsWindowManager` when needed; dialogs use `DialogService`.

## Configuration (centralized)

| Constant | Value |
|----------|-------|
| Default | 1440 × 900 |
| Minimum | 1100 × 700 |
| Sidebar expanded | 240–280 (token: 260) |
| Sidebar collapsed | 64–80 (token: 68) |

Do not scatter dimensions across feature classes. See `SrotsWindowConfiguration` / `AppConstants`.

## Persistence

`SrotsWindowPreferences` + `SrotsWindowStateStore` persist:

- width, height, x, y
- maximized
- sidebar collapsed

On restore, `sanitize()` clamps size and recenters if the saved position does not intersect any monitor (multi-monitor / DPI safe).

## Behavior

- Native resize / minimize / maximize / restore
- Maximized state restored after bounds apply
- Close runs `ApplicationLifecycleService` (unsaved-work confirm only when needed), then saves window state
- Prefer OS window decoration when available; undecorated chrome uses TopBar window controls

## Must not

Window manager must not contain business logic, database access, authentication, or navigation rules.
