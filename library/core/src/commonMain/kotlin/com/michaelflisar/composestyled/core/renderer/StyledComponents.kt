package com.michaelflisar.composestyled.core.renderer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.components.StyledButton
import com.michaelflisar.composestyled.core.components.StyledButtonDefaults

interface StyledComponents {

    @Composable
    fun Root(content: @Composable () -> Unit)

    @Composable
    fun Surface(
        modifier: Modifier = Modifier,
        shape: Shape = RectangleShape,
        color: Color = StyledTheme.colors.surface,
        contentColor: Color = StyledTheme.colors.onSurface,
        border: BorderStroke? = null,
        content: @Composable () -> Unit,
    )

    @Composable
    fun Button(
        variant: StyledButton.Variant,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        shape: Shape = StyledTheme.shapes.button,
        contentPadding: PaddingValues = StyledButtonDefaults.contentPadding(),
        interactionSource: MutableInteractionSource? = null,
        content: @Composable RowScope.() -> Unit,
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

    @Composable
    fun Input(
        value: String,
        onValueChange: (String) -> Unit,
        modifier: Modifier = Modifier,
        config: StyledInputConfig = StyledInputDefaults.configFilled(),
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
        interactionSource: MutableInteractionSource? = null,
        shape: Shape = StyledTheme.shapes.input,
    )*/
}

internal val LocalStyledComponents =
    staticCompositionLocalOf<StyledComponents> { error("No StyledComponents provided!") }