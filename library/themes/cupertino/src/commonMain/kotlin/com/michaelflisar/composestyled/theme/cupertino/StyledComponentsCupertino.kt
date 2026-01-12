package com.michaelflisar.composestyled.theme.cupertino

import com.michaelflisar.composestyled.core.renderer.StyledTokenComponents
import com.michaelflisar.composestyled.theme.cupertino.components.StyledButtonImpl
import com.michaelflisar.composestyled.theme.cupertino.components.StyledCardImpl
import com.michaelflisar.composestyled.theme.cupertino.components.StyledCheckboxImpl
import com.michaelflisar.composestyled.theme.cupertino.components.StyledIconImpl
import com.michaelflisar.composestyled.theme.cupertino.components.StyledSeparatorImpl
import com.michaelflisar.composestyled.theme.cupertino.components.StyledSurfaceImpl
import com.michaelflisar.composestyled.theme.cupertino.components.StyledTextFieldImpl
import com.michaelflisar.composestyled.theme.cupertino.components.StyledTextImpl

val StyledComponentsCupertino = StyledTokenComponents(
    surface = StyledSurfaceImpl,
    button = StyledButtonImpl,
    card = StyledCardImpl,
    checkbox = StyledCheckboxImpl,
    icon = StyledIconImpl,
    separator = StyledSeparatorImpl,
    text = StyledTextImpl,
    textField = StyledTextFieldImpl,
)