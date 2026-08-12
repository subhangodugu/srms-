# Activity State

`ApplicationActivityService` publishes `ApplicationActivity` snapshots:

- type (`SYNCING`, `IMPORTING`, `EXPORTING`, …)
- message (user-facing)
- optional determinate progress `0.0–1.0`
- failed flag

StatusBar consumes this service. It does not execute imports, syncs, or deployments.

Indeterminate operations show a compact spinner; determinate operations append `65%` only when progress is real.
