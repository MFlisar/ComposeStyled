package com.michaelflisar.composestyledcomponents.material3.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyledcomponents.ProvideCustomStyledContentLocals
import com.michaelflisar.composestyledcomponents.ui.components.StyledSurface
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi

class Material3Surface internal constructor(
    override val defaults: StyledSurface.Defaults,
) : StyledSurface.Implementation {

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create(defaults: StyledSurface.Defaults = defaults()) =
            remember(defaults) { Material3Surface(defaults) }

        // -------------
        // 3) defaults
        // -------------

        @Composable
        fun defaults(shape: Shape = RectangleShape) = StyledSurface.Defaults(shape)
    }

    @OptIn(InternalComposeStyledApi::class)
    @Composable
    override fun Render(
        modifier: Modifier,
        shape: Shape,
        color: Color,
        contentColor: Color,
        border: BorderStroke?,
        shadowElevation: Dp,
        content: @Composable () -> Unit,
    ) {
        Surface(
            modifier = modifier,
            shape = shape,
            color = color,
            contentColor = contentColor,
            border = border,
            shadowElevation = shadowElevation,
        ) {
            ProvideCustomStyledContentLocals(contentColor = contentColor, content = content)
        }
    }
}
