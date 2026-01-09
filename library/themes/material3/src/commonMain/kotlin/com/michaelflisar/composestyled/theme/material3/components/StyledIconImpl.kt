package com.michaelflisar.composestyled.theme.material3.components

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import com.composeunstyled.Icon
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.components.StyledIcon
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.core.tokens.StyledColors

internal object StyledIconImpl {

    @OptIn(InternalComposeStyledApi::class)
    @Composable
    fun registerVariantStyles() {

        val colors = StyledTheme.colors

        StyledIcon.registerVariantStyles(
            default = colors.onSurface,
            muted = colors.onSurfaceVariant,
            error = colors.error,
            onPrimary = colors.onPrimary,
        )
    }

    @Composable
    fun Render(
        painter: Painter,
        contentDescription: String?,
        modifier: Modifier,
        tint: Color,
        size: Dp,
    ) {
        Icon(
            painter = painter,
            contentDescription = contentDescription,
            modifier = modifier.size(size),
            tint = tint,
        )
    }

    @Composable
    fun Render(
        imageVector: ImageVector,
        contentDescription: String?,
        modifier: Modifier,
        tint: Color,
        size: Dp,
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            modifier = modifier.size(size),
            tint = tint,
        )
    }
}
