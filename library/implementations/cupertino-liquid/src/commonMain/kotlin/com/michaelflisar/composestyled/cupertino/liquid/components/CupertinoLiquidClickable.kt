package com.michaelflisar.composestyled.cupertino.liquid.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.InputMode
import androidx.compose.ui.platform.LocalInputModeManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.ui.components.StyledClickable

class CupertinoLiquidClickable internal constructor() : StyledClickable.Implementation {

    override val defaults: StyledClickable.Defaults
        @Composable get() = StyledClickable.Defaults(
            shape = RoundedCornerShape(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
        )

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember {
            CupertinoLiquidClickable()
        }
    }

    @Composable
    override fun Render(
        onClick: () -> Unit,
        modifier: Modifier,
        enabled: Boolean,
        interactionSource: MutableInteractionSource?,
        shape: Shape,
        contentPadding: PaddingValues,
        content: @Composable RowScope.() -> Unit,
    ) {
        val source = interactionSource ?: remember { MutableInteractionSource() }
        val pressed by source.collectIsPressedAsState()
        val focused by source.collectIsFocusedAsState()
        val keyboardFocus = enabled &&
            focused &&
            LocalInputModeManager.current.inputMode == InputMode.Keyboard
        val opacity by animateFloatAsState(
            targetValue = if (enabled && pressed) 0.55f else 1f,
            animationSpec = tween(if (!enabled) 0 else if (pressed) 60 else 180),
            label = "CupertinoLiquidClickableOpacity",
        )

        Row(
            modifier = modifier
                .heightIn(min = maxOf(44.dp, StyledTheme.sizes.minimumInteractiveSize))
                .clip(shape)
                .clickable(
                    interactionSource = source,
                    indication = null,
                    enabled = enabled,
                    role = Role.Button,
                    onClick = onClick,
                )
                .then(
                    if (keyboardFocus) {
                        Modifier.border(2.dp, StyledTheme.colorScheme.primary, shape)
                    } else {
                        Modifier
                    }
                )
                .graphicsLayer { alpha = opacity }
                .padding(contentPadding),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            content = content,
        )
    }
}
