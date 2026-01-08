package com.michaelflisar.composestyled.core.components

import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import com.michaelflisar.composestyled.core.renderer.LocalStyledComponents

@Composable
fun StyledText(
    text: String,
    modifier: Modifier,
    style: TextStyle,
    color: Color,
    overflow: TextOverflow,
    softWrap: Boolean,
    maxLines: Int,
    minLines: Int,
    autoSize: TextAutoSize,
) {
    LocalStyledComponents.current.Text(
        text = text,
        modifier = modifier,
        style = style,
        color = color,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        autoSize = autoSize
    )
}

