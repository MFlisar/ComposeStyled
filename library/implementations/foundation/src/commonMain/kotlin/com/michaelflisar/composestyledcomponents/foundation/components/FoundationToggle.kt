package com.michaelflisar.composestyledcomponents.foundation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.InputMode
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.LocalInputModeManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.ui.components.StyledCheckbox
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi

@InternalComposeStyledApi
class FoundationCheckbox : StyledCheckbox.Implementation {
    @Composable
    override fun Render(
        checked: Boolean,
        onCheckedChange: ((Boolean) -> Unit)?,
        modifier: Modifier,
        enabled: Boolean,
        tint: Color,
        onTint: Color,
        interactionSource: MutableInteractionSource?,
    ) = FoundationToggle(
        checked, onCheckedChange, modifier, enabled, tint, onTint, interactionSource,
    )
}

@OptIn(InternalComposeStyledApi::class)
@Composable
private fun FoundationToggle(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier,
    enabled: Boolean,
    tint: Color,
    onTint: Color,
    interactionSource: MutableInteractionSource?,
) {
    val source = interactionSource ?: remember { MutableInteractionSource() }
    val focused by source.collectIsFocusedAsState()
    val keyboardFocus = focused && enabled && onCheckedChange != null &&
        LocalInputModeManager.current.inputMode == InputMode.Keyboard
    val progress by animateFloatAsState(if (checked) 1f else 0f, label = "StyledToggle")
    val colors = StyledTheme.colorScheme
    val alpha = if (enabled) 1f else colors.disableFactors.content
    val active = tint.copy(alpha = tint.alpha * alpha)
    val inactive = colors.outline.copy(alpha = colors.outline.alpha * alpha)
    val foreground = onTint.copy(alpha = onTint.alpha * alpha)
    val minimum = StyledTheme.sizes.minimumInteractiveSize
    val interactiveModifier = if (onCheckedChange != null) {
        Modifier.sizeIn(minWidth = minimum, minHeight = minimum)
            .toggleable(
                value = checked,
                enabled = enabled,
                role = Role.Checkbox,
                interactionSource = source,
                indication = null,
                onValueChange = onCheckedChange,
            )
    } else Modifier
    Box(
        modifier = modifier.then(interactiveModifier).then(
            if (keyboardFocus) Modifier.border(2.dp, active, RoundedCornerShape(4.dp)) else Modifier
        ),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(Modifier.size(20.dp)) {
            val inset = 1.dp.toPx()
            drawRoundRect(
                color = if (checked) active else inactive,
                topLeft = Offset(inset, inset),
                size = Size(size.width - 2 * inset, size.height - 2 * inset),
                cornerRadius = CornerRadius(3.dp.toPx()),
                style = if (checked) androidx.compose.ui.graphics.drawscope.Fill else Stroke(2.dp.toPx()),
            )
            if (progress > 0f) {
                val path = Path().apply {
                    moveTo(size.width * 0.23f, size.height * 0.5f)
                    lineTo(size.width * 0.43f, size.height * 0.7f)
                    lineTo(size.width * 0.78f, size.height * 0.3f)
                }
                drawPath(
                    path,
                    foreground.copy(alpha = foreground.alpha * progress),
                    style = Stroke(2.dp.toPx(), cap = StrokeCap.Round),
                )
            }
        }
    }
}
