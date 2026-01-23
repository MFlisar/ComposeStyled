package com.michaelflisar.composestyled.shared

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.michaelflisar.composestyled.core.components.StyledSurfaceRenderer
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.core.runtime.ProvideStyledLocals

@InternalComposeStyledApi
abstract class UnstyledSurfaceRenderer : StyledSurfaceRenderer.TokenRenderer {

    @Composable
    override fun render(
        modifier: Modifier,
        shape: Shape,
        backgroundColor: Color,
        contentColor: Color,
        border: BorderStroke?,
        content: @Composable (() -> Unit),
    ) {
        val m = modifier
            .clip(shape)
            .background(backgroundColor)
            .then(
                if (border != null) {
                    Modifier.border(border = border, shape = shape)
                } else {
                    Modifier
                }
            )

        ProvideStyledLocals(
            contentColor = contentColor,
            backgroundColor = backgroundColor
        ) {
            Box(modifier = m) {
                content()
            }
        }
    }
}