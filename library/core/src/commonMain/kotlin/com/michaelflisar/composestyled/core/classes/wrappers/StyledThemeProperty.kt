package com.michaelflisar.composestyled.core.classes.wrappers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.composeunstyled.theme.ThemeProperty

@Immutable
class StyledThemeProperty<P>(
    id: String,
) {
    // used to store/retrieve the property in the compose unstyled theme
    internal val property = ThemeProperty<P>(id)
}