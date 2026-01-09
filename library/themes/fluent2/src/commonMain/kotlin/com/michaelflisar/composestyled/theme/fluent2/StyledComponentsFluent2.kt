package com.michaelflisar.composestyled.theme.fluent2

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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import com.michaelflisar.composestyled.core.tokens.StyledColors

object StyledComponentsFluent2 : StyledComponents {

    @Composable
    override fun registerAllComponents(colors: StyledColors) {
       // ...
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
        TODO()
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
        TODO()
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
        TODO()
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
        TODO()
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
        TODO()
    }

    @Composable
    override fun HorizontalSeparator(
        modifier: Modifier,
        color: Color,
        thickness: Dp,
    ) {
        TODO()
    }

    @Composable
    override fun VerticalSeparator(
        modifier: Modifier,
        color: Color,
        thickness: Dp,
    ) {
        TODO()
    }

    @Composable
    override fun Icon(
        painter: Painter,
        contentDescription: String?,
        modifier: Modifier,
        tint: Color,
        size: Dp,
    ) {
        TODO()
    }

    @Composable
    override fun Icon(
        imageVector: ImageVector,
        contentDescription: String?,
        modifier: Modifier,
        tint: Color,
        size: Dp,
    ) {
        TODO()
    }
}