# Connection State

Canonical enum: `SrotsConnectionState`

| Value | StatusBar label |
|-------|-----------------|
| ONLINE | Connected |
| OFFLINE | Offline |
| SYNCING | Connecting... |
| SYNC_ERROR | Connection unavailable |
| UNKNOWN | Connection unavailable |

Source of truth for shell chrome: `TopBarApplicationState.connectionStateProperty()`.

StatusBar and TopBar both observe it; neither owns sockets or HTTP clients.
