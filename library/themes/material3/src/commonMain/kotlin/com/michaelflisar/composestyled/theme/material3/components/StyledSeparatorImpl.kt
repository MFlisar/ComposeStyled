package com.michaelflisar.composestyled.theme.material3.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.composables.core.HorizontalSeparator
import com.composables.core.VerticalSeparator
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.components.StyledSeparator
import com.michaelflisar.composestyled.core.components.StyledSeparatorTokenRenderer
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi

internal object StyledSeparatorImpl : StyledSeparatorTokenRenderer {

    @OptIn(InternalComposeStyledApi::class)
    @Composable
    override fun registerVariantStyles() {

        val colors = StyledTheme.colors

        StyledSeparator.registerVariantStyles(
            default = colors.outlineVariant,
            strong = colors.outline,
        )
    }

    @Composable
    override fun RenderHorizontal(
        modifier: Modifier,
        color: Color,
        thickness: Dp,
    ) {
        HorizontalSeparator(
            modifier = modifier,
            color = color,
            thickness = thickness
        )
    }

    @Composable
    override fun RenderVertical(
        modifier: Modifier,
        color: Color,
        thickness: Dp,
    ) {
        VerticalSeparator(
            modifier = modifier,
            color = color,
            thickness = thickness
        )
    }
}
