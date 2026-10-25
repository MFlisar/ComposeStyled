package com.michaelflisar.composestyledcomponents.cupertino.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.InputMode
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.LocalInputModeManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyledcomponents.ProvideCustomStyledContentLocals
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.ui.components.StyledIconButton
import com.michaelflisar.composestyledcomponents.platform.Platform
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi

class CupertinoIconButton internal constructor(
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
        ) = remember(roleMapping, defaults) { CupertinoIconButton(roleMapping, defaults) }

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
        fun defaults(shape: Shape = RoundedCornerShape(10.dp)) =
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
        val pressed by source.collectIsPressedAsState()
        val focused by source.collectIsFocusedAsState()
        val keyboardFocus = enabled && focused &&
                LocalInputModeManager.current.inputMode == InputMode.Keyboard
        val variant = roleMapping(role)
        val colors = cupertinoIconButtonColors(variant, enabled, customization)
        val opacity by animateFloatAsState(
            targetValue = if (enabled && pressed) 0.55f else 1f,
            animationSpec = tween(if (!enabled) 0 else if (pressed) 60 else 180),
            label = "CupertinoIconButtonOpacity",
        )
        val minimumSize = maxOf(44.dp, StyledTheme.sizes.minimumInteractiveSize)

        ProvideCustomStyledContentLocals(contentColor = colors.foreground) {
            Box(
                modifier = modifier
                    .sizeIn(minWidth = minimumSize, minHeight = minimumSize)
                    .clip(shape)
                    .background(colors.background, shape)
                    .then(
                        if (variant == Variant.Outlined) {
                            Modifier.border(1.dp, colors.border, shape)
                        } else {
                            Modifier
                        }
                    )
                    .clickable(
                        interactionSource = source,
                        indication = null,
                        enabled = enabled,
                        role = Role.Button,
                        onClick = onClick,
                    )
                    .then(
                        if (keyboardFocus) Modifier.border(2.dp, StyledTheme.colorScheme.primary, shape)
                        else Modifier
                    )
                    .graphicsLayer { alpha = opacity },
                contentAlignment = Alignment.Center,
            ) {
                Box(Modifier.size(StyledTheme.sizes.iconSize), contentAlignment = Alignment.Center) {
                    content()
                }
            }
        }
    }
}

@Immutable
private data class CupertinoIconButtonColors(
    val background: Color,
    val foreground: Color,
    val border: Color,
)

@Composable
private fun cupertinoIconButtonColors(
    variant: CupertinoIconButton.Variant,
    enabled: Boolean,
    customization: StyledIconButton.Customization?,
): CupertinoIconButtonColors {
    val theme = StyledTheme.colorScheme
    if (!enabled) {
        val disabled = theme.onSurface.copy(alpha = theme.onSurface.alpha * 0.30f)
        return CupertinoIconButtonColors(
            background = if (variant == CupertinoIconButton.Variant.Filled ||
                variant == CupertinoIconButton.Variant.FilledTonal
            ) theme.onSurface.copy(alpha = theme.onSurface.alpha * 0.08f) else Color.Transparent,
            foreground = disabled,
            border = disabled,
        )
    }

    val filledBackground = customization?.colors?.filledBackground?.resolve()
        ?.takeIf { it != Color.Unspecified } ?: theme.primary
    val filledForeground = customization?.colors?.filledForeground?.resolve()
        ?.takeIf { it != Color.Unspecified }
    val textForeground = customization?.colors?.textForeground?.resolve()
        ?.takeIf { it != Color.Unspecified } ?: theme.primary
    return when (variant) {
        CupertinoIconButton.Variant.Standard ->
            CupertinoIconButtonColors(Color.Transparent, textForeground, textForeground)
        CupertinoIconButton.Variant.Outlined -> CupertinoIconButtonColors(
            Color.Transparent,
            textForeground,
            customization?.border?.resolve()?.takeIf { it != Color.Unspecified } ?: textForeground,
        )
        CupertinoIconButton.Variant.Filled ->
            CupertinoIconButtonColors(filledBackground, filledForeground ?: theme.onPrimary, filledBackground)
        CupertinoIconButton.Variant.FilledTonal -> CupertinoIconButtonColors(
            filledBackground.copy(alpha = filledBackground.alpha * if (theme.isDarkTheme) 0.24f else 0.12f),
                textForeground,
            filledBackground,
        )
    }
}
