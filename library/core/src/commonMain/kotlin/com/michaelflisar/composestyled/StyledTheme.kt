package com.michaelflisar.composestyled

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.michaelflisar.composestyled.classes.StyledLocalMissingException
import com.michaelflisar.composestyled.classes.StyledThemeProvider
import com.michaelflisar.composestyled.platform.LocalPlatform
import com.michaelflisar.composestyled.platform.Platform
import com.michaelflisar.composestyled.platform.PlatformDefaults
import com.michaelflisar.composestyled.registry.LocalStyledRegistry
import com.michaelflisar.composestyled.registry.StyledRegistry
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.themes.StyledContrastLevel
import com.michaelflisar.composestyled.themes.StyledThemeAppearance
import com.michaelflisar.composestyled.themes.StyledThemeColors
import com.michaelflisar.composestyled.themes.StyledThemeContrast
import com.michaelflisar.composestyled.themes.StyledThemeVariant
import com.michaelflisar.composestyled.tokens.LocalStyledColorScheme
import com.michaelflisar.composestyled.tokens.LocalStyledContentColor
import com.michaelflisar.composestyled.tokens.LocalStyledPadding
import com.michaelflisar.composestyled.tokens.LocalStyledShapes
import com.michaelflisar.composestyled.tokens.LocalStyledSizes
import com.michaelflisar.composestyled.tokens.LocalStyledSpacing
import com.michaelflisar.composestyled.tokens.LocalStyledTextStyle
import com.michaelflisar.composestyled.tokens.LocalStyledThemeName
import com.michaelflisar.composestyled.tokens.LocalStyledTypography
import com.michaelflisar.composestyled.tokens.StyledColorScheme
import com.michaelflisar.composestyled.tokens.StyledPadding
import com.michaelflisar.composestyled.tokens.StyledShapes
import com.michaelflisar.composestyled.tokens.StyledSizes
import com.michaelflisar.composestyled.tokens.StyledSpacing
import com.michaelflisar.composestyled.tokens.StyledTypography
import com.michaelflisar.composestyled.ui.layout.LocalFloatingDialogHost
import com.michaelflisar.composestyled.ui.layout.StyledFloatingHost

// only on android 31+
expect val StyledTheme.isDynamicColorsSupported: Boolean

// only on android 34+
expect val StyledTheme.isSystemContrastSupported: Boolean

/** Read-only accessors for the theme and tokens provided by [StyledTheme]. */
object StyledTheme {

    /** Theme-wide behavior settings that do not belong to an individual component. */
    @Immutable
    data class Behavior(
        val showScrollbars: Boolean = true,
    )

    enum class ContentColorFallback {
        /** Selects black or white according to which provides the higher contrast. */
        BlackWhite,

        /** Uses the content color currently provided by the surrounding composition. */
        LocalContentColor
    }

    val platform: Platform
        @Composable @ReadOnlyComposable get() = LocalPlatform.current

    val themeName: String
        @Composable @ReadOnlyComposable get() = LocalStyledThemeName.current

    val colorScheme: StyledColorScheme
        @Composable @ReadOnlyComposable get() = LocalStyledColorScheme.current

    val typography: StyledTypography
        @Composable @ReadOnlyComposable get() = LocalStyledTypography.current

    val shapes: StyledShapes
        @Composable @ReadOnlyComposable get() = LocalStyledShapes.current

    val padding: StyledPadding
        @Composable @ReadOnlyComposable get() = LocalStyledPadding.current

    val spacing: StyledSpacing
        @Composable @ReadOnlyComposable get() = LocalStyledSpacing.current

    val sizes: StyledSizes
        @Composable @ReadOnlyComposable get() = LocalStyledSizes.current

    val behavior: Behavior
        @Composable @ReadOnlyComposable get() = LocalStyledBehavior.current

    // ---------------------
    // functions
    // ---------------------

    /**
     * Returns the matching theme content color, or [fallback] when no matching color is defined.
     * The [ContentColorFallback.BlackWhite] option selects the color with the higher contrast.
     *
     * Inverse matching may not work well with black/white content colors, so [checkInversely] is
     * disabled by default.
     */
    @Composable
    @ReadOnlyComposable
    fun contentColorFor(
        backgroundColor: Color,
        checkInversely: Boolean = false,
        fallback: ContentColorFallback = ContentColorFallback.LocalContentColor,
    ): Color {
        val def = colorScheme.allDefinitions
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
            ContentColorFallback.BlackWhite -> backgroundColor.onColorByContrastRatio()

            ContentColorFallback.LocalContentColor -> LocalStyledContentColor.current
        }
    }

    @Composable
    @ReadOnlyComposable
    fun supportingOnColor(
        backgroundColor: Color,
        minimumContrastRatio: Float = 4.5f,
    ) = backgroundColor.supportingOnColor(
        defaultOnColor = contentColorFor(
            backgroundColor = backgroundColor,
            fallback = ContentColorFallback.BlackWhite
        ),
        minimumContrastRatio = minimumContrastRatio,
    )

    /**
     * Returns a less prominent variant of [contentColor] against [containerColor].
     *
     * The result targets 65% of the original contrast, with [minimumContrastRatio] as its floor
     * when the original contrast permits.
     */
    fun supportColorOf(
        contentColor: Color,
        containerColor: Color,
        minimumContrastRatio: Float = 4.5f,
    ): Color = contentColor.supportingColor(
        backgroundColor = containerColor,
        minimumContrastRatio = minimumContrastRatio,
    )
}

