# SROTS Sign-out Flow

```text
Sign out → confirmation dialog → AuthenticationService.signOut()
        → SessionService.beginSignOut / completeSignOut
        → NavigationService.navigate(LOGIN)
```

## Rules

1. UI never clears JWT/tokens directly.
2. Concurrent / repeat sign-out while already signed out is ignored.
3. During `SIGNING_OUT`, the Sign out menu item is disabled.
4. Failure restores the previous user and shows a safe error (no stack traces).
5. Unsaved-work checks belong to application/session lifecycle (future), not the menu itself.

## Session transitions

| State | Profile UI |
|-------|------------|
| AUTHENTICATED | Visible |
| SIGNING_OUT | Visible; Sign out disabled |
| SIGNED_OUT / SESSION_EXPIRED | Hidden; menu closed |
