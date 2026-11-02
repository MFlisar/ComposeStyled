# Theme colors

`StyledColorScheme` is the concrete light or dark set of colors used by
`StyledTheme`. `StyledThemeColors` contains monochrome and colorful variants,
each with light and dark schemes at all three contrast levels. Shared types
remain in `com.michaelflisar.composestyled.themes`; predefined palettes live in
five independent modules with their own subpackages:

| Artifact (`io.github.mflisar.composestyled`) | Package | Presets |
|---|---|---|
| `theme-cupertino` | `com.michaelflisar.composestyled.themes.cupertino` | `CupertinoThemes` |
| `theme-fluent2` | `com.michaelflisar.composestyled.themes.fluent2` | `Fluent2Themes` |
| `theme-material3` | `com.michaelflisar.composestyled.themes.material3` | `Material3Themes` |
| `theme-pastel` | `com.michaelflisar.composestyled.themes.pastel` | `PastelThemes` |
| `theme-nature` | `com.michaelflisar.composestyled.themes.nature` | `NatureThemes` |

Each implementation exports its matching color-theme module transitively.
Add other color-theme artifacts explicitly to use them with any implementation.
Core contains no presets. A theme within a collection identifies a palette,
such as Blue.

Pass the four explicit values `themeColors`, `themeVariant`, `themeAppearance`
and `themeContrast` to `StyledTheme`; it resolves system appearance and contrast
automatically. `themeProvider` selects the component renderers independently:

```kotlin
import com.michaelflisar.composestyled.themes.fluent2.Fluent2Themes

StyledTheme(
    platform = platform,
    themeProvider = rememberFluent2ThemeProvider(),
    themeColors = Fluent2Themes.blue,
    themeVariant = StyledThemeVariant.Colorful,
    themeAppearance = StyledThemeAppearance.System,
    themeContrast = StyledThemeContrast.System,
) {
    content()
}
```

Applications assemble collections explicitly from the theme modules they use;
there is no global color-theme catalog. Each preset object exposes a
`collection`, such as `Fluent2Themes.collection`. The library's preset names and
IDs are code-facing values; applications should provide localized labels and
persist stable IDs as needed.

`themeProvider.defaultThemeColors` supplies the default colors used by
`StyledTheme`: the matching Blue preset for each built-in provider.

`StyledThemeColorsPicker` accepts four `MutableState` parameters with the same
names, or four plain values with their corresponding `onThemeColorsChange`,
`onThemeVariantChange`, `onThemeAppearanceChange` and `onThemeContrastChange`
callbacks. Both overloads require an explicit `collections` list. Each UI action
updates only its corresponding value directly; there is no combined selection
object or deferred state synchronization.

```kotlin
import com.michaelflisar.composestyled.themes.fluent2.Fluent2Themes
import com.michaelflisar.composestyled.themes.pastel.PastelThemes

// Requires theme-pastel in addition to the Fluent 2 implementation.
val collections = listOf(Fluent2Themes.collection, PastelThemes.collection)
val themeProvider = rememberFluent2ThemeProvider()
val themeColors = remember { mutableStateOf(themeProvider.defaultThemeColors) }
val themeVariant = remember { mutableStateOf(StyledThemeVariant.Colorful) }
val themeAppearance = remember { mutableStateOf(StyledThemeAppearance.System) }
val themeContrast = remember { mutableStateOf(StyledThemeContrast.System) }

StyledTheme(
    platform = platform,
    themeProvider = themeProvider,
    themeColors = themeColors.value,
    themeVariant = themeVariant.value,
    themeAppearance = themeAppearance.value,
    themeContrast = themeContrast.value,
) {
    StyledThemeColorsPicker(
        themeColors = themeColors,
        themeVariant = themeVariant,
        themeAppearance = themeAppearance,
        themeContrast = themeContrast,
        collections = collections,
    )
}
```

Collections must be non-empty, each must contain theme colors, and the current
`themeColors` must be included in the provided collections; invalid input fails
explicitly. Choosing a collection selects its first color preset. Presets are
matched by their full value, not just their palette ID, because different
collections may share IDs such as `"blue"`. To persist a preset, store both its
collection ID and palette ID, along with the three enum choices.
Swatches use a border in the current content color to remain visible on filled
anchors.

Android's system contrast is observed live on API 34+;
its 0..1 preference is divided into Normal, Medium, and High thirds. Other
platforms currently resolve system contrast to Normal.

The former `StyledColorThemeSelection` and
`PlatformDefaults.colorThemeSelection()` APIs have been removed. The former
`StyledColorTheme`, `StyledColorThemeStyle`, `StyledColorThemeMode` and
`StyledColorThemeContrast` types are now `StyledThemeColors`, `StyledThemeVariant`,
`StyledThemeAppearance` and `StyledThemeContrast`. `StyledColorThemePicker` is now
`StyledThemeColorsPicker`. Provider factories are named
`rememberMaterial3ThemeProvider()`, `rememberCupertinoFlatThemeProvider()`,
`rememberCupertinoLiquidThemeProvider()` and `rememberFluent2ThemeProvider()`.

Each built-in palette includes semantic colors as well as brand and surface
colors. Its content colors are selected to meet the configured contrast
target: 4.5:1 for normal, 5.5:1 for medium, and 7:1 for high. When an accent
fill does not meet the selected target, the resolver shifts it toward black or
white only as much as required. Applications should still check their complete
UI and custom renderer colors, which may introduce additional color
combinations.
