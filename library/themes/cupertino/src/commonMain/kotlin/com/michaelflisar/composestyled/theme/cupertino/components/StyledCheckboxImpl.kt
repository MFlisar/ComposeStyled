package com.michaelflisar.composestyled.theme.cupertino.components

import androidx.compose.foundation.Indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyled.core.classes.colors.StatefulBaseColorDef
import com.michaelflisar.composestyled.core.components.StyledCard
import com.michaelflisar.composestyled.core.components.StyledCheckbox
import com.michaelflisar.composestyled.core.components.StyledCheckboxRenderer
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.shared.UnstyledCheckboxRenderer

@OptIn(InternalComposeStyledApi::class)
internal object StyledCheckboxImpl : UnstyledCheckboxRenderer() {

    @Composable
    override fun resolveColors(
        variant: StyledCheckbox.Variant,
        checked: Boolean,
        customization: StyledCheckbox.Customization?
    ): StatefulBaseColorDef {
        // TODO
        throw NotImplementedError("StyledCheckboxImpl.resolveColors is not implemented yet")
    }
}
