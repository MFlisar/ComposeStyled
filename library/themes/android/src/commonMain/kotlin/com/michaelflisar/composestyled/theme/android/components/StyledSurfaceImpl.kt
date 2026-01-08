package com.michaelflisar.composestyled.theme.android.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.michaelflisar.composestyled.core.runtime.ProvideStyledLocals

@Composable
internal fun StyledSurfaceImpl(
    modifier: Modifier,
    shape: Shape,
    color: Color,
    contentColor: Color,
    border: BorderStroke?,
    content: @Composable () -> Unit,
) {
    ProvideStyledLocals(
        contentColor = contentColor,
        backgroundColor = color,
    ) {
        Box(
            modifier
                .clip(shape)
                .background(color)
                .then(if (border != null) Modifier.border(border, shape) else Modifier)
        ) {
            content()
        }
    }
}
