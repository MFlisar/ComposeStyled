package com.michaelflisar.composestyled.shared

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.composables.core.HorizontalSeparator
import com.composables.core.VerticalSeparator
import com.michaelflisar.composestyled.core.components.StyledSeparatorRenderer
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi

@InternalComposeStyledApi
abstract class UnstyledSeparatorRenderer : StyledSeparatorRenderer.TokenRenderer {

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