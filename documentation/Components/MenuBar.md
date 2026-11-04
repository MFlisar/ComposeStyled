# Menu Bar

`StyledMenuBar` is a compact, theme-aware horizontal menu bar for application
commands such as File and Edit. It is available from shared Compose code and is
primarily intended for desktop interfaces.

Use `StyledMenuBarItem` for each top-level menu. Its content is the menu label,
and its `menuContent` slot defines that menu's entries:

```kotlin
StyledMenuBar {
    StyledMenuBarItem(
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
        StyledText("File")
    }

    StyledMenuBarItem(
        icon = StyledIcons.Edit,
        menuContent = { dismiss ->
            StyledDropdownMenuItem(
                text = { StyledText("Undo") },
                onClick = {
                    undo()
                    dismiss()
                },
            )
        },
    ) {
        StyledText("Edit")
    }
}
```

`StyledMenuBar` fills the available width, uses the active theme's surface and
content colors, and applies compact default padding and item spacing. Override
`containerColor`, `contentColor`, `contentPadding` or `itemSpacing` when needed.
Menu-bar items wrap their content with theme-appropriate padding and do not have
a fixed minimum width.
Menu-bar items use `StyledButton` internally, so theme renderer overrides
continue to apply. The bar provides `StyledMenuBar.Context.MenuItem`; buttons
using `Role.Default` receive the theme's menu-bar treatment, while explicit
roles remain authoritative. `StyledIcon` uses a compact size capped at 16 dp in
this context; an explicit `size` still takes precedence. `StyledMenuBarItem`'s
optional `icon` uses that same size and spacing, and takes up space only when
provided. The expand indicator uses the same compact size.

Menu-bar popups open after a brief hover delay, and hovering another top-level
menu switches directly to it. Click and keyboard activation remain available.
Clicking a closed trigger opens it. Clicking the same trigger during its
opening animation has no effect; after the menu is fully shown, clicking it
closes the menu. Their content is measured before the entrance begins, then
unfolds vertically from the top without fading. Customize the animation with
`menuAnimation` on `StyledMenuBarItem` or `animation` on
`StyledPopupMenuSubmenu`. Their width
defaults to the measured content width,
capped at 260 dp; set `menuWidthConfig` when a menu needs a different width. Popups have
vertical content padding matching the menu items' horizontal content padding,
and menu-item minimum height is capped at 36 dp for a compact menu-bar
presentation. Items without a leading icon still reserve its slot to keep their
labels aligned. Popups are
non-focusable by default so they do not capture focus and block hover over
sibling menus. Keep `menuProperties.focusable` false when customizing popup
properties if sibling-menu switching is required.

Outside a `StyledMenuBar`, `StyledMenuBarItem` behaves as a click-to-toggle menu
button.

For cascading menus, use `StyledPopupMenuSubmenu` inside a popup menu. It opens
beside its parent item and flips to the other side when there is not enough
horizontal room. Its `dismiss` callback closes the entire menu hierarchy, while
`dismissSubmenu` closes only that submenu and its descendants. Within a
menu-bar popup, submenu items also open on hover and sibling submenus switch
without requiring the parent popup to close. A short close delay lets the
pointer cross the gap between an anchor and its popup. Hovering any descendant
popup keeps every ancestor menu active. Ordinary dropdown menus keep their
normal size and click-driven behavior. Keyboard shortcut binding is not part
of this API.
