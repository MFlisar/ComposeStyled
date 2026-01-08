package com.michaelflisar.composestyled.core.tokens

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Definiert die Standard-Shapes für das Designsystem.
 */
@Immutable
data class StyledShapes(
    val extraSmall: Dp = 4.dp,
    val small: Dp = 8.dp,
    val medium: Dp = 12.dp,
    val large: Dp = 16.dp,
    val extraLarge: Dp = 24.dp,
    val button: Shape = RoundedCornerShape(size = small),
    val card: Shape = RoundedCornerShape(size = medium),
    val input: Shape = RoundedCornerShape(size = extraSmall),
)

internal val LocalStyledShapes = staticCompositionLocalOf { StyledShapes() }
