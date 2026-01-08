package com.michaelflisar.composestyled.core.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.renderer.LocalStyledComponents

/**
 * Styled surface.
 *
 * This is the basic container primitive used by higher level components.
 * Rendering is delegated via `LocalStyledComponents`.
 */
@Composable
fun StyledSurface(
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    backgroundColor: androidx.compose.ui.graphics.Color = StyledTheme.colors.background,
    contentColor: androidx.compose.ui.graphics.Color = StyledTheme.colors.onBackground,
    border: BorderStroke? = null,
    content: @Composable () -> Unit,
) {
    LocalStyledComponents.current.Surface(
        modifier = modifier,
        shape = shape,
        color = backgroundColor,
        contentColor = contentColor,
        border = border,
        content = content,
    )
}
