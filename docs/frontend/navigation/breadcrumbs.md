# TopBar Breadcrumbs

Breadcrumbs are contextual navigation metadata, not a second router.

```text
NavigationRegistry.breadcrumbPath(route)
        ↓
SrotsTopBarViewModel
        ↓
SrotsBreadcrumb (click → NavigationService.navigate)
```

## Truncation

- Full: `Products / COMPTY / Releases`
- Compact: `Products / … / Releases`

Long trails must never push notifications/profile off-screen.
