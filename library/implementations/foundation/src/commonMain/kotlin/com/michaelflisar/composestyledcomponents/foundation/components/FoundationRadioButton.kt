package com.michaelflisar.composestyledcomponents.foundation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.input.InputMode
import androidx.compose.ui.platform.LocalInputModeManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyledcomponents.ui.components.StyledRadioButton

@InternalComposeStyledApi
class FoundationRadioButton : StyledRadioButton.Implementation {
    @Composable
    override fun Render(
        selected: Boolean,
        onClick: (() -> Unit)?,
        modifier: Modifier,
        enabled: Boolean,
        tint: Color,
        interactionSource: MutableInteractionSource?,
    ) {
        val source = interactionSource ?: remember { MutableInteractionSource() }
        val focused by source.collectIsFocusedAsState()
        val hovered by source.collectIsHoveredAsState()
        val pressed by source.collectIsPressedAsState()
        val interactive = enabled && onClick != null
        val keyboardFocus = focused && enabled && onClick != null &&
            LocalInputModeManager.current.inputMode == InputMode.Keyboard
        val colors = StyledTheme.colorScheme
        val alpha = if (enabled) 1f else colors.disableFactors.content
        val active = tint.copy(alpha = tint.alpha * alpha)
        val inactive = colors.outline.copy(alpha = colors.outline.alpha * alpha)
        val progress by animateFloatAsState(if (selected) 1f else 0f, label = "StyledRadioButton")
        val interactionProgress by animateFloatAsState(
            when {
                !interactive -> 0f
                pressed -> 0.12f
                hovered -> 0.08f
                else -> 0f
            },
            label = "StyledRadioButtonInteraction",
        )
        val minimum = StyledTheme.sizes.minimumInteractiveSize
        val interactiveModifier = if (onClick != null) {
            Modifier.sizeIn(minWidth = minimum, minHeight = minimum)
                .hoverable(source, enabled = enabled)
                .selectable(
                    selected = selected,
                    enabled = enabled,
                    role = Role.RadioButton,
                    interactionSource = source,
                    indication = null,
                    onClick = onClick,
                )
        } else {
            Modifier
        }
        Box(
            modifier = modifier
                .then(interactiveModifier)
                .then(if (keyboardFocus) Modifier.border(2.dp, active, CircleShape) else Modifier),
            contentAlignment = Alignment.Center,
        ) {
            Canvas(Modifier.size(40.dp)) {
                val center = Offset(size.width / 2f, size.height / 2f)
                if (interactionProgress > 0f) {
                    drawCircle(
                        color = colors.onSurface.copy(alpha = interactionProgress),
                        radius = size.minDimension / 2f,
                        center = center,
                    )
                }
                val radius = 10.dp.toPx() - 1.dp.toPx()
                drawCircle(
                    color = lerp(inactive, active, progress),
                    radius = radius,
                    center = center,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(2.dp.toPx()),
                )
                if (progress > 0f) {
                    drawCircle(
                        color = active,
                        radius = radius * 0.55f * progress,
                        center = center,
                    )
                }
            }
        }
    }
}
