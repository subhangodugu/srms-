# SROTS User Profile

## Role

TopBar identity control: who is signed in, and which account actions are available.

```text
SessionState → CurrentUserProvider → SrotsUserProfileViewModel → SrotsUserProfile → SrotsUserProfileMenu
```

## Package

`com.srots.presentation.profile`

| Type | Responsibility |
|------|----------------|
| `CurrentUser` | Presentation user snapshot (no secrets) |
| `SessionState` | AUTHENTICATED / SIGNING_OUT / SIGNED_OUT / SESSION_EXPIRED |
| `SessionService` / `DefaultSessionService` | Session + current user |
| `AuthenticationService` / `DefaultAuthenticationService` | Sign-out boundary |
| `SrotsUserProfileViewModel` | Presentation state + actions |
| `SrotsUserProfileController` | Button/popup lifecycle |
| `SrotsUserProfileMenu` | Popup overlay (not a Stage) |
| `SrotsPopupManager` | Exclusive TopBar popup coordination |

Reusable Prompt 07 control: `components.information.avatar.SrotsUserProfile` (+ `SrotsAvatar`, `UserInitials`).

## Display

- Avatar: image → initials → default (`?`)
- Display name and primary role (informational only)
- Compact TopBar: avatar + chevron

## Non-goals

No JWT, password, REST, DB, employee CRUD, or authorization enforcement in the UI.
