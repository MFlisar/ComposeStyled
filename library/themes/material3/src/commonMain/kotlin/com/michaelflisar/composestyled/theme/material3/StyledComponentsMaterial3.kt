package com.michaelflisar.composestyled.theme.material3

import com.michaelflisar.composestyled.core.renderer.StyledTokenComponents
import com.michaelflisar.composestyled.theme.material3.components.StyledButtonImpl
import com.michaelflisar.composestyled.theme.material3.components.StyledCardImpl
import com.michaelflisar.composestyled.theme.material3.components.StyledCheckboxImpl
import com.michaelflisar.composestyled.theme.material3.components.StyledIconImpl
import com.michaelflisar.composestyled.theme.material3.components.StyledSeparatorImpl
import com.michaelflisar.composestyled.theme.material3.components.StyledSurfaceImpl
import com.michaelflisar.composestyled.theme.material3.components.StyledTextFieldImpl
import com.michaelflisar.composestyled.theme.material3.components.StyledTextImpl

val StyledComponentsMaterial3 = StyledTokenComponents(
    surface = StyledSurfaceImpl,
    button = StyledButtonImpl,
    card = StyledCardImpl,
    checkbox = StyledCheckboxImpl,
    icon = StyledIconImpl,
    separator = StyledSeparatorImpl,
    text = StyledTextImpl,
    textField = StyledTextFieldImpl,
)