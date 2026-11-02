package com.michaelflisar.composestyled.material3.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.michaelflisar.composestyled.ui.components.StyledToggleBehavior
import com.michaelflisar.composestyled.ui.components.StyledRadioButton

class Material3RadioButton internal constructor() : StyledRadioButton.Implementation {

    override val defaults: StyledRadioButton.Defaults
        @Composable get() = StyledRadioButton.Defaults(
            clickBehavior = StyledToggleBehavior.ControlOnly,
            shape = RoundedCornerShape(percent = 50),
        )

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { Material3RadioButton() }
    }

    @Composable
    override fun Render(
        selected: Boolean,
        onClick: (() -> Unit)?,
        modifier: Modifier,
        enabled: Boolean,
        tint: Color,
        interactionSource: MutableInteractionSource?,
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            colors = RadioButtonDefaults.colors(selectedColor = tint),
            interactionSource = interactionSource,
        )
    }
}
