package com.michaelflisar.composestyled.core.components

import com.composeunstyled.theme.ThemeBuilder
import com.michaelflisar.composestyled.core.tokens.StyledColors

internal object ComponentRegistry {

    fun registerAll(
        builder: ThemeBuilder,
        colors: StyledColors,
    ) {
        StyledButton.registerStyle(builder, colors)
        StyledTextField.registerStyle(builder, colors)
        StyledText.registerStyle(builder, colors)
        StyledCard.registerStyle(builder, colors)
        StyledCheckbox.registerStyle(builder, colors)
        // ...
    }
}