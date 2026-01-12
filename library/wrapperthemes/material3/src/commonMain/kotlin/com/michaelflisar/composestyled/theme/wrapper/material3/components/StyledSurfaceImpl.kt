package com.michaelflisar.composestyled.theme.wrapper.material3.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.michaelflisar.composestyled.core.components.StyledSurface
import com.michaelflisar.composestyled.core.components.StyledSurfaceWrapperRenderer
import com.michaelflisar.composestyled.core.runtime.ProvideStyledLocals

internal object StyledSurfaceImpl : StyledSurfaceWrapperRenderer {

    @Composable
    override fun Render(
        variant: StyledSurface.Variant,
        modifier: Modifier,
        shape: Shape,
        backgroundColor: Color,
        contentColor: Color,
        border: BorderStroke?,
        content: @Composable () -> Unit,
    ) {
        androidx.compose.material3.Surface(
            modifier = modifier,
            shape = shape,
            color = backgroundColor,
            contentColor = contentColor,
            border = border,
        ) {
            // Keep core locals aligned with Material3 Surface for nested content
            ProvideStyledLocals(
                contentColor = contentColor,
                backgroundColor = backgroundColor,
                applyTransparentBackgroundColor = true,
                content = content,
            )
        }
    }
}