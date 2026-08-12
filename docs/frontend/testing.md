# Frontend Testing

## Levels

| Level | Focus |
|-------|--------|
| Unit | ViewModel logic, pure state, formatters |
| Integration | Use cases + repository implementations (including mock) |
| TestFX / FX toolkit | Interaction behavior on JavaFX controls |

## Mock architecture

Use Prompt 09 mock repositories.

Never bypass ViewModel → Use Case → Repository just to make a UI test pass.

## Naming

Good:

```text
shouldNavigateToEmployeeListWhenEmployeeMenuSelected()
shouldDisplayErrorWhenEmployeeRepositoryFails()
shouldDisableSubmitWhenFormIsInvalid()
```

Bad: `test1()`, `testButton()`, `testSomething()`.

## What to assert

Test **behavior**, not incidental structure.

Good: user selects Employees → employee page appears.  
Bad: `VBox` child count == 7 (unless structure is the requirement).

## Component coverage

Important reusable components need tests (buttons, form fields, search, filters, tables, pagination, dialogs, toast, navigation).

## Quality gate

Frontend changes are complete only when:

```bash
mvn clean verify
```

passes (compile + tests; TestFX/toolkit coverage where applicable).

## Determinism

Prefer deterministic mock data (fixed IDs/dates). Avoid depending on wall-clock time or random UUIDs unless the test owns that clock.
