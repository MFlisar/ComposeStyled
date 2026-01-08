package com.michaelflisar.composestyled.theme.material3.components

import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import com.michaelflisar.composestyled.core.runtime.LocalTextStyle

/**
 * Material3-based StyledText implementation.
 *
 * Uses Material3 `Text` but stays driven by `StyledTheme`.
 */
@Composable
internal fun StyledTextImpl(
    text: String,
    modifier: Modifier,
    style: TextStyle,
    color: Color,
    overflow: TextOverflow,
    softWrap: Boolean,
    maxLines: Int,
    minLines: Int,
    autoSize: TextAutoSize?,
) {
    Text(
        text = text,
        modifier = modifier,
        style = LocalTextStyle.current.merge(style),
        color = color,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        autoSize = autoSize
    )
}
