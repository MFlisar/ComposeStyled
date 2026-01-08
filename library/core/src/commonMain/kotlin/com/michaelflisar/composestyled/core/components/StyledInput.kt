package com.michaelflisar.composestyled.core.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import com.michaelflisar.composestyled.core.classes.DisableFactorType
import com.michaelflisar.composestyled.core.renderer.LocalStyledComponents
import com.michaelflisar.composestyled.core.theme.StyledTheme

object StyledInputDefaults {

    @Composable
    fun configFilled(
        containerColor: Color = StyledTheme.colors.input,
        contentColor: Color = StyledTheme.colors.onInput,
        disabledContainerColor: Color = StyledTheme.colors.input(enabled = false),
        disabledContentColor: Color = StyledTheme.colors.onInput(
            enabled = false,
            disableFactorType = DisableFactorType.Text
        ),
        borderColor: Color = Color.Transparent,
        disabledBorderColor: Color = Color.Transparent,
    ) = StyledInputConfig(
        variant = StyledInputConfig.Variant.Filled,
        containerColor = containerColor,
        contentColor = contentColor,
        disabledContainerColor = disabledContainerColor,
        disabledContentColor = disabledContentColor,
        borderColor = borderColor,
        disabledBorderColor = disabledBorderColor
    )

    @Composable
    fun configOutlined(
        contentColor: Color = StyledTheme.colors.onInput,
        disabledContentColor: Color = StyledTheme.colors.onInput(
            enabled = false,
            disableFactorType = DisableFactorType.Text
        ),
        borderColor: Color = StyledTheme.colors.outlineVariant,
        disabledBorderColor: Color = StyledTheme.colors.outlineVariant(enabled = false),
    ) = StyledInputConfig(
        variant = StyledInputConfig.Variant.Outlined,
        containerColor = Color.Transparent,
        contentColor = contentColor,
        disabledContainerColor = Color.Transparent,
        disabledContentColor = disabledContentColor,
        borderColor = borderColor,
        disabledBorderColor = disabledBorderColor
    )
}


@Composable
fun StyledInput(
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
    LocalStyledComponents.current.Input(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        config = config,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        prefix = prefix,
        suffix = suffix,
        supportingText = supportingText,
        isError = isError,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        interactionSource = interactionSource,
        shape = shape
    )
}

sealed interface StyledInputVariant {
    data object Filled : StyledInputVariant
    data object Outlined : StyledInputVariant
}

class StyledInputConfig internal constructor(
    val variant: Variant,
    val containerColor: Color,
    val contentColor: Color,
    val disabledContainerColor: Color,
    val disabledContentColor: Color,
    val borderColor: Color,
    val disabledBorderColor: Color,
) {
    enum class Variant {
        Filled,
        Outlined
    }

    fun containerColor(enabled: Boolean): Color =
        if (enabled) containerColor else disabledContainerColor

    fun contentColor(enabled: Boolean): Color = if (enabled) contentColor else disabledContentColor
    fun borderColor(enabled: Boolean): Color = if (enabled) borderColor else disabledBorderColor
}