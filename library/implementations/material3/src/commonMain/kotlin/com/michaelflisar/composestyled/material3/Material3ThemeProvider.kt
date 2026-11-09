package com.michaelflisar.composestyled.material3

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.luminance
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.classes.StyledThemeDefinition
import com.michaelflisar.composestyled.classes.StyledThemeProvider
import com.michaelflisar.composestyled.material3.components.Material3Badge
import com.michaelflisar.composestyled.material3.components.Material3BottomNavigationBar
import com.michaelflisar.composestyled.material3.components.Material3Button
import com.michaelflisar.composestyled.material3.components.Material3Card
import com.michaelflisar.composestyled.material3.components.Material3Checkbox
import com.michaelflisar.composestyled.material3.components.Material3Chip
import com.michaelflisar.composestyled.material3.components.Material3Clickable
import com.michaelflisar.composestyled.material3.layout.Material3Dialog
import com.michaelflisar.composestyled.material3.layout.Material3BottomSheet
import com.michaelflisar.composestyled.material3.components.Material3DropdownMenu
import com.michaelflisar.composestyled.material3.components.Material3FloatingActionButton
import com.michaelflisar.composestyled.material3.components.Material3Icon
import com.michaelflisar.composestyled.material3.components.Material3IconButton
import com.michaelflisar.composestyled.material3.components.Material3Input
import com.michaelflisar.composestyled.material3.components.Material3ListItem
import com.michaelflisar.composestyled.material3.components.Material3SideNavigation
import com.michaelflisar.composestyled.material3.components.Material3Popup
import com.michaelflisar.composestyled.material3.components.Material3PopupMenu
import com.michaelflisar.composestyled.material3.components.Material3ProgressIndicator
import com.michaelflisar.composestyled.material3.components.Material3RangeSlider
import com.michaelflisar.composestyled.material3.components.Material3RadioButton
import com.michaelflisar.composestyled.material3.layout.Material3Scaffold
import com.michaelflisar.composestyled.material3.components.Material3SegmentedButton
import com.michaelflisar.composestyled.material3.components.Material3SegmentedButtonRow
import com.michaelflisar.composestyled.material3.components.Material3Snackbar
import com.michaelflisar.composestyled.material3.components.Material3SnackbarHost
import com.michaelflisar.composestyled.material3.components.Material3Surface
import com.michaelflisar.composestyled.material3.components.Material3Switch
import com.michaelflisar.composestyled.material3.components.Material3Slider
import com.michaelflisar.composestyled.material3.components.Material3Tab
import com.michaelflisar.composestyled.material3.components.Material3TabRow
import com.michaelflisar.composestyled.material3.components.Material3Tooltip
import com.michaelflisar.composestyled.material3.components.Material3TopBar
import com.michaelflisar.composestyled.registry.StyledRegistry
import com.michaelflisar.composestyled.ui.icons.material3.Material3FilledIconSet
import com.michaelflisar.composestyled.themes.material3.Material3Themes
import com.michaelflisar.composestyled.themes.StyledThemeVariant
import com.michaelflisar.composestyled.themes.StyledContrastLevel
import com.michaelflisar.composestyled.tokens.StyledColorScheme
import com.michaelflisar.composestyled.tokens.StyledColorScheme.Definition
import com.michaelflisar.composestyled.tokens.StyledShapes
import com.michaelflisar.composestyled.tokens.StyledTypography

@Composable
fun rememberMaterial3ThemeProvider(
    customizeColorScheme: ((ColorScheme) -> ColorScheme)? = null,
): Material3ThemeProvider = remember(customizeColorScheme) {
    createMaterial3ThemeProvider(customizeColorScheme)
}

fun createMaterial3ThemeProvider(
    customizeColorScheme: ((ColorScheme) -> ColorScheme)? = null,
): Material3ThemeProvider = Material3ThemeProvider(customizeColorScheme)

class Material3ThemeProvider internal constructor(
    private val customizeColorScheme: ((ColorScheme) -> ColorScheme)? = null,
) : StyledThemeProvider {
    override val id = "material3"
    override val defaultThemeColors = Material3Themes.blue
    override val defaultIconSet = Material3FilledIconSet

    @Composable
    override fun Root(content: @Composable () -> Unit) {
        val styledColors = StyledTheme.colorScheme
        val styledTypography = StyledTheme.typography
        val styledShapes = StyledTheme.shapes
        val colorScheme = remember(styledColors, customizeColorScheme) {
            val mapped = styledColors.toMaterial3ColorScheme()
            customizeColorScheme?.invoke(mapped) ?: mapped
        }
        val shapes = remember(styledShapes) { styledShapes.toMaterial3Shapes() }
        val typography = remember(styledTypography) { styledTypography.toMaterial3Typography() }
        MaterialTheme(
            shapes = shapes,
            typography = typography,
            colorScheme = colorScheme,
            content = content,
        )
    }

    @Composable
    override fun definition(): StyledThemeDefinition = material3ThemeDefinition()
}

