package com.michaelflisar.composestyled.theme.wrapper.material3.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyled.core.components.StyledSeparatorWrapperRenderer
import com.michaelflisar.composestyled.core.components.StyledSeparatorWrapperRenderer.Request

internal object StyledSeparatorImpl : StyledSeparatorWrapperRenderer {

    @Composable
    override fun RenderHorizontal(
        request: Request,
        modifier: Modifier,
        color: Color,
        thickness: Dp,
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(color)
                .then(Modifier)
        )
    }

    @Composable
    override fun RenderVertical(
        request: Request,
        modifier: Modifier,
        color: Color,
        thickness: Dp,
    ) {
        Box(
            modifier = modifier
                .fillMaxHeight()
                .background(color)
                .then(Modifier)
        )
    }
}

