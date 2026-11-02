# Themes and registry

Pass a `StyledThemeProvider` to `StyledTheme`. Styled token locals are provided
first, followed by the provider's native root. The provider then calls
`*ThemeDefinition()` inside that root, allowing renderer defaults to read both
Styled and native theme locals. Each provider also supplies
`defaultThemeColors`, used by `StyledTheme` when `themeColors` is omitted and
available to apps as their initial colors. Finally,
registry and theme name are provided to the app content.

```kotlin
StyledTheme(
    platform = platform,
    themeProvider = rememberFluent2ThemeProvider(),
) {
    StyledText("Hello")
}
```

For custom components, use `customization` of `StyledTheme`. It runs after the
theme's default registrations, so it can add components or replace a default one:

```kotlin
StyledTheme(
    platform = platform,
    themeProvider = rememberMaterial3ThemeProvider(
        customizeColorScheme = { it.copy(primary = Color.Red) },
    ),
    customization = {
        register(Material3Card.create())
    },
) { /* ... */ }
```

`remember*ThemeProvider()` remembers the provider using only its own configuration as
keys. The theme's `register*Components()` helper always registers the complete
default set; the theme factories take no registration callbacks.

Registered components implement `StyledComponent<T>` and provide their
`StyledKey<T>` through `key`. Register the component instance directly with
`register(component)`; the same API is used for custom components and theme
renderers. `register` is `@Composable` because a component's defaults are resolved
when it is registered.

## Component defaults

Components with adjustable defaults (for example `StyledButton`) additionally
implement `StyledComponentWithDefaults<D>`. Each implementation ships one fixed
default value, and `register(component)` stores it in the registry under the
component's `DefaultsKey` (e.g. `StyledButton.DefaultsKey`). Defaults are not
configurable through the renderer's `create(...)`; change them in the same
`customization` block with `update`, based on the active value:

```kotlin
StyledTheme(
    platform = platform,
    themeProvider = themeProvider,
    customization = {
        register(MyButton.create())                                   // optional: replace the renderer
        update(StyledButton.DefaultsKey) { copy(shape = RectangleShape) }
    },
) { /* ... */ }
```

Order matters inside the block: `register(...)` stores the renderer's own fixed
defaults again, so call `update(...)` after registering a replacement. Scoped
changes can use `UpdateStyledRegistry` with the same `register` / `update` calls.

All themes register `StyledTopBar`, `StyledBottomNavigationBar`, `StyledSideNavigation`,
`StyledSnackbar`, `StyledFloatingActionButton`, `StyledSurface`, `StyledCheckbox`,
`StyledRadioButton`, `StyledSwitch`, `StyledInput` and `StyledProgressIndicator`. To replace one of
these implementations while keeping the other defaults, register the replacement
in `customization`; no additional `remember` is needed in the registration
lambda.
