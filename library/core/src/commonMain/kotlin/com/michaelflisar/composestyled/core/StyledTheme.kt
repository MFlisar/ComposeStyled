package com.michaelflisar.composestyled.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.composables.compose.ripple.rememberRippleIndication
import com.composeunstyled.platformtheme.bright
import com.composeunstyled.platformtheme.buildPlatformTheme
import com.composeunstyled.platformtheme.dimmed
import com.composeunstyled.platformtheme.indications
import com.composeunstyled.platformtheme.interactiveSizes
import com.composeunstyled.platformtheme.sizeDefault
import com.composeunstyled.platformtheme.sizeMinimum
import com.composeunstyled.theme.Theme
import com.michaelflisar.composestyled.core.classes.StyledThemeDefinition
import com.michaelflisar.composestyled.core.renderer.ExtensionRenderer
import com.michaelflisar.composestyled.core.renderer.LocalStyledComponents
import com.michaelflisar.composestyled.core.renderer.LocalStyledExtensions
import com.michaelflisar.composestyled.core.renderer.StyledComponents
import com.michaelflisar.composestyled.core.renderer.StyledExtensionsRegistry
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.core.runtime.LocalStyledBackgroundColor
import com.michaelflisar.composestyled.core.runtime.LocalStyledContentColor
import com.michaelflisar.composestyled.core.runtime.LocalStyledTextStyle
import com.michaelflisar.composestyled.core.tokens.LocalStyledColors
import com.michaelflisar.composestyled.core.tokens.LocalStyledElevations
import com.michaelflisar.composestyled.core.tokens.LocalStyledIndications
import com.michaelflisar.composestyled.core.tokens.LocalStyledPaddings
import com.michaelflisar.composestyled.core.tokens.LocalStyledShapes
import com.michaelflisar.composestyled.core.tokens.LocalStyledSizes
import com.michaelflisar.composestyled.core.tokens.LocalStyledSpacings
import com.michaelflisar.composestyled.core.tokens.LocalStyledTypography
import com.michaelflisar.composestyled.core.tokens.StyledColors
import com.michaelflisar.composestyled.core.tokens.StyledElevations
import com.michaelflisar.composestyled.core.tokens.StyledIndications
import com.michaelflisar.composestyled.core.tokens.StyledPaddings
import com.michaelflisar.composestyled.core.tokens.StyledShapes
import com.michaelflisar.composestyled.core.tokens.StyledSizes
import com.michaelflisar.composestyled.core.tokens.StyledSpacings
import com.michaelflisar.composestyled.core.tokens.StyledTypography

object StyledTheme {

    enum class ContentColorFallback {
        BlackWhite,
        LocalContentColor
    }

    // ----------------------
    // theme properties (locals that typically to no change locally)
    // ----------------------

    val colors: StyledColors
        @Composable @ReadOnlyComposable get() = LocalStyledColors.current

    val shapes: StyledShapes
        @Composable @ReadOnlyComposable get() = LocalStyledShapes.current

    val elevations: StyledElevations
        @Composable @ReadOnlyComposable get() = LocalStyledElevations.current

    val typography: StyledTypography
        @Composable @ReadOnlyComposable get() = LocalStyledTypography.current

    val paddings: StyledPaddings
        @Composable @ReadOnlyComposable get() = LocalStyledPaddings.current

    val spacings: StyledSpacings
        @Composable @ReadOnlyComposable get() = LocalStyledSpacings.current

    val sizes: StyledSizes
        @Composable @ReadOnlyComposable get() = LocalStyledSizes.current

    val indications: StyledIndications
        @Composable @ReadOnlyComposable get() = LocalStyledIndications.current

    // ----------------------
    // theme properties (locals, often times overridden locally inside components)
    // ----------------------

    val currentBackgroundColor: Color
        @Composable @ReadOnlyComposable get() = LocalStyledBackgroundColor.current

    val currentContentColor: Color
        @Composable @ReadOnlyComposable get() = LocalStyledContentColor.current

    val currentTextStyle: androidx.compose.ui.text.TextStyle
        @Composable @ReadOnlyComposable get() = LocalStyledTextStyle.current

    // ----------------------
    // functions
    // ----------------------

    @Composable
    @ReadOnlyComposable
    fun contentColorFor(
        backgroundColor: Color,
        checkInversely: Boolean = true,
        fallback: ContentColorFallback = ContentColorFallback.LocalContentColor,
    ): Color {
        val def = listOf(
            colors.backgroundDef,
            colors.surfaceDef,
            colors.surfaceVariantDef,
            colors.primaryDef,
            colors.secondaryDef,
            colors.outlineDef,
            colors.outlineVariantDef,
            colors.errorDef,
            colors.successDef,
        )
        // first - check all definitions
        // if backgroundColor is a "color", return the corresponding "onColor"
        for (d in def) {
            if (backgroundColor == d.color) {
                return d.onColor
            }
        }
        // second - check inversely
        // if backgroundColor is an "onColor", return the corresponding "color"
        if (checkInversely) {
            for (d in def) {
                if (backgroundColor == d.onColor) {
                    return d.color
                }
            }
        }
        // fallback
        return when (fallback) {
            ContentColorFallback.BlackWhite -> if (backgroundColor.isDark) {
                Color.White
            } else {
                Color.Black
            }

            ContentColorFallback.LocalContentColor -> LocalStyledContentColor.current
        }
    }
}

/**
 * the main entry point to apply a StyledTheme to your app
 */
