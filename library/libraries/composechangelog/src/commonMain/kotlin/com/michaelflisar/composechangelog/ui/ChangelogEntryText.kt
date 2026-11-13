package com.michaelflisar.composechangelog.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.michaelflisar.composestyled.tokens.LocalStyledContentColor
import com.michaelflisar.composestyled.tokens.LocalStyledTextStyle
import com.michaelflisar.composestyled.ui.primitives.StyledText

/** Renders a changelog entry using the setup provided by its surrounding [ChangelogContent]. */
@Composable
fun ChangelogEntryText(
    text: String,
) {
    val setup = LocalChangelogSetup.current
    val formattedText = setup.textFormatter(text)
    val textStyle = LocalStyledTextStyle.current
    val contentColor = LocalStyledContentColor.current
    val fontSize = textStyle.fontSize
    val bulletSize = with(LocalDensity.current) {
        fontSize.toDp() * 0.32f
    }
    val inlineWidth = fontSize * 0.62f
    val inlineHeight = fontSize * 0.32f
    val inlineContent = remember(bulletSize, inlineWidth, inlineHeight, contentColor) {
        mapOf(
            ChangelogBulletContentId to InlineTextContent(
                placeholder = Placeholder(
                    width = inlineWidth,
                    height = inlineHeight,
                    placeholderVerticalAlign = PlaceholderVerticalAlign.Center,
                ),
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    Box(
                        modifier = Modifier
                            .size(bulletSize)
                            .background(contentColor, CircleShape),
                    )
                }
            },
        )
    }
    StyledText(
        text = buildAnnotatedString {
            withStyle(
                ParagraphStyle(
                    textIndent = TextIndent(
                        firstLine = 0.sp,
                        restLine = inlineWidth,
                    ),
                ),
            ) {
                appendInlineContent(ChangelogBulletContentId)
                append(formattedText)
            }
        },
        inlineContent = inlineContent,
        style = textStyle,
    )
}