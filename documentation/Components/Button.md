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
The `colors` argument is optional when using customization for the outline
border alone.
For caller-defined content that needs click feedback without a button's minimum
width, use [`StyledClickable`](Clickable.md).

Buttons support `leadingIcon` and `trailingIcon` slots using `IconComposable`.
The icons are rendered through `StyledIcon`, inherit the button's content color,
and use a compact button-specific icon size:

```kotlin
StyledButton(
    onClick = { save() },
    leadingIcon = Icons.Default.Save.toIconComposable(),
    trailingIcon = Icons.Default.ArrowForward.toIconComposable(),
) {
    StyledText("Save")
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
and optional outline border. Each theme registers fixed button defaults
(currently only `shape`) together with its renderer; update them through
`StyledTheme(customization)` or a scoped `UpdateStyledRegistry`.

Cupertino buttons retain `bodyMedium` typography and compact vertical padding.
Buttons have a minimum 44 dp footprint, increased when the platform's minimum
interactive size is larger, subject to caller constraints. Content padding is
inside this minimum size, not added on top of it. This also applies to dropdown
anchors and dialog/snackbar actions, which reuse the same button renderers.
