package com.michaelflisar.composestyled.shared

import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import com.composeunstyled.Text
import com.michaelflisar.composestyled.core.components.StyledTextRenderer
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.core.runtime.LocalTextStyle

@InternalComposeStyledApi
abstract class UnstyledTextRenderer : StyledTextRenderer.TokenRenderer {

    @Composable
    override fun render(
        text: String,
        modifier: Modifier,
        style: TextStyle,
        textAlign: TextAlign,
        lineHeight: TextUnit,
        fontSize: TextUnit,
        letterSpacing: TextUnit,
        fontWeight: FontWeight?,
        color: Color,
        fontFamily: FontFamily?,
        singleLine: Boolean,
        minLines: Int,
        maxLines: Int,
        onTextLayout: ((TextLayoutResult) -> Unit)?,
        overflow: TextOverflow,
        autoSize: TextAutoSize?
    ) {
        // merge style overrides (renderer receives both `style` and explicit fields)
        val mergedStyle = LocalTextStyle.current.merge(style).copy(
            textAlign = textAlign,
            lineHeight = lineHeight,
            fontSize = fontSize,
            letterSpacing = letterSpacing,
            fontWeight = fontWeight,
            fontFamily = fontFamily,
        )

        Text(
            text = text,
            modifier = modifier,
            style = mergedStyle,
            textAlign = textAlign,
            lineHeight = lineHeight,
            fontSize = fontSize,
            letterSpacing = letterSpacing,
            fontWeight = fontWeight,
            color = color,
            fontFamily = fontFamily,
            singleLine = singleLine,
            minLines = minLines,
            maxLines = maxLines,
            overflow = overflow,
            autoSize = autoSize,
            onTextLayout = onTextLayout,
        )
    }
}