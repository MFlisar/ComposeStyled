package com.michaelflisar.composestyledcomponents.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.ui.components.StyledInput
import com.michaelflisar.composestyledcomponents.registry.LocalStyledRegistry
import com.michaelflisar.composestyledcomponents.ui.primitives.StyledText


@Composable
fun StyledInputButton(
    value: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    errorMessage: String? = null,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    shape: Shape = StyledInputDefaults.shape,
    customization: StyledInput.Customization? = null,
    textStyle: TextStyle = StyledTheme.typography.bodyMedium,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource? = null,
    label: String? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
    ) {
        StyledInput(
            modifier = Modifier,
            value = value,
            onValueChange = {},
            enabled = enabled,
            readOnly = readOnly,
            isError = isError,
            errorMessage = errorMessage,
            singleLine = singleLine,
            minLines = minLines,
            maxLines = maxLines,
            shape = shape,
            customization = customization,
            textStyle = textStyle,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            visualTransformation = visualTransformation,
            interactionSource = interactionSource,
            label = if (label != null)
            { { StyledText(label) } } else null,
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            supportingText = supportingText,
        )

        Box(
            Modifier
                .matchParentSize()
                .padding(
                    top = if (label?.isNotEmpty() == true) {
                        LocalStyledRegistry.current[StyledInput.Key].topInsetToBorder()
                    } else {
                        0.dp
                    }
                )
                .clip(shape)
                .clickable(
                    enabled = enabled,
                    role = Role.Button,
                    onClick = onClick
                )
        )
    }
}