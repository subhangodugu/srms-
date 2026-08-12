# Navigation

## Purpose

Wayfinding chrome: sidebar, top bar / status indicators, breadcrumbs, and in-page tabs. Navigation components present structure and selection; routing and permission filtering belong in the application layer.

## Usage

| Area | Package |
|------|---------|
| Sidebar | `com.srots.presentation.components.navigation.sidebar` |
| Top / status | `com.srots.presentation.components.navigation.topbar` |
| Breadcrumb | `com.srots.presentation.components.navigation.breadcrumb` |
| Tabs | `com.srots.presentation.components.navigation.tabs` |

CSS: `.srots-sidebar`, `.srots-nav-item`, `.srots-nav-item-active`, `.srots-topbar`, `.srots-statusbar`.

## Key classes

| Class | Role |
|-------|------|
| `SrotsSidebar` | Collapsible nav rail |
| `SrotsNavigationGroup` | Labeled group of items |
| `SrotsNavigationItem` | Single destination entry |
| `SrotsTopBar` | Search / notifications / profile host |
| `SrotsStatusBar` | Connection / sync / version strip |
| `SrotsConnectionIndicator` | Online/offline presentation |
| `SrotsSyncIndicator` | Sync state presentation |
| `SrotsVersionIndicator` | Build/version label |
| `SrotsBreadcrumb` | Hierarchy path |
| `SrotsTabView` | Related entity sections |

## Properties

| Component | Highlights |
|-----------|------------|
| `SrotsSidebar` | `collapsedProperty`, `addGroup`, `addItem`, `clearNavigation` |
| `SrotsNavigationItem` | label, icon, active style class |
| `SrotsTopBar` | left/center/right slots (compose children) |
| `SrotsConnectionIndicator` | `SrotsConnectionState` |
| `SrotsBreadcrumb` | path segments |
| `SrotsTabView` | tab titles + content nodes |

## States

| State | Where |
|-------|--------|
| Expanded / collapsed | Sidebar |
| Active / inactive | Nav items (`.srots-nav-item-active`) |
| Online / offline / syncing / sync-error | Top/status indicators |
| Selected tab | `SrotsTabView` |

## Events

Expose selection via item `setOnAction` / property listeners. Do not embed navigation controllers inside these classes — the host screen maps item IDs to routes.

## Accessibility

- Active nav item must be distinguishable by more than color (text weight / indicator).
- Collapsed sidebar: icon-only items need tooltips (`SrotsTooltip`).
- Tabs: arrow-key movement where provided; keep focus rings visible.
- Breadcrumb links remain keyboard-focusable.

## Do / Don't

| Do | Don't |
|----|-------|
| Build nav trees from permission-filtered models | Hard-code role checks inside `SrotsSidebar` |
| Use one sidebar instance in the shell | Recreate competing nav rails per module |
| Mark exactly one active destination | Style raw `Button`s as nav without `SrotsNavigationItem` |

## Example

```java
SrotsSidebar sidebar = new SrotsSidebar();
SrotsNavigationGroup ops = new SrotsNavigationGroup("Operations");
SrotsNavigationItem companies = new SrotsNavigationItem("Companies");
companies.setOnAction(e -> viewModel.openCompanies());
ops.addItem(companies);
sidebar.addGroup(ops);

SrotsBreadcrumb crumb = new SrotsBreadcrumb();
// add segments for current hierarchy

SrotsTabView tabs = new SrotsTabView();
tabs.addTab("Overview", overviewNode);
tabs.addTab("History", historyNode);
```
