package com.michaelflisar.composestyled.theme.android.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.composeunstyled.UnstyledButton
import com.michaelflisar.composestyled.core.classes.colors.BaseColor

/**
 * Android/Unstyled-based checkbox implementation.
 *
 * compose-unstyled bietet (je nach Version) kein eigenes Checkbox-Primitive,
 * daher wird hier ein simples, aber ordentliches Checkbox-UI gerendert.
 */
@Composable
internal fun StyledCheckboxImpl(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    colors: BaseColor,
    modifier: Modifier,
    enabled: Boolean,
    interactionSource: MutableInteractionSource,
) {
    UnstyledButton(
        onClick = { onCheckedChange(!checked) },
        modifier = modifier.size(20.dp),
        enabled = enabled,
        shape = RoundedCornerShape(4.dp),
        contentPadding = PaddingValues(0.dp),
        interactionSource = interactionSource,
        backgroundColor = colors.background,
        contentColor = colors.foreground,
        borderColor = colors.border ?: Color.Unspecified,
        borderWidth = if (colors.border != null) 2.dp else 0.dp,
    ) {
        if (checked) {
            Box {
                Canvas(modifier = Modifier.size(20.dp)) {
                    val strokeWidth = 2.2.dp.toPx()
                    // simple checkmark path
                    drawLine(
                        color = colors.foreground,
                        start = androidx.compose.ui.geometry.Offset(size.width * 0.28f, size.height * 0.55f),
                        end = androidx.compose.ui.geometry.Offset(size.width * 0.45f, size.height * 0.72f),
                        strokeWidth = strokeWidth,
                        cap = StrokeCap.Round
                    )
                    drawLine(
                        color = colors.foreground,
                        start = androidx.compose.ui.geometry.Offset(size.width * 0.45f, size.height * 0.72f),
                        end = androidx.compose.ui.geometry.Offset(size.width * 0.75f, size.height * 0.34f),
                        strokeWidth = strokeWidth,
                        cap = StrokeCap.Round
                    )
                }
            }
        }
    }
}
