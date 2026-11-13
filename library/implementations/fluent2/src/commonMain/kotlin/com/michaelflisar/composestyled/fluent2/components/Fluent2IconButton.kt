package com.michaelflisar.composestyled.fluent2.components

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
import androidx.compose.ui.platform.LocalInputModeManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.UpdateStyledThemeLocals
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.tokens.LocalStyledContentColor
import com.michaelflisar.composestyled.ui.components.StyledIconButton
import com.michaelflisar.composestyled.ui.components.StyledButton
import com.michaelflisar.composestyled.ui.components.StyledInput
import com.michaelflisar.composestyled.ui.components.StyledSnackbar
import com.michaelflisar.composestyled.ui.components.StyledTopBar
import com.michaelflisar.composestyled.ui.layout.StyledDialog
import com.michaelflisar.composestyled.ui.specials.StyledGroupBox
import com.michaelflisar.composestyled.ui.specials.StyledSidePane
import com.michaelflisar.composestyled.fluent2.FluentButtonColors
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.runtime.LocalStyledContext
import com.michaelflisar.composestyled.runtime.StyledContext
import com.michaelflisar.composestyled.tokens.minimumInteractiveSize

class Fluent2IconButton internal constructor(
    val variantMapping: VariantMapping,
) : StyledIconButton.Implementation {

    override val defaults: StyledIconButton.Defaults
        @Composable get() = StyledIconButton.Defaults(shape = RoundedCornerShape(4.dp))

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create(
            variantMapping: VariantMapping = DefaultVariantMapping,
        ) = remember(variantMapping) { Fluent2IconButton(variantMapping) }

        // -------------
        // 2) mapping
        // -------------

        typealias VariantMapping = (StyledButton.Variant, StyledContext) -> Variant

        val DefaultVariantMapping: VariantMapping = { variant, context ->
            when {
                variant == StyledButton.Variant.Auto &&
                    (context is StyledDialog.ActionContext ||
                        context is StyledTopBar.Context ||
                        context is StyledInput.Context ||
                        context == StyledSnackbar.Context.SnackbarAction ||
                        context is StyledDialog.Context ||
                        context == StyledGroupBox.Context.Clickable ||
                        context == StyledSidePane.Context.IconSlot) -> Variant.Standard
                else -> when (variant) {
                    StyledButton.Variant.Auto,
                    StyledButton.Variant.Filled -> Variant.Filled
                    StyledButton.Variant.Outlined -> Variant.Outlined
                    StyledButton.Variant.Plain -> Variant.Standard
                }
            }
        }
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
        variant: StyledButton.Variant,
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
        val context = LocalStyledContext.current
        val rendererVariant = variantMapping(variant, context)
        val defaultContentColor = if (variant == StyledButton.Variant.Plain ||
            (variant == StyledButton.Variant.Auto &&
                (context is StyledTopBar.Context ||
                    context is StyledDialog.ActionContext &&
                    context.context == StyledDialog.Context.FullscreenAction))
        ) LocalStyledContentColor.current else null
        val colors = FluentButtonColors.resolve(
            variant = when (rendererVariant) {
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
            inheritedContentColor = defaultContentColor,
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

        UpdateStyledThemeLocals(contentColor = foreground) {
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
