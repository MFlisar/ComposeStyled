package com.michaelflisar.composestyled.tokens

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

/** The content color currently provided by the active Styled theme or local override. */
val LocalStyledContentColor =
    compositionLocalOf<Color> { error("No content color provided") }