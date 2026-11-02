package com.michaelflisar.composestyled.tokens

import androidx.compose.foundation.layout.sizeIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyled.classes.StyledLocalMissingException

/** Minimum interactive dimensions and default icon size for the active platform. */
@Immutable
data class StyledSizes(

    // Interactive Sizes
    val minimumInteractiveSize: Dp,
    val minimumInteractiveSmallSize: Dp,

    // Icon Size
    val iconSize: Dp,
)

/** Enforces the theme's minimum interactive width and height on this modifier. */
@Composable
fun Modifier.minimumInteractiveSize(small: Boolean = false) = this.then(
    Modifier
        .sizeIn(
            minWidth = if (small) LocalStyledSizes.current.minimumInteractiveSmallSize else LocalStyledSizes.current.minimumInteractiveSize,
            minHeight = if (small) LocalStyledSizes.current.minimumInteractiveSmallSize else LocalStyledSizes.current.minimumInteractiveSize
        )
)

internal val LocalStyledSizes = staticCompositionLocalOf<StyledSizes> {
    throw StyledLocalMissingException(
        "LocalStyledSizes"
    )
}
