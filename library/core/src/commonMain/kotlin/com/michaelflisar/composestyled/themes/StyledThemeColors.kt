package com.michaelflisar.composestyled.themes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import com.michaelflisar.composestyled.classes.StyledThemeProvider
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
    /** Minimum target contrast ratio used when resolving built-in color pairs. */
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
    /** Selects the light or dark scheme for [contrast]. */
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
 *
 * @property themeId ID of the collection this palette belongs to.
 */
@Immutable
data class StyledThemeColors(
    val themeId: String,
    val schemeId: String,
    val name: String,
    val monochrome: StyledThemeColorSchemes,
    val colorful: StyledThemeColorSchemes,
) {
    companion object {

        fun saver(
            defaultColors: StyledThemeColors,
            collections: List<StyledThemeColorsCollection>,
        ): Saver<MutableState<StyledThemeColors>, List<String>> {
            val colorsById = collections
                .flatMap { it.themes }
                .associateBy { it.id }
            return Saver(
                save = { state -> listOf(state.value.id) },
                restore = { saved ->
                    val uniqueId = when (saved.size) {
                        1 -> saved[0]
                        2 -> "${saved[0]}|${saved[1]}"
                        else -> null
                    }
                    val colors = uniqueId?.let(colorsById::get) ?: defaultColors
                    mutableStateOf(colors)
                },
            )
        }

        fun key(
            colors: StyledThemeColors,
            collections: List<StyledThemeColorsCollection>,
        ): Pair<String, String> {
            val collectionId = collections
                .firstOrNull { it.id == colors.themeId && colors in it.themes }
                ?.id
                ?: colors.themeId
            return collectionId to colors.schemeId
        }

    }

    val id: String
        get() = "$themeId|$schemeId"

    fun getVariant(
        variant: StyledThemeVariant,
    ): StyledThemeColorSchemes = when (variant) {
        StyledThemeVariant.Monochrome -> monochrome
        StyledThemeVariant.Colorful -> colorful
    }

    /** Selects the color variant, then resolves its appearance and contrast scheme. */
    fun resolve(
        variant: StyledThemeVariant,
        isDark: Boolean,
        contrast: StyledContrastLevel,
    ): StyledColorScheme = getVariant(variant).resolve(isDark, contrast)
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

/**
 * Remembers the selected colors and restores them from [collections].
 *
 * If the saved colors are no longer available in [collections], the provider's default colors are
 * used instead.
 */
@Composable
fun rememberThemeColors(
    themeProvider: StyledThemeProvider,
    collections: List<StyledThemeColorsCollection>,
): MutableState<StyledThemeColors> {
    val defaultColors = themeProvider.defaultThemeColors
    return rememberSaveable(
        defaultColors.id,
        saver = StyledThemeColors.saver(defaultColors, collections),
    ) { mutableStateOf(defaultColors) }
}
