package com.michaelflisar.composestyledcomponents.fluent2.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.ui.components.StyledSwitch
import com.michaelflisar.composestyledcomponents.foundation.components.FoundationSwitch
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi

@OptIn(InternalComposeStyledApi::class)
class Fluent2Switch internal constructor() : StyledSwitch.Implementation {
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
        val source = interactionSource ?: remember { MutableInteractionSource() }
        val hovered by source.collectIsHoveredAsState()
        val pressed by source.collectIsPressedAsState()
        val colors = StyledTheme.colorScheme
        val interactionAmount = when {
            !enabled || onCheckedChange == null -> 0f
            pressed -> 0.2f
            hovered -> 0.1f
            else -> 0f
        }
        val neutral = lerp(colors.outline, colors.onSurface, interactionAmount)
        FoundationSwitch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = modifier,
            enabled = enabled,
            interactionSource = source,
            trackColor = when {
                !enabled && checked -> colors.onSurface.copy(alpha = 0.12f)
                checked -> lerp(tint, colors.onSurface, interactionAmount)
                else -> Color.Transparent
            },
            thumbColor = when {
                !enabled -> colors.onSurface.copy(alpha = colors.disableFactors.content)
                checked -> onTint
                else -> neutral
            },
            outlineColor = when {
                checked -> Color.Transparent
                enabled -> neutral
                else -> colors.outline.copy(alpha = colors.disableFactors.outline)
            },
            trackWidth = 40.dp,
            trackHeight = 20.dp,
            thumbSize = 12.dp,
            thumbInset = 4.dp,
            outlineWidth = 1.dp,
        )
    }

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { Fluent2Switch() }
    }
}
