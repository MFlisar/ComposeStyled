package com.michaelflisar.composestyled.theme.wrapper.material3.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import com.michaelflisar.composestyled.core.components.StyledTextFieldWrapperRenderer
import com.michaelflisar.composestyled.core.components.StyledTextFieldWrapperRenderer.Request
import com.michaelflisar.composestyled.core.runtime.ProvideStyledLocals
import com.michaelflisar.composestyled.theme.wrapper.material3.disabled

internal object StyledTextFieldImpl : StyledTextFieldWrapperRenderer {

    @Composable
    @Suppress("UNUSED_PARAMETER")
    override fun Render(
        request: Request,
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
    ) {
        val containerColor = request.customColors?.normal?.background ?: Color.Unspecified
        val contentColor = request.customColors?.normal?.foreground ?: Color.Unspecified
        val disabledContainerColor =
            request.customColors?.normal?.background?.disabled() ?: Color.Unspecified
        val disabledContentColor =
            request.customColors?.normal?.foreground?.disabled() ?: Color.Unspecified

        // M3 TextField doesn't allow full background/border customization via a BaseColor directly.
        // We map what we can via TextFieldDefaults.colors.
        val tfColors = TextFieldDefaults.colors(
            focusedContainerColor = containerColor,
            unfocusedContainerColor = containerColor,
            disabledContainerColor = disabledContainerColor,
            focusedTextColor = contentColor,
            unfocusedTextColor = contentColor,
            disabledTextColor = disabledContentColor,
            cursorColor = contentColor
        )

        ProvideStyledLocals(
            contentColor = contentColor,
            backgroundColor = containerColor
        ) {
            // Use OutlinedTextField for clearer border mapping.
            OutlinedTextField(
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
                shape = shape,
                colors = tfColors,
            )
        }
    }
}
