package com.michaelflisar.composestyled.theme.cupertino.components

import androidx.compose.runtime.Composable
import com.michaelflisar.composestyled.core.classes.colors.StatefulBaseColorDef
import com.michaelflisar.composestyled.core.components.StyledTextField
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.shared.UnstyledTextFieldRenderer

@OptIn(InternalComposeStyledApi::class)
internal object StyledTextFieldImpl : UnstyledTextFieldRenderer() {

    @Composable
    override fun resolveColors(
        variant: StyledTextField.Variant,
        customization: StyledTextField.Customization?,
    ): StatefulBaseColorDef {
        TODO("Not yet implemented")
    }
}
