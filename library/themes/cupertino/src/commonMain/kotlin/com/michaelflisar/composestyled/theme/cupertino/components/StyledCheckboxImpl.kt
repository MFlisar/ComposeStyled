package com.michaelflisar.composestyled.theme.cupertino.components

import androidx.compose.foundation.Indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyled.core.components.StyledCheckboxTokenRenderer
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi

internal object StyledCheckboxImpl : StyledCheckboxTokenRenderer {

    @OptIn(InternalComposeStyledApi::class)
    @Composable
    override fun registerVariantStyles() {
        // TODO
    }

    @Composable
    override fun Render(
        checked: Boolean,
        modifier: Modifier,
        backgroundColor: Color,
        contentColor: Color,
        enabled: Boolean,
        onCheckedChange: ((Boolean) -> Unit)?,
        shape: Shape,
        borderColor: Color?,
        borderWidth: Dp,
        interactionSource: MutableInteractionSource,
        indication: Indication?,
        contentDescription: String?,
        checkIcon: @Composable () -> Unit,
    ) {
        // TODO
    }
}
