# Icon buttons

`StyledIconButton()` shares the button style vocabulary through
`StyledButton.Variant`: `Auto`, `Filled`, `Outlined` and `Plain`. Auto delegates
to the active theme and surrounding context; explicit variants keep their
appearance in every context. `StyledIconFilledButton()` and
`StyledIconOutlinedButton()` are fixed-style convenience composables and do
not accept a variant override.

| Theme | Auto outside special contexts | Auto in navigation/action slots |
|-------|-------------------------------|---------------------------------|
| Material 3 | Standard | Standard |
| Cupertino | Filled tonal | Standard |
| Fluent 2 | Filled | Standard |

It supports `shape`, `interactionSource` and
`StyledIconButtonDefaults.customize(CustomColors, border)`. The `colors`
argument is optional when only overriding the outline border. The content overload
accepts an arbitrary composable slot; the `IconComposable` overload applies the
configured icon size and optional padding:

```kotlin
StyledIconButton(
    icon = Icons.Default.Add.toIconComposable(),
    contentDescription = "Add item",
    onClick = { addItem() },
)
```

Renderer `VariantMapping` callbacks receive `(variant, context)`. Delegate to
`ThemeIconButton.DefaultVariantMapping(variant, context)` for the theme's
default mapping. Icon-button variants map to platform-native appearances:
Material 3 uses Filled, Outlined and Standard; Cupertino uses Filled,
Outlined and Standard (its Auto style is filled tonal); Fluent 2 uses Filled,
Outlined and Standard. In all themes, Plain uses the unfilled standard
appearance.

For `Plain`, the foreground inherits the surrounding Styled content color,
including the `contentColor` provided by a `StyledSurface`. A configured
`CustomColors.textForeground` takes precedence.

Use `CustomColors` for filled background/foreground or standard foreground;
`border` optionally overrides the outline. Fluent 2 buttons and icon buttons
share an internal color resolver for default, hover, pressed and disabled
states.
