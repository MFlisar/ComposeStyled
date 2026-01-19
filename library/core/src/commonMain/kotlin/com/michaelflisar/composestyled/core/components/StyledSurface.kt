package com.michaelflisar.composestyled.core.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.renderer.LocalStyledComponents

object StyledSurface {

    // variants
    enum class Variant {
        Default
    }
}

// ----------------------
// Renderer
// ----------------------

sealed interface StyledSurfaceRenderer {

    interface TokenRenderer : StyledSurfaceRenderer {

        @Composable
        fun render(
            modifier: Modifier,
            shape: Shape,
            backgroundColor: Color,
            contentColor: Color,
            border: BorderStroke?,
            content: @Composable () -> Unit,
        )
    }

    interface WrapperRenderer : StyledSurfaceRenderer {

        @Composable
        fun render(
            variant: StyledSurface.Variant,
            modifier: Modifier,
            shape: Shape,
            backgroundColor: Color,
            contentColor: Color,
            border: BorderStroke?,
            content: @Composable () -> Unit,
        )
    }
}

// ----------------------
// Defaults
// ----------------------

object StyledSurfaceDefaults {
    val shape: Shape = RectangleShape

    @Composable
    fun backgroundColor(): Color = StyledTheme.colors.background

    @Composable
    fun contentColor(): Color = StyledTheme.colors.onBackground

    val variant: StyledSurface.Variant = StyledSurface.Variant.Default
}

// ----------------------
// Composable
// ----------------------

@Composable
fun StyledSurface(
    modifier: Modifier = Modifier,
    variant: StyledSurface.Variant = StyledSurfaceDefaults.variant,
    shape: Shape = StyledSurfaceDefaults.shape,
    backgroundColor: Color = StyledSurfaceDefaults.backgroundColor(),
    contentColor: Color = StyledSurfaceDefaults.contentColor(),
    border: BorderStroke? = null,
    content: @Composable () -> Unit,
) {
    when (val renderer = LocalStyledComponents.current.surface) {
        is StyledSurfaceRenderer.TokenRenderer -> {
            renderer.render(
                modifier = modifier,
                shape = shape,
                backgroundColor = backgroundColor,
                contentColor = contentColor,
                border = border,
                content = content,
            )
        }

        is StyledSurfaceRenderer.WrapperRenderer -> {
            renderer.render(
                variant = variant,
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
