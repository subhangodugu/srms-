# SROTS Notification Model

## Presentation model

`SrotsNotification`

| Field | Notes |
|-------|-------|
| id | Stable id |
| type | `NotificationKind` |
| title | Short |
| message | Context |
| timestamp | `Instant` (real) |
| read | boolean |
| priority | LOW / NORMAL / HIGH / CRITICAL |
| action | `NotificationAction` |

## Kinds

SYSTEM, TASK, PROJECT, RELEASE, DEPLOYMENT, SERVICE_DESK, APPROVAL, SECURITY, PRODUCT, COMPTY, GENERAL

## Icons

`NotificationIconResolver` maps kind → `SrotsIcon`. No per-item icon branching in the view.

## Timestamps

`NotificationTimestampFormatter` centralizes relative/absolute display.
