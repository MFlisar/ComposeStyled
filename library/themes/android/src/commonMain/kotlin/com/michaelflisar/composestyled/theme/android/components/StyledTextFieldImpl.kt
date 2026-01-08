package com.michaelflisar.composestyled.theme.android.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import com.composeunstyled.LocalContentColor
import com.composeunstyled.LocalTextStyle
import com.composeunstyled.TextField
import com.composeunstyled.TextInput
import com.michaelflisar.composestyled.core.classes.colors.BaseColor

/**
 * Android/Unstyled-based text field implementation for `StyledInput`.
 */
@Composable
@Suppress("UNUSED_PARAMETER")
internal fun StyledTextFieldImpl(
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
) {
    val state = remember { TextFieldState(value) }

    // Internal -> external sync (only when user edits)
    LaunchedEffect(state) {
        snapshotFlow { state.text }
            .collect { newText ->
                val newValue = newText.toString()
                if (newValue != value) {
                    onValueChange(newValue)
                }
            }
    }

    val scrollState: ScrollState = rememberScrollState()

    TextField(
        state = state,
        modifier = modifier,
        editable = enabled && !readOnly,
        cursorBrush = SolidColor(colors.foreground),
        textStyle = LocalTextStyle.current.merge(textStyle),
        textAlign = TextAlign.Unspecified,
        lineHeight = TextUnit.Unspecified,
        fontSize = TextUnit.Unspecified,
        letterSpacing = TextUnit.Unspecified,
        fontWeight = null,
        fontFamily = null,
        singleLine = singleLine,
        minLines = minLines,
        maxLines = maxLines,
        onKeyboardAction = null,
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        interactionSource = interactionSource,
        textColor = colors.foreground,
        scrollState = scrollState,
    ) {
        TextInput(
            modifier = Modifier,
            shape = shape,
            backgroundColor = Color.Unspecified,
            contentPadding = contentPadding,
            contentColor = LocalContentColor.current,
            label = null,
            placeholder = placeholder,
            leading = leadingIcon,
            trailing = trailingIcon,
            verticalAlignment = Alignment.CenterVertically,
        )
    }
}

