package com.michaelflisar.composestyled.foundation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyled.UpdateStyledThemeLocals
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.ui.components.StyledDropdownMenuItem
import com.michaelflisar.composestyled.ui.components.StyledMenuItemColors
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi

@InternalComposeStyledApi
abstract class FoundationDropdownMenu(
    private val hoverAlpha: Float,
    private val pressedAlpha: Float,
) : StyledDropdownMenuItem.Implementation {
    @Composable
    override fun Render(
        content: @Composable RowScope.() -> Unit,
        onClick: (() -> Unit)?,
        modifier: Modifier,
        enabled: Boolean,
        colors: StyledMenuItemColors,
        contentPadding: PaddingValues,
        interactionSource: MutableInteractionSource?,
        shape: Shape,
        itemPadding: PaddingValues,
        minHeight: Dp,
    ) {
        val source = interactionSource ?: remember { MutableInteractionSource() }
        val effectiveEnabled = enabled && onClick != null
        val hovered by source.collectIsHoveredAsState()
        val focused by source.collectIsFocusedAsState()
        val pressed by source.collectIsPressedAsState()
        val foreground = if (enabled) colors.textColor else colors.disabledTextColor
        val highlight = when {
            !effectiveEnabled -> 0f
            pressed -> pressedAlpha
            hovered || focused -> hoverAlpha
            else -> 0f
        }
        Row(
            modifier = modifier.fillMaxWidth()
                .heightIn(min = minHeight)
                .padding(itemPadding)
                .clip(shape)
                .background(foreground.copy(alpha = foreground.alpha * highlight))
                .hoverable(source, effectiveEnabled)
                .clickable(
                    interactionSource = source, indication = null,
                    enabled = effectiveEnabled, role = Role.Button, onClick = onClick ?: {},
                )
                .padding(contentPadding),
            horizontalArrangement = Arrangement.spacedBy(StyledTheme.spacing.medium),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            UpdateStyledThemeLocals(
                contentColor = foreground,
                textStyle = StyledTheme.typography.bodyMedium,
                content = { content() },
            )
        }
    }

    @Composable
    override fun Render(
        text: @Composable () -> Unit,
        onClick: () -> Unit,
        modifier: Modifier,
        leadingIcon: (@Composable () -> Unit)?,
        trailingIcon: (@Composable () -> Unit)?,
        enabled: Boolean,
        colors: StyledMenuItemColors,
        contentPadding: PaddingValues,
        interactionSource: MutableInteractionSource?,
        shape: Shape,
        itemPadding: PaddingValues,
        minHeight: Dp,
    ) {
        val source = interactionSource ?: remember { MutableInteractionSource() }
        val hovered by source.collectIsHoveredAsState()
        val focused by source.collectIsFocusedAsState()
        val pressed by source.collectIsPressedAsState()
        val foreground = if (enabled) colors.textColor else colors.disabledTextColor
        val highlight = when {
            !enabled -> 0f
            pressed -> pressedAlpha
            hovered || focused -> hoverAlpha
            else -> 0f
        }
        Row(
            modifier = modifier.fillMaxWidth()
                .heightIn(min = minHeight)
                .padding(itemPadding)
                .clip(shape)
                .background(foreground.copy(alpha = foreground.alpha * highlight))
                .hoverable(source, enabled)
                .clickable(
                    interactionSource = source, indication = null,
                    enabled = enabled, role = Role.Button, onClick = onClick,
                )
                .padding(contentPadding),
            horizontalArrangement = Arrangement.spacedBy(StyledTheme.spacing.medium),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (leadingIcon != null) {
                UpdateStyledThemeLocals(
                    contentColor = if (enabled) colors.leadingIconColor else colors.disabledLeadingIconColor,
                    content = leadingIcon,
                )
            }
            Box(Modifier.weight(1f)) {
                UpdateStyledThemeLocals(
                    contentColor = foreground,
                    textStyle = StyledTheme.typography.bodyMedium,
                    content = text,
                )
            }
            if (trailingIcon != null) {
                UpdateStyledThemeLocals(
                    contentColor = if (enabled) colors.trailingIconColor else colors.disabledTrailingIconColor,
                    content = trailingIcon,
                )
            }
        }
    }
}
