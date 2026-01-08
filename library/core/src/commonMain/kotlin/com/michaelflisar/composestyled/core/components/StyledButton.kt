package com.michaelflisar.composestyled.core.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.core.classes.DisableFactorType
import com.michaelflisar.composestyled.core.renderer.LocalStyledComponents
import com.michaelflisar.composestyled.core.theme.StyledTheme

object StyledButtonDefaults {

    @Composable
    fun configPrimary(
        containerColor: Color = StyledTheme.colors.primary,
        contentColor: Color = StyledTheme.colors.onPrimary,
        disabledContainerColor: Color = StyledTheme.colors.primary(enabled = false),
        disabledContentColor: Color = StyledTheme.colors.onPrimary(disableFactorType = DisableFactorType.Text),
    ) = StyledButtonConfig(
        variant = StyledButtonConfig.Variant.Filled,
        containerColor = containerColor,
        contentColor = contentColor,
        disabledContainerColor = disabledContainerColor,
        disabledContentColor = disabledContentColor,
        borderColor = Color.Transparent,
        disabledBorderColor = Color.Transparent
    )

    @Composable
    fun configOutlined(
        contentColor: Color = StyledTheme.colors.onBackground,
        disabledContentColor: Color = StyledTheme.colors.onBackground(enabled = false, disableFactorType = DisableFactorType.Text),
        borderColor: Color = StyledTheme.colors.outlineVariant,
        disabledBorderColor: Color = StyledTheme.colors.outlineVariant(enabled = false),
    ) = StyledButtonConfig(
        variant = StyledButtonConfig.Variant.Outlined,
        containerColor = Color.Transparent,
        contentColor = contentColor,
        disabledContainerColor = Color.Transparent,
        disabledContentColor = disabledContentColor,
        borderColor = borderColor,
        disabledBorderColor = disabledBorderColor
    )

    @Composable
    fun configText(
        contentColor: Color = StyledTheme.colors.onBackground,
        disabledContentColor: Color = StyledTheme.colors.onBackground(enabled = false, disableFactorType = DisableFactorType.Text),
    ) = StyledButtonConfig(
        variant = StyledButtonConfig.Variant.Text,
        containerColor = Color.Transparent,
        contentColor = contentColor,
        disabledContainerColor = Color.Transparent,
        disabledContentColor = disabledContentColor,
        borderColor = Color.Transparent,
        disabledBorderColor = Color.Transparent
    )

    @Composable
    fun contentPadding(
        horizontal: Dp = StyledTheme.paddings.large,
        vertical: Dp = StyledTheme.paddings.small,
    ) = PaddingValues(horizontal = horizontal, vertical = vertical)
}

@Composable
fun StyledButton(
    onClick: () -> Unit,
    modifier: Modifier,
    enabled: Boolean,
    shape: Shape,
    config: StyledButtonConfig,
    contentPadding: PaddingValues,
    interactionSource: MutableInteractionSource?,
    content: @Composable RowScope.() -> Unit,
) {
    LocalStyledComponents.current.Button(
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

@Immutable
class StyledButtonConfig internal constructor(
    val variant: Variant,
    val containerColor: Color,
    val contentColor: Color,
    val disabledContainerColor: Color,
    val disabledContentColor: Color,
    val borderColor: Color,
    val disabledBorderColor: Color,
    val minWidth: Dp = 58.dp,
    val minHeight: Dp = 40.0.dp,
) {
    enum class Variant {
        Filled,
        Outlined,
        Text
    }

    fun containerColor(enabled: Boolean): Color =
        if (enabled) containerColor else disabledContainerColor

    fun contentColor(enabled: Boolean): Color = if (enabled) contentColor else disabledContentColor
    fun borderColor(enabled: Boolean): Color = if (enabled) borderColor else disabledBorderColor
}
