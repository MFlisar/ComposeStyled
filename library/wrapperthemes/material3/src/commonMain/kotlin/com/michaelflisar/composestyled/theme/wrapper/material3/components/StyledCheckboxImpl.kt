package com.michaelflisar.composestyled.theme.wrapper.material3.components

import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.core.components.StyledCheckbox
import com.michaelflisar.composestyled.core.components.StyledCheckboxWrapperRenderer
import com.michaelflisar.composestyled.theme.wrapper.material3.disabled

internal object StyledCheckboxImpl : StyledCheckboxWrapperRenderer {

    @Composable
    override fun Render(
        variant: StyledCheckbox.Variant,
        customization: StyledCheckbox.Customization?,
        checked: Boolean,
        onCheckedChange: ((Boolean) -> Unit)?,
        modifier: Modifier,
        enabled: Boolean,
        shape: Shape,
        borderWidth: Dp,
        interactionSource: MutableInteractionSource,
        indication: Indication?,
        contentDescription: String?,
        checkIcon: @Composable () -> Unit,
    ) {
        // Material3 Checkbox doesn't support arbitrary shapes/border/background in the same way.
        // We render a stylable container and place the M3 checkbox inside for correct semantics.
        val click = onCheckedChange

        val checkedColor = customization?.checked ?: Color.Unspecified
        val uncheckedColor = customization?.unchecked ?: Color.Unspecified
        val disabledCheckedColor = customization?.checked?.disabled() ?: Color.Unspecified
        val disabledUncheckedColor = customization?.unchecked?.disabled() ?: Color.Unspecified

        Box(
            modifier = modifier
                .then(
                    if (click != null) {
                        Modifier.clickable(
                            enabled = enabled,
                            interactionSource = interactionSource,
                            indication = indication,
                        ) { click(!checked) }
                    } else Modifier
                ),
            contentAlignment = Alignment.Center,
        ) {
            Checkbox(
                checked = checked,
                onCheckedChange = if (click != null) ({ click(it) }) else null,
                enabled = enabled,
                colors = CheckboxDefaults.colors(
                    checkedColor = checkedColor,
                    uncheckedColor = uncheckedColor,
                    checkmarkColor = checkedColor,
                    disabledCheckedColor = disabledCheckedColor,
                    disabledUncheckedColor = disabledUncheckedColor,
                    disabledIndeterminateColor = Color.Unspecified,
                ),
                modifier = Modifier.size(18.dp),
            )

            // If caller supplied a custom icon, overlay it.
            if (checked) {
                checkIcon()
            }
        }
    }
}

