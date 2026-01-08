package com.michaelflisar.composestyled.core.components

import com.composeunstyled.theme.ThemeBuilder
import com.michaelflisar.composestyled.core.tokens.StyledColors

/**
 * Internal hook used by [com.michaelflisar.composestyled.core.StyledTheme] to register component styles
 * into the compose-unstyled [ThemeBuilder].
 *
 */
internal interface BaseStyledComponent {

    fun registerStyle(
        builder: ThemeBuilder,
        colors: StyledColors,
    )

}