# Scenarios

| Scenario | Behavior |
|----------|----------|
| NORMAL | Full realistic dataset (default) |
| EMPTY | Zero records — empty-state UI |
| ERROR | Reads throw — error/retry UI |
| OFFLINE | Reads throw offline message |
| LOADING | Applies NORMAL latency for loading UI |
| LARGE | 1000+ employees, 5000+ tasks, 1000+ issues |

Apply via `MockInfrastructure.applyScenario(...)`.
