package com.michaelflisar.composestyled.fluent2.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.InputMode
import androidx.compose.ui.platform.LocalInputModeManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.fluent2.FluentButtonColors
import com.michaelflisar.composestyled.ui.components.StyledClickable

class Fluent2Clickable internal constructor() : StyledClickable.Implementation {

    override val defaults: StyledClickable.Defaults
        @Composable get() = StyledClickable.Defaults(
            shape = RoundedCornerShape(4.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
        )

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember {
            Fluent2Clickable()
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
        val hovered by source.collectIsHoveredAsState()
        val pressed by source.collectIsPressedAsState()
        val focused by source.collectIsFocusedAsState()
        val keyboardFocus = enabled &&
            focused &&
            LocalInputModeManager.current.inputMode == InputMode.Keyboard
        val colors = FluentButtonColors.resolve(
            variant = FluentButtonColors.Variant.Transparent,
            enabled = enabled,
            hovered = hovered,
            pressed = pressed,
            colors = null,
            border = null,
        )
        val background by animateColorAsState(
            colors.background,
            animationSpec = tween(if (enabled) 83 else 0),
            label = "Fluent2ClickableBackground",
        )
        val focusOuter = if (StyledTheme.colorScheme.isDarkTheme) Color.White else Color.Black
        val focusInner = if (StyledTheme.colorScheme.isDarkTheme) Color.Black else Color.White

        Row(
            modifier = modifier
                .heightIn(min = maxOf(32.dp, StyledTheme.sizes.minimumInteractiveSize))
                .hoverable(interactionSource = source, enabled = enabled)
                .clickable(
                    interactionSource = source,
                    indication = null,
                    enabled = enabled,
                    role = Role.Button,
                    onClick = onClick,
                )
                .clip(shape)
                .background(background, shape)
                .then(
                    if (keyboardFocus) {
                        Modifier.fluentFocusIndicator(shape, focusOuter, focusInner)
                    } else {
                        Modifier
                    }
                )
                .padding(contentPadding),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            content = content,
        )
    }
}
