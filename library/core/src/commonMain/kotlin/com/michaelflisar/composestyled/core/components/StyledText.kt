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
import com.composeunstyled.theme.ThemeProperty
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.classes.IVariantId
import com.michaelflisar.composestyled.core.classes.TokenMap
import com.michaelflisar.composestyled.core.classes.Variant
import com.michaelflisar.composestyled.core.classes.customDataOrNull
import com.michaelflisar.composestyled.core.renderer.LocalStyledComponents
import com.michaelflisar.composestyled.core.renderer.StyledTokenCompontents
import com.michaelflisar.composestyled.core.renderer.StyledTokenRenderer
import com.michaelflisar.composestyled.core.renderer.StyledWrapperComponents
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.core.runtime.LocalContentColor

typealias StyledTextVariant = Variant<StyledText.VariantId, Color>

object StyledText {

    // properties
    private val Property = ThemeProperty<Color>("text")

    // variant ids
    enum class VariantId(
        override val id: String,
    ) : IVariantId {
        Default("text.default"),
    }

    // variants
    object Variants {
        val Default: StyledTextVariant = Variant.Token(VariantId.Default)
        fun custom(color: Color) = Variant.Custom(VariantId.Default, color)
    }

    // tokens
    internal val Tokens = TokenMap.create<VariantId, Color>(Property)

    @InternalComposeStyledApi
    @Composable
    fun registerVariantStyles(
        default: Color,
    ) {
        Tokens.registerStyles(
            styles = mapOf(
                VariantId.Default to default,
            )
        )
    }
}

// ----------------------
// Renderer
// ----------------------

interface StyledTextTokenRenderer : StyledTokenRenderer {

    @Composable
    fun Render(
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

interface StyledTextWrapperRenderer {

    @Composable
    fun Render(
        request: Request,
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

    data class Request(
        val variant: StyledText.VariantId,
        val customColor: Color?,
    )
}

// ----------------------
// Defaults
// ----------------------

object StyledTextDefaults {

    val DefaultVariant: StyledTextVariant = StyledText.Variants.Default

    val Style: TextStyle
        @Composable get() = StyledTheme.typography.bodyMedium
}

// ----------------------
// Composables
// ----------------------

@Composable
fun StyledText(
    text: String,
    modifier: Modifier = Modifier,
    variant: StyledTextVariant = StyledTextDefaults.DefaultVariant,
    style: TextStyle = StyledTextDefaults.Style,
    textAlign: TextAlign = TextAlign.Unspecified,
    lineHeight: TextUnit = TextUnit.Unspecified,
    fontSize: TextUnit = style.fontSize,
    letterSpacing: TextUnit = style.letterSpacing,
    fontWeight: FontWeight? = style.fontWeight,
    color: Color = LocalContentColor.current,
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

    when (val components = LocalStyledComponents.current) {
        is StyledTokenCompontents -> {
            val themedColor = StyledText.Tokens.resolveVariantData(variant)
            val finalColor = if (color != Color.Unspecified) color else themedColor
            components.text.Render(
                text = text,
                modifier = modifier,
                style = resolvedStyle,
                textAlign = textAlign,
                lineHeight = lineHeight,
                fontSize = fontSize,
                letterSpacing = letterSpacing,
                fontWeight = fontWeight,
                color = finalColor,
                fontFamily = fontFamily,
                singleLine = singleLine,
                minLines = minLines,
                maxLines = maxLines,
                onTextLayout = onTextLayout,
                overflow = overflow,
                autoSize = autoSize,
            )
        }

        is StyledWrapperComponents -> {
            components.text.Render(
                request = StyledTextWrapperRenderer.Request(
                    variant = variant.variantId,
                    customColor = variant.customDataOrNull(),
                ),
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
