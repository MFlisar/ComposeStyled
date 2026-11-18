package com.michaelflisar.composestyled

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalWindowInfo
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi

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

/** Adjusts this color to an approximate CIE L* tone by interpolating toward black or white. */
@InternalComposeStyledApi
fun Color.atTone(tone: Float): Color {
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

/**
 * if possible, prefer using [com.michaelflisar.composestyled.adaptive.StyledAdaptiveInfo] isLandscape property
 */
val isLandscape: Boolean
    @Composable get() = with(LocalWindowInfo.current.containerSize) { width > height }
