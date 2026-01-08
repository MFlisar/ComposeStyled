package com.michaelflisar.composestyled.core.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.michaelflisar.composestyled.core.classes.DisableFactorType
import com.michaelflisar.composestyled.core.renderer.LocalStyledComponents
import com.michaelflisar.composestyled.core.theme.StyledTheme

object StyledCardDefaults {

    @Composable
    fun configFilled(
        containerColor: Color = StyledTheme.colors.surface,
        contentColor: Color = StyledTheme.colors.onSurface,
        disabledContainerColor: Color = StyledTheme.colors.surface(enabled = false),
        disabledContentColor: Color = StyledTheme.colors.onSurface(
            enabled = false,
            disableFactorType = DisableFactorType.Text
        ),
        borderColor: Color = Color.Transparent,
        disabledBorderColor: Color = Color.Transparent,
    ) = StyledCardConfig(
        variant = StyledCardConfig.Variant.Filled,
        containerColor = containerColor,
        contentColor = contentColor,
        disabledContainerColor = disabledContainerColor,
        disabledContentColor = disabledContentColor,
        borderColor = borderColor,
        disabledBorderColor = disabledBorderColor
    )

    @Composable
    fun configOutlined(
        contentColor: Color = StyledTheme.colors.onSurface,
        disabledContentColor: Color = StyledTheme.colors.onSurface(
            enabled = false,
            disableFactorType = DisableFactorType.Text
        ),
        borderColor: Color = StyledTheme.colors.outlineVariant,
        disabledBorderColor: Color = StyledTheme.colors.outlineVariant(enabled = false),
    ) = StyledCardConfig(
        variant = StyledCardConfig.Variant.Outlined,
        containerColor = Color.Transparent,
        contentColor = contentColor,
        disabledContainerColor = Color.Transparent,
        disabledContentColor = disabledContentColor,
        borderColor = borderColor,
        disabledBorderColor = disabledBorderColor
    )
}

@Composable
fun StyledCard(
    modifier: Modifier,
    shape: Shape,
    config: StyledCardConfig,
    content: @Composable ColumnScope.() -> Unit,
) {
    LocalStyledComponents.current.Card(
        modifier = modifier,
        shape = shape,
        config = config,
        content = content
    )
}

@Immutable
class StyledCardConfig internal constructor(
    val variant: Variant,
    val containerColor: Color,
    val contentColor: Color,
    val disabledContainerColor: Color,
    val disabledContentColor: Color,
    val borderColor: Color,
    val disabledBorderColor: Color,
) {
    enum class Variant {
        Filled,
        Outlined
    }
}