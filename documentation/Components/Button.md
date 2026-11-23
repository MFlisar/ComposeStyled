# Buttons

`StyledButton()` defaults to `StyledButton.Variant.Auto`. In Material 3, Auto
uses the filled primary button outside context-sensitive slots, matching the
usual M3 default. Themes may choose another suitable appearance for navigation,
dialog, snackbar or other component contexts. Set an explicit variant when the
visual treatment should remain fixed across contexts:

| Variant | Meaning |
|---------|---------|
| `Auto` | Lets the active theme and surrounding context choose |
| `Filled` | Filled action |
| `Outlined` | Outlined action |
| `Plain` | Unfilled text/plain action |

`StyledOutlinedButton()` and `StyledTextButton()` are fixed-style convenience
composables; they do not accept a variant override. Use
`StyledButton(variant = StyledButton.Variant.Filled)` when a filled style must
remain explicit in every context. Use `StyledButtonDefaults.customize` and
`CustomColors` for color changes rather than primary/secondary role variants.
`disabledColors` applies a separate `CustomColors` mapping when `enabled` is
false; those tokens are used directly, while omitting the argument preserves
the active theme's disabled appearance. Filled styles use its filled background
and foreground, while plain and outlined styles use its text foreground. The
`colors` argument is optional when using customization for the outline border
alone. A customized border has a default thickness of `1.dp`; override it with
`borderThickness`:

```kotlin
StyledButtonDefaults.customize(
    border = StyledTheme.colorScheme.primary,
    borderThickness = 2.dp,
)
```
For caller-defined content that needs click feedback without a button's minimum
width, use [`StyledClickable`](Clickable.md).

Buttons support optional `leadingIcon` and `trailingIcon` slots using
`IconComposable`. By default, the leading and trailing icons sit next to the
content. Set `iconPosition = StyledButton.IconPosition.AtButtonEdges` to place
the leading icon at the start and the trailing icon at the end of the button.
`AdjacentToContent` keeps the icons with the content as a centered group.
`iconSpacing` defines the exact spacing between adjacent elements in that
group. Defaults match each renderer: Material 3 uses `8.dp`, while Cupertino
and Fluent 2 use `6.dp`.

The icons are rendered through `StyledIcon`, inherit the button's content color,
and use a compact button-specific icon size:

```kotlin
StyledButton(
    onClick = { save() },
    leadingIcon = Icons.Default.Save.toIconComposable(),
    trailingIcon = Icons.Default.ArrowForward.toIconComposable(),
    iconPosition = StyledButton.IconPosition.AdjacentToContent,
    iconSpacing = 4.dp,
) {
    StyledText("Save")
}
```

The positions and spacing can be changed globally through the button defaults:

```kotlin
StyledTheme(
customization = {
    update(StyledButton.DefaultsKey) {
        copy(
            iconPosition = StyledButton.IconPosition.AtButtonEdges,
            iconSpacing = 8.dp,
        )
    }
},
) {
// ...
}
```

For the common leading-icon layout, `StyledIconTextButton()` requires an icon
and forwards the rest of the interaction, variant, customization and content
handling to `StyledButton()`:

```kotlin
StyledIconTextButton(
    onClick = { save() },
    icon = Icons.Default.Save.toIconComposable(),
) {
    StyledText("Save")
}
```

An explicit `Plain` variant inherits the surrounding Styled content color.
`CustomColors.textForeground` takes precedence when provided.

Default Auto mappings preserve the style vocabulary while adapting to the
active design system. Explicit variants are never replaced by context:

| Theme | Auto outside a special context | Auto in navigation/action slots |
|-------|--------------------------------|---------------------------------|
| Material 3 | Filled primary | Text |
| Cupertino | Tinted | Plain |
| Fluent 2 | Primary | Transparent; dialog action slots retain their positive/negative/neutral hierarchy |

Clickable `StyledGroupBox` content maps Auto to Outlined. Other context sources,
such as side-pane and swipe-reveal actions, may also choose Plain/ Text styles.
The Auto mapping does not change the visual meaning of Filled, Outlined or
Plain.

Renderer factories accept a `VariantMapping` callback with `(variant, context)`
parameters. Delegate to `DefaultVariantMapping` for cases that should retain the
theme defaults:

```kotlin
val myMapping: Material3Button.VariantMapping = { variant, context ->
    if (variant == StyledButton.Variant.Auto && context == StyledContext.Default) {
        Material3Button.Variant.Outlined
    } else {
        Material3Button.DefaultVariantMapping(variant, context)
    }
}

StyledTheme(
    customization = {
        register(Material3Button.create(variantMapping = myMapping))
    },
) {
    // ...
}
```

Use `CustomColors` to customize filled foreground/background, plain foreground
and optional outline border. Each theme registers its button defaults together
with its renderer; update them through `StyledTheme(customization)` or a scoped
`UpdateStyledRegistry`.

Cupertino buttons retain `bodyMedium` typography and compact vertical padding.
Buttons have a minimum 44 dp footprint, increased when the platform's minimum
interactive size is larger, subject to caller constraints. Content padding is
inside this minimum size, not added on top of it. This also applies to dropdown
anchors and dialog/snackbar actions, which reuse the same button renderers.
