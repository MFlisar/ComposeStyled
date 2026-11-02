package com.michaelflisar.composestyled.themes

import androidx.compose.runtime.Immutable
import com.michaelflisar.composestyled.platform.Platform
import com.michaelflisar.composestyled.tokens.StyledColorScheme

/**
 * Selects monochrome or colorful theme colors.
 */
enum class StyledThemeVariant {
    Monochrome,
    Colorful,
}

/**
 * Contrast targets used when resolving built-in color schemes.
 */
enum class StyledContrastLevel(
    val targetContrastRatio: Float,
) {
    Normal(4.5f),
    Medium(5.5f),
    High(7f),
}

/**
 * Selects whether the color theme follows the system appearance or forces light/dark colors.
 */
enum class StyledThemeAppearance {
    System,
    Light,
    Dark,
}

/**
 * Selects a fixed contrast level or follows the platform's contrast preference.
 */
enum class StyledThemeContrast {
    System,
    Normal,
    Medium,
    High,
}

/**
 * Light and dark schemes for all supported contrast levels.
 */
@Immutable
data class StyledThemeColorSchemes(
    val lightNormal: StyledColorScheme,
    val lightMedium: StyledColorScheme,
    val lightHigh: StyledColorScheme,
    val darkNormal: StyledColorScheme,
    val darkMedium: StyledColorScheme,
    val darkHigh: StyledColorScheme,
) {
    fun resolve(
        isDark: Boolean,
        contrast: StyledContrastLevel,
    ): StyledColorScheme = when {
        isDark -> when (contrast) {
            StyledContrastLevel.Normal -> darkNormal
            StyledContrastLevel.Medium -> darkMedium
            StyledContrastLevel.High -> darkHigh
        }

        else -> when (contrast) {
            StyledContrastLevel.Normal -> lightNormal
            StyledContrastLevel.Medium -> lightMedium
            StyledContrastLevel.High -> lightHigh
        }
    }
}

/**
 * A named palette with monochrome and colorful variants.
 */
@Immutable
data class StyledThemeColors(
    val id: String,
    val name: String,
    val monochrome: StyledThemeColorSchemes,
    val colorful: StyledThemeColorSchemes,
) {
    fun resolve(
        variant: StyledThemeVariant,
        isDark: Boolean,
        contrast: StyledContrastLevel,
    ): StyledColorScheme = when (variant) {
        StyledThemeVariant.Monochrome -> monochrome
        StyledThemeVariant.Colorful -> colorful
    }.resolve(isDark, contrast)
}

/**
 * A named collection of related color themes and the platforms it matches.
 */
data class StyledThemeColorsCollection(
    val id: String,
    val name: String,
    val themes: List<StyledThemeColors>,
    val matchingPlatforms: Set<Platform> = Platform.entries.toSet(),
) {
    /**
     * Returns whether this collection matches [platform].
     */
    fun fits(platform: Platform): Boolean = platform in matchingPlatforms
}
