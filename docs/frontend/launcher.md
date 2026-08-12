# SROTS Application Launcher (Prompt 11)

## Purpose

Process entry and startup lifecycle for the **native JavaFX** desktop app.

```text
OS
 ↓
SrotsLauncher
 ↓
JavaFX Runtime (SROTSApplication)
 ↓
ApplicationBootstrap
 ├── Configuration
 ├── Logging
 ├── Dependency graph (AppContainer)
 ├── Data mode validation
 └── Presentation readiness
 ↓
PrimaryWindowFactory
├── App shell FXML
├── SrotsWindowManager / SrotsMainWindow
├── Theme + icons
├── Navigation + shortcuts
 └── Default route (OVERVIEW)
 ↓
SROTS Desktop Main Window
```

Main-window details: `docs/frontend/main-window.md` (Prompt 13).

## Entry points

| Class | Role |
|-------|------|
| `com.srots.app.SrotsLauncher` | Process `main` |
| `com.srots.app.SROTSApplication` | JavaFX `Application` (`init` / `start` / `stop`) |
| `ApplicationBootstrap` | Config + DI (no UI) |
| `PrimaryWindowFactory` | Primary Stage / Scene / shell |
| `ShutdownCoordinator` | Orderly shutdown |

Maven / javafx-maven-plugin main class: `com.srots.app.SrotsLauncher`

## Configuration

| Property | Default | Notes |
|----------|---------|--------|
| `srots.env` | `development` | `production` refuses MOCK |
| `srots.data.mode` | `MOCK` | LOCAL/REMOTE/HYBRID reserved |

## Startup UI

After configuration parse, {@link com.srots.app.lifecycle.ApplicationLifecycle}
shows the splash (Prompt 12), runs bootstrap on a background thread with real phase
progress, then opens the main shell and closes the splash.

See `docs/frontend/splash.md`.
