# SROTS Status Bar

## Role

Operational status frame at the bottom of `SrotsAppShell`.

```text
ConnectionState + ApplicationActivity + Build/Env metadata
        ↓
SrotsStatusBarViewModel
        ↓
SrotsStatusBar
```

## Regions

| Region | Content |
|--------|---------|
| Left | `SrotsConnectionIndicator` (shared connection state with TopBar) |
| Center | Activity message + compact progress |
| Right | Environment · data mode (dev only) · version |

## Packages

| Class | Location |
|-------|----------|
| `SrotsStatusBar` | `components.navigation.topbar` |
| `SrotsStatusBarViewModel` | `shell.statusbar` |
| `ApplicationActivityService` | `shell.statusbar` |
| `StatusBarEnvironmentInfo` | `shell.statusbar` |

## Rules

- Version comes from centralized build metadata (passed from launcher).
- Production never shows Mock Data.
- StatusBar displays state; it does not open DB/REST/WebSocket connections.
- Same `TopBarApplicationState.connectionState` drives TopBar + StatusBar.
