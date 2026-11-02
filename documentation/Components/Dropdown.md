The dropdown compounds live in
`com.michaelflisar.composestyled.common`. Their anchors compose
`StyledButton` with the same `Primary`, `Secondary` and `Tertiary` roles,
alongside `StyledIcon`, `StyledText`, `StyledInput`, `StyledCheckbox` and styled
action buttons. They do not own a separate theme-specific renderer and work
with all registered themes, without Material 3 dependencies in core.

```kotlin
val fruits = listOf("Apple", "Banana", "Cherry")
val selected = remember { mutableStateOf(fruits.first()) }
val selectedIndices = remember { mutableStateOf(emptyList<Int>()) }
val filter = remember {
    StyledDropdown.Filter<String>("Filter fruit") { query, item ->
        item.contains(query, ignoreCase = true)
    }
}

StyledDropdown(
    items = fruits,
    selected = selected,
    role = StyledButton.Role.Secondary,
    title = "Fruit",
    filter = filter,
    popupWidthConfig = StyledPopupMenu.WidthConfig.AtLeastAnchor,
    content = StyledDropdownDefaults.defaultRenderer(
        mapper = { it.uppercase() },
        mapperDropdown = { "Choose $it" },
    ),
)
StyledDropdownMultiSelectIndex(
    items = fruits,
    selectedIndices = selectedIndices,
    title = "Several fruits",
    filter = filter,
    sortSelectionByItems = false,
    actions = StyledDropdownMultiSelectDefaults.defaultActions(
        selectAllLabel = "Select all fruits",
        clearSelectionLabel = "Clear",
    ),
)
```

`StyledDropdown` and `StyledDropdownMultiSelect` accept selected values;
`StyledDropdownIndex` and `StyledDropdownMultiSelectIndex` accept original list
indices. Each has controlled value/callback and `MutableState` overloads. State
overloads update the state before invoking the optional callback. Single
selection closes the popup; multi-selection toggles items while keeping it open.
Callbacks always receive original indices, including when a filter hides items.
An absent single value or out-of-range single index displays no selection.
Multi-selection values must exist in `items`, and indices must be valid;
otherwise the component throws `IllegalArgumentException`. Repeated selected
indices are treated as one selection. Value-based selection uses the first
equal item; use index overloads to distinguish equal entries.

`StyledDropdown` and `StyledDropdownMultiSelect` default to
`StyledButton.Role.Default`, allowing the theme to choose an appropriate anchor
style for the surrounding context. All value- and index-based single- and
multi-select functions expose the same `role` parameter. Set an explicit
`StyledButton.Role` only when you intentionally want primary, secondary or
tertiary emphasis.

`sortSelectionByItems` defaults to `true`, ordering rendered selections and
callback results by original index. Set it to `false` to preserve selection
order. Filtering affects only visible entries: select-all and clear operate on
the complete list. The filter and scroll position reset on reopening. Disabling
an open dropdown dismisses it. The focusable popup dismisses through the platform
back/Escape and outside-click handling.

All dropdown functions accept `shape` and button `customization`; titles
render inside the button anchor. Popup colors inherit the styled surface tokens.
`popupWidthConfig` supports `MatchAnchor` (default),
`AtLeastAnchor`, `WrapContent` and `Fixed(width)`. Width is constrained by the
window. `popupMaxHeight` defaults to `320.dp`; the title, filter and
multi-selection actions remain pinned above the scrollable entries. Popups align
to the logical start, prefer opening below the anchor, and move above it when
space permits.

Material 3 uses the shared popup layout for this pinned header, styled with the
active Material 3 surface, shape, border and elevation tokens; its native
dropdown menu scrolls the complete content and cannot pin a header separately.

Dropdown `role` accepts the `StyledButton.Role` values directly; its
`RenderContext.Selection` and `RenderContext.DropdownItem` are content data
contexts, not theme-mapping contexts. Renderer helpers retain separate
selection and item customization: text
mappers, selected/unselected colors and font weights, line limits, leading
content and the multi-selection formatter. To replace the complete rendering,
use the `content`/`dropdownContent` overload of `defaultRenderer`, or handle
`StyledDropdown.RenderContext.Selection` and `DropdownItem` yourself.
Multi-selection entries use passive styled checkboxes; the row owns the toggle
semantics. The multi-selection `actions` slot can replace the complete header;
its context provides `canSelectAll`, `canClear`, `selectAll` and
`clearSelection`. Supply localized action labels with `defaultActions`.

The former `MyDropdown`, `MyDropdownIndex`, `MyDropdownMultiSelect`,
`MyDropdownMultiSelectIndex` and their defaults/context types have been replaced
by the corresponding `Styled*` APIs. No `My*` compatibility aliases are retained.