@OptIn(InternalComposeStyledApi::class)
@Composable
fun StyledTheme(
    theme: StyledThemeDefinition<*>,
    colors: StyledColors,
    renderers: List<ExtensionRenderer<*>> = emptyList(),
    content: @Composable () -> Unit,
) {
    StyledTheme(
        components = theme.components,
        colors = colors,
        shapes = theme.shapes,
        typography = theme.typography,
        paddings = theme.paddings,
        elevations = theme.elevations,
        spacings = theme.spacings,
        renderers = renderers,
        content = content
    )
}

@OptIn(InternalComposeStyledApi::class)
@Composable
private fun StyledTheme(
    components: StyledComponents = LocalStyledComponents.current,
    colors: StyledColors = StyledTheme.colors,
    shapes: StyledShapes = StyledTheme.shapes,
    typography: StyledTypography = StyledTheme.typography,
    paddings: StyledPaddings = StyledTheme.paddings,
    elevations: StyledElevations = StyledTheme.elevations,
    spacings: StyledSpacings = StyledTheme.spacings,
    renderers: List<ExtensionRenderer<*>> = emptyList(),
    content: @Composable () -> Unit,
) {
    // this provides platform specific defaults for:
    // - typography (heading1-9, text1-9)
    // - indications (bright, dimmed)
    // - interaction sizes (default, minimum)
    // - shapes (roundedNone, roundedSmall, roundedMedium, roundedLarge, roundedFull)

    // 1) provide the theme, the styled locals and foundation locals
    CompositionLocalProvider(
        // components
        LocalStyledComponents provides components,
        // styled locals
        LocalStyledColors provides colors,
        LocalStyledShapes provides shapes,
        LocalStyledTypography provides typography,
        LocalStyledPaddings provides paddings,
        LocalStyledElevations provides elevations,
        LocalStyledSpacings provides spacings
    ) {
        // TODO: https://github.com/composablehorizons/compose-unstyled/discussions/191
        val platformTheme = buildPlatformTheme {
            properties[indications] = mapOf(
                bright to rememberRippleIndication(Color.White),
                dimmed to rememberRippleIndication(Color.Black),
            )
        }

        platformTheme {

            // compose unstyled predefined platform theme tokens
            // => can be used inside styled components if needed
            // E.g.:
            // val h5 = Theme[textStyles][heading5]
            // val indication = Theme[indications][bright]
            // val roundNone = Theme[com.composeunstyled.platformtheme.shapes][roundedNone]
            // val size = Theme[interactiveSizes][sizeDefault]

            val indicationBright = Theme[indications][bright]
            val indicationDimmed = Theme[indications][dimmed]

            val interactiveSizeDefault = Theme[interactiveSizes][sizeDefault]
            val interactiveSizeSmall = Theme[interactiveSizes][sizeMinimum]
            val interactiveSizes = StyledSizes(
                minimumInteractiveSize = interactiveSizeDefault,
                minimumInteractiveSizeSmall = interactiveSizeSmall,
                minimumInteractiveSizeLarge = interactiveSizeDefault + (interactiveSizeDefault - interactiveSizeSmall)
            )
            val indications = remember {
                StyledIndications(
                    onDark = indicationBright,
                    onLight = indicationDimmed
                )
            }

            val registry = remember(*renderers.toTypedArray()) {
                StyledExtensionsRegistry(
                    renderers.associateBy { it.key }
                )
            }

            CompositionLocalProvider(
                // extensions
                LocalStyledExtensions provides registry,
                // styled locals
                LocalStyledIndications provides indications,
                LocalStyledSizes provides interactiveSizes,
                LocalStyledContentColor provides colors.onBackground,
                LocalStyledBackgroundColor provides colors.background,
                LocalStyledTextStyle provides typography.bodyMedium
            ) {
                when (components) {
                    is StyledComponents.Components -> content()
                    is StyledComponents.WrapperComponents -> components.root(content)
                }
            }
        }
    }
}

/**
 * provides the ability to locally override parts of the StyledTheme
 *
 * [StyledTheme] must have been applied higher up in the compose tree!
 */
@OptIn(InternalComposeStyledApi::class)
@Composable
fun ProvideStyledTheme(
    theme: StyledThemeDefinition<*>,
    colors: StyledColors = LocalStyledColors.current,
    content: @Composable () -> Unit,
) {
    ProvideStyledTheme(
        components = theme.components,
        colors = colors,
        shapes = theme.shapes,
        typography = theme.typography,
        paddings = theme.paddings,
        elevations = theme.elevations,
        spacings = theme.spacings,
        content = content
    )
}


/**
 * provides the ability to locally override parts of the StyledTheme
 *
 * [StyledTheme] must have been applied higher up in the compose tree!
 */
@OptIn(InternalComposeStyledApi::class)
@Composable
fun ProvideStyledTheme(
    components: StyledComponents = LocalStyledComponents.current,
    colors: StyledColors = StyledTheme.colors,
    shapes: StyledShapes = StyledTheme.shapes,
    typography: StyledTypography = StyledTheme.typography,
    paddings: StyledPaddings = StyledTheme.paddings,
    elevations: StyledElevations = StyledTheme.elevations,
    spacings: StyledSpacings = StyledTheme.spacings,
    content: @Composable () -> Unit,
) {
    val localComponentsChanged = components != LocalStyledComponents.current
    CompositionLocalProvider(
        LocalStyledComponents provides components,
        // styled locals
        LocalStyledColors provides colors,
        LocalStyledShapes provides shapes,
        LocalStyledTypography provides typography,
        LocalStyledPaddings provides paddings,
        LocalStyledElevations provides elevations,
        LocalStyledSpacings provides spacings
    ) {
        if (localComponentsChanged) {
            when (components) {
                is StyledComponents.Components -> content()
                is StyledComponents.WrapperComponents -> throw IllegalStateException("WrapperComponents cannot be used inside ProvideStyledTheme, they can just be used at the root StyledTheme!")
            }
        } else {
            content()
        }
    }
}
