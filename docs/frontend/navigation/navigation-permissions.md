# Navigation Permissions

## Rule

Hiding a sidebar item is **not** authorization. Application/guards must still enforce access.

## Flow

```text
NavigationRegistry
      ↓
UserAccessContext (hasPermission / hasRole / isAuthenticated)
      ↓
NavigationVisibilityService.filterVisible(...)
      ↓
SrotsSidebarViewModel
      ↓
SrotsSidebar
```

## Contexts

| Implementation | Use |
|----------------|-----|
| `DevOpenAccessContext` | Local frontend bootstrap (all allowed) |
| `StaticUserAccessContext` | Tests / future mock sessions (`admin()`, `employee()`) |

## Catalog example

`Analytics` requires role `ADMIN`. Employees using `StaticUserAccessContext.employee()` do not see it.

Least privilege: if access context cannot be resolved, treat restricted items as hidden.
