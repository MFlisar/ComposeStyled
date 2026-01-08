package com.michaelflisar.composestyled.core.runtime

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.InternalComposeApi
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

val LocalBackgroundColor = compositionLocalOf<Color> { error("No background color provided") }
val LocalContentColor = compositionLocalOf<Color> { error("No content color provided") }
val LocalTextStyle = compositionLocalOf<TextStyle> { error("No text style provided") }

@Composable
fun ProvideContentColor(color: Color, content: @Composable () -> Unit) =
    CompositionLocalProvider(LocalContentColor provides color, content = content)

@Composable
fun ProvideBackgroundColor(color: Color, content: @Composable () -> Unit) =
    CompositionLocalProvider(LocalBackgroundColor provides color, content = content)

@Composable
fun ProvideTextStyle(style: TextStyle, content: @Composable () -> Unit) =
    CompositionLocalProvider(
        LocalTextStyle provides LocalTextStyle.current.merge(style),
        content = content
    )

/**
 * Provides multiple styled locals at once.
 *
 * @param contentColor The content color to provide. If null, the current content color is used.
 * @param backgroundColor The background color to provide. If null, the current background color is used.
 * @param textStyle The text style to provide. If null, the current text style is used.
 * @param indication The indication to provide. If null, the current indication is used.
 * @param applyTransparentBackgroundColor If true, allows providing a transparent background color.
 * @param content The composable content that will have access to the provided locals.
 *
 * only public because of usage in other modules, should not be used directly elsewhere
 *
 */
@InternalComposeApi
@Composable
fun ProvideStyledLocals(
    contentColor: Color? = null,
    backgroundColor: Color? = null,
    textStyle: TextStyle? = null,
    applyTransparentBackgroundColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalContentColor provides (contentColor ?: LocalContentColor.current),
        LocalBackgroundColor provides (backgroundColor.takeIf { it != Color.Transparent || applyTransparentBackgroundColor }
            ?: LocalBackgroundColor.current),
        LocalTextStyle provides (textStyle ?: LocalTextStyle.current),
        content = content
    )
}
