package com.michaelflisar.composestyled.theme.material3.components

import androidx.compose.foundation.Indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.composeunstyled.UnstyledCheckbox
import com.michaelflisar.composestyled.core.classes.colors.BaseColorDef
import com.michaelflisar.composestyled.core.classes.colors.StatefulBaseColorDef
import com.michaelflisar.composestyled.core.components.StyledCheckbox
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.core.tokens.StyledColors

internal object StyledCheckboxImpl {

    @OptIn(InternalComposeStyledApi::class)
    @Composable
    fun registerVariantStyles(colors: StyledColors) {
        val unchecked = StatefulBaseColorDef(
            normal = BaseColorDef(
                background = Color.Transparent,
                foreground = colors.onSurface,
                border = colors.outline,
            ),
            focused = BaseColorDef(
                background = Color.Transparent,
                foreground = colors.onSurface,
                border = colors.primary,
            ),
            error = BaseColorDef(
                background = Color.Transparent,
                foreground = colors.onSurface,
                border = colors.error,
            )
        )

        val checked = StatefulBaseColorDef(
            normal = BaseColorDef(
                background = colors.primary,
                foreground = colors.onPrimary,
                border = colors.primary,
            ),
            focused = BaseColorDef(
                background = colors.primary,
                foreground = colors.onPrimary,
                border = colors.primary,
            ),
            error = BaseColorDef(
                background = colors.primary,
                foreground = colors.onPrimary,
                border = colors.error,
            )
        )

        StyledCheckbox.registerVariantStyles(
            unchecked = unchecked,
            checked = checked,
        )
    }


    /**
     * Android/Unstyled-based checkbox implementation.
     *
     */
    @Composable
    fun Render(
        checked: Boolean,
        modifier: Modifier,
        backgroundColor: Color,
        contentColor: Color,
        enabled: Boolean,
        onCheckedChange: ((Boolean) -> Unit)?,
        shape: Shape,
        borderColor: Color,
        borderWidth: Dp,
        interactionSource: MutableInteractionSource?,
        indication: Indication?,
        contentDescription: String?,
        checkIcon: @Composable () -> Unit,
    ) {
        UnstyledCheckbox(
            checked = checked,
            modifier = modifier,
            backgroundColor = backgroundColor,
            contentColor = contentColor,
            enabled = enabled,
            onCheckedChange = onCheckedChange,
            shape = shape,
            borderColor = borderColor,
            borderWidth = borderWidth,
            interactionSource = interactionSource,
            indication = indication,
            contentDescription = contentDescription,
            checkIcon = checkIcon,
        )
    }
}
