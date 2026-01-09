package com.michaelflisar.composestyled.theme.material3.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.composables.core.HorizontalSeparator
import com.composables.core.VerticalSeparator
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.components.StyledSeparator
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.core.tokens.StyledColors

internal object StyledSeparatorImpl {

    @OptIn(InternalComposeStyledApi::class)
    @Composable
    fun registerVariantStyles() {

        val colors = StyledTheme.colors

        StyledSeparator.registerVariantStyles(
            default = colors.outlineVariant,
            strong = colors.outline,
        )
    }

    @Composable
    fun RenderHorizontal(
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
    fun RenderVertical(
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
