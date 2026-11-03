package com.michaelflisar.composestyled.material3.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MenuDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.ui.components.StyledDropdownMenuItem
import com.michaelflisar.composestyled.ui.components.StyledMenuItemColors
import com.michaelflisar.composestyled.material3.CopyMaterialThemeLocals
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi

class Material3DropdownMenu internal constructor() : StyledDropdownMenuItem.Implementation {

    override val defaults: StyledDropdownMenuItem.Defaults
        @Composable get() = StyledDropdownMenuItem.Defaults(
            minHeight = 48.dp,
            itemPadding = PaddingValues(horizontal = StyledTheme.padding.small),
            itemShape = StyledTheme.shapes.small,
        )

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { Material3DropdownMenu() }
    }

    @OptIn(InternalComposeStyledApi::class)
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
        val effectiveEnabled = enabled && onClick != null
        val renderColors = if (enabled && onClick == null) {
            colors.copy(
                disabledTextColor = colors.textColor,
                disabledLeadingIconColor = colors.leadingIconColor,
                disabledTrailingIconColor = colors.trailingIconColor,
            )
        } else {
            colors
        }
        CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides minHeight) {
            DropdownMenuItem(
                text = {
                    CopyMaterialThemeLocals(
                        contentColor = LocalContentColor.current,
                        content = { Row(content = content) },
                    )
                },
                onClick = onClick ?: {},
                modifier = modifier
                    .heightIn(min = minHeight)
                    .padding(itemPadding)
                    .clip(shape),
                enabled = effectiveEnabled,
                colors = MenuDefaults.itemColors(
                    textColor = renderColors.textColor,
                    leadingIconColor = renderColors.leadingIconColor,
                    trailingIconColor = renderColors.trailingIconColor,
                    disabledTextColor = renderColors.disabledTextColor,
                    disabledLeadingIconColor = renderColors.disabledLeadingIconColor,
                    disabledTrailingIconColor = renderColors.disabledTrailingIconColor,
                ),
                contentPadding = contentPadding,
                interactionSource = interactionSource,
            )
        }
    }

    @OptIn(InternalComposeStyledApi::class)
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
        CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides minHeight) {
            DropdownMenuItem(
                text = { CopyMaterialThemeLocals(contentColor = LocalContentColor.current, content = text) },
                onClick = onClick,
                modifier = modifier
                    .heightIn(min = minHeight)
                    .padding(itemPadding)
                    .clip(shape),
                leadingIcon = leadingIcon?.let { slot ->
                    { CopyMaterialThemeLocals(contentColor = LocalContentColor.current, content = slot) }
                },
                trailingIcon = trailingIcon?.let { slot ->
                    { CopyMaterialThemeLocals(contentColor = LocalContentColor.current, content = slot) }
                },
                enabled = enabled,
                colors = MenuDefaults.itemColors(
                    textColor = colors.textColor,
                    leadingIconColor = colors.leadingIconColor,
                    trailingIconColor = colors.trailingIconColor,
                    disabledTextColor = colors.disabledTextColor,
                    disabledLeadingIconColor = colors.disabledLeadingIconColor,
                    disabledTrailingIconColor = colors.disabledTrailingIconColor,
                ),
                contentPadding = contentPadding,
                interactionSource = interactionSource,
            )
        }
    }
}
