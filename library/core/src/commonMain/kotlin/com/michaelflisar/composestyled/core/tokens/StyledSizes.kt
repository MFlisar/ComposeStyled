package com.michaelflisar.composestyled.core.tokens

import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.core.StyledTheme

/**
 * Definiert die Standard-Sizes für das Designsystem.
 */
@Immutable
data class StyledSizes(
    val minimumInteractiveSize: Dp = 48.dp,
    val minimumInteractiveSizeSmall: Dp = 40.dp,
    val iconSize: Dp = 24.dp,
)

@Composable
fun Modifier.minimumInteractiveComponentSize() = this.then(
    Modifier
        .sizeIn(
            minWidth = StyledTheme.sizes.minimumInteractiveSize,
            minHeight = StyledTheme.sizes.minimumInteractiveSize
        )
)

internal val LocalStyledSizes = staticCompositionLocalOf { StyledSizes() }
