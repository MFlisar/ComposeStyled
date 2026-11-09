package com.michaelflisar.composestyled.material3

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.michaelflisar.composestyled.UpdateStyledThemeLocals
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.tokens.LocalStyledContentColor
import com.michaelflisar.composestyled.tokens.LocalStyledTextStyle

@OptIn(InternalComposeStyledApi::class)
@Composable
internal fun ProvideStyledLocals(
    contentColor: Color? = null,
    textStyle: TextStyle? = null,
    content: @Composable () -> Unit,
) {
    val resolvedContentColor = contentColor
        ?.takeUnless { it == Color.Unspecified }
        ?: LocalStyledContentColor.current
    val resolvedTextStyle = textStyle ?: LocalStyledTextStyle.current
    CompositionLocalProvider(
        LocalContentColor provides resolvedContentColor,
        LocalTextStyle provides resolvedTextStyle,
    ) {
        UpdateStyledThemeLocals(
            contentColor = resolvedContentColor,
            textStyle = resolvedTextStyle,
            content = content,
        )
    }
}

@Composable
internal fun CopyStyledLocals(
    outline: Color? = null,
    outlineVariant: Color? = null,
    contentColor: Color? = null,
    textStyle: TextStyle? = null,
    content: @Composable () -> Unit,
) {
    ProvideStyledLocals(contentColor, textStyle) {
        if (outline == null && outlineVariant == null) {
            content()
        } else {
            MaterialTheme(
                colorScheme = MaterialTheme.colorScheme.let {
                    it.copy(
                        outline = outline ?: it.outline,
                        outlineVariant = outlineVariant ?: it.outlineVariant,
                    )
                }
            ) {
                content()
            }
        }
    }
}