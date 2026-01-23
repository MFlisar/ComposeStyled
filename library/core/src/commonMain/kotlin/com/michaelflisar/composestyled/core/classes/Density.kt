package com.michaelflisar.composestyled.core.classes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyled.core.StyledTheme

enum class Density {
    Normal,
    Dense;

    @Composable
    @ReadOnlyComposable
    fun dp(size: Dp) = when (this) {
        Dense -> size * StyledTheme.sizes.factorDensitySmall
        Normal -> size
    }
}