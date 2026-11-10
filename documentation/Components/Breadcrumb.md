# Breadcrumb

`StyledBreadcrumb` displays a hierarchical path using styled text and a
configurable separator icon. The current item is styled separately and is never
clickable; earlier items can receive optional click actions. Clickable segments
use `StyledClickable`, preserving theme-specific feedback without imposing a
button's minimum width.

```kotlin
StyledBreadcrumb(
    items = listOf(
        StyledBreadcrumbItem("Settings", onClick = { /* navigate to settings */ }),
        StyledBreadcrumbItem("Display", onClick = { /* navigate to display */ }),
        StyledBreadcrumbItem("Font size"),
    ),
)
```

Customize colors, typography, spacing and separator size with
`rememberStyledBreadcrumbStyle`, or provide a different `separatorIcon`. Long
paths remain on one line and can be scrolled horizontally.

## Preference navigation

Hoist one `PreferenceScreenState` and pass it to both the screen and
`PreferenceBreadcrumb`. This lets the breadcrumb live anywhere in the surrounding
layout while remaining synchronized with group navigation. Earlier group
segments navigate directly to that level; the current group is non-clickable.

```kotlin
val preferenceState = rememberPreferenceScreenState()

Column {
    PreferenceBreadcrumb(state = preferenceState, rootLabel = "Settings")
    PreferenceScreen(state = preferenceState) {
        PreferenceGroup(title = "Display") {
            // Preference content
        }
    }
}
```
