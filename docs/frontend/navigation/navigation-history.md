# Navigation History

`NavigationHistory` stores desktop back/forward stacks.

## Stored data

Each `NavigationHistoryEntry` holds:

- `route`
- `parameters`
- `timestamp`

Never store `Node`, `Scene`, `Parent`, or controllers.

## Behavior

- Navigate → push previous entry onto back stack; clear forward stack
- Back → pop back, push current to forward
- Forward → reverse
- Same-route no-op does not push history
- Max depth capped (default 100)

## Shortcuts

- Windows/Linux: `Alt+Left` back, `Alt+Right` forward
- Registered centrally via `NavigationShortcutRegistry`

Optional TopBar back/forward buttons must call `NavigationService.back()` / `forward()` — they must not keep independent history.
