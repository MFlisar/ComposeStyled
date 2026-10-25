package com.michaelflisar.composestyledcomponents.classes

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
class CustomColors(
    val filledBackground: ColorToken,
    val filledForeground: ColorToken,
    val textForeground: ColorToken,
) {
    companion object {

        val Primary = CustomColors(
            filledBackground = ColorToken.Theme.Primary,
            filledForeground = ColorToken.Theme.OnPrimary,
            textForeground = ColorToken.Theme.Primary,
        )

        val Secondary = CustomColors(
            filledBackground = ColorToken.Theme.Secondary,
            filledForeground = ColorToken.Theme.OnSecondary,
            textForeground = ColorToken.Theme.Secondary,
        )

        val PrimarySurface = CustomColors(
            filledBackground = ColorToken.Theme.PrimarySurface,
            filledForeground = ColorToken.Theme.OnPrimarySurface,
            textForeground = ColorToken.Theme.Primary,
        )

        val SecondarySurface = CustomColors(
            filledBackground = ColorToken.Theme.SecondarySurface,
            filledForeground = ColorToken.Theme.OnSecondarySurface,
            textForeground = ColorToken.Theme.Secondary,
        )

        val Error = CustomColors(
            filledBackground = ColorToken.Theme.Error,
            filledForeground = ColorToken.Theme.OnError,
            textForeground = ColorToken.Theme.Error,
        )

        val ErrorSurface = CustomColors(
            filledBackground = ColorToken.Theme.ErrorSurface,
            filledForeground = ColorToken.Theme.OnErrorSurface,
            textForeground = ColorToken.Theme.Error,
        )

    }

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