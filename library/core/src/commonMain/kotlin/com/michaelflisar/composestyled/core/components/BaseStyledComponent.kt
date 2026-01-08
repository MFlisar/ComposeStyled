package com.michaelflisar.composestyled.core.components

import androidx.compose.runtime.Composable
import com.composeunstyled.theme.ThemeBuilder
import com.michaelflisar.composestyled.core.tokens.StyledColors

abstract class BaseStyledComponent() {

    @Composable
    internal abstract fun registerStyle(
        builder: ThemeBuilder,
        colors: StyledColors,
    )

}