/**
 * Provides theme tokens, the provider's component registry and floating-window host to [content].
 *
 * Use this as the root of a Styled UI subtree. [customization] is applied after the provider's
 * default registrations, so it can replace renderers and update their defaults.
 */
@Composable
fun StyledTheme(
    platform: Platform,
    themeProvider: StyledThemeProvider,
    themeColors: StyledThemeColors = themeProvider.defaultThemeColors,
    themeVariant: StyledThemeVariant = StyledThemeVariant.Colorful,
    themeAppearance: StyledThemeAppearance = StyledThemeAppearance.System,
    themeContrast: StyledThemeContrast = StyledThemeContrast.System,
    typography: StyledTypography = PlatformDefaults.typography(platform),
    shapes: StyledShapes = PlatformDefaults.shapes(platform),
    paddings: StyledPadding = PlatformDefaults.paddings(platform),
    spacings: StyledSpacing = PlatformDefaults.spacings(platform),
    sizes: StyledSizes = PlatformDefaults.sizes(platform),
    behavior: StyledTheme.Behavior = StyledTheme.Behavior(
        showScrollbars = PlatformDefaults.showScrollbars(platform)
    ),
    customization: (@Composable StyledRegistry.Builder.() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val isDark = when (themeAppearance) {
        StyledThemeAppearance.System -> isSystemInDarkTheme()
        StyledThemeAppearance.Light -> false
        StyledThemeAppearance.Dark -> true
    }
    val contrast = when (themeContrast) {
        StyledThemeContrast.System -> PlatformDefaults.systemContrastLevel()
        StyledThemeContrast.Normal -> StyledContrastLevel.Normal
        StyledThemeContrast.Medium -> StyledContrastLevel.Medium
        StyledThemeContrast.High -> StyledContrastLevel.High
    }
    val colors = themeColors.resolve(themeVariant, isDark, contrast)

    CompositionLocalProvider(
        LocalPlatform provides platform,
        // styles
        LocalStyledColorScheme provides colors,
        LocalStyledTypography provides typography,
        LocalStyledShapes provides shapes,
        LocalStyledPadding provides paddings,
        LocalStyledSpacing provides spacings,
        LocalStyledTextStyle provides typography.bodyMedium,
        LocalStyledContentColor provides colors.onBackground,
        LocalStyledSizes provides sizes,
        LocalStyledBehavior provides behavior
    ) {
        ProvideFloatingHostIfMissing {
            themeProvider.Root {
                val definition = themeProvider.definition()
                val defaultRegistry = StyledRegistry.createComposable(definition.registry) {
                    register(themeProvider.defaultIconSet)
                }
                val registry = customization?.let {
                    StyledRegistry.createComposable(defaultRegistry, it)
                } ?: defaultRegistry
                CompositionLocalProvider(
                    LocalStyledThemeName provides definition.name,
                    LocalStyledRegistry provides registry,
                ) {
                    content()
                }
            }
        }
    }
}

@Composable
@OptIn(InternalComposeStyledApi::class)
private fun ProvideFloatingHostIfMissing(content: @Composable () -> Unit) {
    if (LocalFloatingDialogHost.current == null) {
        StyledFloatingHost(content = content)
    } else {
        content()
    }
}

internal val LocalStyledBehavior = staticCompositionLocalOf<StyledTheme.Behavior> {
    throw StyledLocalMissingException("LocalStyledBehavior")
}

/**
 * Provides a local content color and/or text style to [content], inheriting the current value for
 * each argument that is null or unspecified.
 */
@InternalComposeStyledApi
@Composable
fun UpdateStyledThemeLocals(
    contentColor: Color? = null,
    textStyle: TextStyle? = null,
    content: @Composable () -> Unit,
) {
    if ((contentColor == null || contentColor == Color.Unspecified) && textStyle == null) {
        content()
        return
    }

    CompositionLocalProvider(
        LocalStyledContentColor provides (contentColor ?: LocalStyledContentColor.current),
        LocalStyledTextStyle provides (textStyle ?: LocalStyledTextStyle.current),
        content = content
    )
}

/**
 * Applies registry changes to a subtree without changing the enclosing theme.
 *
 * The [update] builder inherits the current registry; register replacements before updating their
 * defaults.
 */
@Composable
fun UpdateStyledCustomization(
    update: @Composable StyledRegistry.Builder.() -> Unit,
    content: @Composable () -> Unit,
) {
    val parentRegistry = LocalStyledRegistry.current
    val childRegistry = StyledRegistry.createComposable(parentRegistry, update)

    CompositionLocalProvider(
        LocalStyledRegistry provides childRegistry,
    ) {
        content()
    }
}