package com.michaelflisar.composestyled.shared

import androidx.compose.foundation.Indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.composeunstyled.UnstyledCheckbox
import com.michaelflisar.composestyled.core.components.StyledCheckboxRenderer
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.core.runtime.ProvideStyledLocals

@InternalComposeStyledApi
abstract class UnstyledCheckboxRenderer : StyledCheckboxRenderer.TokenRenderer {

    @Composable
    override fun render(
        checked: Boolean,
        modifier: Modifier,
        backgroundColor: Color,
        contentColor: Color,
        enabled: Boolean,
        onCheckedChange: ((Boolean) -> Unit)?,
        shape: Shape,
        borderColor: Color?,
        borderWidth: Dp,
        interactionSource: MutableInteractionSource,
        indication: Indication?,
        contentDescription: String?,
        checkIcon: @Composable () -> Unit,
    ) {
        ProvideStyledLocals(
            contentColor = contentColor,
            backgroundColor = backgroundColor,
        ) {
            UnstyledCheckbox(
                checked = checked,
                modifier = modifier,
                backgroundColor = backgroundColor,
                contentColor = contentColor,
                enabled = enabled,
                onCheckedChange = onCheckedChange,
                shape = shape,
                borderColor = borderColor ?: Color.Unspecified,
                borderWidth = borderWidth,
                interactionSource = interactionSource,
                indication = indication,
                contentDescription = contentDescription,
                checkIcon = checkIcon,
            )
        }
    }
}