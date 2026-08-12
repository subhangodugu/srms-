# TopBar Actions

Contextual actions are data-driven via `SrotsTopBarAction`:

- id, label, icon, tooltip
- enabled / visible
- priority
- onAction callback (application command)

## Limits

Show at most **3** visible actions in the TopBar.

Pages publish actions through the ViewModel (`setActions`); the TopBar does not hard-code route → button maps.
