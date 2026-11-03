# Toolbar

`StyledToolbar` is a compact, theme-aware horizontal component for application
actions and menu triggers. It is rendered in Compose content, not as a native
operating-system toolbar, and can be used from shared Compose code as well as
desktop apps.

The toolbar deliberately does not define an action model. Compose existing
Styled controls in its `RowScope` content. Use `StyledToolbarMenuButton` for a
menu trigger that participates in the toolbar's hover behavior:

```kotlin
StyledToolbar {
    StyledToolbarMenuButton(
        menuContent = { dismiss ->
            StyledDropdownMenuItem(
                text = { StyledText("Export") },
                onClick = {
                    export()
                    dismiss()
                },
                leadingIcon = {
                    StyledIcon(icon = StyledIcons.Share, contentDescription = null)
                },
            )
        },
    ) {
        StyledText("File")
    }

    StyledButton(
        onClick = { save() },
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(StyledTheme.spacing.small)) {
            StyledIcon(icon = StyledIcons.Save, contentDescription = null)
            StyledText("Save")
        }
    }
}
```

`StyledToolbar` fills the available width, uses the active theme's surface and
content colors, and applies compact default padding and item spacing. Override
`containerColor`, `contentColor`, `contentPadding` or `itemSpacing` when needed.
Toolbar actions remain ordinary Styled controls, so theme renderer overrides
continue to apply. The toolbar provides `StyledToolbar.Context.Action`, so
buttons using `Role.Default` resolve to the theme's compact text-style button;
explicit roles remain authoritative. `StyledToolbarMenuButton` is a styled button with an
expand indicator; it opens after a brief hover delay, and hovering another
toolbar menu switches directly to that menu. Click and keyboard activation
remain available. Clicking a closed trigger opens it. Clicking the same trigger
during its opening animation has no effect; after the menu is fully shown,
clicking it closes the menu.
Toolbar popups are non-focusable by default so they do not capture focus and
block hover over other toolbar triggers. Keep `menuProperties.focusable` false
when customizing popup properties if sibling-menu switching is required.
Outside a `StyledToolbar`, it behaves as a click-to-toggle menu button.

For cascading menus, use `StyledPopupMenuSubmenu` inside a popup menu. It opens
beside its parent item and flips to the other side when there is not enough
horizontal room. Its `dismiss` callback closes the entire menu hierarchy, while
`dismissSubmenu` closes only that submenu and its descendants. Within a
toolbar menu, submenu items also open on hover and sibling submenus switch
without requiring the parent popup to close. A short close delay lets the
pointer cross the gap between an anchor and its popup. Hovering any descendant
popup keeps every ancestor menu active. Toolbar menus use compact item and menu
padding; ordinary dropdown menus keep their normal size and click-driven
behavior. Keyboard shortcut binding is not part of this API.
