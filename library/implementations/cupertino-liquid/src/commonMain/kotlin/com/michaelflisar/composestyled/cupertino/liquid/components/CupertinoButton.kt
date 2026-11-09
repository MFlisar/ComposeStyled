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
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.UpdateStyledThemeLocals
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.cupertino.liquid.liquidGlass
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.runtime.LocalStyledContext
import com.michaelflisar.composestyled.runtime.StyledContext
import com.michaelflisar.composestyled.tokens.LocalStyledContentColor
import com.michaelflisar.composestyled.ui.layout.StyledDialog
import com.michaelflisar.composestyled.ui.components.StyledButton
import com.michaelflisar.composestyled.ui.components.StyledSnackbar
import com.michaelflisar.composestyled.ui.components.StyledMenuBar
import com.michaelflisar.composestyled.ui.components.StyledTopBar
import com.michaelflisar.composestyled.ui.specials.StyledGroupBox
import com.michaelflisar.composestyled.ui.specials.StyledSidePane
import com.michaelflisar.composestyled.ui.specials.StyledSwipeRevealActionBox

/**
 * Foundation-based, classic iOS button styles, not native UIKit or Liquid Glass.
 * Uses ripple-free press fading, button semantics and a minimum 44 dp target,
 * subject to the constraints supplied by the caller.
 */
class CupertinoLiquidButton internal constructor(
    val variantMapping: VariantMapping,
) : StyledButton.Implementation {

    override val defaults: StyledButton.Defaults
        @Composable get() = StyledButton.Defaults(shape = RoundedCornerShape(16.dp))

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create(
            variantMapping: VariantMapping = DefaultVariantMapping,
        ): CupertinoLiquidButton = remember(variantMapping) {
            CupertinoLiquidButton(variantMapping)
        }

        // -------------
        // 2) mapping
        // -------------

        typealias VariantMapping = (StyledButton.Variant, StyledContext) -> Variant

        val DefaultVariantMapping: VariantMapping = { variant, context ->
            when {
                variant == StyledButton.Variant.Auto &&
                    context == StyledGroupBox.Context.Clickable -> Variant.Outlined
                variant == StyledButton.Variant.Auto &&
                    context == StyledSidePane.Context.IconSlot -> Variant.Plain
                variant == StyledButton.Variant.Auto &&
                    context == StyledSwipeRevealActionBox.Context.RevealAction -> Variant.Plain
                variant == StyledButton.Variant.Auto &&
                    (context is StyledDialog.ActionContext ||
                        context == StyledSnackbar.Context.SnackbarAction ||
                        context == StyledDialog.Context.DialogAction ||
                        context == StyledDialog.Context.FullscreenAction ||
                        context == StyledDialog.Context.BottomSheetAction ||
                        context is StyledTopBar.Context ||
                        context == StyledMenuBar.Context.MenuItem) -> Variant.Plain
                else -> when (variant) {
                    StyledButton.Variant.Auto -> Variant.Tinted
                    StyledButton.Variant.Filled -> Variant.Filled
                    StyledButton.Variant.Outlined -> Variant.Outlined
                    StyledButton.Variant.Plain -> Variant.Plain
                }
            }
        }
    }

    /** Outlined is an adaptive API extension, not a standard UIKit configuration. */
    enum class Variant {
        Filled,
        Tinted,
        Gray,
        Outlined,
        Plain,
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
        customization: StyledButton.Customization?,
        content: @Composable RowScope.() -> Unit,
    ) {
        val context = LocalStyledContext.current
        CupertinoLiquidButton(
            variant = variantMapping(variant, context),
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            shape = shape,
            interactionSource = interactionSource,
            customization = customization,
            defaultContentColor = if (variant == StyledButton.Variant.Auto &&
                (context is StyledTopBar.Context ||
                    context == StyledMenuBar.Context.MenuItem)
            ) LocalStyledContentColor.current else null,
            content = content,
        )
    }
}

/**
 * Tint sets the filled background, the tinted wash, or the unfilled foreground.
 * FilledForeground overrides content in Filled; TextForeground applies to Tinted
 * and unfilled variants. Border applies to Outlined.
 * Disabled styling deliberately uses neutral colors instead of custom accents.
 */
