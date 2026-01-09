package com.michaelflisar.composestyled.core

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance

val Color.isDark
    get() = luminance() < 0.5

val Color.hovered
    get() = if (isDark) lighten(0.1f) else darken(0.1f)

val Color.pressed
    get() = if (isDark) lighten(0.2f) else darken(0.2f)

val Color.focus
    get() = if (isDark) Color.White else Color.Black

/**
 * Darkens this color by the given `factor`.
 *
 * - `factor = 0f`  => unchanged
 * - `factor = 0.1f` => 10% darker
 * - `factor = 1f`  => black (same alpha)
 */
fun Color.darken(factor: Float = 0.1f): Color {
    val f = factor.coerceIn(0f, 1f)
    val mul = 1f - f
    return copy(
        red = (red * mul).coerceIn(0f, 1f),
        green = (green * mul).coerceIn(0f, 1f),
        blue = (blue * mul).coerceIn(0f, 1f),
        alpha = alpha,
    )
}

/**
 * Lightens this color by the given `factor`.
 *
 * - `factor = 0f`  => unchanged
 * - `factor = 0.1f` => 10% lighter
 * - `factor = 1f`  => white (same alpha)
 */
fun Color.lighten(factor: Float = 0.1f): Color {
    val f = factor.coerceIn(0f, 1f)
    return copy(
        red = (red + (1f - red) * f).coerceIn(0f, 1f),
        green = (green + (1f - green) * f).coerceIn(0f, 1f),
        blue = (blue + (1f - blue) * f).coerceIn(0f, 1f),
        alpha = alpha,
    )
}
