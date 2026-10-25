package com.michaelflisar.composestyledcomponents.fluent2.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.LocalInputModeManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyledcomponents.ProvideCustomStyledContentLocals
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.ui.components.StyledIconButton
import com.michaelflisar.composestyledcomponents.fluent2.FluentButtonColors
import com.michaelflisar.composestyledcomponents.platform.Platform
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyledcomponents.tokens.minimumInteractiveSize

class Fluent2IconButton internal constructor(
    val roleMapping: RoleMapping,
    override val defaults: StyledIconButton.Defaults,
) : StyledIconButton.Implementation {

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create(
            roleMapping: RoleMapping = DefaultRoleMapping,
            defaults: StyledIconButton.Defaults = defaults(),
        ) = remember(roleMapping, defaults) { Fluent2IconButton(roleMapping, defaults) }

        // -------------
        // 2) mapping
        // -------------

        typealias RoleMapping = (StyledIconButton.Role) -> Variant

        val DefaultRoleMapping: RoleMapping = { role ->
            when (role) {
                StyledIconButton.Role.Primary -> Variant.Filled
                StyledIconButton.Role.Secondary -> Variant.FilledTonal
                StyledIconButton.Role.Tertiary -> Variant.Standard
            }
        }

        // -------------
        // 3) defaults
        // -------------

        @Composable
        fun defaults(shape: Shape = RoundedCornerShape(4.dp)) =
            StyledIconButton.Defaults(shape = shape)
    }

    enum class Variant {
        Standard,
        Outlined,
        Filled,
        FilledTonal,
    }

    @OptIn(InternalComposeStyledApi::class)
    @Composable
    override fun Render(
        role: StyledIconButton.Role,
        onClick: () -> Unit,
        modifier: Modifier,
        enabled: Boolean,
        shape: Shape,
        interactionSource: MutableInteractionSource?,
        customization: StyledIconButton.Customization?,
        content: @Composable () -> Unit,
    ) {
        val source = interactionSource ?: remember { MutableInteractionSource() }
        val hovered by source.collectIsHoveredAsState()
        val pressed by source.collectIsPressedAsState()
        val focused by source.collectIsFocusedAsState()
        val keyboardFocus = enabled && focused &&
                LocalInputModeManager.current.inputMode == InputMode.Keyboard
        val variant = roleMapping(role)
        val colors = FluentButtonColors.resolve(
            variant = when (variant) {
                Variant.Filled -> FluentButtonColors.Variant.Primary
                Variant.FilledTonal -> FluentButtonColors.Variant.Secondary
                Variant.Standard -> FluentButtonColors.Variant.Standard
                Variant.Outlined -> FluentButtonColors.Variant.IconOutline
            },
            enabled = enabled,
            hovered = hovered,
            pressed = pressed,
            colors = customization?.colors,
            border = customization?.border,
        )
        val background by animateColorAsState(
            colors.background,
            animationSpec = tween(if (enabled) 83 else 0),
            label = "Fluent2IconButtonBackground",
        )
        val foreground by animateColorAsState(
            colors.foreground,
            animationSpec = tween(if (enabled) 83 else 0),
            label = "Fluent2IconButtonForeground",
        )
        val border by animateColorAsState(
            colors.border,
            animationSpec = tween(if (enabled) 83 else 0),
            label = "Fluent2IconButtonBorder",
        )

        ProvideCustomStyledContentLocals(contentColor = foreground) {
            Box(
                modifier = modifier
                    .minimumInteractiveSize()
                    .hoverable(interactionSource = source, enabled = enabled)
                    .clip(shape)
                    .background(background, shape)
                    .border(1.dp, border, shape)
                    .clickable(
                        interactionSource = source,
                        indication = null,
                        enabled = enabled,
                        role = Role.Button,
                        onClick = onClick,
                    )
                    .then(
                        if (keyboardFocus) Modifier.border(
                            2.dp,
                            if (StyledTheme.colorScheme.isDarkTheme) Color.White else Color.Black,
                            shape,
                        ) else Modifier
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Box(Modifier.size(StyledTheme.sizes.iconSize), contentAlignment = Alignment.Center) {
                    content()
                }
            }
        }
    }
}
