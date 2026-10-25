package com.michaelflisar.composestyledcomponents.tokens

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

internal val LocalStyledContentColor =
    compositionLocalOf<Color> { error("No content color provided") }