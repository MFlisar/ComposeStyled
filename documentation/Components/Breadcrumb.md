# Breadcrumb

`StyledBreadcrumb` displays a hierarchical path using styled text and a
configurable separator icon. The current item is styled separately and is never
clickable; earlier items can receive optional click actions. Clickable segments
use `StyledClickable`, preserving theme-specific feedback without imposing a
button's minimum width.

```kotlin
StyledBreadcrumb(
    items = listOf(
        StyledBreadcrumbItem(
            label = "Home",
            icon = StyledIcons.Home,
            onClick = { /* navigate to home */ },
        ),
        StyledBreadcrumbItem(
            icon = StyledIcons.Settings,
            iconContentDescription = "Settings",
            onClick = { /* navigate to settings */ },
        ),
        StyledBreadcrumbItem(label = "Font size"),
    ),
)
```

Each item's `label` and `icon` are optional, allowing text-only, icon-only, or
combined items. Provide `iconContentDescription` for icon-only items so they
have an accessible name.

Customize colors, typography, spacing and separator size with
`rememberStyledBreadcrumbStyle`, or provide a different `separatorIcon`. Long
paths remain on one line and can be scrolled horizontally. By default, adding
items animates the scroll to the end; set `scrollToEndOnItemsAdded = false` to
disable this behavior. Replacing or removing items does not trigger an
auto-scroll.

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
