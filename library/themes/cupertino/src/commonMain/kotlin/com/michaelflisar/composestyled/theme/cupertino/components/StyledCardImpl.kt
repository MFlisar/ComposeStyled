package com.michaelflisar.composestyled.theme.cupertino.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyled.core.classes.Emphasis
import com.michaelflisar.composestyled.core.classes.colors.StatefulBaseColorDef
import com.michaelflisar.composestyled.core.components.StyledCard
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.shared.UnstyledCardRenderer

@OptIn(InternalComposeStyledApi::class)
internal object StyledCardImpl : UnstyledCardRenderer() {

    @Composable
    override fun resolveColors(
        variant: StyledCard.Variant,
        customization: StyledCard.Customization?,
    ): StatefulBaseColorDef {
        // TODO
        throw NotImplementedError("StyledCardImpl.resolveColors is not implemented yet")
    }

    @Composable
    override fun calcBackgroundColor(backgroundColor: Color, emphasis: Emphasis): Color {
        // TODO
        throw NotImplementedError("StyledCardImpl.calcBackgroundColor is not implemented yet")
    }

    @Composable
    override fun calcElevation(emphasis: Emphasis): Dp {
        // TODO
        throw NotImplementedError("StyledCardImpl.calcElevation is not implemented yet")
    }
}
