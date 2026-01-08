package com.michaelflisar.composestyled.theme.material3

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
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
import com.michaelflisar.composestyled.core.runtime.ProvideStyledLocals
import com.michaelflisar.composestyled.core.theme.StyledTheme
import com.michaelflisar.composestyled.core.tokens.StyledColors
import com.michaelflisar.composestyled.core.tokens.StyledShapes
import com.michaelflisar.composestyled.core.tokens.StyledTypography
import com.michaelflisar.composestyled.theme.material3.components.StyledButtonImpl
import com.michaelflisar.composestyled.theme.material3.components.StyledCardImpl
import com.michaelflisar.composestyled.theme.material3.components.StyledInputImpl
import com.michaelflisar.composestyled.theme.material3.components.StyledSurfaceImpl
import com.michaelflisar.composestyled.theme.material3.components.StyledTextImpl

object StyledComponentsMaterial3 : StyledComponents {

    @Composable
    override fun Root(content: @Composable () -> Unit) {

        val colors = StyledTheme.colors
        val shapes = StyledTheme.shapes
        val typography = StyledTheme.typography
        val cs = colors.toMaterial3ColorScheme()
        val t = typography.toMaterial3Typography()
        val s = shapes.toMaterial3Shapes()

        MaterialTheme(
            colorScheme = cs,
            typography = t,
            shapes = s,
        ) {
            // Keep core locals aligned with MaterialTheme by default
            ProvideStyledLocals(
                contentColor = cs.onBackground,
                backgroundColor = cs.background,
                content = content,
                applyTransparentBackgroundColor = true // here we save the background color even if it's transparent
            )
        }
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


private fun StyledColors.toMaterial3ColorScheme() =
    lightColorScheme(
        background = this.background,
        onBackground = this.onBackground,
        primary = this.primary,
        onPrimary = this.onPrimary,
        outline = this.outline,
        surface = this.background,
        onSurface = this.onBackground,
    )

private fun StyledTypography.toMaterial3Typography(): androidx.compose.material3.Typography =
    Typography(
        bodyLarge = this.bodyLarge,
        bodyMedium = this.bodyMedium,
        bodySmall = this.bodySmall,
        titleLarge = this.titleLarge,
        titleMedium = this.titleMedium,
        titleSmall = this.titleSmall,
        labelLarge = this.labelLarge,
        labelMedium = this.labelMedium,
        labelSmall = this.labelSmall,
    )

private fun StyledShapes.toMaterial3Shapes(): Shapes =
    Shapes(
        extraSmall = RoundedCornerShape(this.small * .8f),
        small = RoundedCornerShape(this.small),
        medium = RoundedCornerShape(this.medium),
        large = RoundedCornerShape(this.large),
        extraLarge = RoundedCornerShape(this.large * 1.2f)
    )