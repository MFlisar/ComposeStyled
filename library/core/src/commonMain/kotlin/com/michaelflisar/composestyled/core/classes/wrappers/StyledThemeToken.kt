package com.michaelflisar.composestyled.core.classes.wrappers

import androidx.compose.runtime.Immutable
import com.composeunstyled.theme.ThemeToken

@Immutable
class StyledThemeToken<T>(
    id: String,
) {
    // used to store/retrieve the token in the compose unstyled theme
    internal val token = ThemeToken<T>(id)


}