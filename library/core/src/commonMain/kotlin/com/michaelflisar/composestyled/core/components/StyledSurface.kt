package com.michaelflisar.composestyled.core.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.renderer.LocalStyledComponents
import com.michaelflisar.composestyled.core.renderer.StyledTokenCompontents
import com.michaelflisar.composestyled.core.renderer.StyledTokenRenderer
import com.michaelflisar.composestyled.core.renderer.StyledWrapperComponents

// ----------------------
// Renderer
// ----------------------

interface StyledSurfaceTokenRenderer : StyledTokenRenderer {

    @Composable
    fun Render(
        modifier: Modifier,
        shape: Shape,
        backgroundColor: Color,
        contentColor: Color,
        border: BorderStroke?,
        content: @Composable () -> Unit,
    )
}

interface StyledSurfaceWrapperRenderer {

    @Composable
    fun Render(
        request: Request,
        modifier: Modifier,
        shape: Shape,
        backgroundColor: Color,
        contentColor: Color,
        border: BorderStroke?,
        content: @Composable () -> Unit,
    )

    data object Request
}

// ----------------------
// Defaults
// ----------------------

object StyledSurfaceDefaults {
    val Shape: Shape = RectangleShape

    @Composable
    fun backgroundColor(): Color = StyledTheme.colors.background

    @Composable
    fun contentColor(): Color = StyledTheme.colors.onBackground
}

// ----------------------
// Composable
// ----------------------

@Composable
fun StyledSurface(
    modifier: Modifier = Modifier,
    shape: Shape = StyledSurfaceDefaults.Shape,
    backgroundColor: Color = StyledSurfaceDefaults.backgroundColor(),
    contentColor: Color = StyledSurfaceDefaults.contentColor(),
    border: BorderStroke? = null,
    content: @Composable () -> Unit,
) {
    when (val components = LocalStyledComponents.current) {
        is StyledTokenCompontents -> {
            components.surface.Render(
                modifier = modifier,
                shape = shape,
                backgroundColor = backgroundColor,
                contentColor = contentColor,
                border = border,
                content = content,
            )
        }

        is StyledWrapperComponents -> {
            components.surface.Render(
                request = StyledSurfaceWrapperRenderer.Request,
                modifier = modifier,
                shape = shape,
                backgroundColor = backgroundColor,
                contentColor = contentColor,
                border = border,
                content = content,
            )
        }
    }

}
