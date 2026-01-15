package com.michaelflisar.composestyled.theme.fluent2

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.renderer.StyledTokenComponents
import com.michaelflisar.composestyled.core.tokens.StyledElevations
import com.michaelflisar.composestyled.core.tokens.StyledPaddings
import com.michaelflisar.composestyled.core.tokens.StyledShapes
import com.michaelflisar.composestyled.core.tokens.StyledSpacings
import com.michaelflisar.composestyled.core.tokens.StyledTypography
import com.michaelflisar.composestyled.theme.fluent2.components.StyledButtonImpl
import com.michaelflisar.composestyled.theme.fluent2.components.StyledCardImpl
import com.michaelflisar.composestyled.theme.fluent2.components.StyledCheckboxImpl
import com.michaelflisar.composestyled.theme.fluent2.components.StyledIconImpl
import com.michaelflisar.composestyled.theme.fluent2.components.StyledSeparatorImpl
import com.michaelflisar.composestyled.theme.fluent2.components.StyledSurfaceImpl
import com.michaelflisar.composestyled.theme.fluent2.components.StyledTextFieldImpl
import com.michaelflisar.composestyled.theme.fluent2.components.StyledTextImpl

object StyleFluent2 {

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
        // Fluent 2: eher "crisp" + konsistent abgerundet (weniger "pill" als iOS/Cupertino)
        extraSmall = 2.dp,  // very small rounding (chips, small affordances)
        small = 4.dp,       // common controls
        medium = 6.dp,      // inputs/buttons baseline
        large = 8.dp,       // containers/cards
        extraLarge = 12.dp, // dialogs/sheets (still not fully pill)
    )

    val shapes = StyledShapes(
        sizes = sizes,
        // Controls (Buttons, Toggles) are moderately rounded in Fluent 2
        control = RoundedCornerShape(size = sizes.medium),

        // Inputs typically are not capsule-like; keep them slightly rounded
        input = RoundedCornerShape(size = sizes.medium),

        // Containers/Cards a bit larger rounding than controls
        container = RoundedCornerShape(size = sizes.large),

        indicator = RoundedCornerShape(size = sizes.extraSmall),

        // Dialogs/Sheets slightly more rounded than containers
        dialog = RoundedCornerShape(size = sizes.extraLarge),

        // Fluent 2: "rounded" eher als moderat-gerundet (nicht automatisch capsule).
        rounded = RoundedCornerShape(size = sizes.extraLarge),
    )

    val typography = StyledTypography()
    val paddings = StyledPaddings()
    val spacings = StyledSpacings()

    val elevations = StyledElevations(
        // Fluent uses subtle depth; keep it low but non-zero
        small = 1.dp,
        medium = 3.dp,
        large = 8.dp,
    )

    val setup = StyledTheme.Setup(
        shapes = shapes,
        typography = typography,
        paddings = paddings,
        elevations = elevations,
        spacings = spacings,
    )

}