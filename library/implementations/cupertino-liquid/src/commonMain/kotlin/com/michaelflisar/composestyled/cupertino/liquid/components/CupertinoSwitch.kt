package com.michaelflisar.composestyled.cupertino.liquid.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.ui.components.StyledSwitch
import com.michaelflisar.composestyled.ui.components.StyledToggleBehavior
import com.michaelflisar.composestyled.foundation.components.FoundationSwitch
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi

@OptIn(InternalComposeStyledApi::class)
class CupertinoLiquidSwitch internal constructor() : StyledSwitch.Implementation {

    override val defaults: StyledSwitch.Defaults
        @Composable get() = StyledSwitch.Defaults(
            clickBehavior = StyledToggleBehavior.ControlOnly,
            shape = RoundedCornerShape(percent = 50),
        )

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
        fun create() = remember { CupertinoLiquidSwitch() }
    }
}

