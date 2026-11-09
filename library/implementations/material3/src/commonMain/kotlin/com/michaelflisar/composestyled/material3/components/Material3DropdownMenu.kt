package com.michaelflisar.composestyled.material3.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MenuDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.ui.components.StyledPopupMenuItemRenderer
import com.michaelflisar.composestyled.ui.components.StyledMenuItemColors
import com.michaelflisar.composestyled.material3.ProvideStyledLocals
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi

@OptIn(InternalComposeStyledApi::class)
class Material3DropdownMenu internal constructor() : StyledPopupMenuItemRenderer.Implementation {

    override val defaults: StyledPopupMenuItemRenderer.Defaults
        @Composable get() = StyledPopupMenuItemRenderer.Defaults(
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
        horizontalArrangement: Arrangement.Horizontal,
        verticalAlignment: Alignment.Vertical,
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
                    ProvideStyledLocals(
                        contentColor = renderColors.textColor,
                        textStyle = StyledTheme.typography.bodyLarge,
                        content = {
                            Row(
                                horizontalArrangement = horizontalArrangement,
                                verticalAlignment = verticalAlignment,
                                content = content,
                            )
                        },
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
                text = {
                    ProvideStyledLocals(
                        contentColor = if (enabled) colors.textColor else colors.disabledTextColor,
                        textStyle = StyledTheme.typography.bodyLarge,
                        content = text,
                    )
                },
                onClick = onClick,
                modifier = modifier
                    .heightIn(min = minHeight)
                    .padding(itemPadding)
                    .clip(shape),
                leadingIcon = leadingIcon?.let { slot ->
                    {
                        ProvideStyledLocals(
                            contentColor = if (enabled) colors.leadingIconColor else colors.disabledLeadingIconColor,
                            content = slot,
                        )
                    }
                },
                trailingIcon = trailingIcon?.let { slot ->
                    {
                        ProvideStyledLocals(
                            contentColor = if (enabled) colors.trailingIconColor else colors.disabledTrailingIconColor,
                            content = slot,
                        )
                    }
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
