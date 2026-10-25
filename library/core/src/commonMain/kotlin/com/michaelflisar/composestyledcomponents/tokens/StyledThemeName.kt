package com.michaelflisar.composestyledcomponents.tokens

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyledcomponents.classes.StyledLocalMissingException

internal val LocalStyledThemeName = staticCompositionLocalOf<String> { throw StyledLocalMissingException("LocalStyledThemeName") }
