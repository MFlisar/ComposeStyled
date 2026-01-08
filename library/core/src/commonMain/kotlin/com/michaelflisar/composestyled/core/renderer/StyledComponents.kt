package com.michaelflisar.composestyled.core.renderer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import com.michaelflisar.composestyled.core.classes.colors.BaseColor

/**
 * Defines the platform specific implementations of styled components.
 *
 * do not add default parameters here, only define them inside the actual implementations!
 *
 */
interface StyledComponents {

    @Composable
    fun Root(content: @Composable () -> Unit)

    @Composable
    fun Surface(
        modifier: Modifier,
        shape: Shape,
        color: Color,
        contentColor: Color,
        border: BorderStroke?,
        content: @Composable () -> Unit,
    )

    @Composable
    fun Button(
        colors: BaseColor,
        onClick: () -> Unit,
        modifier: Modifier,
        enabled: Boolean,
        shape: Shape,
        contentPadding: PaddingValues,
        interactionSource: MutableInteractionSource,
        content: @Composable RowScope.() -> Unit,
    )

    @Composable
    fun Input(
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
        interactionSource: MutableInteractionSource,
        shape: Shape,
    )

    /*

    @Composable
    fun Card(
        modifier: Modifier = Modifier,
        shape: Shape = StyledTheme.shapes.card,
        config: StyledCardConfig = StyledCardDefaults.configFilled(),
        contentPadding: PaddingValues = PaddingValues(0.dp),
        content: @Composable ColumnScope.() -> Unit,
    )

    @Composable
    fun Text(
        text: String,
        modifier: Modifier = Modifier,
        style: TextStyle = TextStyle.Default,
        color: Color = StyledTheme.colors.onBackground,
        overflow: TextOverflow = TextOverflow.Clip,
        softWrap: Boolean = true,
        maxLines: Int = Int.MAX_VALUE,
        minLines: Int = 1,
        autoSize: TextAutoSize? = null,
    )

    */
}

internal val LocalStyledComponents =
    staticCompositionLocalOf<StyledComponents> { error("No StyledComponents provided!") }