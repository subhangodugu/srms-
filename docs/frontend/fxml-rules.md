# FXML Rules

## When to use FXML

Prefer FXML when it improves layout/code separation:

- Complex screens
- Composite components
- Dialogs / forms
- Application shell

Do not create FXML for every trivial control.

## Controllers

- One clear responsibility per controller
- Avoid god controllers (`MainEverythingController`, unrelated multi-screen owners)
- Keep methods small; forward to ViewModel
- Prefer method references / clean `@FXML` handlers

## fx:id naming

Good: `employeeTable`, `searchField`, `statusBadge`, `createButton`  
Bad: `box1`, `button2`, `thing`, `temp`

## Resource loading

Load FXML through centralized mechanisms (`ViewFactory` / established loaders).
Do not duplicate `FXMLLoader` + location boilerplate on every screen.

## Event flow

```text
Button → Controller → ViewModel → Use Case → Repository
```

Never: Button → Controller → Repository.

## Class size

Avoid 500+ line controllers mixing unrelated UI and logic.
Split by feature / responsibility.