@OptIn(InternalComposeStyledApi::class)
@Composable
private fun CupertinoLiquidButton(
    variant: CupertinoLiquidButton.Variant,
    onClick: () -> Unit,
    modifier: Modifier,
    enabled: Boolean,
    shape: Shape,
    interactionSource: MutableInteractionSource?,
    customization: StyledButton.Customization?,
    defaultContentColor: Color?,
    content: @Composable RowScope.() -> Unit,
) {
    val source = interactionSource ?: remember { MutableInteractionSource() }
    val context = LocalStyledContext.current
    val isGroupBox = context == StyledGroupBox.Context.Clickable
    val pressed by source.collectIsPressedAsState()
    val focused by source.collectIsFocusedAsState()
    val keyboardFocus = enabled && focused &&
            LocalInputModeManager.current.inputMode == InputMode.Keyboard
    val colors = cupertinoButtonColors(variant, enabled, customization, defaultContentColor)
    val opacity by animateFloatAsState(
        targetValue = if (enabled && pressed) 0.55f else 1f,
        animationSpec = tween(if (!enabled) 0 else if (pressed) 60 else 180),
        label = "CupertinoButtonOpacity",
    )
    val textStyle = StyledTheme.typography.bodyMedium.copy(
        color = colors.foreground,
        fontWeight = if (variant == CupertinoLiquidButton.Variant.Filled) {
            FontWeight.SemiBold
        } else {
            FontWeight.Normal
        },
    )
    val minimumSize = maxOf(44.dp, StyledTheme.sizes.minimumInteractiveSize)
    val minimumWidth = if (LocalStyledContext.current == StyledMenuBar.Context.MenuItem) {
        0.dp
    } else {
        minimumSize
    }
    val buttonPadding = if (isGroupBox) {
        PaddingValues(0.dp)
    } else {
        PaddingValues(horizontal = 16.dp, vertical = 6.dp)
    }

    UpdateStyledThemeLocals(
        contentColor = colors.foreground,
        textStyle = textStyle,
    ) {
        Row(
            modifier = modifier
                .sizeIn(minWidth = minimumWidth, minHeight = minimumSize)
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
                .clip(shape)
                .liquidGlass(shape, colors.background)
                .then(
                    if (variant == CupertinoLiquidButton.Variant.Outlined && !isGroupBox) {
                        Modifier.border(1.dp, colors.border, shape)
                    } else {
                        Modifier
                    }
                )
                .padding(buttonPadding),
            horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            content = content,
        )
    }
}

@Immutable
private data class CupertinoLiquidButtonColors(
    val background: Color,
    val foreground: Color,
    val border: Color,
)

@Composable
private fun cupertinoButtonColors(
    variant: CupertinoLiquidButton.Variant,
    enabled: Boolean,
    customization: StyledButton.Customization?,
    defaultContentColor: Color?,
): CupertinoLiquidButtonColors {
    val theme = StyledTheme.colorScheme
    val filled = variant == CupertinoLiquidButton.Variant.Filled
    val tinted = variant == CupertinoLiquidButton.Variant.Tinted
    val gray = variant == CupertinoLiquidButton.Variant.Gray
    val hasBackground = filled || tinted || gray
    if (!enabled) {
        val foreground = theme.onSurface.copy(alpha = theme.onSurface.alpha * 0.30f)
        return CupertinoLiquidButtonColors(
            background = if (hasBackground) {
                theme.onSurface.copy(alpha = theme.onSurface.alpha * 0.08f)
            } else {
                Color.Transparent
            },
            foreground = foreground,
            border = foreground,
        )
    }

    val filledBackgroundColor =
        customization?.colors?.filledBackground?.resolve()?.takeIf { it != Color.Unspecified }
            ?: theme.primary
    val filledForegroundColor =
        customization?.colors?.filledForeground?.resolve()?.takeIf { it != Color.Unspecified }
    val textForeground =
        customization?.colors?.textForeground?.resolve()?.takeIf { it != Color.Unspecified }
            ?: defaultContentColor
            ?: theme.primary
    val customBorder =
        customization?.border?.resolve()?.takeIf { it != Color.Unspecified } ?: theme.outline

    return CupertinoLiquidButtonColors(
        background = when {
            filled -> filledBackgroundColor
            tinted -> filledBackgroundColor.copy(alpha = filledBackgroundColor.alpha * if (theme.isDarkTheme) 0.24f else 0.12f)
            gray -> theme.onSurface.copy(alpha = theme.onSurface.alpha * 0.08f)
            else -> Color.Transparent
        },
        foreground = when {
            filled -> filledForegroundColor ?: theme.onPrimary
            tinted -> textForeground
            else -> textForeground
        },
        border = customBorder
    )
}
