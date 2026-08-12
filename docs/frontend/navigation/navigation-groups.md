# Navigation Groups

`NavigationGroup` organizes sidebar sections:

| Group | Typical content |
|-------|-----------------|
| OVERVIEW | Overview, Design System |
| WORKSPACE | My Workspace + children |
| COMPANY | Employees, Teams, Departments, Organization |
| WORK | Projects, Tasks, Issues |
| PRODUCTS | Products, SROTS, COMPTY |
| ENGINEERING | Engineering tree |
| RELEASE | Versions, Releases, Deployments |
| BUSINESS | Sales tree |
| SUPPORT | Support, Knowledge |
| SYSTEM | Analytics (admin), Settings |

## Group collapse vs sidebar collapse

- **Sidebar collapse**: icon-only chrome (68px). Group headers hide; items remain.
- **Group collapse**: hides that group's children while the sidebar stays expanded.

Do not share state between these modes.
