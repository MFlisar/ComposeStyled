package com.michaelflisar.composestyled

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalWindowInfo

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

/**
 * Returns a less prominent foreground by blending [defaultOnColor] toward this background.
 *
 * The resulting contrast is 65% of the original contrast, but never lower than
 * [minimumContrastRatio]. If [defaultOnColor] already has less contrast than that minimum, it is
 * returned unchanged.
 */
fun Color.supportingOnColor(
    defaultOnColor: Color,
    minimumContrastRatio: Float = 4.5f,
): Color = defaultOnColor.supportingColor(
    backgroundColor = this,
    minimumContrastRatio = minimumContrastRatio,
)

/**
 * Returns a less prominent variant of this foreground color by blending it toward
 * [backgroundColor].
 *
 * The resulting contrast is 65% of the original contrast, but never lower than
 * [minimumContrastRatio]. If this color already has less contrast than that minimum, it is returned
 * unchanged.
 */
fun Color.supportingColor(
    backgroundColor: Color,
    minimumContrastRatio: Float = 4.5f,
): Color {
    require(minimumContrastRatio in 1f..21f) {
        "Minimum contrast ratio must be between 1 and 21"
    }

    val originalContrast = contrastRatio(backgroundColor)
    val targetContrast = maxOf(minimumContrastRatio, originalContrast * 0.65f)
    if (targetContrast >= originalContrast) return this

    fun blendTowardBackground(amount: Float) = Color(
        red = red + (backgroundColor.red - red) * amount,
        green = green + (backgroundColor.green - green) * amount,
        blue = blue + (backgroundColor.blue - blue) * amount,
        alpha = alpha + (backgroundColor.alpha - alpha) * amount,
    )

    var low = 0f
    var high = 1f
    repeat(24) {
        val amount = (low + high) / 2f
        val candidate = blendTowardBackground(amount)
        if (candidate.contrastRatio(backgroundColor) >= targetContrast) {
            low = amount
        } else {
            high = amount
        }
    }

    return blendTowardBackground(low)
}

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