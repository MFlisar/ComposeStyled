package com.michaelflisar.composestyled.core.components

import androidx.compose.runtime.Composable
import com.composeunstyled.theme.ThemeBuilder
import com.michaelflisar.composestyled.core.tokens.StyledColors

internal object ComponentRegistry {

    @Composable
    fun registerAll(
        builder: ThemeBuilder,
        colors: StyledColors,
    ) {
        StyledButton.registerStyle(builder, colors)
        StyledInput.registerStyle(builder, colors)
        // ...
    }
}