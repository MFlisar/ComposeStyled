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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.input.InputMode
import androidx.compose.ui.platform.LocalInputModeManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.michaelflisar.composestyled.UpdateStyledThemeLocals
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.fluent2.FluentButtonColors
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.runtime.LocalStyledContext
import com.michaelflisar.composestyled.runtime.StyledContext
import com.michaelflisar.composestyled.ui.components.StyledButton
import com.michaelflisar.composestyled.ui.layout.StyledDialog
import com.michaelflisar.composestyled.ui.components.StyledSnackbar
import com.michaelflisar.composestyled.ui.components.StyledToolbar
import com.michaelflisar.composestyled.ui.components.StyledTopBar
import com.michaelflisar.composestyled.ui.specials.StyledGroupBox
import com.michaelflisar.composestyled.ui.specials.StyledSidePane
import com.michaelflisar.composestyled.ui.specials.StyledSwipeRevealActionBox

class Fluent2Button internal constructor(
    val roleMapping: RoleMapping,
) : StyledButton.Implementation {

    override val defaults: StyledButton.Defaults
        @Composable get() = StyledButton.Defaults(shape = RoundedCornerShape(4.dp))

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create(
            roleMapping: RoleMapping = DefaultRoleMapping,
        ) = remember(roleMapping) {
            Fluent2Button(roleMapping)
        }

        // -------------
        // 2) mapping
        // -------------

        typealias RoleMapping = (StyledButton.Role, StyledContext) -> Variant

        val DefaultRoleMapping: RoleMapping = { role, context ->
            when {
                role == StyledButton.Role.Default && context is StyledDialog.ActionContext ->
                    when (context.slot) {
                        StyledDialog.ActionSlot.Positive -> Variant.Primary
                        StyledDialog.ActionSlot.Negative -> Variant.Secondary
                        StyledDialog.ActionSlot.Neutral -> Variant.Subtle
                    }
                role == StyledButton.Role.Default &&
                    context == StyledGroupBox.Context.Clickable -> Variant.Outline
                role == StyledButton.Role.Default &&
                    context == StyledSwipeRevealActionBox.Context.RevealAction -> Variant.Transparent
                role == StyledButton.Role.Default &&
                    context == StyledSidePane.Context.IconSlot -> Variant.Transparent
                role == StyledButton.Role.Default &&
                    context == StyledSnackbar.Context.SnackbarAction -> Variant.Transparent
                role == StyledButton.Role.Default &&
                    (context is StyledTopBar.Context ||
                        context == StyledToolbar.Context.Action) -> Variant.Transparent
                else -> when (role) {
                    StyledButton.Role.Default,
                    StyledButton.Role.Primary -> Variant.Primary
                    StyledButton.Role.Secondary -> Variant.Secondary
                    StyledButton.Role.Tertiary -> Variant.Subtle
                }
            }
        }
    }

    enum class Variant {
        Primary,
        Secondary,
        Subtle,
        Outline,
        Transparent,
    }

    @OptIn(InternalComposeStyledApi::class)
    @Composable
    override fun Render(
        role: StyledButton.Role,
        onClick: () -> Unit,
        modifier: Modifier,
        enabled: Boolean,
        shape: Shape,
        interactionSource: MutableInteractionSource?,
        customization: StyledButton.Customization?,
        content: @Composable RowScope.() -> Unit,
    ) {
        val context = LocalStyledContext.current
        Fluent2Button(
            onClick = onClick,
            variant = roleMapping(role, context),
            modifier = modifier,
            enabled = enabled,
            shape = shape,
            interactionSource = interactionSource,
            customization = customization,
            defaultContentColor = if (role == StyledButton.Role.Default &&
                (context is StyledTopBar.Context ||
                    context == StyledToolbar.Context.Action)
            ) StyledTheme.currentContentColor else null,
            content = content,
        )
    }
}

/**
 * Foundation-based Fluent 2 medium button: 4 dp corners, a 96 x 32 dp minimum
 * footprint, semibold labels and flat, ripple-free interaction states.
 *
 * [StyledTheme.sizes] can increase the minimum height. [customization] overrides
 * the fill/content of filled variants; tint controls text for unfilled variants.
 * Border customization only applies to Secondary and Outline.
 */
@OptIn(InternalComposeStyledApi::class)
@Composable
private fun Fluent2Button(
    onClick: () -> Unit,
    modifier: Modifier,
    variant: Fluent2Button.Variant,
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
    val hovered by source.collectIsHoveredAsState()
    val pressed by source.collectIsPressedAsState()
    val focused by source.collectIsFocusedAsState()
    val keyboardFocus = enabled && focused &&
            LocalInputModeManager.current.inputMode == InputMode.Keyboard
    val colors = FluentButtonColors.resolve(
        variant = when (variant) {
            Fluent2Button.Variant.Primary -> FluentButtonColors.Variant.Primary
            Fluent2Button.Variant.Secondary -> FluentButtonColors.Variant.Secondary
            Fluent2Button.Variant.Subtle -> FluentButtonColors.Variant.Subtle
            Fluent2Button.Variant.Outline -> FluentButtonColors.Variant.Outline
            Fluent2Button.Variant.Transparent -> FluentButtonColors.Variant.Transparent
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
        label = "FluentButtonBackground",
    )
    val foreground by animateColorAsState(
        colors.foreground,
        animationSpec = tween(if (enabled) 83 else 0),
        label = "FluentButtonForeground",
    )
    val border by animateColorAsState(
        colors.border,
        animationSpec = tween(if (enabled) 83 else 0),
        label = "FluentButtonBorder",
    )
    val textStyle = StyledTheme.typography.labelMedium.copy(
        color = foreground,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 20.sp,
    )
    val focusOuter = if (StyledTheme.colorScheme.isDarkTheme) Color.White else Color.Black
    val focusInner = if (StyledTheme.colorScheme.isDarkTheme) Color.Black else Color.White
    // settings

    val minWidth = 96.dp
    val minHeight = maxOf(32.dp, StyledTheme.sizes.minimumInteractiveSize)
    val buttonPadding = if (isGroupBox) {
        PaddingValues(0.dp)
    } else {
        PaddingValues(horizontal = 12.dp, vertical = 6.dp)
    }
    val spacing = 6.dp

    UpdateStyledThemeLocals(
        contentColor = foreground,
        textStyle = textStyle
    ) {
        Row(
            modifier = modifier
                .defaultMinSize(
                    minWidth = minWidth,
                    minHeight = minHeight,
                )
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
                    if (isGroupBox) {
                        Modifier
                    } else {
                        Modifier.border(1.dp, border, shape)
                    }
                )
                .then(
                    if (keyboardFocus) {
                        Modifier.fluentFocusIndicator(shape, focusOuter, focusInner)
                    } else {
                        Modifier
                    }
                )
                .padding(buttonPadding),
            horizontalArrangement = Arrangement.spacedBy(spacing, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            content = content,
        )
    }
}

private fun Modifier.fluentFocusIndicator(
    shape: Shape,
    outer: Color,
    inner: Color,
) = drawWithCache {
    val inset = 2.dp.toPx()
    val outline = shape.createOutline(size, layoutDirection, this)
    val innerOutline = shape.createOutline(
        Size(
            (size.width - 2 * inset).coerceAtLeast(0f),
            (size.height - 2 * inset).coerceAtLeast(0f)
        ),
        layoutDirection,
        this,
    )
    onDrawWithContent {
        drawContent()
        drawOutline(outline, outer, style = Stroke(4.dp.toPx()))
        translate(inset, inset) {
            drawOutline(innerOutline, inner, style = Stroke(1.dp.toPx()))
        }
    }
}