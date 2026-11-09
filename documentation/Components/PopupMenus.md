# Popup Menus

Use `StyledPopup` for arbitrary popup content and `StyledPopupMenu` for a
button-triggered command menu. `StyledPopupMenu` is the menu entry point: its
`StyledPopupMenuScope` provides standard menu rows while retaining escape
hatches for custom composables.

```kotlin
StyledPopupMenu(
    triggerContent = { StyledText("Actions") },
) {
    item("Save", onClick = { save() })
    divider()
    header("More")
    item(
        text = { StyledText("Export") },
        onClick = { export() },
        closeOnClick = false,
    )
    selection(
        title = "Sort by",
        items = listOf("Name", "Date"),
        selectedIndex = sortIndex,
        onSelectionChange = { sortIndex = it },
    )
    checkbox(
        text = "Show hidden files",
        checked = showHidden,
        onCheckedChange = { showHidden = it },
    )
    custom {
        StyledText("Custom row content")
    }
}
```

Simple `item` entries dismiss the menu after their callback by default.
`closeOnClick = false` leaves the menu open. Use `checkbox` for checkable
entries; these stay open by default. `header` and `divider` organize a menu,
`selection` creates a submenu of options with a check mark on the selected item.
It closes only that submenu after selection, leaving its parent menus open. Use
the generic overload with `selected`, `label` and `onSelectionChange` for typed
items. `submenu` creates a cascading submenu, and `custom` accepts arbitrary
`RowScope` content with optional click behavior. Custom rows use medium theme
spacing between children and center them vertically by default; configure
`horizontalArrangement` or `verticalAlignment` when needed. `customColumn`
inserts custom content directly into the popup column. Call `dismiss()` from a
selection callback to close the complete menu hierarchy, or `dismissSubmenu()`
to close only the current submenu and its descendants.

The button-like menu trigger uses a `StyledButton` variant and supports
`enabled`, `interactionSource`, `shape` and button `customization`. Its
caller-owned `expanded` state is a `MutableState<Boolean>`; when omitted, the
state is remembered. Menu width, shape, colors, offset, maximum height and
animation can be configured through the `menu*` parameters. Set
`menuHorizontalAlignment` to `StyledPopupMenu.HorizontalAlignment.End` to align
the menu's logical end edge with its anchor; `Start` is the default.

`StyledPopupContainer` is the shared anchored-overlay container. It measures
the anchor and supplies a scope for its `popup` slot; it does not own visibility
or dismissal. `StyledPopup` renders arbitrary content, while the
`StyledPopupContainerScope.StyledPopupMenu` extension renders the same menu DSL
inside a custom anchored popup:

```kotlin
val infoExpanded = remember { mutableStateOf(false) }

StyledPopupContainer(
    popup = {
        StyledPopup(
            visible = infoExpanded,
            content = {
                StyledText(
                    modifier = Modifier.padding(StyledTheme.padding.medium),
                    text = "A short explanation of this option.",
                )
            },
        )
    },
    content = {
        StyledButton(onClick = { infoExpanded.value = true }) {
            StyledText("About")
        }
    },
)
```

For a custom-trigger menu, replace the `StyledPopup` slot with the scoped menu
extension. This keeps menu behavior and dismissal consistent while letting the
caller own the anchor:

```kotlin
val menuExpanded = remember { mutableStateOf(false) }

StyledPopupContainer(
    popup = {
        StyledPopupMenu(
            expanded = menuExpanded.value,
            onDismissRequest = { menuExpanded.value = false },
        ) {
            item("Action", onClick = { performAction() })
        }
    },
    content = {
        StyledButton(onClick = { menuExpanded.value = true }) {
            StyledText("Open menu")
        }
    },
)
```

This anchored overload supports a popup header, width and placement rules,
horizontal alignment, scroll state, shape, colors, elevation, border, maximum
height, content padding, outside-tap behavior and animation.
`StyledPopupMenu.WidthConfig.Auto` wraps
content with an optional width cap; `MatchParent` matches the measured anchor
width (optionally capped). `MatchAnchor`, `AtLeastAnchor`, `WrapContent` and
`Fixed` are also available. Anchored menus prefer opening below the anchor and
move above it when needed. Submenus open beside their parent and flip to the
opposite side when space is limited.

Horizontal alignment is logical: `Start` aligns start edges and `End` aligns
end edges, mirrored automatically in RTL. A positive horizontal `offset` moves
toward logical `End`. If the preferred edge would place the menu outside the
window, the opposite alignment is tried before the position is clamped. This
alignment applies to anchor menus; submenu placement continues to open beside
its parent.

The default menu animation unfolds vertically away from its anchor. Customize
it with `StyledPopup.Animation`; `StyledPopupDefaults.style(animation = ...)`
configures the equivalent transition for generic popups. Menu-bar menus use a
short vertical unfold from the top without fading. Regular Foundation popups
measure content before starting the enter transition.

`StyledPopupMenu.OutsideTapBehavior.PlatformDefault` preserves the platform
popup's normal outside-tap behavior. `DismissAndConsume` uses a non-focusable,
full-window overlay when screen coordinates are available; it dismisses and
consumes outside taps instead of activating background content. This is useful
for menus that must stay open with the software keyboard visible. If screen
coordinates are unavailable, the platform popup behavior is used instead.
Anchored menus using the overlay are positioned and height-limited inside the
visible safe window bounds, including system insets and the keyboard.
`StyledSearchBar` suggestions use this mode; regular menus use the platform
default.

Menu content is scrollable when it exceeds the available height. Its scrollbar
indicator follows `StyledTheme.behavior.showScrollbars`; hiding indicators does
not disable scrolling. Material 3 uses its native dropdown-menu surface, while
Foundation-based renderers share the popup layout. Popup-surface and menu-row
renderers can be customized independently through the theme registry.
