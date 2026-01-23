package com.michaelflisar.composestyled.core.components

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
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.renderer.LocalStyledComponents
import com.michaelflisar.composestyled.core.runtime.LocalStyledContentColor

object StyledText {

    // variants
    enum class Variant {
        Default
    }
}

// ----------------------
// Renderer
// ----------------------

sealed interface StyledTextRenderer {

    interface TokenRenderer : StyledTextRenderer {

        @Composable
        fun render(
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
            autoSize: TextAutoSize?,
        )
    }

    interface WrapperRenderer : StyledTextRenderer {

        @Composable
        fun render(
            variant: StyledText.Variant,
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
            autoSize: TextAutoSize?,
        )
    }

}

// ----------------------
// Defaults
// ----------------------

object StyledTextDefaults {

    val variant: StyledText.Variant = StyledText.Variant.Default

}

// ----------------------
// Composables
// ----------------------

@Composable
fun StyledText(
    text: String,
    modifier: Modifier = Modifier,
    variant: StyledText.Variant = StyledTextDefaults.variant,
    style: TextStyle = StyledTheme.currentTextStyle,
    textAlign: TextAlign = TextAlign.Unspecified,
    lineHeight: TextUnit = TextUnit.Unspecified,
    fontSize: TextUnit = style.fontSize,
    letterSpacing: TextUnit = style.letterSpacing,
    fontWeight: FontWeight? = style.fontWeight,
    color: Color = LocalStyledContentColor.current,
    fontFamily: FontFamily? = style.fontFamily,
    singleLine: Boolean = false,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
    overflow: TextOverflow = TextOverflow.Clip,
    autoSize: TextAutoSize? = null,
) {
    val resolvedStyle = style.copy(
        textAlign = textAlign,
        lineHeight = lineHeight,
        fontSize = fontSize,
        letterSpacing = letterSpacing,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
    )

    when (val renderer = LocalStyledComponents.current.text) {
        is StyledTextRenderer.TokenRenderer -> {
            renderer.render(
                text = text,
                modifier = modifier,
                style = resolvedStyle,
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
                onTextLayout = onTextLayout,
                overflow = overflow,
                autoSize = autoSize,
            )
        }

        is StyledTextRenderer.WrapperRenderer -> {
            renderer.render(
                variant = variant,
                text = text,
                modifier = modifier,
                style = resolvedStyle,
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
                onTextLayout = onTextLayout,
                overflow = overflow,
                autoSize = autoSize,
            )
        }
    }
}
