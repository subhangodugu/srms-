# Feedback

## Purpose

System and page feedback: inline alerts, toasts, loading/empty/error/offline panels, and the notification center. Feedback components communicate state; retries and sync are triggered by the host.

## Usage

| Area | Package |
|------|---------|
| Alert | `com.srots.presentation.components.feedback.alert` |
| Toast | `com.srots.presentation.components.feedback.toast` |
| Loading | `com.srots.presentation.components.feedback.loading` |
| Empty | `com.srots.presentation.components.feedback.empty` |
| Error | `com.srots.presentation.components.feedback.error` |
| Offline | `com.srots.presentation.components.feedback.offline` |
| Notification | `com.srots.presentation.components.feedback.notification` |

CSS: `.srots-alert`, `.srots-toast`, `.srots-empty-state`, `.srots-error-state`.

## Key classes

| Class | Role |
|-------|------|
| `SrotsAlert` | Inline semantic banner (optional action / dismiss) |
| `SrotsToast` / `SrotsToastManager` | Transient notifications |
| `SrotsLoadingState` | Loading copy + indicator |
| `SrotsEmptyState` | Empty explanation + CTA |
| `SrotsErrorState` | Failure message + retry |
| `SrotsOfflineState` | Offline / pending changes presentation |
| `SrotsNotificationCenter` / `SrotsNotificationItem` | In-app notification list |
| `StatePanels` | Helpers to swap common state panels |

## Properties

| API | Notes |
|-----|--------|
| `SrotsAlert.of(variant, title, description)` | Factory |
| `setVariant` / `setTitle` / `setDescription` | Content |
| `setAction(label, Runnable)` | Inline CTA |
| `setDismissible` / `setOnDismiss` | Close behavior |
| Toast factories | `success`, `info`, `warning`, `error` |
| Empty/error/loading/offline | title, message, optional action node |

## States

Page-level: `Loading · Loaded · Empty · Error · Offline`. Toast variants: success / info / warning / error. Alerts: matching semantic variants.

## Events

Action / dismiss / retry runnables supplied by the host. Toast manager owns show/hide timing only.

## Accessibility

- Include title + description text; don’t rely on color or icon alone.
- Toasts should not steal focus for non-critical info; critical errors may use dialog/alert.
- Retry and dismiss controls are keyboard reachable.
- Live updates: keep wording concise for screen readers via accessible text.

## Do / Don't

| Do | Don't |
|----|-------|
| Use empty/error panels inside page content hosts | Leave blank regions with no explanation |
| Toast for short confirmations | Toast for long forms or blocking failures |
| Wire Retry to ViewModel reload | Swallow exceptions silently |

## Example

```java
contentHost.getChildren().setAll(
        SrotsLoadingState.of("Loading companies…")
);

// on success with no rows:
contentHost.getChildren().setAll(
        SrotsEmptyState.of("No companies", "Create a company to get started.",
                SrotsButton.primary("New company"))
);

// on failure:
contentHost.getChildren().setAll(
        SrotsErrorState.of("Could not load", "Check your connection, then retry.",
                () -> viewModel.reload())
);

SrotsToastManager.show(SrotsToast.success("Company saved"));
```
