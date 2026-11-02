package com.michaelflisar.composestyled.foundation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyled.UpdateStyledThemeLocals
import com.michaelflisar.composestyled.ui.components.StyledSurface
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi

@InternalComposeStyledApi
abstract class FoundationSurface : StyledSurface.Implementation {

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
        UpdateStyledThemeLocals(contentColor = contentColor) {
            Box(
                modifier = modifier
                    .shadow(shadowElevation, shape, clip = false)
                    .then(if (border != null) Modifier.border(border, shape) else Modifier)
                    .background(color, shape)
                    .clip(shape)
                    .semantics { isTraversalGroup = true }
                    .pointerInput(Unit) {},
                propagateMinConstraints = true,
            ) {
                content()
            }
        }
    }
}
