package com.michaelflisar.composestyled.core.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.core.renderer.LocalStyledComponents
import com.michaelflisar.composestyled.core.renderer.StyledTokenCompontents
import com.michaelflisar.composestyled.core.renderer.StyledTokenRenderer
import com.michaelflisar.composestyled.core.renderer.StyledWrapperComponents
import com.michaelflisar.composestyled.core.runtime.LocalContentColor

// ----------------------
// Renderer
// ----------------------

interface StyledIconTokenRenderer : StyledTokenRenderer {

    @Composable
    fun Render(
        painter: Painter,
        contentDescription: String?,
        modifier: Modifier,
        tint: Color,
        size: Dp,
    )

    @Composable
    fun Render(
        imageVector: ImageVector,
        contentDescription: String?,
        modifier: Modifier,
        tint: Color,
        size: Dp,
    )
}

interface StyledIconWrapperRenderer {

    @Composable
    fun Render(
        request: Request,
        painter: Painter,
        contentDescription: String?,
        modifier: Modifier,
        tint: Color,
        size: Dp,
    )

    @Composable
    fun Render(
        request: Request,
        imageVector: ImageVector,
        contentDescription: String?,
        modifier: Modifier,
        tint: Color,
        size: Dp,
    )

    data object Request
}

// ----------------------
// Defaults
// ----------------------

object StyledIconDefaults {
    val Size: Dp = 24.dp
}

// ----------------------
// Composables
// ----------------------

@Composable
fun StyledIcon(
    painter: Painter,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current,
    size: Dp = StyledIconDefaults.Size,
) {
    when (val components = LocalStyledComponents.current) {
        is StyledTokenCompontents -> {
            components.icon.Render(
                painter = painter,
                contentDescription = contentDescription,
                modifier = modifier,
                tint = tint,
                size = size,
            )
        }

        is StyledWrapperComponents -> {
            components.icon.Render(
                request = StyledIconWrapperRenderer.Request,
                painter = painter,
                contentDescription = contentDescription,
                modifier = modifier,
                tint = tint,
                size = size,
            )
        }
    }
}

@Composable
fun StyledIcon(
    imageVector: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current,
    size: Dp = StyledIconDefaults.Size,
) {
    when (val components = LocalStyledComponents.current) {
        is StyledTokenCompontents -> {
            components.icon.Render(
                imageVector = imageVector,
                contentDescription = contentDescription,
                modifier = modifier,
                tint = tint,
                size = size,
            )
        }

        is StyledWrapperComponents -> {
            components.icon.Render(
                request = StyledIconWrapperRenderer.Request,
                imageVector = imageVector,
                contentDescription = contentDescription,
                modifier = modifier,
                tint = tint,
                size = size,
            )
        }
    }
}
