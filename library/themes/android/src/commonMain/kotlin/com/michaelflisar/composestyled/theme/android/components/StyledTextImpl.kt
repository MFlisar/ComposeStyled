package com.michaelflisar.composestyled.theme.android.components

import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow

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
    BasicText(
        text = text,
        modifier = modifier,
        style = style.copy(color = color),
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        autoSize = autoSize
    )
}
