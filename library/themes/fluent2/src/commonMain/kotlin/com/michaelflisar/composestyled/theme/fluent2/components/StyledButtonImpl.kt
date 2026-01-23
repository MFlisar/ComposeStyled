package com.michaelflisar.composestyled.theme.fluent2.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.core.classes.StyledResolveState
import com.michaelflisar.composestyled.core.classes.colors.StatefulBaseColorDef
import com.michaelflisar.composestyled.core.components.StyledButton
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.shared.UnstyledButtonRenderer
import com.michaelflisar.composestyled.theme.fluent2.Fluent2Util

@OptIn(InternalComposeStyledApi::class)
internal object StyledButtonImpl : UnstyledButtonRenderer() {

    @Composable
    override fun resolveColors(
        variant: StyledButton.Variant,
        customization: StyledButton.Customization?,
    ): StatefulBaseColorDef {
        return Fluent2Util.resolveButtonColors(variant, customization)
    }

    @Composable
    override fun resolveBorderWidth(
        variant: StyledButton.Variant,
        customization: StyledButton.Customization?,
        state: StyledResolveState,
    ): Dp? {
        return 1.dp
    }
}
