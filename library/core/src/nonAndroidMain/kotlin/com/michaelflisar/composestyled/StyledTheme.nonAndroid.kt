package com.michaelflisar.composestyled

import androidx.compose.runtime.Composable
import com.michaelflisar.composestyled.themes.StyledThemeColors

actual val StyledTheme.isDynamicColorsSupported: Boolean
    get() = false

actual val StyledTheme.isSystemContrastSupported: Boolean
    get() = false

@Composable
internal actual fun platformDynamicThemeColors(): StyledThemeColors? = null
