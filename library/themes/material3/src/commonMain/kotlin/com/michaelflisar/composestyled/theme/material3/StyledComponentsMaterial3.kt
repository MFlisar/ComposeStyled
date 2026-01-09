package com.michaelflisar.composestyled.theme.material3

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Indication
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
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import com.michaelflisar.composestyled.core.classes.colors.BaseColor
import com.michaelflisar.composestyled.core.renderer.StyledComponents
import com.michaelflisar.composestyled.theme.material3.components.StyledButtonImpl
import com.michaelflisar.composestyled.theme.material3.components.StyledCardImpl
import com.michaelflisar.composestyled.theme.material3.components.StyledCheckboxImpl
import com.michaelflisar.composestyled.theme.material3.components.StyledIconImpl
import com.michaelflisar.composestyled.theme.material3.components.StyledTextFieldImpl
import com.michaelflisar.composestyled.theme.material3.components.StyledTextImpl
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import com.michaelflisar.composestyled.core.tokens.StyledColors
import com.michaelflisar.composestyled.theme.material3.components.StyledSeparatorImpl

object StyledComponentsMaterial3 : StyledComponents {

    @Composable
    override fun registerAllComponents(colors: StyledColors) {
        StyledButtonImpl.registerVariantStyles(colors)
        StyledTextFieldImpl.registerVariantStyles(colors)
        StyledTextImpl.registerVariantStyles(colors)
        StyledCardImpl.registerVariantStyles(colors)
        StyledCheckboxImpl.registerVariantStyles(colors)
        StyledSeparatorImpl.registerVariantStyles(colors)
        StyledIconImpl.registerVariantStyles(colors)
    }

    // Root => use default
    // override fun Root()

    // Surface => use default
    // override fun Surface()

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
        StyledButtonImpl.Render(
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
    override fun Card(
        modifier: Modifier,
        shape: Shape,
        backgroundColor: Color,
        contentColor: Color,
        border: BorderStroke?,
        contentPadding: PaddingValues,
        content: @Composable ColumnScope.() -> Unit,
    ) {
        StyledCardImpl.Render(
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
        modifier: Modifier,
        backgroundColor: Color,
        contentColor: Color,
        enabled: Boolean,
        onCheckedChange: ((Boolean) -> Unit)?,
        shape: Shape,
        borderColor: Color,
        borderWidth: Dp,
        interactionSource: MutableInteractionSource?,
        indication: Indication?,
        contentDescription: String?,
        checkIcon: @Composable () -> Unit,
    ) {
        StyledCheckboxImpl.Render(
            checked = checked,
            modifier = modifier,
            backgroundColor = backgroundColor,
            contentColor = contentColor,
            enabled = enabled,
            onCheckedChange = onCheckedChange,
            shape = shape,
            borderColor = borderColor,
            borderWidth = borderWidth,
            interactionSource = interactionSource,
            indication = indication,
            contentDescription = contentDescription,
            checkIcon = checkIcon,
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
        StyledTextFieldImpl.Render(
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
        StyledTextImpl.Render(
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
    override fun Icon(
        painter: Painter,
        contentDescription: String?,
        modifier: Modifier,
        tint: Color,
        size: Dp,
    ) {
        StyledIconImpl.Render(
            painter = painter,
            contentDescription = contentDescription,
            modifier = modifier,
            tint = tint,
            size = size,
        )
    }

    @Composable
    override fun Icon(
        imageVector: ImageVector,
        contentDescription: String?,
        modifier: Modifier,
        tint: Color,
        size: Dp,
    ) {
        StyledIconImpl.Render(
            imageVector = imageVector,
            contentDescription = contentDescription,
            modifier = modifier,
            tint = tint,
            size = size,
        )
    }

    @Composable
    override fun HorizontalSeparator(
        modifier: Modifier,
        color: Color,
        thickness: Dp,
    ) {
        StyledSeparatorImpl.RenderHorizontal(
            modifier = modifier,
            color = color,
            thickness = thickness,
        )
    }

    @Composable
    override fun VerticalSeparator(
        modifier: Modifier,
        color: Color,
        thickness: Dp,
    ) {
        StyledSeparatorImpl.RenderVertical(
            modifier = modifier,
            color = color,
            thickness = thickness,
        )
    }
}