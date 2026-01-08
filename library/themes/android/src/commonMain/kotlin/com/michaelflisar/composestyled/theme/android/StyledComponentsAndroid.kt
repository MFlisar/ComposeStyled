package com.michaelflisar.composestyled.theme.android

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import com.michaelflisar.composestyled.core.components.StyledButtonConfig
import com.michaelflisar.composestyled.core.components.StyledCardConfig
import com.michaelflisar.composestyled.core.components.StyledInputConfig
import com.michaelflisar.composestyled.core.renderer.StyledComponents
import com.michaelflisar.composestyled.theme.android.components.StyledButtonImpl
import com.michaelflisar.composestyled.theme.android.components.StyledCardImpl
import com.michaelflisar.composestyled.theme.android.components.StyledInputImpl
import com.michaelflisar.composestyled.theme.android.components.StyledSurfaceImpl
import com.michaelflisar.composestyled.theme.android.components.StyledTextImpl

object StyledComponentsAndroid : StyledComponents {

    @Composable
    override fun Root(content: @Composable () -> Unit) {
        content()
    }

    @Composable
    override fun Surface(
        modifier: Modifier,
        shape: Shape,
        color: Color,
        contentColor: Color,
        border: BorderStroke?,
        content: @Composable (() -> Unit),
    ) {
        StyledSurfaceImpl(
            modifier = modifier,
            shape = shape,
            color = color,
            contentColor = contentColor,
            border = border,
            content = content
        )
    }

    @Composable
    override fun Button(
        onClick: () -> Unit,
        modifier: Modifier,
        enabled: Boolean,
        shape: Shape,
        config: StyledButtonConfig,
        contentPadding: PaddingValues,
        interactionSource: MutableInteractionSource?,
        content: @Composable (RowScope.() -> Unit),
    ) {
        StyledButtonImpl(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            shape = shape,
            config = config,
            contentPadding = contentPadding,
            interactionSource = interactionSource,
            content = content
        )
    }

    @Composable
    override fun Card(
        modifier: Modifier,
        shape: Shape,
        config: StyledCardConfig,
        contentPadding: PaddingValues,
        content: @Composable ColumnScope.() -> Unit,
    ) {
        StyledCardImpl(
            modifier = modifier,
            shape = shape,
            config = config,
            contentPadding = contentPadding,
            content = content
        )
    }

    @Composable
    override fun Text(
        text: String,
        modifier: Modifier,
        style: TextStyle,
        color: Color,
        overflow: TextOverflow,
        softWrap: Boolean,
        maxLines: Int,
        minLines: Int,
        autoSize: TextAutoSize?,
    ) {
        StyledTextImpl(
            text = text,
            modifier = modifier,
            style = style,
            color = color,
            overflow = overflow,
            softWrap = softWrap,
            maxLines = maxLines,
            minLines = minLines,
            autoSize = autoSize
        )
    }

    @Composable
    override fun Input(
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
        StyledInputImpl(
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
}