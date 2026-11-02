package com.michaelflisar.composestyled.tokens

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyled.classes.StyledLocalMissingException

internal val LocalStyledThemeName = staticCompositionLocalOf<String> { throw StyledLocalMissingException("LocalStyledThemeName") }
