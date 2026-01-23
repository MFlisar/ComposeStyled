package com.michaelflisar.composestyled.shared

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
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
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.components.StyledCardRenderer
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.core.runtime.ProvideStyledLocals

@InternalComposeStyledApi
abstract class UnstyledCardRenderer : StyledCardRenderer.TokenRenderer {

    @Composable
    override fun render(
        modifier: Modifier,
        onClick: (() -> Unit)?,
        shape: Shape,
        backgroundColor: Color,
        contentColor: Color,
        borderStroke: BorderStroke?,
        elevation: Dp,
        contentPadding: PaddingValues,
        interactionSource: MutableInteractionSource,
        content: @Composable ColumnScope.() -> Unit,
    ) {
        ProvideStyledLocals(
            contentColor = contentColor,
            backgroundColor = backgroundColor,
        ) {
            Box(
                modifier = modifier
                    .shadow(elevation = elevation, shape = shape)
                    .then(
                        if (borderStroke != null) Modifier.border(
                            borderStroke,
                            shape
                        ) else Modifier
                    )
                    .clip(shape)
                    .background(backgroundColor)
                    .then(
                        if (onClick == null)
                            Modifier
                        else Modifier.clickable(
                            interactionSource = interactionSource,
                            indication = StyledTheme.indications.select(backgroundColor),
                            onClick = onClick
                        )
                    )
            ) {
                Column(
                    modifier = Modifier.padding(contentPadding)
                ) {
                    content()
                }
            }
        }
    }
}