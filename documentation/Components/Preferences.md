# Preferences

The `ui.preferences` package provides a composable-first preference screen. Declare
sections, nested groups and preference composables directly; the framework keeps
navigation, filtering, row styling and visibility/enabled dependencies consistent.
Preference components are provided in the
`com.michaelflisar.composepreferences.styled` package and use the
`StyledPreference*` naming pattern.

## Basic screen

Keep the filter query and preference values in caller-owned state. The screen does
not add a search field; connect any search UI to `filterQuery`.

```kotlin
val filter = rememberPreferenceFilter()
val notifications = rememberSaveable { mutableStateOf(true) }
val appearance = rememberSaveable { mutableStateOf("System") }
val preferenceState = rememberPreferenceScreenState()

Column {
    StyledInput(
        value = filter.query,
        onValueChange = { filter.query = it },
        label = { StyledText("Search preferences") },
    )
    PreferenceBreadcrumb(state = preferenceState)
    PreferenceScreen(state = preferenceState, filter = filter) {
        PreferenceSection(
            title = "General",
            filterTags = listOf("Account"),
        ) {
            StyledPreferenceBool(
                value = notifications,
                title = "Notifications",
                subtitle = "Show updates and reminders",
            )
            StyledPreferenceList(
                items = listOf("System", "Light", "Dark"),
                selected = appearance,
                title = "Appearance",
            )
        }
        PreferenceGroup(title = "Colors") {
            PreferenceSection(title = "Toolbar") {
                StyledPreferenceInputText(
                    value = rememberSaveable { mutableStateOf("Blue") },
                    title = "Toolbar color",
                )
            }
        }
    }
}
```

Preference value components also have controlled overloads that take `value` and
`onValueChange`. Use those when the state is owned by a view model or another
caller.

## Sections, groups and state

`PreferenceSection` expands or collapses its children in the current list by
default. Its rows animate vertically during expansion and collapse, and stay
clipped to the animated content area until the transition finishes. Set
`isExpandable = false` on a section to keep its rows visible without an expand
icon or click behavior. The screen-wide default is configurable with
`PreferenceStyleDefaults.section(isExpandable = false)`; each section can
override that default with its own `isExpandable` value. Use
`isInitiallyExpanded = false` to start an individual section collapsed. For
fully controlled expansion, use the
overload that requires a caller-owned `MutableState<Boolean>` as `expanded`.
These overloads are mutually exclusive: a section uses either caller-owned
state or its own state initialized by `isInitiallyExpanded`. Internally managed
state resets to `isInitiallyExpanded` when its group page is closed.
`PreferenceGroup` opens a separate list level with a slide transition. The root
list preserves its scroll position, while each group page starts at the top when
opened; leaving a group resets its scroll position and internally managed
section expansion state for the next visit. Caller-owned `expanded` state
remains under the caller's control.
`rememberPreferenceScreenState()` can be supplied to `PreferenceScreen` when the
navigation stack also needs to be hoisted.
Pass the same state to `PreferenceBreadcrumb` to place a breadcrumb anywhere
around the screen. Its group segments navigate directly to that level; the
current group is displayed as the final, non-clickable segment.
Nested group pages show a title row with a back button by default. When a
`PreferenceBreadcrumb` already provides navigation, hide that row with
`rememberPreferenceStyle(openedGroupHeader = PreferenceOpenGroupHeaderStyle.Disabled)`.
Disabling the header also removes its top spacing above the group content.

By default, an expanded section is one clipped, rounded container containing
its header and rows. `PreferenceSectionStyle.headerIsPartOfShape = false` makes
the header and its rows use separate shapes instead. `itemSpacing` controls the
vertical gaps between sibling rows, sections and groups in a list.
`inGroupSeparator` controls separation inside an optically grouped section or
group, including the gap from a section header to its first row. It defaults to
spacing. Use a divider style to draw lines between visible entries; no divider
is drawn after the last visible entry. Divider insets follow the preference item
text column, including `reserveLeadingIconSpace`, with `verticalPadding`
configurable. When a section header is not part of the content shape, no divider
is drawn between them; `headerSpacing` provides a separate gap instead.

