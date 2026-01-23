package com.michaelflisar.composestyled.core.tokens

import androidx.compose.foundation.layout.sizeIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.core.classes.Size

@Immutable
data class StyledSizes(
    val minimumInteractiveSize: Dp = 48.dp,
    val minimumInteractiveSizeSmall: Dp = 40.dp,
    val minimumInteractiveSizeLarge: Dp = 56.dp,
    val iconSize: Dp = 24.dp,
    val factorSizeSmall: Float = .9f,
    val factorSizeLarge: Float = 1.1f,
    val factorDensitySmall: Float = .75f,
)

@Composable
fun Modifier.minimumInteractiveComponentSize(size: Size = Size.Normal) = this.then(
    Modifier
        .sizeIn(
            minWidth = size.minimumInteractiveSize(),
            minHeight = size.minimumInteractiveSize()
        )
)

internal val LocalStyledSizes = staticCompositionLocalOf { StyledSizes() }
