package com.michaelflisar.composestyled.core.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.classes.colors.BaseColor
import com.michaelflisar.composestyled.core.classes.colors.ColorRef
import com.michaelflisar.composestyled.core.classes.colors.StatefulBaseColorDef
import com.michaelflisar.composestyled.core.renderer.LocalStyledComponents
import com.michaelflisar.composestyled.core.runtime.interaction.rememberStyledResolveState

object StyledTextField {

    // variants
    enum class Variant {
        Filled,
        Outlined
    }

    fun customize(
        background: ColorRef? = null,
        content: ColorRef? = null,
        border: ColorRef? = null,
    ) = Customization(background, content, border)

    @Immutable
    class Customization internal constructor(
        val background: ColorRef?,
        val content: ColorRef?,
        val border: ColorRef?,
    )
}

// ----------------------
// Renderer
// ----------------------

sealed interface StyledTextFieldRenderer {

    interface TokenRenderer : StyledTextFieldRenderer {

        @Composable
        fun resolveColors(
            variant: StyledTextField.Variant,
            customization: StyledTextField.Customization?,
        ): StatefulBaseColorDef

        @Composable
        fun render(
            value: String,
            onValueChange: (String) -> Unit,
            colors: BaseColor,
            modifier: Modifier,
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
            contentPadding: PaddingValues,
            interactionSource: MutableInteractionSource,
            shape: Shape,
        )
    }

    interface WrapperRenderer : StyledTextFieldRenderer {

        @Composable
        fun render(
            variant: StyledTextField.Variant,
            customization: StyledTextField.Customization?,
            value: String,
            onValueChange: (String) -> Unit,
            modifier: Modifier,
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
            contentPadding: PaddingValues,
            interactionSource: MutableInteractionSource,
            shape: Shape,
        )
    }

}

// ----------------------
// Defaults
// ----------------------

object StyledTextFieldDefaults {

    val variant: StyledTextField.Variant = StyledTextField.Variant.Filled

    val shape: Shape
        @Composable @ReadOnlyComposable get() = StyledTheme.shapes.input

    @Composable
    @ReadOnlyComposable
    fun contentPadding() = PaddingValues(
        horizontal = StyledTheme.paddings.medium,
        vertical = StyledTheme.paddings.small,
    )
}

// ----------------------
// Composables
// ----------------------

@Composable
fun StyledTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    variant: StyledTextField.Variant = StyledTextFieldDefaults.variant,
    customization: StyledTextField.Customization? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = StyledTheme.typography.bodyMedium,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = StyledTextFieldDefaults.shape,
) {
    when (val renderer = LocalStyledComponents.current.textField) {

        is StyledTextFieldRenderer.TokenRenderer -> {
            val state = rememberStyledResolveState(
                interactionSource = interactionSource,
                enabled = enabled,
                isError = isError,
            )
            val colors = renderer
                .resolveColors(variant, customization)
                .resolve(state)

            renderer.render(
                value = value,
                onValueChange = onValueChange,
                colors = colors,
                modifier = modifier,
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
                contentPadding = StyledTextFieldDefaults.contentPadding(),
                interactionSource = interactionSource,
                shape = shape,
            )
        }

        is StyledTextFieldRenderer.WrapperRenderer -> {
            renderer.render(
                variant = variant,
                customization = customization,
                value = value,
                onValueChange = onValueChange,
                modifier = modifier,
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
                contentPadding = StyledTextFieldDefaults.contentPadding(),
                interactionSource = interactionSource,
                shape = shape,
            )
        }
    }
}
