package com.michaelflisar.composestyledcomponents.material3.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyledcomponents.ui.components.StyledCheckbox

class Material3Checkbox internal constructor() : StyledCheckbox.Implementation {
    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { Material3Checkbox() }
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
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = modifier,
            enabled = enabled,
            colors = CheckboxDefaults.colors(checkedColor = tint, checkmarkColor = onTint),
            interactionSource = interactionSource,
        )
    }
}
