package com.michaelflisar.composestyled.theme.cupertino

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.renderer.StyledTokenComponents
import com.michaelflisar.composestyled.core.tokens.StyledElevations
import com.michaelflisar.composestyled.core.tokens.StyledPaddings
import com.michaelflisar.composestyled.core.tokens.StyledShapes
import com.michaelflisar.composestyled.core.tokens.StyledSpacings
import com.michaelflisar.composestyled.core.tokens.StyledTypography
import com.michaelflisar.composestyled.theme.cupertino.components.StyledButtonImpl
import com.michaelflisar.composestyled.theme.cupertino.components.StyledCardImpl
import com.michaelflisar.composestyled.theme.cupertino.components.StyledCheckboxImpl
import com.michaelflisar.composestyled.theme.cupertino.components.StyledIconImpl
import com.michaelflisar.composestyled.theme.cupertino.components.StyledSeparatorImpl
import com.michaelflisar.composestyled.theme.cupertino.components.StyledSurfaceImpl
import com.michaelflisar.composestyled.theme.cupertino.components.StyledTextFieldImpl
import com.michaelflisar.composestyled.theme.cupertino.components.StyledTextImpl

object StyleCupertino {

    val components = StyledTokenComponents(
        surface = StyledSurfaceImpl,
        button = StyledButtonImpl,
        card = StyledCardImpl,
        checkbox = StyledCheckboxImpl,
        icon = StyledIconImpl,
        separator = StyledSeparatorImpl,
        text = StyledTextImpl,
        textField = StyledTextFieldImpl,
    )

    val sizes = StyledShapes.Sizes(
        // Cupertino-Baseline: insgesamt etwas kleiner als Material3,
        // aber mit klarer "pill/rounded" Stufe nach oben.
        extraSmall = 4.dp,   // ähnlich M3 extraSmall: kleine UI-Details
        small = 8.dp,        // allgemeine Controls
        medium = 12.dp,      // Container/Card-Baseline
        large = 16.dp,       // größere Container/Sheets
        extraLarge = 20.dp,  // stärker gerundet (Inputs/Buttons können darauf mappen)
    )
    val shapes = StyledShapes(
        sizes = sizes,
        control = RoundedCornerShape(size = sizes.large),
        input = RoundedCornerShape(size = sizes.extraLarge),
        container = RoundedCornerShape(size = sizes.medium),
        indicator = RoundedCornerShape(size = sizes.extraSmall),
        dialog = RoundedCornerShape(size = sizes.large),
        rounded = RoundedCornerShape(percent = 50),
    )
    val typography = StyledTypography()
    val paddings = StyledPaddings()
    val spacings = StyledSpacings()

    val elevations = StyledElevations(
        small = 0.dp,
        medium = 0.dp,
        large = 0.dp,
    )

    val setup = StyledTheme.Setup(
        shapes = shapes,
        typography = typography,
        paddings = paddings,
        elevations = elevations,
        spacings = spacings,
    )
}

