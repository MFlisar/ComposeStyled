package com.michaelflisar.composestyled.theme.cupertino.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.michaelflisar.composestyled.core.components.StyledSurfaceTokenRenderer
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi

internal object StyledSurfaceImpl : StyledSurfaceTokenRenderer {

    @InternalComposeStyledApi
    @Composable
    override fun registerVariantStyles() {
        // No variant for surfaces
    }

    @Composable
    override fun Render(
        modifier: Modifier,
        shape: Shape,
        backgroundColor: Color,
        contentColor: Color,
        border: BorderStroke?,
        content: @Composable (() -> Unit),
    ) {
        // TODO
    }
}