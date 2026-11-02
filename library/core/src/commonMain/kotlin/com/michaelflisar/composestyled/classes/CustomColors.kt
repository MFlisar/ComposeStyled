package com.michaelflisar.composestyled.classes

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * Color mapping for filled and text-style component renderers.
 *
 * Theme tokens are resolved when the component is composed, so the mapping follows theme changes.
 */
@Immutable
class CustomColors(
    val filledBackground: ColorToken,
    val filledForeground: ColorToken,
    val textForeground: ColorToken,
) {
    companion object {

        /** Uses the theme's primary colors for filled and text-style renderers. */
        val Primary = CustomColors(
            filledBackground = ColorToken.Theme.Primary,
            filledForeground = ColorToken.Theme.OnPrimary,
            textForeground = ColorToken.Theme.Primary,
        )

        /** Uses the theme's secondary colors for filled and text-style renderers. */
        val Secondary = CustomColors(
            filledBackground = ColorToken.Theme.Secondary,
            filledForeground = ColorToken.Theme.OnSecondary,
            textForeground = ColorToken.Theme.Secondary,
        )

        /** Uses the primary surface colors for filled styles and primary for text styles. */
        val PrimarySurface = CustomColors(
            filledBackground = ColorToken.Theme.PrimarySurface,
            filledForeground = ColorToken.Theme.OnPrimarySurface,
            textForeground = ColorToken.Theme.Primary,
        )

        /** Uses the secondary surface colors for filled styles and secondary for text styles. */
        val SecondarySurface = CustomColors(
            filledBackground = ColorToken.Theme.SecondarySurface,
            filledForeground = ColorToken.Theme.OnSecondarySurface,
            textForeground = ColorToken.Theme.Secondary,
        )

        /** Uses the theme's error colors for filled and text-style renderers. */
        val Error = CustomColors(
            filledBackground = ColorToken.Theme.Error,
            filledForeground = ColorToken.Theme.OnError,
            textForeground = ColorToken.Theme.Error,
        )

        /** Uses the theme's error-surface colors for filled styles and error for text styles. */
        val ErrorSurface = CustomColors(
            filledBackground = ColorToken.Theme.ErrorSurface,
            filledForeground = ColorToken.Theme.OnErrorSurface,
            textForeground = ColorToken.Theme.Error,
        )

    }

    /** Creates a color mapping from fixed colors instead of theme-aware tokens. */
    constructor(
        filledBackground: Color,
        filledForeground: Color,
        textForeground: Color,
    ) : this(
        filledBackground = filledBackground.asColorToken(),
        filledForeground = filledForeground.asColorToken(),
        textForeground = textForeground.asColorToken(),
    )
}