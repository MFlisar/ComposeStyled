package com.michaelflisar.composestyledcomponents

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.michaelflisar.composestyledcomponents.classes.StyledThemeProvider
import com.michaelflisar.composestyledcomponents.platform.LocalPlatform
import com.michaelflisar.composestyledcomponents.platform.Platform
import com.michaelflisar.composestyledcomponents.platform.PlatformDefaults
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyledcomponents.tokens.LocalStyledColorScheme
import com.michaelflisar.composestyledcomponents.tokens.LocalStyledContentColor
import com.michaelflisar.composestyledcomponents.tokens.LocalStyledPadding
import com.michaelflisar.composestyledcomponents.registry.LocalStyledRegistry
import com.michaelflisar.composestyledcomponents.tokens.LocalStyledShapes
import com.michaelflisar.composestyledcomponents.tokens.LocalStyledSizes
import com.michaelflisar.composestyledcomponents.tokens.LocalStyledSpacing
import com.michaelflisar.composestyledcomponents.tokens.LocalStyledTextStyle
import com.michaelflisar.composestyledcomponents.tokens.LocalStyledThemeName
import com.michaelflisar.composestyledcomponents.tokens.LocalStyledTypography
import com.michaelflisar.composestyledcomponents.tokens.StyledColorScheme
import com.michaelflisar.composestyledcomponents.tokens.StyledPadding
import com.michaelflisar.composestyledcomponents.tokens.StyledShapes
import com.michaelflisar.composestyledcomponents.tokens.StyledSizes
import com.michaelflisar.composestyledcomponents.tokens.StyledSpacing
import com.michaelflisar.composestyledcomponents.tokens.StyledTypography
import com.michaelflisar.composestyledcomponents.tokens.lightStyledColors

object StyledTheme {

    enum class ContentColorFallback {
        BlackWhite,
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

    // ----------------------
    // theme properties (locals, often times overridden locally inside components)
    // ----------------------

    val currentContentColor: Color
        @Composable @ReadOnlyComposable get() = LocalStyledContentColor.current

    val currentTextStyle: TextStyle
        @Composable @ReadOnlyComposable get() = LocalStyledTextStyle.current

    // ---------------------
    // functions
    // ---------------------

    /**
     * ATTENTION: inverse may not work well with white/black colors
     * as those are used as content colors quite often...
     *
     * because of this [checkInversely] is deactivated by default
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
            ContentColorFallback.BlackWhite -> if (backgroundColor.isDark) {
                Color.White
            } else {
                Color.Black
            }

            ContentColorFallback.LocalContentColor -> LocalStyledContentColor.current
        }
    }
}

/**
 * Applies Styled tokens and the provider root before creating the theme definition.
 */
@Composable
fun StyledTheme(
    platform: Platform,
    theme: StyledThemeProvider,
    colors: StyledColorScheme = lightStyledColors(),
    typography: StyledTypography = PlatformDefaults.typography(platform),
    shapes: StyledShapes = PlatformDefaults.shapes(platform),
    paddings: StyledPadding = PlatformDefaults.paddings(platform),
    spacings: StyledSpacing = PlatformDefaults.spacings(platform),
    sizes: StyledSizes = PlatformDefaults.sizes(platform),
    content: @Composable () -> Unit,
) {
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
    ) {
        theme.Root {
            val definition = theme.definition()
            CompositionLocalProvider(
                LocalStyledThemeName provides definition.name,
                LocalStyledRegistry provides definition.registry,
            ) {
                content()
            }
        }
    }
}

/**
 * Provides a custom content color and text style for the content inside this composable.
 * If the contentColor or textStyle is null or unspecified, the current values will be used.
 */
@InternalComposeStyledApi
@Composable
fun ProvideCustomStyledContentLocals(
    contentColor: Color? = null,
    textStyle: TextStyle? = null,
    content: @Composable () -> Unit,
) {
    if ((contentColor == null || contentColor == Color.Unspecified) && textStyle == null) {
        content()
        return
    }

    CompositionLocalProvider(
        LocalStyledContentColor provides (contentColor ?: StyledTheme.currentContentColor),
        LocalStyledTextStyle provides (textStyle ?: StyledTheme.currentTextStyle),
        content = content
    )
}