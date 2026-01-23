package com.michaelflisar.composestyled.core.runtime

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

internal val LocalStyledBackgroundColor =
    compositionLocalOf<Color> { error("No background color provided") }
internal val LocalStyledContentColor =
    compositionLocalOf<Color> { error("No content color provided") }
internal val LocalStyledTextStyle =
    compositionLocalOf<TextStyle> { error("No text style provided") }

/**
 * Provides multiple styled locals at once.
 *
 * @param contentColor The content color to provide. If null, the current content color is used.
 * @param backgroundColor The background color to provide. If null, the current background color is used.
 * @param textStyle The text style to provide. If null, the current text style is used.
 * @param applyTransparentBackgroundColor If true, allows providing a transparent background color.
 * @param content The composable content that will have access to the provided locals.
 *
 * only public because of usage in other modules, should not be used directly elsewhere
 *
 */
@InternalComposeStyledApi
@Composable
fun ProvideStyledLocals(
    contentColor: Color? = null,
    backgroundColor: Color? = null,
    textStyle: TextStyle? = null,
    applyTransparentBackgroundColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalStyledContentColor provides (contentColor ?: LocalStyledContentColor.current),
        LocalStyledBackgroundColor provides (backgroundColor.takeIf { it != Color.Transparent || applyTransparentBackgroundColor }
            ?: LocalStyledBackgroundColor.current),
        LocalStyledTextStyle provides (textStyle ?: LocalStyledTextStyle.current),
        content = content
    )
}
