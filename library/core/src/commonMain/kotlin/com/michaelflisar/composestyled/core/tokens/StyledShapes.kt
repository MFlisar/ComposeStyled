package com.michaelflisar.composestyled.core.tokens

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Definiert die Standard-Shapes für das Designsystem.
 *
 * control...  buttons, chips, menus, checkbox
 * input... textfields, inputs
 * container... cards
 * indicator... tabs underline, button markers, ...
 * dialog... dialogs
 * rounded... floating action button, avatars
 */
@Immutable
data class StyledShapes(
    val sizes: Sizes = Sizes(),
    val control: Shape = RoundedCornerShape(size = sizes.medium),
    val input: Shape = RoundedCornerShape(size = sizes.extraSmall),
    val container: Shape = RoundedCornerShape(size = sizes.large),
    val indicator: Shape = RoundedCornerShape(size = sizes.extraSmall),
    val dialog: Shape = RoundedCornerShape(size = sizes.extraLarge),
    val rounded: Shape = RoundedCornerShape(percent = 50),
) {
    @Immutable
    data class Sizes(
        val extraSmall: Dp = 4.dp,
        val small: Dp = 8.dp,
        val medium: Dp = 12.dp,
        val large: Dp = 16.dp,
        val extraLarge: Dp = 24.dp,
    ) {
        fun next(size: Dp) : Dp {
            return when (size) {
                extraSmall -> small
                small -> medium
                medium -> large
                large -> extraLarge
                else -> extraLarge
            }
        }

        fun previous(size: Dp) : Dp {
            return when (size) {
                extraLarge -> large
                large -> medium
                medium -> small
                small -> extraSmall
                else -> extraSmall
            }
        }
    }
}

internal val LocalStyledShapes = staticCompositionLocalOf { StyledShapes() }
