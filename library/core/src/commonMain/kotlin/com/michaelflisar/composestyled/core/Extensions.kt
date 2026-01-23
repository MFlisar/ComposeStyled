package com.michaelflisar.composestyled.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import com.michaelflisar.composestyled.core.classes.StyledDisableFactor

val Color.isDark
    get() = luminance() <= 0.5

val Color.isLight
    get() = !isDark

@Composable
@ReadOnlyComposable
fun Color.disabled(type: StyledDisableFactor = StyledDisableFactor.Default): Color {
    return copy(alpha = alpha * StyledTheme.colors.disableFactors.get(type))
}

internal fun Color.darken(factor: Float = 0.1f): Color {
    val f = factor.coerceIn(0f, 1f)
    val mul = 1f - f
    return copy(
        red = (red * mul).coerceIn(0f, 1f),
        green = (green * mul).coerceIn(0f, 1f),
        blue = (blue * mul).coerceIn(0f, 1f),
        alpha = alpha,
    )
}

internal fun Color.lighten(factor: Float = 0.1f): Color {
    val f = factor.coerceIn(0f, 1f)
    return copy(
        red = (red + (1f - red) * f).coerceIn(0f, 1f),
        green = (green + (1f - green) * f).coerceIn(0f, 1f),
        blue = (blue + (1f - blue) * f).coerceIn(0f, 1f),
        alpha = alpha,
    )
}
