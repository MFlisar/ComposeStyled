# Preferences

The `ui.preferences` package provides a composable-first preference screen. Declare
sections, nested groups and preference composables directly; the framework keeps
navigation, filtering, row styling and visibility/enabled dependencies consistent.
Preference components are provided in the
`com.michaelflisar.composepreferences.styled` package and use the
`Preference*` naming pattern.

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
            PreferenceBool(
                value = notifications,
                title = "Notifications",
                subtitle = "Show updates and reminders",
            )
            PreferenceList(
                items = listOf("System", "Light", "Dark"),
                selected = appearance,
                title = "Appearance",
            )
        }
        PreferenceGroup(
            title = "Colors",
            icon = StyledIcons.Settings,
        ) {
            PreferenceSection(title = "Toolbar") {
                PreferenceInputText(
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

`PreferenceSection` is not expandable by default, so its rows stay visible.
Set `isExpandable = true` to make a section collapsible; its rows animate
vertically and stay clipped to the animated content area until the transition
finishes. Use `isInitiallyExpanded = false` to start an expandable,
internally-managed section collapsed. The screen-wide default is configurable with
`PreferenceStyleDefaults.section(isExpandable = true)`, and each section can
override it. The overload with caller-owned `MutableState<Boolean>` state is
expandable by default; passing that state opts into controlled expansion.
`isExpandable = false` keeps its rows visible instead. Internally managed state
resets to `isInitiallyExpanded` when its group page is closed.
`PreferenceGroup` opens a separate list level with a slide transition. The root
list preserves its scroll position, while each group page starts at the top when
opened; leaving a group resets its scroll position and internally managed
section expansion state for the next visit. Caller-owned `expanded` state
remains under the caller's control.
Standalone preferences on a group page keep their own item shape; only rows
inside a `PreferenceSection` use the section's grouped shape.
Groups can also display an optional leading `IconComposable` through the `icon`
parameter.
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
the header and its rows use separate shapes instead. `PreferenceStyle.spacing`
groups the spacing configuration. `itemSpacing` controls the vertical gaps
between ordinary sibling entries. Independently, `sectionSpacing` is used
before a section header when `headerIsPartOfShape` is false, replacing the
list's regular gap at that boundary, and between the detached header and its
content. When the header is part of the shape, `inGroupSeparator` controls the
boundary between the header and its first row instead. It also controls
separation between rows inside an optically grouped section or group. It
defaults to a divider with no vertical padding; use
`PreferenceInGroupSeparatorStyle.Spacing` for gaps instead. No divider is drawn
after the last visible entry. Divider insets follow the preference item text
column, including `reserveLeadingIconSpace`, with `verticalPadding`
configurable. No divider is drawn between a detached section header and its
rows.

Non-expandable section headers default to a minimum height of `0.dp`;
expandable headers default to `PreferenceStyleDefaults.minimumItemHeight()`.
Configure them independently with `minimumItemHeight` and
`minimumExpandableItemHeight` in `PreferenceStyleDefaults.section(...)`.

```kotlin
val preferenceStyle = rememberPreferenceStyle(
    spacing = PreferenceStyleDefaults.spacing(
        inGroupSeparator = PreferenceInGroupSeparatorStyle.Divider(verticalPadding = 4.dp),
    ),
)
```

When filtering flattens the hierarchy, the ordinary `spacing.itemSpacing` is
used.
Separators do not change a section's shape. When the header is part of the
shape, its lower corners animate closed when collapsed and square when expanded.
Passing a non-null `shape` to a preference row overrides that row's shape and
breaks the section shape around it. The row and the gaps on both sides are
treated like ordinary sibling items: `inGroupSeparator` dividers are omitted,
and `itemSpacing` is used. If it is the first row, the same spacing replaces
the section-header separator. The section shape resumes after the gaps around
the row. This applies to `PreferenceItem`, `PreferenceBool`,
`PreferenceCustomItem`, `PreferenceGroup`, the input and number preferences,
split preferences, and both `PreferenceList` variants. For lists, `shape`
affects only the preference row; dropdown popup and option shapes keep their
defaults.

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
PreferenceBool(
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

- `PreferenceItem` is the standard row with optional icon, trailing
  `content` and optional `inlineContent`. Use either slot or both.
- `PreferenceCustomItem` renders a complete preference item with
  caller-defined content and the active preference item's default shape,
  colors, padding and minimum height. It has no title or subtitle; use
  `filterTags` when it should match preference searches.
- `PreferenceItem` is non-interactive when `onClick` is omitted; provide
  `onClick` for caller-defined actions.
- `PreferenceBool` renders a switch or checkbox and can make either the
  whole row or only the control toggle. `PreferenceSplitSwitch` and
  `PreferenceSplitCheckbox` separate navigation from the trailing toggle
  action.
- `PreferenceInputText` and `PreferenceInputNumber` edit in dialogs.
  `PreferenceNumber` uses a number-picker dialog or an inline slider. The
  slider sits below the title and supporting text, aligned with the text column
  after the leading icon, and spans to the trailing current-value label.
- `PreferenceList` and `PreferenceListMultiSelect` support dialog
  and dropdown presentations. Pass a `PreferenceList.Filter` to enable
  option filtering. `itemText` maps an option to its displayed and, by default,
  filtered text. Use `itemLeadingContent` for optional per-option leading
  content, such as a `StyledIcon`; it appears beside options in the open list
  and beside selected values when the list is closed, in either the preference
  row or the dropdown trigger. Dropdown presentation keeps the standard
  preference row and places a plain text-style dropdown in its trailing
  content. The trigger uses the active theme's `StyledButton` typography, with
  `PreferenceTrailingStyle.textStyle` applied as an override to the value and
  option text.
  `dropdownCustomization` can override the button's text foreground color.
  By default, clicking anywhere on the row opens the dropdown. To make only the
  trailing dropdown clickable, configure `clickBehavior` on `Presentation.Dropdown`:

  ```kotlin
  presentation = PreferenceList.Presentation.Dropdown(
      clickBehavior = PreferenceList.Presentation.Dropdown.ClickBehavior.DropdownOnly,
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
`spacing.inGroupSeparator` remains screen-wide because it separates grouped
content inside sections and opened group pages.

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

Sections are non-expandable by default; enable expansion only where it is
useful:

```kotlin
val preferenceStyle = rememberPreferenceStyle()

PreferenceScreen(style = preferenceStyle) {
    PreferenceSection(title = "Always visible") {
        // Preference rows
    }
    PreferenceSection(title = "Expandable", isExpandable = true) {
        // Preference rows
    }
}
```

The screen style also configures outer padding, reserved leading-icon space and
`spacing` (`itemSpacing`, `sectionSpacing` and `inGroupSeparator`). Standard rows accept
local shape, padding and color overrides; individual controls expose their
relevant `Styled*` variants and customization options.

`reserveLeadingIconSpace` defaults to `true`, so standard rows without a leading
icon keep the same alignment by rendering `StyledIcons.Empty`. Set
`reserveLeadingIconSpace` to `false` in `rememberPreferenceStyle` to remove
that reserved space.

Use `PreferenceStyleDefaults.openedGroupHeader(...)` to customize the back
row's container/content colors, shape, padding, minimum height and title style,
then pass it to `rememberPreferenceStyle(openedGroupHeader = ...)`. Its
`contentColor` controls both the back-row title and its leading icon.
