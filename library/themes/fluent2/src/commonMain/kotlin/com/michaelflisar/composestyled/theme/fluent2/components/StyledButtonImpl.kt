package com.michaelflisar.composestyled.theme.fluent2.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.michaelflisar.composestyled.core.components.StyledButtonTokenRenderer
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi

internal object StyledButtonImpl : StyledButtonTokenRenderer {

    @OptIn(InternalComposeStyledApi::class)
    @Composable
    override fun registerVariantStyles() {
        // TODO
    }

    @Composable
    override fun Render(
        onClick: () -> Unit,
        modifier: Modifier,
        enabled: Boolean,
        shape: Shape,
        backgroundColor: Color,
        contentColor: Color,
        borderColor: Color?,
        contentPadding: PaddingValues,
        interactionSource: MutableInteractionSource,
        content: @Composable (RowScope.() -> Unit),
    ) {
        // TODO
    }

}
