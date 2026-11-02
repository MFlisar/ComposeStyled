package com.michaelflisar.composestyled.material3.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyled.UpdateStyledThemeLocals
import com.michaelflisar.composestyled.ui.components.StyledSurface
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi

class Material3Surface internal constructor() : StyledSurface.Implementation {

    override val defaults: StyledSurface.Defaults
        @Composable get() = StyledSurface.Defaults(shape = RectangleShape)

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { Material3Surface() }
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
            UpdateStyledThemeLocals(contentColor = contentColor, content = content)
        }
    }
}
