# Icon buttons

`StyledIconButton()` is a separately registered control whose default
`Role.Default` is resolved by the active theme and surrounding context. Set
`role = StyledIconButton.Role.Primary`, `Secondary` or `Tertiary` only when you
intentionally want that emphasis; explicit roles retain their mapping even in
top bars, dialogs and snackbars. It supports `shape`, `interactionSource` and
`StyledIconButtonDefaults.customize(CustomColors, border)`, and is registered
by all three built-in themes. The content overload accepts an arbitrary
composable slot; the `IconComposable` overload applies the configured icon size
and optional padding.

| Theme | Primary | Secondary | Tertiary |
|-------|---------|-----------|----------|
| Material 3 | Filled | Outlined | Standard |
| Cupertino | Filled tonal | Gray | Standard |
| Fluent 2 | Filled | Neutral secondary | Standard |

Cupertino uses an accent-tinted Primary icon button, a neutral gray Secondary
icon button with accent-colored content, and an unfilled accent Tertiary.

Renderer `RoleMapping` callbacks receive `(role, context)`. For the default
role, Material 3, Cupertino and Fluent 2 use Standard icon buttons in modal,
snackbar, top-bar, input-action and clickable GroupBox contexts. Outside those
contexts, the defaults above are unchanged. Custom mappings can delegate with
`ThemeIconButton.DefaultRoleMapping(role, context)`. See [modal actions](Dialog.md)
for structured labels, automatic top-bar presentation and explicit icon actions.

Fluent 2 buttons and icon buttons share an internal color resolver for default,
hover, pressed and disabled states. Secondary uses a neutral surface and a
contrasting neutral foreground in both controls, rather than a Primary-tinted
icon surface with Primary-colored content. The icon renderer's existing
`FilledTonal` variant name is retained and now selects this neutral secondary
appearance. Both controls honor `filledBackground`/`filledForeground` overrides
for Primary and Secondary, and `textForeground` for transparent/outlined styles.

```kotlin
StyledIconButton(
    icon = Icons.Default.Add.toIconComposable(),
    contentDescription = "Add item",
    onClick = { addItem() },
)
```

Use `CustomColors` for role-aware customization: filled styles use
`filledBackground`/`filledForeground`, while standard or outlined styles use
`textForeground`; `border` optionally overrides the outline.
