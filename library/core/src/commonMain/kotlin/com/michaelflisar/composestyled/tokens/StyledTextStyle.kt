package com.michaelflisar.composestyled.tokens

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.TextStyle

/** The text style currently provided by the active Styled theme or local override. */
val LocalStyledTextStyle = compositionLocalOf<TextStyle> { error("No text style provided") }