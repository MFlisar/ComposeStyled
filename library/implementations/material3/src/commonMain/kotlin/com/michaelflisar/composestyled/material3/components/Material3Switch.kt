package com.michaelflisar.composestyled.material3.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.michaelflisar.composestyled.ui.components.StyledToggleBehavior
import com.michaelflisar.composestyled.ui.components.StyledSwitch

class Material3Switch internal constructor() : StyledSwitch.Implementation {

    override val defaults: StyledSwitch.Defaults
        @Composable get() = StyledSwitch.Defaults(
            clickBehavior = StyledToggleBehavior.ControlOnly,
            shape = RoundedCornerShape(percent = 50),
        )

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { Material3Switch() }
    }

    @Composable
    override fun Render(
        checked: Boolean,
        onCheckedChange: ((Boolean) -> Unit)?,
        modifier: Modifier,
        enabled: Boolean,
        tint: Color,
        onTint: Color,
        interactionSource: MutableInteractionSource?,
    ) {
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = modifier,
            enabled = enabled,
            colors = SwitchDefaults.colors(checkedTrackColor = tint, checkedThumbColor = onTint),
            interactionSource = interactionSource,
        )
    }
}
