package com.michaelflisar.composestyled.theme.material3.components

import androidx.compose.runtime.Composable
import com.michaelflisar.composestyled.core.classes.colors.StatefulBaseColorDef
import com.michaelflisar.composestyled.core.components.StyledButton
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.shared.UnstyledButtonRenderer
import com.michaelflisar.composestyled.theme.material3.Material3Util

@OptIn(InternalComposeStyledApi::class)
internal object StyledButtonImpl : UnstyledButtonRenderer() {

    @Composable
    override fun resolveColors(
        variant: StyledButton.Variant,
        customization: StyledButton.Customization?,
    ): StatefulBaseColorDef {
        return Material3Util.resolveButtonColors(variant, customization)
    }
}