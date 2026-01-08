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
import com.composeunstyled.theme.ThemeBuilder
import com.composeunstyled.theme.ThemeProperty
import com.composeunstyled.theme.ThemeToken
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.renderer.LocalStyledComponents
import com.michaelflisar.composestyled.core.tokens.StyledColors

object StyledText : BaseStyledComponent {

    internal val Property = ThemeProperty<Color>("text")

    internal val TokenDefault = ThemeToken<Color>("text.default")

    sealed class Variant {
        companion object {
            val Default: Variant = Token(TokenDefault)
            fun custom(color: Color): Variant = Custom(color)
        }

        internal data class Token(val token: ThemeToken<Color>) : Variant()
        internal data class Custom(val color: Color) : Variant()
    }

    override fun registerStyle(builder: ThemeBuilder, colors: StyledColors) {
        with(builder) {
            properties[Property] = mapOf(
                TokenDefault to colors.onBackground
            )
        }
    }
}

/** Defaults for [StyledText]. */
object StyledTextDefaults {
    /** Default text color from the current [StyledTheme]. */
    @Composable
    fun color(): Color = StyledTheme.colors.onBackground
}

/**
 * Styled text.
 *
 * Public API stays renderer-agnostic. Rendering is delegated via `LocalStyledComponents`.
 */
@Composable
@Suppress("UNUSED_PARAMETER")
fun StyledText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = StyledTheme.typography.bodyMedium,
    textAlign: TextAlign = TextAlign.Unspecified,
    lineHeight: TextUnit = TextUnit.Unspecified,
    fontSize: TextUnit = style.fontSize,
    letterSpacing: TextUnit = style.letterSpacing,
    fontWeight: FontWeight? = style.fontWeight,
    color: Color = Color.Unspecified,
    fontFamily: FontFamily? = style.fontFamily,
    singleLine: Boolean = false,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
    overflow: TextOverflow = TextOverflow.Clip,
    autoSize: TextAutoSize? = null,
) {
    // Build the final style from the provided parameters.
    val resolvedStyle = style.copy(
        textAlign = textAlign,
        lineHeight = lineHeight,
        fontSize = fontSize,
        letterSpacing = letterSpacing,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
    )

    val resolvedColor = if (color == Color.Unspecified) StyledTheme.colors.onBackground else color

    LocalStyledComponents.current.Text(
        text = text,
        modifier = modifier,
        style = resolvedStyle,
        textAlign = textAlign,
        lineHeight = lineHeight,
        fontSize = fontSize,
        letterSpacing = letterSpacing,
        fontWeight = fontWeight,
        color = resolvedColor,
        fontFamily = fontFamily,
        singleLine = singleLine,
        minLines = minLines,
        maxLines = maxLines,
        onTextLayout = onTextLayout,
        overflow = overflow,
        autoSize = autoSize,
    )
}
