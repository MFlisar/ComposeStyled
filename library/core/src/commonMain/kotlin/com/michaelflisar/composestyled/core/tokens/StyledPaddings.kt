package com.michaelflisar.composestyled.core.tokens

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Definiert die Standard-Paddings für das Designsystem.
 */
@Immutable
data class StyledPaddings(
    val small: Dp = 4.dp,
    val medium: Dp = 8.dp,
    val large: Dp = 16.dp,
    val content: Dp = 16.dp
)

internal val LocalStyledPaddings = staticCompositionLocalOf { StyledPaddings() }
