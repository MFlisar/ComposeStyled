package com.michaelflisar.composestyled.core.runtime

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
        LocalBackgroundColor provides (backgroundColor.takeIf { it != Color.Transparent || applyTransparentBackgroundColor } ?: LocalBackgroundColor.current),
        LocalTextStyle provides (textStyle ?: LocalTextStyle.current),
        content = content
    )
}
