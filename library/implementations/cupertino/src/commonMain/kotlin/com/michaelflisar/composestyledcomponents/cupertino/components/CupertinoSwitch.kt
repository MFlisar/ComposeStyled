package com.michaelflisar.composestyledcomponents.cupertino.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.ui.components.StyledSwitch
import com.michaelflisar.composestyledcomponents.foundation.components.FoundationSwitch
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi

@OptIn(InternalComposeStyledApi::class)
class CupertinoSwitch internal constructor() : StyledSwitch.Implementation {
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
        val colors = StyledTheme.colorScheme
        val opacity = if (enabled) 1f else colors.disableFactors.content
        val track = if (checked) tint else colors.onSurface.copy(alpha = 0.16f)
        val thumb = if (checked) onTint else Color.White
        FoundationSwitch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = modifier,
            enabled = enabled,
            interactionSource = interactionSource,
            trackColor = track.copy(alpha = track.alpha * opacity),
            thumbColor = thumb.copy(alpha = thumb.alpha * opacity),
            outlineColor = Color.Transparent,
            trackWidth = 51.dp,
            trackHeight = 31.dp,
            thumbSize = 27.dp,
            thumbInset = 2.dp,
            pressedThumbExpansion = 6.dp,
            thumbShadowColor = Color.Black.copy(alpha = 0.12f * opacity),
            animationDurationMillis = 250,
        )
    }

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { CupertinoSwitch() }
    }
}
