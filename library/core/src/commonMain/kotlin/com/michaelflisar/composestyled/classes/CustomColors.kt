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

        /** Uses the theme's tertiary-surface colors for filled styles and tertiary for text styles. */
        val TertiarySurface = CustomColors(
            filledBackground = ColorToken.Theme.TertiarySurface,
            filledForeground = ColorToken.Theme.OnTertiarySurface,
            textForeground = ColorToken.Theme.Tertiary,
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

        /** Uses the theme's success-surface colors for filled styles and success for text styles. */
        val SuccessSurface = CustomColors(
            filledBackground = ColorToken.Theme.SuccessSurface,
            filledForeground = ColorToken.Theme.OnSuccessSurface,
            textForeground = ColorToken.Theme.Success,
        )

        /** Uses the theme's warning-surface colors for filled styles and warning for text styles. */
        val WarningSurface = CustomColors(
            filledBackground = ColorToken.Theme.WarningSurface,
            filledForeground = ColorToken.Theme.OnWarningSurface,
            textForeground = ColorToken.Theme.Warning,
        )

        /** Uses the theme's lowest surface colors for filled and text-style renderers. */
        val SurfaceLowest = CustomColors(
            filledBackground = ColorToken.Theme.SurfaceLowest,
            filledForeground = ColorToken.Theme.OnSurfaceLowest,
            textForeground = ColorToken.Theme.OnSurfaceLowest,
        )

        /** Uses the theme's low surface colors for filled and text-style renderers. */
        val SurfaceLow = CustomColors(
            filledBackground = ColorToken.Theme.SurfaceLow,
            filledForeground = ColorToken.Theme.OnSurfaceLow,
            textForeground = ColorToken.Theme.OnSurfaceLow,
        )

        /** Uses the theme's surface colors for filled and text-style renderers. */
        val Surface = CustomColors(
            filledBackground = ColorToken.Theme.Surface,
            filledForeground = ColorToken.Theme.OnSurface,
            textForeground = ColorToken.Theme.OnSurface,
        )

        /** Uses the theme's high surface colors for filled and text-style renderers. */
        val SurfaceHigh = CustomColors(
            filledBackground = ColorToken.Theme.SurfaceHigh,
            filledForeground = ColorToken.Theme.OnSurfaceHigh,
            textForeground = ColorToken.Theme.OnSurfaceHigh,
        )

        /** Uses the theme's highest surface colors for filled and text-style renderers. */
        val SurfaceHighest = CustomColors(
            filledBackground = ColorToken.Theme.SurfaceHighest,
            filledForeground = ColorToken.Theme.OnSurfaceHighest,
            textForeground = ColorToken.Theme.OnSurfaceHighest,
        )

        /** Uses the theme's surface-variant colors for filled and text-style renderers. */
        val SurfaceVariant = CustomColors(
            filledBackground = ColorToken.Theme.SurfaceVariant,
            filledForeground = ColorToken.Theme.OnSurfaceVariant,
            textForeground = ColorToken.Theme.OnSurfaceVariant,
        )

        /** Uses the theme's inverse-surface colors for filled and text-style renderers. */
        val InverseSurface = CustomColors(
            filledBackground = ColorToken.Theme.InverseSurface,
            filledForeground = ColorToken.Theme.InverseOnSurface,
            textForeground = ColorToken.Theme.InverseSurface,
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