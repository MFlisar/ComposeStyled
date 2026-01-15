package com.michaelflisar.composestyled.core.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.classes.IVariant
import com.michaelflisar.composestyled.core.renderer.LocalStyledComponents
import com.michaelflisar.composestyled.core.renderer.StyledTokenComponents
import com.michaelflisar.composestyled.core.renderer.StyledTokenRenderer
import com.michaelflisar.composestyled.core.renderer.StyledWrapperComponents
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi

object StyledSurface {

    // variants
    enum class Variant(
        override val id: String,
    ) : IVariant {
        Default("surface.default"),
    }

    data class Colors(
        val background: Color,
        val content: Color,
    )

    @InternalComposeStyledApi
    @Composable
    fun registerVariantStyles(
        default: Colors,
    ) {
        // --
    }
}

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
        variant: StyledSurface.Variant,
        modifier: Modifier,
        shape: Shape,
        backgroundColor: Color,
        contentColor: Color,
        border: BorderStroke?,
        content: @Composable () -> Unit,
    )
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

    val DefaultVariant: StyledSurface.Variant = StyledSurface.Variant.Default
}

// ----------------------
// Composable
// ----------------------

@Composable
fun StyledSurface(
    modifier: Modifier = Modifier,
    variant: StyledSurface.Variant = StyledSurfaceDefaults.DefaultVariant,
    shape: Shape = StyledSurfaceDefaults.shape,
    backgroundColor: Color = StyledSurfaceDefaults.backgroundColor(),
    contentColor: Color = StyledSurfaceDefaults.contentColor(),
    border: BorderStroke? = null,
    content: @Composable () -> Unit,
) {
    when (val components = LocalStyledComponents.current) {
        is StyledTokenComponents -> {
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
