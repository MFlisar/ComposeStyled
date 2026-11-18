package com.michaelflisar.composestyled.utils.text

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import com.michaelflisar.composestyled.tokens.StyledColorScheme

private enum class MarkupKind {
    Bold,
    Italic,
    Underline,
    Color,
}

private data class MarkupTag(
    val kind: MarkupKind,
    val colorName: String? = null,
    val closing: Boolean = false,
    val genericColorClose: Boolean = false,
)

private data class MarkupToken(
    val start: Int,
    val end: Int,
    val tag: MarkupTag,
)

private data class StyledRange(
    val start: Int,
    val end: Int,
    val style: SpanStyle,
)

/**
 * Parses supported text markup into an [AnnotatedString].
 *
 * Supports nested `<b>`, `<i>`, and `<u>` tags, plus named color tags when [colors] contains
 * their names. Color tags can use either `<name>text</name>` or `<color=name>text</color>`.
 */
fun parseAnnotatedText(
    text: String,
    colors: Map<String, Color> = emptyMap(),
): AnnotatedString {
    validateColorNames(colors.keys)

    val tokens = findMarkupTokens(text, colors.keys)
    val pairedTokens = BooleanArray(tokens.size)
    val pairs = IntArray(tokens.size) { -1 }
    val stack = mutableListOf<Int>()

    tokens.forEachIndexed { index, token ->
        if (!token.tag.closing) {
            stack += index
        } else {
            val openingIndex = stack.lastOrNull()
            if (openingIndex != null && tagsMatch(tokens[openingIndex].tag, token.tag)) {
                stack.removeAt(stack.lastIndex)
                pairedTokens[openingIndex] = true
                pairedTokens[index] = true
                pairs[openingIndex] = index
                pairs[index] = openingIndex
            } else {
                stack.clear()
            }
        }
    }

    val styledRanges = mutableListOf<StyledRange>()
    val outputStarts = mutableMapOf<Int, Int>()
    return buildAnnotatedString {
        var sourceIndex = 0
        tokens.forEachIndexed { index, token ->
            if (!pairedTokens[index]) return@forEachIndexed

            append(text.substring(sourceIndex, token.start))
            sourceIndex = token.end
            if (!token.tag.closing) {
                outputStarts[index] = length
            } else {
                val openingIndex = pairs[index]
                val openingTag = tokens[openingIndex].tag
                val start = outputStarts.getValue(openingIndex)
                if (start < length) {
                    styledRanges += StyledRange(
                        start = start,
                        end = length,
                        style = openingTag.toSpanStyle(colors),
                    )
                }
            }
        }
        append(text.substring(sourceIndex))

        styledRanges
            .sortedWith(compareBy<StyledRange> { it.start }.thenByDescending { it.end })
            .forEach { addStyle(it.style, it.start, it.end) }
    }
}

/**
 * Parses text markup using every semantic color exposed by [colorScheme].
 *
 * [colors] adds custom names and overrides matching names from [colorScheme].
 */
fun parseAnnotatedText(
    text: String,
    colorScheme: StyledColorScheme,
    colors: Map<String, Color> = emptyMap(),
): AnnotatedString = parseAnnotatedText(
    text = text,
    colors = colorScheme.namedColors() + colors,
)

private fun validateColorNames(colorNames: Set<String>) {
    colorNames.forEach { name ->
        require(
            name.isNotBlank() &&
                name !in setOf("b", "i", "u", "color") &&
                name.none { it.isWhitespace() || it in "<>/=" },
        ) {
            "Color tag names must be non-blank, non-reserved, and contain no whitespace or tag delimiters."
        }
    }
}

