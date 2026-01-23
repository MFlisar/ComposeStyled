package com.michaelflisar.composestyled.theme.cupertino.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.core.classes.StyledResolveState
import com.michaelflisar.composestyled.core.classes.colors.StatefulBaseColorDef
import com.michaelflisar.composestyled.core.components.StyledButton
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.shared.UnstyledButtonRenderer
import com.michaelflisar.composestyled.theme.cupertino.CupertinoUtil

@OptIn(InternalComposeStyledApi::class)
internal object StyledButtonImpl : UnstyledButtonRenderer() {

    @Composable
    override fun resolveColors(
        variant: StyledButton.Variant,
        customization: StyledButton.Customization?
    ): StatefulBaseColorDef {
        return CupertinoUtil.resolveButtonColors(variant, customization)
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
