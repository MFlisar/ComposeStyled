package com.michaelflisar.composestyled.core.tokens

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Definiert die Standard-Sizes für das Designsystem.
 */
@Immutable
data class StyledSizes(
    val minimumInteractiveSize: Dp = 48.dp,
    val minimumInteractiveSizeSmall: Dp = 40.dp,
)

internal val LocalStyledSizes = staticCompositionLocalOf { StyledSizes() }
