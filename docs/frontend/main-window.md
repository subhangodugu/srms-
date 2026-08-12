# SROTS Main Window (Prompt 13)

## Purpose

Permanent native JavaFX frame after splash / bootstrap:

```text
SrotsLauncher → SROTSApplication → Splash → Bootstrap READY → SrotsMainWindow
```

One primary `Stage`, one `Scene`, one persistent AppShell. Modules swap only inside the content host.

## Package

`com.srots.presentation.window`

| Class | Responsibility |
|-------|----------------|
| `SrotsMainWindow` | Stage / Scene / icons / title bind / close save |
| `SrotsWindowManager` | create / show / hide / close / save / restore |
| `SrotsWindowConfiguration` | default 1280×820, min 1024×700 |
| `SrotsWindowState` | x, y, width, height, maximized, sidebarCollapsed |
| `SrotsWindowStateStore` | Preferences persistence + multi-monitor sanitize |
| `SrotsWindowTitleService` | `SROTS — {route title}` from navigation metadata |
| `SrotsWindowControls` | Top-bar minimize / maximize / close (undecorated stage) |

The main window opens **windowed** (not maximized) so caption buttons stay visible. Drag the top bar to move; double-click it to maximize or restore.

## Shell integration

`PrimaryWindowFactory` loads `MainView.fxml`, wires `NavigationModule`, then:

```text
SrotsWindowManager.createMainWindow(...)
 → restore window state
 → navigate OVERVIEW (if needed)
 → show Stage
```

Chrome (sidebar / topbar / content / status) stays alive across navigation.

## Non-goals

Main window must **not** own databases, REST clients, authentication, or feature business logic.

## Next

**Prompt 20** — Command palette + global keyboard shortcuts (shared Ctrl/Cmd + K with Global Search).
