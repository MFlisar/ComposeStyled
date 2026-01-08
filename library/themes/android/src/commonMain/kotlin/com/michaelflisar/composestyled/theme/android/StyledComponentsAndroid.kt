package com.michaelflisar.composestyled.theme.android

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.ui.text.style.TextOverflow
import com.michaelflisar.composestyled.core.classes.colors.BaseColor
import com.michaelflisar.composestyled.core.renderer.StyledComponents
import com.michaelflisar.composestyled.theme.android.components.StyledButtonImpl
import com.michaelflisar.composestyled.theme.android.components.StyledCardImpl
import com.michaelflisar.composestyled.theme.android.components.StyledTextFieldImpl
import com.michaelflisar.composestyled.theme.android.components.StyledSurfaceImpl
import com.michaelflisar.composestyled.theme.android.components.StyledTextImpl
import com.michaelflisar.composestyled.theme.android.components.StyledCheckboxImpl
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit

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
        content: @Composable () -> Unit,
    ) {
        StyledSurfaceImpl(
            modifier = modifier,
            shape = shape,
            color = color,
            contentColor = contentColor,
            border = border,
            content = content,
        )
    }

    @Composable
    override fun Button(
        colors: BaseColor,
        onClick: () -> Unit,
        modifier: Modifier,
        enabled: Boolean,
        shape: Shape,
        contentPadding: PaddingValues,
        interactionSource: MutableInteractionSource,
        content: @Composable RowScope.() -> Unit,
    ) {
        StyledButtonImpl(
            colors = colors,
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            shape = shape,
            contentPadding = contentPadding,
            interactionSource = interactionSource,
            content = content,
        )
    }

    @Composable
    override fun TextField(
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
        StyledTextFieldImpl(
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
            contentPadding = contentPadding,
            interactionSource = interactionSource,
            shape = shape,
        )
    }

    @Composable
    override fun Text(
        text: String,
        modifier: Modifier,
        style: TextStyle,
        textAlign: TextAlign,
        lineHeight: TextUnit,
        fontSize: TextUnit,
        letterSpacing: TextUnit,
        fontWeight: FontWeight?,
        color: Color,
        fontFamily: FontFamily?,
        singleLine: Boolean,
        minLines: Int,
        maxLines: Int,
        onTextLayout: ((TextLayoutResult) -> Unit)?,
        overflow: TextOverflow,
        autoSize: TextAutoSize?,
    ) {
        StyledTextImpl(
            text = text,
            modifier = modifier,
            style = style,
            textAlign = textAlign,
            lineHeight = lineHeight,
            fontSize = fontSize,
            letterSpacing = letterSpacing,
            fontWeight = fontWeight,
            color = color,
            fontFamily = fontFamily,
            singleLine = singleLine,
            minLines = minLines,
            maxLines = maxLines,
            onTextLayout = onTextLayout,
            overflow = overflow,
            autoSize = autoSize,
        )
    }

    @Composable
    override fun Card(
        modifier: Modifier,
        shape: Shape,
        backgroundColor: Color,
        contentColor: Color,
        border: BorderStroke?,
        contentPadding: PaddingValues,
        content: @Composable ColumnScope.() -> Unit,
    ) {
        StyledCardImpl(
            modifier = modifier,
            shape = shape,
            backgroundColor = backgroundColor,
            contentColor = contentColor,
            border = border,
            contentPadding = contentPadding,
            content = content,
        )
    }

    @Composable
    override fun Checkbox(
        checked: Boolean,
        onCheckedChange: (Boolean) -> Unit,
        colors: BaseColor,
        modifier: Modifier,
        enabled: Boolean,
        interactionSource: MutableInteractionSource,
    ) {
        StyledCheckboxImpl(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = colors,
            modifier = modifier,
            enabled = enabled,
            interactionSource = interactionSource,
        )
    }
}