package com.michaelflisar.composestyled.core

import androidx.compose.foundation.LocalIndication
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.composeunstyled.platformtheme.bright
import com.composeunstyled.platformtheme.buildPlatformTheme
import com.composeunstyled.platformtheme.indications
import com.composeunstyled.platformtheme.interactiveSizes
import com.composeunstyled.platformtheme.sizeDefault
import com.composeunstyled.platformtheme.sizeMinimum
import com.composeunstyled.theme.Theme
import com.michaelflisar.composestyled.core.renderer.ExtensionRenderer
import com.michaelflisar.composestyled.core.renderer.LocalStyledComponents
import com.michaelflisar.composestyled.core.renderer.LocalStyledExtensions
import com.michaelflisar.composestyled.core.renderer.StyledComponents
import com.michaelflisar.composestyled.core.renderer.StyledExtensionsRegistry
import com.michaelflisar.composestyled.core.renderer.StyledTokenComponents
import com.michaelflisar.composestyled.core.renderer.StyledWrapperComponents
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.core.runtime.LocalBackgroundColor
import com.michaelflisar.composestyled.core.runtime.LocalContentColor
import com.michaelflisar.composestyled.core.runtime.LocalTextStyle
import com.michaelflisar.composestyled.core.runtime.LocalThemeBuilder
import com.michaelflisar.composestyled.core.tokens.LocalStyledColors
import com.michaelflisar.composestyled.core.tokens.LocalStyledElevations
import com.michaelflisar.composestyled.core.tokens.LocalStyledPaddings
import com.michaelflisar.composestyled.core.tokens.LocalStyledShapes
import com.michaelflisar.composestyled.core.tokens.LocalStyledSizes
import com.michaelflisar.composestyled.core.tokens.LocalStyledSpacings
import com.michaelflisar.composestyled.core.tokens.LocalStyledTypography
import com.michaelflisar.composestyled.core.tokens.StyledColors
import com.michaelflisar.composestyled.core.tokens.StyledElevations
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

    class Setup(
        val shapes: StyledShapes,
        val typography: StyledTypography,
        val paddings: StyledPaddings,
        val elevations: StyledElevations,
        val spacings: StyledSpacings,
    )

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

            ContentColorFallback.LocalContentColor -> LocalContentColor.current
        }
    }
}

@OptIn(InternalComposeStyledApi::class)
@Composable
fun StyledTheme(
    components: StyledComponents,
    colors: StyledColors,
    setup: StyledTheme.Setup,
    extensionRenderers: List<ExtensionRenderer<*>>,
    content: @Composable () -> Unit,
) {
    StyledTheme(
        components = components,
        colors = colors,
        shapes = setup.shapes,
        typography = setup.typography,
        paddings = setup.paddings,
        elevations = setup.elevations,
        spacings = setup.spacings,
        extensionRenderers = extensionRenderers,
        content = content
    )
}

@OptIn(InternalComposeStyledApi::class)
@Composable
private fun StyledTheme(
    components: StyledComponents,
    colors: StyledColors = StyledTheme.colors,
    shapes: StyledShapes = StyledTheme.shapes,
    typography: StyledTypography = StyledTheme.typography,
    paddings: StyledPaddings = StyledTheme.paddings,
    elevations: StyledElevations = StyledTheme.elevations,
    spacings: StyledSpacings = StyledTheme.spacings,
    extensionRenderers: List<ExtensionRenderer<*>>,
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
        // 1) register all component styles and create a theme
        val platformTheme =
            key(components, colors, shapes, typography, paddings, elevations, spacings) {
                buildPlatformTheme {
                    CompositionLocalProvider(
                        LocalThemeBuilder provides this
                    ) {
                        when (components) {
                            is StyledTokenComponents -> {
                                components.registerAllComponents()
                            }

                            is StyledWrapperComponents -> {
                                // no-op
                            }
                        }

                        extensionRenderers.forEach { it.registerVariantStyles() }
                    }
                }
            }

        platformTheme {

            // compose unstyled predefined platform theme tokens
            // => can be used inside styled components if needed
            // E.g.:
            // val h5 = Theme[textStyles][heading5]
            // val indication = Theme[indications][bright]
            // val roundNone = Theme[com.composeunstyled.platformtheme.shapes][roundedNone]
            // val size = Theme[interactiveSizes][sizeDefault]

            val indication = Theme[indications][bright]
            val interactiveSizeDefault = Theme[interactiveSizes][sizeDefault]
            val interactiveSizeSmall = Theme[interactiveSizes][sizeMinimum]
            val interactiveSizes = StyledSizes(
                minimumInteractiveSize = interactiveSizeDefault,
                minimumInteractiveSizeSmall = interactiveSizeSmall
            )

            val registry = remember(*extensionRenderers.toTypedArray()) {
                StyledExtensionsRegistry(
                    extensionRenderers.associateBy { it.key }
                )
            }

            CompositionLocalProvider(
                // extensions
                LocalStyledExtensions provides registry,
                // foundaton
                LocalIndication provides indication,
                // styled locals
                LocalStyledSizes provides interactiveSizes,
                LocalContentColor provides colors.onBackground,
                LocalBackgroundColor provides colors.background,
                LocalTextStyle provides typography.bodyMedium
            ) {
                when (components) {
                    is StyledTokenComponents -> content()
                    is StyledWrapperComponents -> components.root(content)
                }
            }
        }
    }
}
