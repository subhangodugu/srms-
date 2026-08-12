# Forms

## Purpose

Labeled inputs, validation messaging, and selection controls. Fields display and collect values; validation rules and persistence stay in ViewModels/services.

## Usage

Package root: `com.srots.presentation.components.forms`

| Area | Package |
|------|---------|
| Field wrapper | `forms.field` |
| Text inputs | `forms.input` |
| Selection | `forms.selection` |
| Date | `forms.date` |

CSS: `.srots-form-field`, `.srots-text-field`, plus Prompt 06 invalid/focus states.

**Authoritative:** `SrotsFormField` wrapping `Srots*` inputs. Prefer over legacy `forms.FormField` for new work.

## Key classes

| Class | Role |
|-------|------|
| `SrotsFormField` | Label, required marker, helper/error/warning, control slot |
| `SrotsTextField` | Single-line text |
| `SrotsPasswordField` | Password entry |
| `SrotsTextArea` | Multi-line text |
| `SrotsComboBox` | Single select |
| `SrotsCheckBox` | Boolean |
| `SrotsRadioButton` | Exclusive choice |
| `SrotsToggle` | On/off switch |
| `SrotsDatePicker` | Date selection |

## Properties

| API | Notes |
|-----|--------|
| `setRequired(boolean)` | Required indicator |
| `setHelperText(String)` | Neutral guidance |
| `setError(String)` / `setWarning(String)` | Message + state class |
| `clearMessages()` | Reset field messaging |
| `getControl()` | Underlying input node |
| Input value APIs | Standard JavaFX `textProperty`, `valueProperty`, etc. |

## States

`Default · Hover · Focused · Disabled · Error · Warning · Read-only` (where applicable).

Show one message severity at a time (error wins over helper).

## Events

Bind to control properties (`textProperty`, `selectedProperty`, `valueProperty`). Do not put save/submit logic inside form components — host forms call ViewModel commands.

## Accessibility

- Label is associated with the control (wrapper owns the relationship).
- Errors must be text, not color-only; keep them near the field.
- Required fields announce via label marker + accessible text.
- Keyboard: Tab between fields; Space/Enter for toggles and buttons.

## Do / Don't

| Do | Don't |
|----|-------|
| Wrap every labeled input in `SrotsFormField` | Orphan `TextField`s without labels |
| Surface ViewModel validation via `setError` | Throw dialogs for every field typo |
| Reuse `Srots*` inputs | Mix raw JavaFX controls with conflicting CSS |

## Example

```java
SrotsTextField name = new SrotsTextField();
SrotsFormField nameField = new SrotsFormField("Company name", name);
nameField.setRequired(true);
nameField.setHelperText("Legal registered name");

name.textProperty().addListener((obs, o, n) -> {
    if (n == null || n.isBlank()) {
        nameField.setError("Name is required");
    } else {
        nameField.clearMessages();
    }
});
```
