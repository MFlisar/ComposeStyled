package com.michaelflisar.composestyled

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance

/** Whether this color's relative luminance is at most 50%. */
val Color.isDark
    get() = luminance() <= 0.5

/** Whether this color's relative luminance is above 50%. */
val Color.isLight
    get() = !isDark

/** Blends this color toward white by [factor], preserving its alpha. */
fun Color.lighten(factor: Float): Color {
    require(factor in 0f..1f) { "Factor must be between 0 and 1" }
    val r = red + (1 - red) * factor
    val g = green + (1 - green) * factor
    val b = blue + (1 - blue) * factor
    return Color(r, g, b, alpha)
}

/** Blends this color toward black by [factor], preserving its alpha. */
fun Color.darken(factor: Float): Color {
    require(factor in 0f..1f) { "Factor must be between 0 and 1" }
    val r = red * (1 - factor)
    val g = green * (1 - factor)
    val b = blue * (1 - factor)
    return Color(r, g, b, alpha)
}

/**
 * Returns the WCAG contrast ratio between this color and [other].
 */
fun Color.contrastRatio(other: Color): Float {
    val firstLuminance = relativeLuminance()
    val secondLuminance = other.relativeLuminance()
    val lighter = maxOf(firstLuminance, secondLuminance)
    val darker = minOf(firstLuminance, secondLuminance)
    return (lighter + 0.05f) / (darker + 0.05f)
}

/**
 * Returns black or white, whichever has the higher contrast ratio against this color.
 */
fun Color.onColorByContrastRatio(): Color =
    if (contrastRatio(Color.Black) >= contrastRatio(Color.White)) Color.Black else Color.White

private fun Color.relativeLuminance(): Float {
    fun linearize(channel: Float): Float {
        if (channel <= 0.04045f) return channel / 12.92f
        val adjusted = (channel + 0.055f) / 1.055f
        return adjusted * adjusted * adjusted
    }

    return 0.2126f * linearize(red) +
        0.7152f * linearize(green) +
        0.0722f * linearize(blue)
}