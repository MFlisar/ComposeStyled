package com.michaelflisar.composestyled.cupertino.liquid.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.platform.LocalInputModeManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.UpdateStyledThemeLocals
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.cupertino.liquid.liquidGlass
import com.michaelflisar.composestyled.tokens.LocalStyledContentColor
import com.michaelflisar.composestyled.ui.components.StyledIconButton
import com.michaelflisar.composestyled.ui.components.StyledButton
import com.michaelflisar.composestyled.ui.components.StyledInput
import com.michaelflisar.composestyled.ui.specials.StyledGroupBox
import com.michaelflisar.composestyled.ui.specials.StyledSidePane
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.runtime.LocalStyledContext
import com.michaelflisar.composestyled.runtime.StyledContext
import com.michaelflisar.composestyled.ui.layout.StyledDialog
import com.michaelflisar.composestyled.ui.components.StyledSnackbar
import com.michaelflisar.composestyled.ui.components.StyledTopBar

class CupertinoLiquidIconButton internal constructor(
    val variantMapping: VariantMapping,
) : StyledIconButton.Implementation {

    override val defaults: StyledIconButton.Defaults
        @Composable get() = StyledIconButton.Defaults(shape = RoundedCornerShape(14.dp))

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create(
            variantMapping: VariantMapping = DefaultVariantMapping,
        ) = remember(variantMapping) { CupertinoLiquidIconButton(variantMapping) }

        // -------------
        // 2) mapping
        // -------------

        typealias VariantMapping = (StyledButton.Variant, StyledContext) -> Variant

        val DefaultVariantMapping: VariantMapping = { variant, context ->
            when {
                variant == StyledButton.Variant.Auto &&
                    (context is StyledDialog.ActionContext ||
                        context == StyledDialog.Context.DialogAction ||
                        context == StyledDialog.Context.FullscreenAction ||
                        context == StyledDialog.Context.BottomSheetAction ||
                        context == StyledSnackbar.Context.SnackbarAction ||
                        context is StyledInput.Context ||
                        context is StyledTopBar.Context ||
                        context == StyledGroupBox.Context.Clickable ||
                        context == StyledSidePane.Context.IconSlot) -> Variant.Standard
                else -> when (variant) {
                    StyledButton.Variant.Auto -> Variant.FilledTonal
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
        Gray,
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
        val pressed by source.collectIsPressedAsState()
        val focused by source.collectIsFocusedAsState()
        val keyboardFocus = enabled && focused &&
                LocalInputModeManager.current.inputMode == InputMode.Keyboard
        val context = LocalStyledContext.current
        val rendererVariant = variantMapping(variant, context)
        val defaultContentColor = if (variant == StyledButton.Variant.Auto &&
            (context is StyledTopBar.Context ||
                context is StyledDialog.ActionContext &&
                context.context == StyledDialog.Context.FullscreenAction)
        ) LocalStyledContentColor.current else null
        val colors = cupertinoIconButtonColors(rendererVariant, enabled, customization, defaultContentColor)
        val opacity by animateFloatAsState(
            targetValue = if (enabled && pressed) 0.55f else 1f,
            animationSpec = tween(if (!enabled) 0 else if (pressed) 60 else 180),
            label = "CupertinoIconButtonOpacity",
        )
        val minimumSize = maxOf(44.dp, StyledTheme.sizes.minimumInteractiveSize)

        UpdateStyledThemeLocals(contentColor = colors.foreground) {
            Box(
                modifier = modifier
                    .sizeIn(minWidth = minimumSize, minHeight = minimumSize)
                    .clip(shape)
                    .liquidGlass(shape, colors.background)
                    .then(
                        if (rendererVariant == Variant.Outlined) {
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
private data class CupertinoLiquidIconButtonColors(
    val background: Color,
    val foreground: Color,
    val border: Color,
)

@Composable
private fun cupertinoIconButtonColors(
    variant: CupertinoLiquidIconButton.Variant,
    enabled: Boolean,
    customization: StyledIconButton.Customization?,
    defaultContentColor: Color?,
): CupertinoLiquidIconButtonColors {
    val theme = StyledTheme.colorScheme
    if (!enabled) {
        val disabled = theme.onSurface.copy(alpha = theme.onSurface.alpha * 0.30f)
        return CupertinoLiquidIconButtonColors(
            background = if (variant == CupertinoLiquidIconButton.Variant.Filled ||
                variant == CupertinoLiquidIconButton.Variant.FilledTonal ||
                variant == CupertinoLiquidIconButton.Variant.Gray
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
        ?.takeIf { it != Color.Unspecified } ?: defaultContentColor ?: theme.primary
    return when (variant) {
        CupertinoLiquidIconButton.Variant.Standard ->
            CupertinoLiquidIconButtonColors(Color.Transparent, textForeground, textForeground)
        CupertinoLiquidIconButton.Variant.Outlined -> CupertinoLiquidIconButtonColors(
            Color.Transparent,
            textForeground,
            customization?.border?.resolve()?.takeIf { it != Color.Unspecified } ?: textForeground,
        )
        CupertinoLiquidIconButton.Variant.Filled ->
            CupertinoLiquidIconButtonColors(filledBackground, filledForeground ?: theme.onPrimary, filledBackground)
        CupertinoLiquidIconButton.Variant.FilledTonal -> CupertinoLiquidIconButtonColors(
            filledBackground.copy(alpha = filledBackground.alpha * if (theme.isDarkTheme) 0.24f else 0.12f),
                textForeground,
            filledBackground,
        )
        CupertinoLiquidIconButton.Variant.Gray -> CupertinoLiquidIconButtonColors(
            theme.onSurface.copy(alpha = theme.onSurface.alpha * 0.08f),
            textForeground,
            theme.onSurface.copy(alpha = theme.onSurface.alpha * 0.08f),
        )
    }
}
