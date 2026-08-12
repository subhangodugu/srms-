# Search Result Model

Application DTO: `SearchResult`

| Field | Purpose |
|-------|---------|
| id | Stable id |
| type | `SearchEntityType` |
| title / subtitle | Display |
| score | Relevance |
| routeId | Navigation route enum name |
| entityReference | Optional deep link |

UI flattens groups into `SrotsSearchListEntry` for ListView virtualization.
