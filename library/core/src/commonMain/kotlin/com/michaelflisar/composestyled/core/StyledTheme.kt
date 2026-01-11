package com.michaelflisar.composestyled.core

import androidx.compose.foundation.LocalIndication
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.key
import com.composeunstyled.platformtheme.bright
import com.composeunstyled.platformtheme.buildPlatformTheme
import com.composeunstyled.platformtheme.indications
import com.composeunstyled.platformtheme.interactiveSizes
import com.composeunstyled.platformtheme.sizeDefault
import com.composeunstyled.platformtheme.sizeMinimum
import com.composeunstyled.theme.Theme
import com.michaelflisar.composestyled.core.renderer.LocalStyledComponents
import com.michaelflisar.composestyled.core.renderer.StyledComponents
import com.michaelflisar.composestyled.core.renderer.StyledTokenCompontents
import com.michaelflisar.composestyled.core.renderer.StyledWrapperComponents
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.core.runtime.LocalBackgroundColor
import com.michaelflisar.composestyled.core.runtime.LocalContentColor
import com.michaelflisar.composestyled.core.runtime.LocalTextStyle
import com.michaelflisar.composestyled.core.runtime.LocalThemeBuilder
import com.michaelflisar.composestyled.core.tokens.LocalStyledColors
import com.michaelflisar.composestyled.core.tokens.LocalStyledPaddings
import com.michaelflisar.composestyled.core.tokens.LocalStyledShapes
import com.michaelflisar.composestyled.core.tokens.LocalStyledSizes
import com.michaelflisar.composestyled.core.tokens.LocalStyledSpacings
import com.michaelflisar.composestyled.core.tokens.LocalStyledTypography
import com.michaelflisar.composestyled.core.tokens.StyledColors
import com.michaelflisar.composestyled.core.tokens.StyledPaddings
import com.michaelflisar.composestyled.core.tokens.StyledShapes
import com.michaelflisar.composestyled.core.tokens.StyledSizes
import com.michaelflisar.composestyled.core.tokens.StyledSpacings
import com.michaelflisar.composestyled.core.tokens.StyledTypography

object StyledTheme {

    val colors: StyledColors
        @Composable @ReadOnlyComposable get() = LocalStyledColors.current

    val shapes: StyledShapes
        @Composable @ReadOnlyComposable get() = LocalStyledShapes.current

    val typography: StyledTypography
        @Composable @ReadOnlyComposable get() = LocalStyledTypography.current

    val paddings: StyledPaddings
        @Composable @ReadOnlyComposable get() = LocalStyledPaddings.current

    val spacings: StyledSpacings
        @Composable @ReadOnlyComposable get() = LocalStyledSpacings.current

    val sizes: StyledSizes
        @Composable @ReadOnlyComposable get() = LocalStyledSizes.current
}

@OptIn(InternalComposeStyledApi::class)
@Composable
fun StyledTheme(
    styledComponents: StyledComponents,
    colors: StyledColors = StyledTheme.colors,
    shapes: StyledShapes = StyledTheme.shapes,
    typography: StyledTypography = StyledTheme.typography,
    paddings: StyledPaddings = StyledTheme.paddings,
    spacings: StyledSpacings = StyledTheme.spacings,
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
        LocalStyledComponents provides styledComponents,
        // styled locals
        LocalStyledColors provides colors,
        LocalStyledShapes provides shapes,
        LocalStyledTypography provides typography,
        LocalStyledPaddings provides paddings,
        LocalStyledSpacings provides spacings
    ) {

        // 1) register all component styles and create a theme
        val platformTheme = key(styledComponents, colors, shapes, typography, paddings, spacings) {
            buildPlatformTheme {
                CompositionLocalProvider(
                    LocalThemeBuilder provides this
                ) {
                    when (styledComponents) {
                        is StyledTokenCompontents -> styledComponents.registerAllComponents()
                        is StyledWrapperComponents -> {
                            // no-op
                        }
                    }
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

            CompositionLocalProvider(
                // foundaton
                LocalIndication provides indication,
                // styled locals
                LocalStyledSizes provides interactiveSizes,
                LocalContentColor provides colors.onBackground,
                LocalBackgroundColor provides colors.background,
                LocalTextStyle provides typography.bodyMedium
            ) {
                when (styledComponents) {
                    is StyledTokenCompontents -> content()
                    is StyledWrapperComponents -> styledComponents.root(content)
                }
            }
        }
    }
}