```kotlin
val preferenceStyle = rememberPreferenceStyle(
    inGroupSeparator = PreferenceInGroupSeparatorStyle.Divider(verticalPadding = 4.dp),
)
```

When filtering flattens the hierarchy, the ordinary `itemSpacing` is used.
Separators do not change a section's shape. When the header is part of the
shape, its lower corners animate closed when collapsed and square when expanded.

IDs are generated automatically for ordinary, statically ordered preference
compositions. Supply the optional `key` when an item or container may be inserted,
removed or reordered dynamically, so its saveable state and navigation identity
remain associated with the same preference; keys must be unique within their
container scope. For custom content that is removed
from composition, hoist its state or use `rememberSaveable` when it should survive
filtering and navigation.

Sections and groups can contribute `filterTags`, `isEnabled` and `isVisible`.
Their enabled and visible state is inherited by descendants. A preference can
define additional composable dependencies, for example:

```kotlin
StyledPreferenceBool(
    value = advanced,
    title = "Advanced option",
    isEnabled = { accountEnabled.value },
    isVisible = { userLevel.value >= 2 },
)
```

## Filtering

Titles, subtitles and `filterTags` are searched automatically. By default,
`filter.includeHierarchyInFilter` is `true`, so matching also considers ancestor
titles, subtitles and tags. While enabled, filtered results include a compact
section/group path once per consecutive block of matching preferences from the
same hierarchy. Disable it to search and show only the preference's own
information.

`filter.scope` controls where the query searches. `PreferenceFilter.Scope.Global`
searches the complete preference tree and is the default.
`PreferenceFilter.Scope.CurrentLevel` searches only the currently open page.
`PreferenceFilter.Scope.CurrentBranch` searches the open page and all nested
groups under it. Set the initial options when creating the filter, for example:

```kotlin
val filter = rememberPreferenceFilter(
    initialScope = PreferenceFilter.Scope.CurrentBranch,
    initialIncludeHierarchyInFilter = false,
)
```

These initial values apply when a new filter is created; a saved filter restores
its previous query and options. As a non-empty query changes, matching rows
expand or shrink and fade into or out of the results, including rows nested
inside groups. Switching between filtered and unfiltered results crossfades
the pages.
During current-level and current-branch filtering, `PreferenceBreadcrumb` keeps
the navigable current path and adds a filter indicator. During global filtering
it shows a non-navigable “Search everywhere” indicator instead of the current
route. Matching result paths remain informational.

## Preference types

- `StyledPreferenceItem` is the standard row with optional icon, trailing slot
  and inline content; `StyledPreferenceCustom` gives custom content and optional
  padding.
- `StyledPreferenceInfo` is non-interactive; use `StyledPreferenceItem` with
  `onClick` for caller-defined actions.
- `StyledPreferenceBool` renders a switch or checkbox and can make either the
  whole row or only the control toggle. `StyledPreferenceSplitSwitch` and
  `StyledPreferenceSplitCheckbox` separate navigation from the trailing toggle
  action.
- `StyledPreferenceInputText` and `StyledPreferenceInputNumber` edit in dialogs.
  `StyledPreferenceNumber` uses a number-picker dialog or an inline slider. The
  slider sits below the title and supporting text, aligned with the text column
  after the leading icon, and spans to the trailing current-value label.
- `StyledPreferenceList` and `StyledPreferenceListMultiSelect` support dialog
  and dropdown presentations. Pass a `StyledPreferenceList.Filter` to enable
  option filtering. Dropdown presentation keeps the standard preference row
  and places a plain text-style dropdown in its trailing content. The trigger
  uses the active theme's `StyledButton` typography, with
  `PreferenceTrailingStyle.textStyle` applied as an override to the value and
  option text.
  `dropdownCustomization` can override the button's text foreground color.
  By default, clicking anywhere on the row opens the dropdown. To make only the
  trailing dropdown clickable, configure `clickBehavior` on `Presentation.Dropdown`:

  ```kotlin
  presentation = StyledPreferenceList.Presentation.Dropdown(
      clickBehavior = StyledPreferenceList.Presentation.Dropdown.ClickBehavior.DropdownOnly,
  )
  ```
