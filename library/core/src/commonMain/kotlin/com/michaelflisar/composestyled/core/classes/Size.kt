package com.michaelflisar.composestyled.core.classes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import com.michaelflisar.composestyled.core.StyledTheme

enum class Size {
    Small,
    Normal,
    Large;

    @Composable
    @ReadOnlyComposable
    fun minimumInteractiveSize() = when (this) {
        Small -> StyledTheme.sizes.minimumInteractiveSizeSmall
        Normal -> StyledTheme.sizes.minimumInteractiveSize
        Large -> StyledTheme.sizes.minimumInteractiveSizeLarge
    }
}