package com.michaelflisar.composestyled.core.classes

import com.michaelflisar.composestyled.core.renderer.StyledComponents
import com.michaelflisar.composestyled.core.tokens.StyledElevations
import com.michaelflisar.composestyled.core.tokens.StyledPaddings
import com.michaelflisar.composestyled.core.tokens.StyledShapes
import com.michaelflisar.composestyled.core.tokens.StyledSpacings
import com.michaelflisar.composestyled.core.tokens.StyledTypography

data class StyledThemeDefinition<Components : StyledComponents>(
    val components: Components,
    val shapes: StyledShapes,
    val typography: StyledTypography,
    val paddings: StyledPaddings,
    val elevations: StyledElevations,
    val spacings: StyledSpacings,
)