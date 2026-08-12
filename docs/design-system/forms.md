# Form Design System

Consistent field anatomy, validation, and control styling for SROTS Desktop.

## Field anatomy

```text
Label
Input
Helper text
Validation message
```

Example pattern:

```text
Product Name *

[ COMPTY                         ]

Product Code *

[ COMPTY-ATE                    ]

Description

[                              ]
[                              ]

                         [Cancel] [Create Product]
```

| Element | Class | Spec |
|---------|-------|------|
| Label | `.srots-form-label` | 12 px, bold, secondary text |
| Helper | `.srots-form-helper` | 11 px, muted |
| Error | `.srots-form-error` | 11 px, bold, danger |
| Invalid control | `.srots-invalid` | Danger border |

## Controls

| Control | Style class |
|---------|-------------|
| TextField | `.srots-text-field` |
| PasswordField | (same field styles) |
| ComboBox | `.srots-combo-box` |
| TextArea | `.srots-text-area` |
| DatePicker | `.srots-date-picker` |
| CheckBox | `.srots-check-box` |
| RadioButton | (semantic group; theme-aligned) |
| Toggle | `.srots-toggle` |

Shared field chrome:

- Background: `-srots-surface-elevated`
- Border: `-srots-border`, radius 6 px
- Text: `-srots-text-primary`
- Prompt: `-srots-text-muted`
- Padding: 8 × 12
- Focus: `-srots-border-focus`

## Validation states

```text
Neutral · Focused · Valid · Invalid · Disabled
```

| State | Visual |
|-------|--------|
| Neutral | Default border |
| Focused | Primary focus border |
| Valid | Optional subtle success cue when explicitly confirmed |
| Invalid | `.srots-invalid` + `.srots-form-error` message |
| Disabled | Disabled text; non-editable |

### Error message quality

| Bad | Better |
|-----|--------|
| Invalid input | Product code must contain 3–30 characters. |

Errors must be **specific, short, and actionable**. Do not show stack traces.

## Layout

- Vertical stack of label → control → helper/error with spacing **8–12**.
- Form dialogs: fields in a single column by default; two columns only when labels remain scannable.
- Footer actions: Secondary (Cancel) left of Primary (Submit); Danger only for destructive confirms.

## Search fields

- Page/filter search uses the same field chrome inside `FilterBar`.
- Global search lives in the topbar and must look distinct (placement + optional shortcut hint).

## Filters

Compose ComboBoxes / DatePickers / Search with `[Clear Filters]`. Clearing resets to defaults without navigating away.

## CSS source

`forms.css`
