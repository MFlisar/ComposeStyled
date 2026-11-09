# Menu Bar

`StyledMenuBar` is a compact, theme-aware horizontal menu bar for application
commands such as File and Edit. Its content receiver is a
`StyledMenuBarScope`: use `menu` for a popup trigger, `item` for an immediate
command, `divider` for a separator, and `custom` for arbitrary row content.

```kotlin
StyledMenuBar {
    menu("File") {
        item("Save", onClick = {
            save()
            dismiss()
        })
        divider()
        item("Close", onClick = { closeDocument() })
    }

    menu("Edit", icon = StyledIcons.Edit) {
        item("Undo", onClick = {
            undo()
            dismiss()
        })
    }

    item("Help", onClick = { showHelp() })
}
```

Menu content uses `StyledPopupMenuScope`, so popup menus and menu-bar menus
share the same `item`, `header`, `divider`, `checkbox`, `submenu` and `custom`
operations. Call `dismiss()` after a command to close the complete menu
hierarchy; simple items close automatically by default. Set `closeOnClick =
false` for an action that should leave the menu open. The `custom` operation
accepts `RowScope` content and can also make the row clickable. Use
`StyledMenuBarScope.custom` for arbitrary content in the horizontal bar itself.

`StyledMenuBar` fills the available width, uses the active theme's surface and
content colors, and applies compact default padding and item spacing. Override
`containerColor`, `contentColor`, `contentPadding` or `itemSpacing` when needed.
Menu-bar entries use `StyledButton` internally, so theme renderer overrides
continue to apply. Buttons using `Role.Default` receive the menu-bar treatment;
explicit roles remain authoritative. `StyledIcon` uses a compact size capped
at 16 dp in this context, and an explicit size takes precedence.

Menu-bar popups open after a brief hover delay, and hovering another top-level
menu switches directly to it. Click and keyboard activation remain available.
Clicking a closed trigger opens it; clicking it during its opening animation
has no effect, and clicking after the menu is fully shown closes it. Popups are
measured before entrance and unfold vertically from the top without fading.
Their width defaults to the measured content width, capped at 260 dp. Menu
items without a leading icon still reserve its slot to keep labels aligned.
Popups are non-focusable by default so they do not capture focus and block
hover over sibling menus.

For cascading menus, use `submenu` inside a popup scope. It opens beside its
parent item and flips to the other side when there is not enough horizontal
room. `dismiss()` closes the full hierarchy, while `dismissSubmenu()` closes
only that submenu and its descendants. Within a menu-bar popup, submenu items
also open on hover and sibling submenus switch without closing the parent.
Ordinary popup menus remain click-driven. Keyboard shortcut binding is not
part of this API.
