package com.michaelflisar.composestyled.theme.fluent2

import com.michaelflisar.composestyled.core.renderer.StyledTokenComponents
import com.michaelflisar.composestyled.theme.fluent2.components.StyledButtonImpl
import com.michaelflisar.composestyled.theme.fluent2.components.StyledCardImpl
import com.michaelflisar.composestyled.theme.fluent2.components.StyledCheckboxImpl
import com.michaelflisar.composestyled.theme.fluent2.components.StyledIconImpl
import com.michaelflisar.composestyled.theme.fluent2.components.StyledSeparatorImpl
import com.michaelflisar.composestyled.theme.fluent2.components.StyledSurfaceImpl
import com.michaelflisar.composestyled.theme.fluent2.components.StyledTextFieldImpl
import com.michaelflisar.composestyled.theme.fluent2.components.StyledTextImpl

val StyledComponentsFluent2 = StyledTokenComponents(
    surface = StyledSurfaceImpl,
    button = StyledButtonImpl,
    card = StyledCardImpl,
    checkbox = StyledCheckboxImpl,
    icon = StyledIconImpl,
    separator = StyledSeparatorImpl,
    text = StyledTextImpl,
    textField = StyledTextFieldImpl,
)