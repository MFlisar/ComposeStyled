package com.michaelflisar.composestyled.theme.material3

import com.michaelflisar.composestyled.core.classes.StyledThemeDefinition
import com.michaelflisar.composestyled.core.renderer.StyledComponents
import com.michaelflisar.composestyled.core.tokens.StyledElevations
import com.michaelflisar.composestyled.core.tokens.StyledPaddings
import com.michaelflisar.composestyled.core.tokens.StyledShapes
import com.michaelflisar.composestyled.core.tokens.StyledSpacings
import com.michaelflisar.composestyled.core.tokens.StyledTypography
import com.michaelflisar.composestyled.theme.material3.components.StyledButtonImpl
import com.michaelflisar.composestyled.theme.material3.components.StyledCardImpl
import com.michaelflisar.composestyled.theme.material3.components.StyledCheckboxImpl
import com.michaelflisar.composestyled.theme.material3.components.StyledIconImpl
import com.michaelflisar.composestyled.theme.material3.components.StyledSeparatorImpl
import com.michaelflisar.composestyled.theme.material3.components.StyledSurfaceImpl
import com.michaelflisar.composestyled.theme.material3.components.StyledTextFieldImpl
import com.michaelflisar.composestyled.theme.material3.components.StyledTextImpl

object Definitions {

    val components = StyledComponents.Components(
        surface = StyledSurfaceImpl,
        button = StyledButtonImpl,
        card = StyledCardImpl,
        checkbox = StyledCheckboxImpl,
        icon = StyledIconImpl,
        separator = StyledSeparatorImpl,
        text = StyledTextImpl,
        textField = StyledTextFieldImpl,
    )

    val shapes = StyledShapes()
    val typography = StyledTypography()
    val paddings = StyledPaddings()
    val spacings = StyledSpacings()
    val elevations = StyledElevations()
}

val StyleMaterial3 = StyledThemeDefinition(
    components = Definitions.components,
    shapes = Definitions.shapes,
    typography = Definitions.typography,
    paddings = Definitions.paddings,
    elevations = Definitions.elevations,
    spacings = Definitions.spacings
)
