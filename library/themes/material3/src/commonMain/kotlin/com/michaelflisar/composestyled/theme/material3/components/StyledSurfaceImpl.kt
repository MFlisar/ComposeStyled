package com.michaelflisar.composestyled.theme.material3.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.michaelflisar.composestyled.core.runtime.ProvideStyledLocals

/**
 * Material3-based surface.
 *
 * No elevation parameter on purpose (platform-specific).
 * Uses Material3 Surface for proper clipping, semantics, etc.
 */
@Composable
internal fun StyledSurfaceImpl(
    modifier: Modifier,
    shape: Shape,
    color: Color,
    contentColor: Color,
    border: BorderStroke?,
    content: @Composable () -> Unit,
) {
    ProvideStyledLocals(
        contentColor = contentColor,
        backgroundColor = color
    ) {
        Surface(
            modifier = modifier,
            color = color,
            contentColor = contentColor,
            shape = shape,
            border = border,
        ) {
            content()
        }
    }
}
