package com.michaelflisar.composestyled.shared

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import com.composeunstyled.Icon
import com.michaelflisar.composestyled.core.components.StyledIconRenderer
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi

@InternalComposeStyledApi
abstract class UnstyledIconRenderer : StyledIconRenderer.TokenRenderer {

    @Composable
    override fun render(
        painter: Painter,
        contentDescription: String?,
        modifier: Modifier,
        tint: Color,
        size: Dp,
    ) {
        Icon(
            painter = painter,
            contentDescription = contentDescription,
            modifier = modifier.size(size),
            tint = tint,
        )
    }

    @Composable
    override fun render(
        imageVector: ImageVector,
        contentDescription: String?,
        modifier: Modifier,
        tint: Color,
        size: Dp,
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            modifier = modifier.size(size),
            tint = tint,
        )
    }
}