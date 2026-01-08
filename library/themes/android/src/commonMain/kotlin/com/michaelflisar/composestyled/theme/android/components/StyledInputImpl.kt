package com.michaelflisar.composestyled.theme.android.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyled.core.components.StyledInputConfig
import com.michaelflisar.composestyled.core.runtime.LocalContentColor
import com.michaelflisar.composestyled.core.runtime.ProvideStyledLocals

@Composable
internal fun StyledInputImpl(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier,
    config: StyledInputConfig,
    enabled: Boolean,
    readOnly: Boolean,
    textStyle: TextStyle,
    label: @Composable (() -> Unit)?,
    placeholder: @Composable (() -> Unit)?,
    leadingIcon: @Composable (() -> Unit)?,
    trailingIcon: @Composable (() -> Unit)?,
    prefix: @Composable (() -> Unit)?,
    suffix: @Composable (() -> Unit)?,
    supportingText: @Composable (() -> Unit)?,
    isError: Boolean,
    visualTransformation: VisualTransformation,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    singleLine: Boolean,
    maxLines: Int,
    minLines: Int,
    interactionSource: MutableInteractionSource?,
    shape: Shape,
) {
    ProvideStyledLocals(
        contentColor = config.contentColor(enabled),
        backgroundColor = config.containerColor(enabled),
        textStyle = textStyle
    ) {
        Row(
            modifier
                .clip(shape)
                .then(if (variant is StyledInputVariant.Filled) Modifier.background(container) else Modifier)
                .then(if (border != null) Modifier.border(Dp.Hairline, border, shape) else Modifier)
                .padding(contentPadding)
        ) {
            BasicTextField(
                value = value,
                onValueChange = { if (!readOnly && enabled) onValueChange(it) },
                enabled = enabled,
                readOnly = readOnly,
                textStyle = textStyle.copy(color = LocalContentColor.current),
                interactionSource = remember { MutableInteractionSource() },
                modifier = Modifier.weight(1f),
                decorationBox = { inner ->
                    if (value.isEmpty() && placeholder != null)
                        StyledText(
                            text = placeholder,
                            style = textStyle,
                            color = LocalContentColor.current.copy(alpha = .55f)
                        )
                    inner()
                }
            )
        }
    }
}
