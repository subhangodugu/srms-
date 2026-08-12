# SROTS Profile Menu

## Surface

JavaFX `Popup` (`SrotsUserProfileMenu`), width ~300px, top-right aligned to the profile button, clamped to window/screen bounds.

## Sections

1. Header — avatar, name, role, email  
2. Profile / Preferences / Settings  
3. About SROTS  
4. Sign out  

Help is omitted until a Help destination/service exists.

## Behavior

| Input | Result |
|-------|--------|
| Click profile | Toggle open/closed |
| Click outside / Escape | Close |
| Window resize/move | Close |
| Session expired / signed out | Close + hide profile |

## CSS

`srots-user-profile*`, `srots-user-menu-*` in `navigation.css`. No inline styles.

## Accessibility

- Button accessible name: Open/Close user profile menu  
- Menu items: labeled rows, keyboard Enter/Space/Escape/Tab  
