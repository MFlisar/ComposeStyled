package com.michaelflisar.composestyled.core.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import com.michaelflisar.composestyled.core.renderer.LocalStyledComponents
import com.michaelflisar.composestyled.core.renderer.StyledComponents
import com.michaelflisar.composestyled.core.runtime.ProvideStyledLocals
import com.michaelflisar.composestyled.core.tokens.LocalStyledColors
import com.michaelflisar.composestyled.core.tokens.LocalStyledPaddings
import com.michaelflisar.composestyled.core.tokens.LocalStyledShapes
import com.michaelflisar.composestyled.core.tokens.LocalStyledSpacings
import com.michaelflisar.composestyled.core.tokens.LocalStyledTypography
import com.michaelflisar.composestyled.core.tokens.StyledColors
import com.michaelflisar.composestyled.core.tokens.StyledPaddings
import com.michaelflisar.composestyled.core.tokens.StyledShapes
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
}

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
    CompositionLocalProvider(
        LocalStyledComponents provides styledComponents,
        LocalStyledColors provides colors,
        LocalStyledShapes provides shapes,
        LocalStyledTypography provides typography,
        LocalStyledPaddings provides paddings,
        LocalStyledSpacings provides spacings,
    ) {
        ProvideStyledLocals(
            contentColor = colors.onBackground,
            backgroundColor = colors.background,
            textStyle = typography.bodyMedium,
            applyTransparentBackgroundColor = true // here we save the background color even if it's transparent
        ) {
            styledComponents.Root(content)
        }
    }
}
