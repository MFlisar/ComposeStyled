# Popup Dropdowns

`StyledPopupDropdown` and `StyledPopupDropdownMultiSelect` are input-like
selection controls. They own the selected values, filtering and popup behavior;
their content scope renders the selected value and each option. Use
`StyledPopupMenu` for a button-like command menu instead.

```kotlin
val fruits = listOf("Apple", "Banana", "Cherry")
val selected = remember { mutableStateOf(fruits.first()) }
val filter = StyledPopupDropdown.Filter<String>("Filter fruit") { query, item ->
    item.contains(query, ignoreCase = true)
}

StyledPopupDropdown(
    items = fruits,
    selected = selected,
    title = "Fruit",
    filter = filter,
    content = {
        selection { value ->
            StyledText(value ?: "Choose a fruit")
        }
        item { fruit, isSelected ->
            StyledText(
                text = fruit,
                color = if (isSelected) StyledTheme.colorScheme.primary
                else StyledTheme.colorScheme.onSurface,
            )
        }
    },
)
```

In the `content` scope, `selection { ... }` is called for the input-like
selected-value area and `item { item, selected -> ... }` for each option row.
The scope deliberately provides render data rather than exposing the internal
menu-row composables. `StyledPopupDropdownDefaults.defaultRenderer(...)` and
`StyledPopupDropdownMultiSelectDefaults.defaultRenderer(...)` provide the
standard styled presentation and accept text mappers, colors, typography and
leading content when full custom rendering is unnecessary. Their text inherits
the current Styled text style; an optional `textStyle` is merged over it for
both the selected value and option rows. `dropdownTextStyle` can separately
override the option-row style and defaults to `textStyle`. When used as a
button-backed trigger, the inherited style is the active theme's button text
style.

`StyledPopupDropdown` and `StyledPopupDropdownMultiSelect` accept selected
values. Their `StyledPopupDropdownIndex` and
`StyledPopupDropdownMultiSelectIndex` counterparts accept original list
indices, which distinguishes equal entries. Each component has controlled
value/callback and `MutableState` overloads. State overloads update the state
before invoking the optional callback. Single selection closes the popup;
multi-selection toggles options while keeping it open. Callbacks always receive
original indices, including when a filter hides entries. An absent single value
or out-of-range single index displays no selection. Multi-selection values must
exist in `items`, and indices must be valid; otherwise the component throws
`IllegalArgumentException`. Repeated selected indices are treated as one
selection. Value-based selection uses the first equal item; use index overloads
to distinguish duplicates. The value-based `StyledPopupDropdown` and
`StyledPopupDropdownMultiSelect` overloads also accept an optional
`MutableState<Boolean>` as `expanded` to control popup visibility externally;
the state is updated when the popup is dismissed or its trigger is clicked.

`StyledPopupDropdownMultiSelect` accepts an `actions` slot whose
`StyledPopupDropdownMultiSelect.Actions` value exposes `canSelectAll`,
`canClear`, `selectAll()` and `clearSelection()`. The default actions are
available from `StyledPopupDropdownMultiSelectDefaults.defaultActions(...)`.
`sortSelectionByItems` defaults to `true`, ordering rendered selections and
callback results by original index; set it to `false` to preserve selection
order. Filtering affects only visible entries: select-all and clear operate on
the complete list. The filter and scroll position reset on reopening.

The dropdown filter input uses `StyledInput.Context.DropdownFilter`. Built-in
renderers map its default `Auto` variant to an outlined input; explicit input
variants remain authoritative, and custom renderer mappings can override this
context-specific default.

All popup dropdowns accept a `StyledButton.Variant` for their input-like
trigger, plus `shape` and button `customization`. The default variant is
`Auto`, allowing the theme to choose an appropriate treatment. Fluent 2 maps
`Auto` dropdown anchors and built-in multi-selection actions to outlined
buttons; explicit variants remain authoritative. Use `popupWidthConfig` to
choose `StyledPopupMenu.WidthConfig.MatchAnchor` (the default),
`AtLeastAnchor`, `Auto`, `WrapContent` or `Fixed(width)`. The filter field fills
the resolved popup width but does not contribute its intrinsic width when
content-sized modes choose that width. Width is constrained by the window.
`popupHorizontalAlignment` defaults to `StyledPopupMenu.HorizontalAlignment.Start`;
use `End` to align the popup container's logical end edge with the anchor. This
only controls the popup's position relative to its anchor; option content keeps
its normal item layout and alignment. A positive horizontal offset moves toward
logical `End` in both layout directions.
`popupMaxHeight` defaults to `320.dp`; the title, filter and multi-selection
actions remain pinned above the scrollable options.

Popups prefer the selected horizontal alignment, fall back to the opposite edge
when needed, and clamp to the window if neither edge fits. They prefer opening
below the anchor, moving above it when space permits. Disabling an open dropdown
dismisses it. The focusable popup dismisses through platform back/Escape and
outside-click handling. Hiding scroll indicators through
`StyledTheme.behavior.showScrollbars` does not disable scrolling.
