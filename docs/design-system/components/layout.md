# Layout

## Purpose

Application chrome and page structure: shell slots, page padding/scroll, headers, sections, panels, and master–detail splits. Layout components host content; they do not route, authorize, or load data.

## Usage

| Area | Package |
|------|---------|
| App shell | `com.srots.presentation.components.layout.app` |
| Page | `com.srots.presentation.components.layout.page` |
| Panel | `com.srots.presentation.components.layout.panel` |
| Split | `com.srots.presentation.components.layout.split` |

CSS: `.srots-shell`, `.srots-content`, `.srots-page`, `.srots-page-header` (Prompt 05/06).

## Key classes

| Class | Role |
|-------|------|
| `SrotsAppShell` | BorderPane chrome: sidebar / top bar / content host / status bar |
| `SrotsPageContainer` | Standard page: optional header + scrollable content |
| `SrotsPageHeader` | Title, description, primary/secondary actions |
| `SrotsSection` | Titled content block with section spacing |
| `SrotsPanel` | Surface panel for grouped content |
| `SrotsSplitView` | Resizable master–detail regions |

Prefer these over legacy `layout.PageHeader` / `SrotsContentContainer` when building new screens.

## Properties

| Component | API highlights |
|-----------|----------------|
| `SrotsAppShell` | `setSidebar`, `setTopBar`, `setStatusBar`, `setContent`, `getContentHost` |
| `SrotsPageContainer` | `setHeader`, `setContent` |
| `SrotsPageHeader` | title / description / action nodes (see class) |
| `SrotsSection` | title + content children |
| `SrotsPanel` | content region, optional title |
| `SrotsSplitView` | left/right (or top/bottom) nodes, divider position |

## States

Default layout only. Content inside may show loading/empty/error via [feedback](./feedback.md). Shell regions stay mounted; swap page content in the center host.

## Events

No domain events. Wire navigation and actions on child controls (`setOnAction`, selection listeners) from the screen/ViewModel.

## Accessibility

- Keep a single primary scroll host per page (`SrotsPageContainer`).
- Header title should be the page’s accessible name anchor.
- Split dividers must remain keyboard-reachable where JavaFX supports it; don’t remove focus styling.

## Do / Don't

| Do | Don't |
|----|-------|
| Compose shell once per window | Nest multiple `SrotsAppShell` instances |
| Put module UI in `setContent` | Put REST/auth logic in layout classes |
| Use `SrotsPageHeader` for page titles | Hand-roll padding that fights `.srots-page` |
| One authoritative layout per pattern | Duplicate shell markup per module |

## Example

```java
SrotsAppShell shell = new SrotsAppShell();
shell.setSidebar(sidebar);
shell.setTopBar(topBar);
shell.setStatusBar(statusBar);

SrotsPageHeader header = new SrotsPageHeader();
header.setTitle("Companies");

SrotsPageContainer page = new SrotsPageContainer();
page.setHeader(header);
page.setContent(new SrotsSection()); // fill with module content

shell.setContent(page);
```
