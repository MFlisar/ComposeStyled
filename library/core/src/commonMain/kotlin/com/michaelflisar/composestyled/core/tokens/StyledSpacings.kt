package com.michaelflisar.composestyled.core.tokens

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Definiert die Standard-Shapes für das Designsystem.
 */
@Immutable
data class StyledSpacings(
    val small: Dp = 4.dp,
    val medium: Dp = 8.dp,
    val large: Dp = 16.dp,
)

internal val LocalStyledSpacings = staticCompositionLocalOf { StyledSpacings() }
