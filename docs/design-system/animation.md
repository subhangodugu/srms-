# Motion & Animation Standards

Animation in SROTS is short, functional, and subtle. Prefer clarity over spectacle.

## Duration

| Range | Use |
|-------|-----|
| **120–220 ms** | Default UI transitions |
| < 120 ms | Micro feedback (press) if needed |
| > 220 ms | Avoid for routine UI |

## Appropriate uses

- Page / content region transitions
- Dialog appearance / dismissal
- Sidebar expand / collapse
- Drawer slide-in
- Loading indicators
- Status changes (badge / connectivity)

## Avoid

- Excessive or looping decorative motion
- Long transitions
- Constant movement on dashboards
- Parallax / bounce / gaming effects
- Animating layout thrash on every data poll

## Implementation notes (JavaFX)

- Prefer `FadeTransition`, `TranslateTransition`, or `Timeline` with durations in the 120–220 ms band.
- Respect future “reduce motion” preferences by skipping non-essential transitions when configured.
- Do not animate via unsupported web CSS `@keyframes` in JavaFX stylesheets.
- Loading spinners are allowed to animate continuously while an operation is in progress; stop immediately on completion/error.

## State change guidance

| Change | Motion |
|--------|--------|
| Dialog open | Fade + slight scale/slide ≤ 220 ms |
| Sidebar collapse | Width transition ≤ 220 ms |
| Toast in | Fade/slide ≤ 200 ms |
| Toast out | Fade ≤ 180 ms |
| Row selection | Instant or ≤ 120 ms background |

## Performance

Animate transform/opacity where possible; avoid repeatedly rebuilding large table cell graphs solely for animation.