/**
 * Material 3 implementation
 *
 * "1:1" M3 components - as good as possible, but not all M3 features are supported
 * Internally rendering is done with M3 components, but the API is StyledComponents API
 *
 * Target: Mobile devices (Android)
 *
 * <b>ATTENTION</b>: M3 compenents are not designed to be desktop devices, their target are mobile / touch devices.
 * Therefore the M3 implementation does not fully support [com.michaelflisar.composestyled.tokens.StyledSizes], [com.michaelflisar.composestyled.tokens.StyledSpacing] and [com.michaelflisar.composestyled.tokens.StyledPadding] and will ignore them in most cases.
 * BUT the default values of [StyledSizes], [StyledSpacing] and [StyledPadding] are set to the M3 defaults,
 * so that all components will look like M3 components
 *
 * Colors are mapped from StyledTheme, including derived surface and inverse roles.
 * [Material3ThemeProvider] applies the native root before this factory runs.
 */
@Composable
fun material3ThemeDefinition(): StyledThemeDefinition {
    val registry = StyledRegistry.createComposable {
        registerMaterial3Components()
    }
    return remember(registry) {
        StyledThemeDefinition(name = "Material3", registry = registry)
    }
}

@Composable
fun StyledRegistry.Builder.registerMaterial3Components() {
    register(Material3Badge.create())
    register(Material3Button.create())
    register(Material3Clickable.create())
    register(Material3Dialog.create())
    register(Material3BottomSheet.create())
    register(Material3Chip.create())
    register(Material3IconButton.create())
    register(Material3Popup.create())
    register(Material3PopupMenu.create())
    register(Material3Tooltip.create())
    register(Material3DropdownMenu.create())
    register(Material3Card.create())
    register(Material3ListItem.create())
    register(Material3Icon.create())
    register(Material3Scaffold.create())
    register(Material3TopBar.create())
    register(Material3BottomNavigationBar.create())
    register(Material3Snackbar.create())
    register(Material3SnackbarHost.create())
    register(Material3FloatingActionButton.create())
    register(Material3Surface.create())
    register(Material3SideNavigation.create())
    register(Material3Checkbox.create())
    register(Material3RadioButton.create())
    register(Material3Switch.create())
    register(Material3Input.create())
    register(Material3ProgressIndicator.create())
    register(Material3Slider.create())
    register(Material3RangeSlider.create())
    register(Material3TabRow.create())
    register(Material3Tab.create())
    register(Material3SegmentedButton.create())
    register(Material3SegmentedButtonRow.create())
}

private fun StyledShapes.toMaterial3Shapes() = Shapes(
    extraSmall = extraSmall,
    small = small,
    medium = medium,
    large = large,
    extraLarge = extraLarge,
)

private fun StyledTypography.toMaterial3Typography() = Typography(
    displayLarge = headlineLarge,
    displayMedium = headlineMedium,
    displaySmall = headlineSmall,
    headlineLarge = headlineLarge,
    headlineMedium = headlineMedium,
    headlineSmall = headlineSmall,
    titleLarge = titleLarge,
    titleMedium = titleMedium,
    titleSmall = titleSmall,
    bodyLarge = bodyLarge,
    bodyMedium = bodyMedium,
    bodySmall = bodySmall,
    labelLarge = labelLarge,
    labelMedium = labelMedium,
    labelSmall = labelSmall,
)

/**
 * Existing styled pairs are preserved; tertiary uses secondary, not a status color.
 * Missing roles approximate Material tonal targets using luminance, not HCT.
 * Independently supplied light/dark accents can yield different fixed colors.
 */
