package com.michaelflisar.composestyled.core.runtime

import androidx.compose.runtime.compositionLocalOf
import com.composeunstyled.theme.ThemeBuilder

internal val LocalThemeBuilder = compositionLocalOf<ThemeBuilder> { error("No ThemeBuilder provided") }