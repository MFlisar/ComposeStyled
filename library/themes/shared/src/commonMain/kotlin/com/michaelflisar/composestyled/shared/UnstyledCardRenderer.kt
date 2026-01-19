package com.michaelflisar.composestyled.shared

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyled.core.classes.Emphasis
import com.michaelflisar.composestyled.core.components.StyledCardRenderer
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.core.runtime.ProvideStyledLocals

@InternalComposeStyledApi
abstract class UnstyledCardRenderer : StyledCardRenderer.TokenRenderer {

    @Composable
    abstract fun calcBackgroundColor(backgroundColor: Color, emphasis: Emphasis): Color

    @Composable
    abstract fun calcElevation(emphasis: Emphasis): Dp

    @Composable
    override fun render(
        modifier: Modifier,
        shape: Shape,
        emphasis: Emphasis,
        backgroundColor: Color,
        contentColor: Color,
        border: BorderStroke?,
        contentPadding: PaddingValues,
        content: @Composable ColumnScope.() -> Unit,
    ) {
        val bg = calcBackgroundColor(backgroundColor, emphasis)
        val elevation = calcElevation(emphasis)
        ProvideStyledLocals(
            contentColor = contentColor,
            backgroundColor = bg,
        ) {
            Column(
                modifier = modifier
                    .shadow(elevation = elevation, shape = shape, clip = false)
                    .clip(shape)
                    .background(bg)
                    .then(if (border != null) Modifier.border(border, shape) else Modifier)
                    .padding(contentPadding),
                content = content,
            )
        }
    }
}