package com.michaelflisar.composestyled.core.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.michaelflisar.composestyled.core.renderer.LocalStyledComponents

@Composable
fun StyledSurface(
    modifier: Modifier,
    shape: Shape,
    color: Color,
    contentColor: Color,
    border: BorderStroke?,
    content: @Composable () -> Unit,
) {
    LocalStyledComponents.current.Surface(
        modifier = modifier,
        shape = shape,
        color = color,
        contentColor = contentColor,
        border = border,
        content = content
    )
}
