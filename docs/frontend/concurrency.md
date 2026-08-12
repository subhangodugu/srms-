# Concurrency (JavaFX)

## Hard rule

All UI updates run on the **JavaFX Application Thread**.

Never run on the FX thread:

- Database / repository I/O
- HTTP / WebSocket
- Large file parsing
- Heavy processing / AI inference

## Async flow

```text
User action → ViewModel → Background use case → Success/Error
→ Platform.runLater / Task callbacks → UI update
```

## Tools

Use the simplest appropriate abstraction:

- `Task` / `Service`
- `CompletableFuture`
- `ExecutorService`

## Thread safety

Do not mutate JavaFX nodes or properties from arbitrary background threads.
Publish results back to the FX thread in a controlled way.

## Cancellation

When leaving a screen: cancel outstanding tasks and clear listeners to avoid leaks and late UI updates.