private fun StyledColorScheme.toMaterial3ColorScheme(): ColorScheme {
    val defaults = if (isDarkTheme) darkColorScheme() else lightColorScheme()
    return defaults.copy(
        primary = primary,
        onPrimary = onPrimary,
        primaryContainer = primarySurface,
        onPrimaryContainer = onPrimarySurface,
        inversePrimary = primary.atTone(if (isDarkTheme) 40f else 80f),
        secondary = secondary,
        onSecondary = onSecondary,
        secondaryContainer = secondarySurface,
        onSecondaryContainer = onSecondarySurface,
        tertiary = secondary,
        onTertiary = onSecondary,
        tertiaryContainer = secondarySurface,
        onTertiaryContainer = onSecondarySurface,
        background = background,
        onBackground = onBackground,
        surface = surface,
        onSurface = onSurface,
        surfaceVariant = surfaceVariant,
        onSurfaceVariant = onSurfaceVariant,
        surfaceTint = primary,
        inverseSurface = surface.atTone(if (isDarkTheme) 90f else 20f),
        inverseOnSurface = surface.atTone(if (isDarkTheme) 20f else 95f),
        error = error,
        onError = onError,
        errorContainer = errorSurface,
        onErrorContainer = onErrorSurface,
        outline = outline,
        outlineVariant = outlineVariant,
        scrim = Color.Black,
        surfaceBright = surface.atTone(if (isDarkTheme) 24f else 98f),
        surfaceDim = surface.atTone(if (isDarkTheme) 6f else 87f),
        surfaceContainerLowest = surface.atTone(if (isDarkTheme) 4f else 100f),
        surfaceContainerLow = surface.atTone(if (isDarkTheme) 10f else 96f),
        surfaceContainer = surface.atTone(if (isDarkTheme) 12f else 94f),
        surfaceContainerHigh = surface.atTone(if (isDarkTheme) 17f else 92f),
        surfaceContainerHighest = surface.atTone(if (isDarkTheme) 22f else 90f),
        primaryFixed = primary.atTone(90f),
        primaryFixedDim = primary.atTone(80f),
        onPrimaryFixed = primary.atTone(10f),
        onPrimaryFixedVariant = primary.atTone(30f),
        secondaryFixed = secondary.atTone(90f),
        secondaryFixedDim = secondary.atTone(80f),
        onSecondaryFixed = secondary.atTone(10f),
        onSecondaryFixedVariant = secondary.atTone(30f),
        tertiaryFixed = secondary.atTone(90f),
        tertiaryFixedDim = secondary.atTone(80f),
        onTertiaryFixed = secondary.atTone(10f),
        onTertiaryFixedVariant = secondary.atTone(30f),
    )
}

private fun Color.atTone(tone: Float): Color {
    // CIE L* -> relative luminance; interpolation does not preserve HCT chroma.

    val f = (tone + 16f) / 116f
    val target = if (tone > 8f) f * f * f else tone / 903.2963f
    val opaque = copy(alpha = 1f)
    val current = opaque.luminance()
    val endpoint = if (target > current) Color.White else Color.Black
    var low = 0f
    var high = 1f
    repeat(24) {
        val fraction = (low + high) / 2f
        val luminance = lerp(opaque, endpoint, fraction).luminance()
        if ((target > current && luminance < target) ||
            (target <= current && luminance > target)
        ) {
            low = fraction
        } else {
            high = fraction
        }
    }
    return lerp(opaque, endpoint, (low + high) / 2f)
}

fun ColorScheme.toStyledColorScheme(
    isDarkTheme: Boolean,
): StyledColorScheme {
    // just needed for success and warning colors and disableFactors, which are not part of M3 color scheme
    val baseColorScheme = Material3Themes.blue.resolve(
        variant = StyledThemeVariant.Colorful,
        isDark = isDarkTheme,
        contrast = StyledContrastLevel.Normal,
    )

    return StyledColorScheme(
        isDarkTheme = isDarkTheme,
        backgroundDef = Definition(background, onBackground),
        surfaceDef = Definition(surface, onSurface),
        surfaceVariantDef = Definition(surfaceVariant, onSurfaceVariant),
        primaryDef = Definition(primary, onPrimary),
        primarySurfaceDef = Definition(primaryContainer, onPrimaryContainer),
        secondaryDef = Definition(secondary, onSecondary),
        secondarySurfaceDef = Definition(secondaryContainer, onSecondaryContainer),
        tertiaryDef = Definition(tertiary, onTertiary),
        tertiarySurfaceDef = Definition(tertiaryContainer, onTertiaryContainer),
        outline = outline,
        outlineVariant = outlineVariant,
        errorDef = Definition(error, onError),
        errorSurfaceDef = Definition(errorContainer, onErrorContainer),
        successDef = Definition(baseColorScheme.success, baseColorScheme.onSuccess),
        successSurfaceDef = Definition(
            baseColorScheme.successSurface,
            baseColorScheme.onSuccessSurface
        ),
        warningDef = Definition(baseColorScheme.warning, baseColorScheme.onWarning),
        warningSurfaceDef = Definition(
            baseColorScheme.warningSurface,
            baseColorScheme.onWarningSurface
        ),
        disableFactors = baseColorScheme.disableFactors,
    )
}

fun Typography.toStyledTypography(): StyledTypography = StyledTypography(
    headlineLarge = headlineLarge,
    headlineMedium = headlineMedium,
    headlineSmall = headlineSmall,
    titleLarge = titleLarge,
    titleMedium = titleMedium,
    titleSmall = titleSmall,
    bodyLarge = bodyLarge,
    bodyMedium = bodyMedium,
    bodySmall = bodySmall,
    labelLarge = labelLarge,
    labelMedium = labelMedium,
    labelSmall = labelSmall,
)

fun Shapes.toStyledShapes(): StyledShapes = StyledShapes(
    extraSmall = extraSmall,
    small = small,
    medium = medium,
    large = large,
    extraLarge = extraLarge,
)