- Text/number editors, sliders and dialog-based selections show their current
  value in the trailing area by default; an explicit `subtitle` remains
  available for supporting text. Dropdown selections stay in the dropdown
  control, while boolean state is represented by its switch or checkbox.

## Styling

Create a `PreferenceStyle` with `rememberPreferenceStyle` and pass it to
`PreferenceScreen`. It has separate `item` and `section` configs
(`PreferenceItemStyle` and `PreferenceSectionStyle`) for colors, shape, content
padding, minimum row height and text styles. Group entries use the item config
and respect a surrounding section's shape; their opened content uses the item
shape. Each config's `colors` is a `PreferenceRowColors`; its `contentColor`
applies to the title and icon. `PreferenceSectionStyle` also controls whether
sections expand by default, their grouped-header corner size and whether the
header shares its shape with its rows.

Trailing content options are grouped in `PreferenceTrailingStyle`. Its
`textStyle` defaults to the theme's `bodySmall` typography, `contentColor`
defaults to the theme's primary color, and `maxWidthFraction` defaults to
`0.4f`. Trailing content is arranged at the logical end of its available width,
and text defaults to `TextAlign.End`; an explicit `textStyle.textAlign` is
preserved. Preference dropdown triggers use this style, while their option text
keeps logical-start alignment. Passing `null` for `contentColor` inherits the row's resolved color,
including any row-level override. The width fraction must be between `0f` and
`1f`; it applies to trailing content both beside the title and beside inline
content such as a slider. The title and subtitle use the remaining weighted
space and can wrap.
`openedGroupHeader` is a screen-level setting because it styles the back row on
an opened group page, independently of the group entry. It defaults to a
theme-derived style based on the item config. Set it to
`PreferenceOpenGroupHeaderStyle.Disabled` when a
`PreferenceBreadcrumb` provides navigation, or use
`PreferenceStyleDefaults.openedGroupHeader(...)` to customize the row.
`inGroupSeparator` remains screen-wide because it separates grouped content
inside sections and opened group pages.

`rememberPreferenceStyle` remembers the complete immutable style, so nested
style configs do not need their own `remember` calls.

Use `PreferenceStyleDefaults` to create individual style configs with defaults
from the active theme, then pass them to `rememberPreferenceStyle`. Ordinary
items default to at least 40 dp high while respecting the theme's minimum
interactive size. Style constructors are internal so these factories keep
theme-based defaults consistent.

```kotlin
val style = rememberPreferenceStyle(
    item = PreferenceStyleDefaults.item(minimumItemHeight = 44.dp),
    trailingStyle = PreferenceStyleDefaults.trailing(maxWidthFraction = 0.35f),
)
```

For example, create a section config from theme defaults to render each header
as its own shape:

```kotlin
val preferenceStyle = rememberPreferenceStyle(
    section = PreferenceStyleDefaults.section(headerIsPartOfShape = false),
)
```

Section expansion can also be disabled globally and selectively enabled for
individual sections:

```kotlin
val preferenceStyle = rememberPreferenceStyle(
    section = PreferenceStyleDefaults.section(isExpandable = false),
)

PreferenceScreen(style = preferenceStyle) {
    PreferenceSection(title = "Always visible") {
        // Preference rows
    }
    PreferenceSection(title = "Expandable", isExpandable = true) {
        // Preference rows
    }
}
```

The screen style also configures outer padding, reserved leading-icon space,
`itemSpacing` and `inGroupSeparator`. Standard rows accept local shape, padding
and color overrides; individual controls expose their relevant `Styled*`
variants and customization options.

`reserveLeadingIconSpace` defaults to `true`, so rows without a leading icon,
including `StyledPreferenceCustom`, keep the same alignment by rendering
`StyledIcons.Empty`. Set it to `false` in `rememberPreferenceStyle` to remove
that reserved space.

Use `PreferenceStyleDefaults.openedGroupHeader(...)` to customize the back
row's container/content colors, shape, padding, minimum height and title style,
then pass it to `rememberPreferenceStyle(openedGroupHeader = ...)`. Its
`contentColor` controls both the back-row title and its leading icon.
