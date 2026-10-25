package com.michaelflisar.composestyledcomponents.foundation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.InputMode
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.LocalInputModeManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi

@InternalComposeStyledApi
@Composable
fun FoundationSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier,
    enabled: Boolean,
    interactionSource: MutableInteractionSource?,
    trackColor: Color,
    thumbColor: Color,
    outlineColor: Color,
    trackWidth: Dp,
    trackHeight: Dp,
    thumbSize: Dp,
    thumbInset: Dp,
    pressedThumbExpansion: Dp = 0.dp,
    outlineWidth: Dp = 0.dp,
    thumbShadowColor: Color = Color.Transparent,
    animationDurationMillis: Int = 150,
) {
    val source = interactionSource ?: remember { MutableInteractionSource() }
    val focused by source.collectIsFocusedAsState()
    val pressed by source.collectIsPressedAsState()
    val interactive = enabled && onCheckedChange != null
    val keyboardFocus = interactive && focused &&
        LocalInputModeManager.current.inputMode == InputMode.Keyboard
    val colors = StyledTheme.colorScheme
    val progress by animateFloatAsState(
        if (checked) 1f else 0f, tween(animationDurationMillis), label = "Switch position"
    )
    val pressProgress by animateFloatAsState(
        if (interactive && pressed) 1f else 0f, tween(100), label = "Switch press"
    )
    val track by animateColorAsState(
        trackColor, tween(animationDurationMillis), label = "Switch track"
    )
    val thumb by animateColorAsState(
        thumbColor, tween(animationDurationMillis), label = "Switch thumb"
    )
    val outline by animateColorAsState(
        outlineColor, tween(animationDurationMillis), label = "Switch outline"
    )
    val rtl = LocalLayoutDirection.current == LayoutDirection.Rtl
    val minimum = StyledTheme.sizes.minimumInteractiveSize
    val inputModifier = if (onCheckedChange != null) {
        Modifier.sizeIn(minWidth = minimum, minHeight = minimum)
            .hoverable(source, enabled = enabled)
            .toggleable(
                value = checked,
                enabled = enabled,
                role = Role.Switch,
                interactionSource = source,
                indication = null,
                onValueChange = onCheckedChange,
            )
    } else Modifier
    Box(
        modifier = modifier.then(inputModifier).then(
            if (keyboardFocus) {
                Modifier.border(2.dp, colors.onSurface, RoundedCornerShape(4.dp))
            } else Modifier
        ),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(Modifier.size(trackWidth, trackHeight)) {
            val radius = size.height / 2f
            drawRoundRect(track, cornerRadius = CornerRadius(radius))
            if (outlineWidth > 0.dp) {
                val stroke = outlineWidth.toPx()
                drawRoundRect(
                    color = outline,
                    topLeft = Offset(stroke / 2, stroke / 2),
                    size = Size(size.width - stroke, size.height - stroke),
                    cornerRadius = CornerRadius(radius - stroke / 2),
                    style = Stroke(stroke),
                )
            }
            val thumbHeight = thumbSize.toPx()
            val thumbWidth = thumbHeight + pressedThumbExpansion.toPx() * pressProgress
            val inset = thumbInset.toPx()
            val fraction = if (rtl) 1f - progress else progress
            val x = inset + (size.width - inset * 2 - thumbWidth) * fraction
            val y = (size.height - thumbHeight) / 2
            if (thumbShadowColor.alpha > 0f) {
                drawRoundRect(
                    color = thumbShadowColor,
                    topLeft = Offset(x - 0.5.dp.toPx(), y + 1.dp.toPx()),
                    size = Size(thumbWidth + 1.dp.toPx(), thumbHeight + 1.dp.toPx()),
                    cornerRadius = CornerRadius(thumbHeight / 2),
                )
            }
            drawRoundRect(
                color = thumb,
                topLeft = Offset(x, y),
                size = Size(thumbWidth, thumbHeight),
                cornerRadius = CornerRadius(thumbHeight / 2),
            )
        }
    }
}
