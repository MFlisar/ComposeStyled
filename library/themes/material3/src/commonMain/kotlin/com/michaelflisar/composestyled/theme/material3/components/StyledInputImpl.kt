package com.michaelflisar.composestyled.theme.material3.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import com.michaelflisar.composestyled.core.components.StyledInputConfig
import com.michaelflisar.composestyled.core.runtime.LocalContentColor
import com.michaelflisar.composestyled.core.runtime.ProvideStyledLocals
import com.michaelflisar.composestyled.core.theme.StyledTheme

/**
 * Material3-based input.
 *
 * Maps core flags:
 * - enabled
 * - readOnly
 * - isError
 * - filled/outlined
 */
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
    val colors = StyledTheme.colors
    val shapes = StyledTheme.shapes

    ProvideStyledLocals(
        contentColor = if (enabled) colors.onBackground else colors.onBackground.copy(alpha = 0.55f),
        backgroundColor = colors.background,
        textStyle = textStyle
    ) {
        val ph: (@Composable () -> Unit)? = placeholder?.let {
            @Composable {
                StyledText(
                    text = it,
                    style = textStyle,
                    color = LocalContentColor.current.copy(alpha = 0.55f)
                )
            }
        }
        when (variant) {
            StyledInputVariant.Filled -> TextField(
                value = value,
                onValueChange = onValueChange,
                modifier = modifier,
                enabled = enabled,
                readOnly = readOnly,
                isError = isError,
                textStyle = textStyle,
                placeholder = ph,
                shape = RoundedCornerShape(shapes.medium),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = colors.onBackground,
                    unfocusedTextColor = colors.onBackground,
                    disabledTextColor = colors.onBackground.copy(alpha = 0.55f),
                    errorTextColor = colors.onBackground,
                    focusedContainerColor = colors.background,
                    unfocusedContainerColor = colors.background,
                    disabledContainerColor = colors.background,
                    errorContainerColor = colors.background,
                    focusedIndicatorColor = colors.primary,
                    unfocusedIndicatorColor = colors.border,
                    disabledIndicatorColor = colors.border.copy(alpha = 0.35f),
                    errorIndicatorColor = Color(0xFFDC2626),
                )
            )

            StyledInputVariant.Outlined -> OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = modifier,
                enabled = enabled,
                readOnly = readOnly,
                isError = isError,
                textStyle = textStyle,
                placeholder = ph,
                shape = RoundedCornerShape(shapes.medium),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = colors.onBackground,
                    unfocusedTextColor = colors.onBackground,
                    disabledTextColor = colors.onBackground.copy(alpha = 0.55f),
                    errorTextColor = colors.onBackground,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                    focusedIndicatorColor = colors.primary,
                    unfocusedIndicatorColor = colors.border,
                    disabledIndicatorColor = colors.border.copy(alpha = 0.35f),
                    errorIndicatorColor = Color(0xFFDC2626),
                )
            )
        }
    }
}
