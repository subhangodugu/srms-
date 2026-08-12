# Window Lifecycle

## Startup

```text
SrotsLauncher
  → Splash / ApplicationBootstrap
  → Session ready
  → PrimaryWindowFactory
  → SrotsWindowManager.createMainWindow
  → restore window state
  → NavigationService.navigate(OVERVIEW)
  → show Stage
```

Login must not instantiate `OverviewView` directly — it navigates via `NavigationService`.

## Runtime

User actions → `NavigationRequest` → `NavigationService` → ContentHost feature swap. Shell chrome stays alive.

## Shutdown

```text
Window close
  → ApplicationLifecycleService.confirmClose (unsaved work if any)
  → save window state
  → stop background tasks / ShutdownCoordinator
  → release resources
  → Platform.exit
```

Forced shutdown logs and does not leave services running indefinitely.

## Session

Logout / session expiry invalidates protected content and returns to Login through session services — not by leaving stale authenticated screens usable.
