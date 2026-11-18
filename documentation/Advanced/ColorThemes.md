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

Pass the theme colors, variant, appearance and contrast together in a
`StyledThemeConfig`; it resolves system appearance and contrast automatically.
`themeProvider` selects the component renderers independently:

```kotlin
import com.michaelflisar.composestyled.StyledThemeConfig
import com.michaelflisar.composestyled.themes.fluent2.Fluent2Themes

StyledTheme(
    platform = platform,
    themeProvider = rememberFluent2ThemeProvider(),
    themeConfig = StyledThemeConfig(
        colors = Fluent2Themes.blue,
        variant = StyledThemeVariant.Colorful,
        appearance = StyledThemeAppearance.System,
        contrast = StyledThemeContrast.System,
    ),
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

On Android 12 and newer, set `dynamicColors = true` in `StyledThemeConfig` to
use the system's dynamic accent colors as the base for Styled's semantic
palette. This works without a Material 3 dependency and keeps the selected
variant, appearance and contrast; when dynamic colors are disabled or
unsupported, the configured `colors` are used as usual.

`StyledThemeColorsPicker` accepts four separate `MutableState` parameters, or
four plain values with their corresponding `onThemeColorsChange`,
`onThemeVariantChange`, `onThemeAppearanceChange` and `onThemeContrastChange`
callbacks. Both overloads require an explicit `collections` list. Each UI action
updates only its corresponding value directly; `StyledThemeConfig` groups
settings only when passing them to `StyledTheme`.

```kotlin
import com.michaelflisar.composestyled.StyledThemeConfig
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
    themeConfig = StyledThemeConfig(
        colors = themeColors.value,
        variant = themeVariant.value,
        appearance = themeAppearance.value,
        contrast = themeContrast.value,
    ),
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

The standard surface role has five levels: `surfaceLowest`, `surfaceLow`,
`surface`, `surfaceHigh` and `surfaceHighest`. In Material 3 these map to the
five `surfaceContainer` roles, with `surface` as the middle level.
`surfaceVariant`, `primarySurface`, `secondarySurface` and `tertiarySurface`
remain separate semantic roles. `inverseSurface` and `inverseOnSurface` form a
paired role for content shown on an inverted surface.

`CustomColors` provides matching presets for the five surface levels,
`SurfaceVariant` and `InverseSurface`, as well as the semantic surface roles
`PrimarySurface`, `SecondarySurface`, `TertiarySurface`, `ErrorSurface`,
`SuccessSurface` and `WarningSurface`.

Use `StyledTheme.supportingContentColor(containerColor, contentColor)` to derive
a less prominent foreground for supporting text. `containerColor` is required;
`contentColor` defaults to `LocalStyledContentColor.current`. It blends the
content color toward the container and keeps at least a 4.5:1 contrast ratio
when the original pair allows it. Pass `minimumContrastRatio` to choose a
different floor. This is a color calculation;
`StyledTheme.contentColorFor(backgroundColor)` remains the theme-role lookup.
