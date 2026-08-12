# SROTS Splash Screen (Prompt 12)

## Role

Startup presentation only — branding, status, progress. No login, data, or business logic.

```text
SrotsLauncher → SROTSApplication → ApplicationLifecycle
  → Splash visible
  → SrotsStartupTask (background bootstrap + progress)
  → Main window
  → Splash fade-out close
  → Overview
```

## Packages

| Location | Types |
|----------|--------|
| `presentation.splash` | `SrotsSplashWindow`, `SrotsSplashView`, `SrotsSplashViewModel`, `StartupPhase`, `StartupProgress` |
| `app.lifecycle` | `ApplicationLifecycle`, `SrotsStartupTask` |
| Resources | `/images/srots-logo.png`, `/css/srots-splash.css` |

## Rules

- Progress maps to real phases (`CONFIGURATION` → `READY`)
- No artificial startup delay
- Failures stay on splash with Retry / Exit
- Version from `SrotsBuildInfo`
- Development badge only when `srots.env` is not production