private fun findMarkupTokens(
    text: String,
    colorNames: Set<String>,
): List<MarkupToken> {
    val tokens = mutableListOf<MarkupToken>()
    var index = 0
    while (index < text.length) {
        if (text[index] != '<') {
            index++
            continue
        }

        val tagEnd = text.indexOf('>', startIndex = index + 1)
        if (tagEnd < 0) {
            index++
            continue
        }

        val tag = parseMarkupTag(text.substring(index + 1, tagEnd), colorNames)
        if (tag != null) {
            tokens += MarkupToken(
                start = index,
                end = tagEnd + 1,
                tag = tag,
            )
            index = tagEnd + 1
        } else {
            index++
        }
    }
    return tokens
}

private fun parseMarkupTag(
    value: String,
    colorNames: Set<String>,
): MarkupTag? {
    val closing = value.startsWith('/')
    val name = if (closing) value.drop(1) else value
    return when (name) {
        "b" -> MarkupTag(MarkupKind.Bold, closing = closing)
        "i" -> MarkupTag(MarkupKind.Italic, closing = closing)
        "u" -> MarkupTag(MarkupKind.Underline, closing = closing)
        "color" -> if (closing) {
            MarkupTag(MarkupKind.Color, closing = true, genericColorClose = true)
        } else {
            null
        }
        else -> when {
            !closing && name.startsWith("color=") -> {
                val colorName = name.removePrefix("color=")
                if (colorName in colorNames) MarkupTag(MarkupKind.Color, colorName) else null
            }
            name in colorNames -> MarkupTag(MarkupKind.Color, colorName = name, closing = closing)
            else -> null
        }
    }
}

private fun tagsMatch(
    openingTag: MarkupTag,
    closingTag: MarkupTag,
): Boolean =
    openingTag.kind == closingTag.kind &&
        (closingTag.genericColorClose || openingTag.colorName == closingTag.colorName)

private fun MarkupTag.toSpanStyle(colors: Map<String, Color>): SpanStyle = when (kind) {
    MarkupKind.Bold -> SpanStyle(fontWeight = FontWeight.Bold)
    MarkupKind.Italic -> SpanStyle(fontStyle = FontStyle.Italic)
    MarkupKind.Underline -> SpanStyle(textDecoration = TextDecoration.Underline)
    MarkupKind.Color -> SpanStyle(color = requireNotNull(colors[colorName]))
}

private fun StyledColorScheme.namedColors(): Map<String, Color> = mapOf(
    "background" to background,
    "onBackground" to onBackground,
    "surfaceLowest" to surfaceLowest,
    "onSurfaceLowest" to onSurfaceLowest,
    "surfaceLow" to surfaceLow,
    "onSurfaceLow" to onSurfaceLow,
    "surface" to surface,
    "onSurface" to onSurface,
    "surfaceHigh" to surfaceHigh,
    "onSurfaceHigh" to onSurfaceHigh,
    "surfaceHighest" to surfaceHighest,
    "onSurfaceHighest" to onSurfaceHighest,
    "surfaceVariant" to surfaceVariant,
    "onSurfaceVariant" to onSurfaceVariant,
    "inverseSurface" to inverseSurface,
    "inverseOnSurface" to inverseOnSurface,
    "primary" to primary,
    "onPrimary" to onPrimary,
    "primarySurface" to primarySurface,
    "onPrimarySurface" to onPrimarySurface,
    "secondary" to secondary,
    "onSecondary" to onSecondary,
    "secondarySurface" to secondarySurface,
    "onSecondarySurface" to onSecondarySurface,
    "tertiary" to tertiary,
    "onTertiary" to onTertiary,
    "tertiarySurface" to tertiarySurface,
    "onTertiarySurface" to onTertiarySurface,
    "error" to error,
    "onError" to onError,
    "errorSurface" to errorSurface,
    "onErrorSurface" to onErrorSurface,
    "success" to success,
    "onSuccess" to onSuccess,
    "successSurface" to successSurface,
    "onSuccessSurface" to onSuccessSurface,
    "warning" to warning,
    "onWarning" to onWarning,
    "warningSurface" to warningSurface,
    "onWarningSurface" to onWarningSurface,
    "outline" to outline,
    "outlineVariant" to outlineVariant,
)
