package com.michaelflisar.composestyled.theme.material3.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.michaelflisar.composestyled.core.components.StyledSurfaceTokenRenderer
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.core.runtime.LocalBackgroundColor
import com.michaelflisar.composestyled.core.runtime.LocalContentColor

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
        val m = modifier
            .clip(shape)
            .background(backgroundColor)
            .then(
                if (border != null) {
                    Modifier.border(border = border, shape = shape)
                } else {
                    Modifier
                }
            )

        CompositionLocalProvider(
            LocalContentColor provides contentColor,
            LocalBackgroundColor provides backgroundColor
        ) {
            Box(modifier = m) {
                content()
            }
        }
    }


}