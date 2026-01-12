package com.michaelflisar.composestyled.theme.fluent2.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyled.core.components.StyledSeparatorTokenRenderer
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi

internal object StyledSeparatorImpl : StyledSeparatorTokenRenderer {

    @OptIn(InternalComposeStyledApi::class)
    @Composable
    override fun registerVariantStyles() {
        // TODO
    }

    @Composable
    override fun RenderHorizontal(
        modifier: Modifier,
        color: Color,
        thickness: Dp,
    ) {
        // TODO
    }

    @Composable
    override fun RenderVertical(
        modifier: Modifier,
        color: Color,
        thickness: Dp,
    ) {
        // TODO
    }
}
