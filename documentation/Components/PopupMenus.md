# Popup Menus

`StyledPopupContainer` is the single anchored-overlay container. It measures
the anchor and supplies a scope for its `popup` slot. It does not own visibility
or dismissal; compose either `StyledPopup` or `StyledPopupMenu` in the slot.
Generic popups, menus and future tooltips use this same container.

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

`StyledPopupMenu` is the menu-specific registered component and has
`ColumnScope` content. Dismissal does not automatically select or close items,
so callbacks update the caller-owned `expanded` state:

```kotlin
val menuExpanded = remember { mutableStateOf(false) }

StyledPopupContainer(
    popup = {
        StyledPopupMenu(
            expanded = menuExpanded.value,
            onDismissRequest = { menuExpanded.value = false },
        ) {
            StyledDropdownMenuItem(
                text = { StyledText("Action") },
                onClick = { menuExpanded.value = false },
            )
        }
    },
    content = {
        StyledButton(onClick = { menuExpanded.value = true }) {
            StyledText("Open menu")
        }
    },
)
```

`StyledPopupMenu.WidthConfig.Auto(maxWidth = 280.dp, limitToContainerWidth = false)`
wraps content with an optional width cap. `MatchParent(maxWidth = null)` matches
the measured container width exactly, optionally capped; unlike the legacy
minimum-width behavior it cannot grow beyond that width. Existing `MatchAnchor`,
`AtLeastAnchor`, `WrapContent` and `Fixed` options are also available.
Customize `offset`, `scrollState`, `maxHeight`, `shape`, `containerColor`,
`contentColor`, `shadowElevation` and `border`. Material-only `tonalElevation`
is replaced by explicit Styled colors; no tonal overlay is applied.
Menu defaults are centralized in `StyledPopupMenuDefaults`, including width
configuration, size limits, offset, shape, colors, shadow, border and content
padding. Theme-dependent defaults are read from the current Styled locals.
Cupertino Liquid popup menus use Liquid Glass without an additional drop
shadow, regardless of the configured `shadowElevation`.
The ComposeStyled-specific `outsideTapBehavior` is separate from the platform
`PopupProperties` and defaults to `PlatformDefault`, preserving the platform
popup's normal outside-tap behavior. `DismissAndConsume` uses a non-focusable,
full-window overlay that closes on outside taps and consumes them instead of
passing them to the background. This mode is suitable for menus that must stay
open with the software keyboard visible. The overlay consumes the complete
pointer gesture before requesting dismissal, so the same tap cannot activate
background content, while the keyboard area remains outside the overlay's
touchable dismissal bounds. If screen coordinates are unavailable, the menu
uses the platform popup behavior instead. Anchored menus in overlay mode are
positioned and height-limited inside the visible safe window bounds, accounting
for system insets and the keyboard as those bounds change. On Android, the
overlay window tracks the currently visible host bounds, leaving the keyboard
interactive; its menu is remeasured after the available bounds or measured
content size changes. The built-in
`StyledSearchBar` suggestions use this mode; regular menus keep the platform's
default popup behavior.

Use `StyledDropdownMenuItem(text = { StyledText("Action") }, onClick = { ... })`
inside the menu. It supports `leadingIcon`, `trailingIcon`, `enabled`,
`interactionSource`, `contentPadding`, outer `itemPadding` and `shape`.
`StyledDropdownMenuItemDefaults.colors()` configures text and both icon colors,
including independent disabled colors. Slots inherit Styled content locals.
Items provide keyboard activation and hover/focus/press feedback.
Single- and multi-select dropdowns use this same menu component.
For custom row content, use `StyledDropdownMenuContent` with `RowScope` content.
It uses the menu-item renderer and defaults, so custom rows share the standard
item layout and styling. Its nullable `onClick` defaults to `null`; provide a
callback to enable the same click, keyboard and interaction feedback as a
regular item. Without a callback the content remains non-interactive and keeps
its normal appearance; setting `enabled = false` applies the disabled appearance.
Use `StyledDropdownMenuSectionHeader(text = { ... })` to label groups. It uses
`labelLarge` typography and the theme's primary color by default, aligns its
start inset with menu items, and exposes heading semantics. Styled locals carry
its text style and color to nested content. Override `textStyle`, `contentColor`,
`itemPadding` or `contentPadding` when a menu needs a different hierarchy.
The menu's nullable `header` slot defaults to `null`. Material3 keeps its native
`DropdownMenu` surface and uses the supplied `scrollState` for its scrollable
content, including the header. Cupertino and Fluent2 keep the header above their
scrollable list. Dropdown filters and multi-selection actions use that slot.
`contentPadding` controls padding within the scrollable section.
Use `StyledDropdownMenuDivider()` to separate groups with a horizontal divider
and vertical spacing based on `StyledTheme.spacing.small`.

When the menu content overflows, it displays a scrollbar indicator according
to `StyledTheme.behavior.showScrollbars`. Hiding indicators globally leaves
menus and dropdowns scrollable.

`StyledDropdownButton(menuContent = ..., expanded = ...) { StyledText("Actions") }`
combines a themed `StyledButton`, an animated expand icon and a Styled dropdown
menu. `expanded` is optional caller-owned state; by default it is remembered.
The `menuContent` slot has `ColumnScope` and receives a `dismiss` callback.
Compose `StyledDropdownMenuItem`, `StyledHorizontalDivider` or arbitrary content
directly; there is no separate entry model. Call `dismiss()` after an action to
close the menu, or omit it to keep the menu open.
The menu is hidden and closed when the button is disabled.
An `onClick` overload performs a direct action without opening a menu.

`StyledDropdownButton` defaults to `StyledButton.Role.Default`, so its theme can
choose a style appropriate to a toolbar, modal or other surrounding context.
Use `role` for an intentional explicit `Primary`, `Secondary` or `Tertiary`
emphasis. The same generic API has both an `onClick` and a `menuContent`
overload. Shared button parameters follow `StyledButton` ordering: action,
modifier, enabled, interactionSource, role, shape, customization and content,
with menu state/configuration where applicable.
Button customization uses `shape`,
`customization = StyledButtonDefaults.customize(...)` and `interactionSource` instead of
Material button colors/elevation types. Menu width, shape, colors, offset and
maximum height are separately adjustable with the `menu*` parameters.

```kotlin
StyledDropdownButton(
    menuContent = { dismiss ->
        StyledDropdownMenuItem(
            text = { StyledText("Save") },
            onClick = {
                save()
                dismiss()
            },
        )
    },
) {
    StyledText("Actions")
}
```

All theme definition factories expose `popup`, `popupMenu` and `dropdownMenuItem`
renderer overrides. `popup` customizes generic overlays; Foundation-based
menus in Cupertino and Fluent 2 delegate through it, while Material 3 menus use
the `popupMenu` renderer and native `DropdownMenu`. Menu-item renderers ship fixed `StyledDropdownMenuItem.Defaults` (`minHeight`,
`itemPadding`, `itemShape`); change them via
`StyledTheme(customization = { update(StyledDropdownMenuItem.DefaultsKey) { copy(...) } })`;
individual items can
override these through `minHeight`, `itemPadding` and `shape`.
Single- and multi-select dropdowns compose `StyledDropdownMenuItem` directly,
inheriting the active renderer's styling and interaction behavior. Their
radio-button and checkbox selection semantics are preserved.
Foundation renderers contain no theme identity flags;
Cupertino and Fluent 2 supply their own menu-item interaction styling.
