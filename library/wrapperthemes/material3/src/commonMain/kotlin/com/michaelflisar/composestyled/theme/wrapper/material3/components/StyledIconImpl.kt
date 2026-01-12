package com.michaelflisar.composestyled.theme.wrapper.material3.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyled.core.components.StyledIcon
import com.michaelflisar.composestyled.core.components.StyledIconWrapperRenderer

internal object StyledIconImpl : StyledIconWrapperRenderer {

    @Composable
    override fun Render(
        variant: StyledIcon.Variant,
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
    override fun Render(
        variant: StyledIcon.Variant,
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

