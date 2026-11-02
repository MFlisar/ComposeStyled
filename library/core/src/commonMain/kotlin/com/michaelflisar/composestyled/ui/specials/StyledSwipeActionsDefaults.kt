package com.michaelflisar.composestyled.ui.specials

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.StyledTheme

object StyledSwipeActionsDefaults {
    val threshold: Dp = 56.dp

    val shape: Shape
        @Composable get() = StyledTheme.shapes.medium

    val backgroundColor: Color
        @Composable get() = StyledTheme.colorScheme.surface

    val contentColor: Color
        @Composable get() = StyledTheme.colorScheme.onSurface

    val revealBackgroundColor: Color
        @Composable get() = StyledTheme.colorScheme.surfaceVariant

    val revealContentColor: Color
        @Composable get() = StyledTheme.colorScheme.onSurfaceVariant

    val contentPadding: PaddingValues
        @Composable get() = PaddingValues(all = StyledTheme.padding.medium)

    val contentAlignment: Alignment = Alignment.CenterStart

    val actionSpacing: Dp = 0.dp

    val overscrollBehavior: StyledSwipeOverscrollBehavior =
        StyledSwipeOverscrollBehavior.None
}
