package com.michaelflisar.composestyled.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.michaelflisar.composestyled.UpdateStyledThemeLocals
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.registry.LocalStyledRegistry
import com.michaelflisar.composestyled.registry.StyledComponent
import com.michaelflisar.composestyled.registry.StyledComponentWithDefaults
import com.michaelflisar.composestyled.registry.StyledKey
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.icons.StyledIcons
import com.michaelflisar.composestyled.ui.internal.LocalStyledToolbarPopupContext
import com.michaelflisar.composestyled.ui.internal.LocalStyledToolbarSubmenuAnchor
import com.michaelflisar.composestyled.ui.internal.StyledToolbarMenuAncestor
import com.michaelflisar.composestyled.ui.internal.StyledToolbarPopupContext
import com.michaelflisar.composestyled.ui.internal.ToolbarPopupMenuContent
import com.michaelflisar.composestyled.ui.overlay.StyledPopupContainer
import com.michaelflisar.composestyled.ui.primitives.StyledText
import com.michaelflisar.composestyled.ui.primitives.StyledHorizontalDivider

object StyledDropdownMenuItem {

    // -------------
    // 1) key
    // -------------

    val Key = StyledKey<Implementation>("StyledDropdownMenuItem")
    val DefaultsKey = StyledKey<Defaults>("StyledDropdownMenuItemDefaults")

    // -------------
    // 2) defaults
    // -------------

    @Stable
    data class Defaults(
        val minHeight: Dp,
        val itemPadding: PaddingValues,
        val itemShape: Shape,
    )

    // -------------
    // 5) implementation
    // -------------

    interface Implementation : StyledComponent<Implementation>,
        StyledComponentWithDefaults<Defaults> {
        override val key: StyledKey<Implementation>
            get() = Key

        override val defaultsKey: StyledKey<Defaults>
            get() = DefaultsKey

        @Composable
        fun Render(
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
        )

        @Composable
        fun Render(
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
            Render(
                text = { Row(content = content) },
                onClick = onClick ?: {},
                modifier = modifier,
                leadingIcon = null,
                trailingIcon = null,
                enabled = effectiveEnabled,
                colors = renderColors,
                contentPadding = contentPadding,
                interactionSource = interactionSource,
                shape = shape,
                itemPadding = itemPadding,
                minHeight = minHeight,
            )
        }
    }
}

@Immutable
data class StyledMenuItemColors(
    val textColor: Color,
    val leadingIconColor: Color,
    val trailingIconColor: Color,
    val disabledTextColor: Color,
    val disabledLeadingIconColor: Color,
    val disabledTrailingIconColor: Color,
)

// -------------------------
// defaults
// -------------------------

object StyledDropdownMenuItemDefaults {
    val shape: Shape
        @Composable get() = LocalStyledRegistry.current[StyledDropdownMenuItem.DefaultsKey].itemShape

    val itemPadding: PaddingValues
        @Composable get() = LocalStyledRegistry.current[StyledDropdownMenuItem.DefaultsKey].itemPadding

    val minHeight: Dp
        @Composable get() {
            val default = LocalStyledRegistry.current[StyledDropdownMenuItem.DefaultsKey].minHeight
            return if (LocalStyledToolbarPopupContext.current != null) {
                minOf(default, 36.dp)
            } else {
                default
            }
        }

    val contentPadding: PaddingValues
        @Composable get() = PaddingValues(
            horizontal = StyledTheme.padding.medium,
            vertical = if (LocalStyledToolbarPopupContext.current != null) 0.dp
            else StyledTheme.padding.small,
        )

    @Composable
    fun colors(
        textColor: Color = StyledTheme.currentContentColor,
        leadingIconColor: Color = textColor,
        trailingIconColor: Color = textColor,
        disabledTextColor: Color = textColor.copy(
            alpha = textColor.alpha * StyledTheme.colorScheme.disableFactors.content
        ),
        disabledLeadingIconColor: Color = leadingIconColor.copy(
            alpha = leadingIconColor.alpha * StyledTheme.colorScheme.disableFactors.content
        ),
        disabledTrailingIconColor: Color = trailingIconColor.copy(
            alpha = trailingIconColor.alpha * StyledTheme.colorScheme.disableFactors.content
        ),
    ) = StyledMenuItemColors(
        textColor, leadingIconColor, trailingIconColor,
        disabledTextColor, disabledLeadingIconColor, disabledTrailingIconColor,
    )
}

// -------------------------
// public API
// -------------------------

/** Renders a clickable menu row with optional leading and trailing content. */
@Composable
fun StyledDropdownMenuItem(
    text: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    enabled: Boolean = true,
    colors: StyledMenuItemColors = StyledDropdownMenuItemDefaults.colors(),
    contentPadding: PaddingValues = StyledDropdownMenuItemDefaults.contentPadding,
    interactionSource: MutableInteractionSource? = null,
    shape: Shape = StyledDropdownMenuItemDefaults.shape,
    itemPadding: PaddingValues = StyledDropdownMenuItemDefaults.itemPadding,
    minHeight: Dp = StyledDropdownMenuItemDefaults.minHeight,
) {
    val effectiveInteractionSource = menuItemInteractionSource(interactionSource)
    ToolbarMenuItemHoverEffect(effectiveInteractionSource)

    LocalStyledRegistry.current[StyledDropdownMenuItem.Key].Render(
        text, onClick, modifier, leadingIcon, trailingIcon, enabled, colors,
        contentPadding, effectiveInteractionSource, shape, itemPadding, minHeight,
    )
}

/** Renders a custom menu row with the same interaction and styling as a menu item. */
@Composable
fun StyledDropdownMenuContent(
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    // defaults / customisation
    colors: StyledMenuItemColors = StyledDropdownMenuItemDefaults.colors(),
    contentPadding: PaddingValues = StyledDropdownMenuItemDefaults.contentPadding,
    interactionSource: MutableInteractionSource? = null,
    shape: Shape = StyledDropdownMenuItemDefaults.shape,
    itemPadding: PaddingValues = StyledDropdownMenuItemDefaults.itemPadding,
    minHeight: Dp = StyledDropdownMenuItemDefaults.minHeight,
    // content
    content: @Composable RowScope.() -> Unit,
) {
    val effectiveInteractionSource = menuItemInteractionSource(interactionSource)
    ToolbarMenuItemHoverEffect(effectiveInteractionSource)

    LocalStyledRegistry.current[StyledDropdownMenuItem.Key].Render(
        content, onClick, modifier, enabled, colors, contentPadding,
        effectiveInteractionSource, shape, itemPadding, minHeight,
    )
}


/**
 * Adds a consistently styled, non-interactive heading between dropdown menu item groups.
 */
@OptIn(InternalComposeStyledApi::class)
@Composable
fun StyledDropdownMenuSectionHeader(
    text: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = StyledTheme.typography.labelLarge,
    contentColor: Color = StyledTheme.colorScheme.primary,
    itemPadding: PaddingValues = StyledDropdownMenuItemDefaults.itemPadding,
    contentPadding: PaddingValues = StyledDropdownMenuItemDefaults.contentPadding,
) {
    val layoutDirection = LocalLayoutDirection.current
    UpdateStyledThemeLocals(
        contentColor = contentColor,
        textStyle = textStyle,
        content = {
            Box(
                modifier = modifier
                    .semantics { heading() }
                    .padding(
                        start = itemPadding.calculateStartPadding(layoutDirection) +
                                contentPadding.calculateStartPadding(layoutDirection),
                        top = contentPadding.calculateTopPadding(),
                        bottom = contentPadding.calculateBottomPadding(),
                    ),
                content = { text() },
            )
        },
    )
}

/**
 * Adds a horizontal divider with vertical spacing between dropdown menu groups.
 */
@Composable
fun StyledDropdownMenuDivider(
    modifier: Modifier = Modifier,
) {
    StyledHorizontalDivider(
        modifier = modifier.padding(
            vertical = if (LocalStyledToolbarPopupContext.current != null) 4.dp
            else StyledTheme.spacing.small,
        ),
    )
}

/**
 * Adds a cascading submenu entry that opens beside its parent menu item.
 * The [dismiss] callback closes the whole menu hierarchy, while [dismissSubmenu]
 * closes only this submenu and its descendants.
 */
@Composable
fun StyledPopupMenuSubmenu(
    text: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    enabled: Boolean = true,
    properties: PopupProperties = StyledPopupDefaults.properties(
        focusable = LocalStyledToolbarPopupContext.current == null,
    ),
    content: @Composable ColumnScope.(
        dismiss: () -> Unit,
        dismissSubmenu: () -> Unit,
    ) -> Unit,
) {
    val expanded = remember { mutableStateOf(false) }
    val toolbarContext = LocalStyledToolbarPopupContext.current
    val menuLevel = toolbarContext?.menuLevel
    val submenuId = remember(menuLevel) { Any() }
    val interactionSource = remember { MutableInteractionSource() }
    val hovered by interactionSource.collectIsHoveredAsState()
    val anchorHoverTarget = remember { Any() }
    val submenuPopupContext = if (toolbarContext != null && menuLevel != null) {
        remember(toolbarContext, submenuId) {
            StyledToolbarPopupContext(
                rootLevel = toolbarContext.rootLevel,
                rootId = toolbarContext.rootId,
                menuLevel = menuLevel.createChildLevel(submenuId),
                ancestors = toolbarContext.ancestors +
                    StyledToolbarMenuAncestor(menuLevel, submenuId),
            )
        }
    } else {
        null
    }

    LaunchedEffect(enabled, hovered, menuLevel, submenuId) {
        if (menuLevel != null) {
            if (enabled) {
                menuLevel.setHoverTarget(
                    id = submenuId,
                    target = anchorHoverTarget,
                    hovered = hovered,
                    openOnHover = true,
                )
            } else {
                menuLevel.close(submenuId)
            }
        } else if (!enabled) {
            expanded.value = false
        }
    }

    val isExpanded = menuLevel?.isActive(submenuId) ?: expanded.value
    val dismissSubmenu = {
        if (menuLevel != null) {
            menuLevel.close(submenuId)
        } else {
            expanded.value = false
        }
    }
    val dismissAll: () -> Unit = LocalStyledPopupMenuDismiss.current ?: {
        if (toolbarContext != null) {
            toolbarContext.rootLevel.close(toolbarContext.rootId)
        } else {
            expanded.value = false
        }
    }
    val dismissFromPopup = {
        if (menuLevel != null) {
            menuLevel.closeFromPopup(submenuId, hovered)
        } else {
            expanded.value = false
        }
    }

    StyledPopupContainer(
        popup = {
            if (submenuPopupContext != null) {
                CompositionLocalProvider(
                    LocalStyledToolbarPopupContext provides submenuPopupContext,
                ) {
                    StyledPopupMenu(
                        expanded = isExpanded && enabled,
                        onDismissRequest = dismissFromPopup,
                        properties = properties,
                        widthConfig = StyledPopupMenu.WidthConfig.Auto(),
                        placement = StyledPopupMenu.Placement.Submenu,
                    ) {
                        ToolbarPopupMenuContent(submenuPopupContext) {
                            content(dismissAll, dismissSubmenu)
                        }
                    }
                }
            } else {
                StyledPopupMenu(
                    expanded = isExpanded && enabled,
                    onDismissRequest = dismissFromPopup,
                    properties = properties,
                    widthConfig = StyledPopupMenu.WidthConfig.Auto(),
                    placement = StyledPopupMenu.Placement.Submenu,
                ) {
                    content(dismissAll, dismissSubmenu)
                }
            }
        },
        content = {
            CompositionLocalProvider(
                LocalStyledToolbarSubmenuAnchor provides (toolbarContext != null),
            ) {
                StyledDropdownMenuItem(
                    text = text,
                    onClick = {
                        if (menuLevel != null) {
                            menuLevel.openFromClick(submenuId)
                        } else {
                            expanded.value = !expanded.value
                        }
                    },
                    modifier = modifier,
                    leadingIcon = leadingIcon,
                    trailingIcon = {
                        StyledIcon(
                            icon = StyledIcons.NavigateForward,
                            contentDescription = "Open submenu",
                        )
                    },
                    enabled = enabled,
                    interactionSource = interactionSource,
                )
            }
        },
    )
}

// -------------------------
// private functions
// -------------------------

@Composable
private fun menuItemInteractionSource(
    interactionSource: MutableInteractionSource?,
): MutableInteractionSource? {
    if (LocalStyledToolbarPopupContext.current == null || interactionSource != null) {
        return interactionSource
    }
    return remember { MutableInteractionSource() }
}

@Composable
private fun ToolbarMenuItemHoverEffect(
    interactionSource: MutableInteractionSource?,
) {
    val toolbarContext = LocalStyledToolbarPopupContext.current
    val isSubmenuAnchor = LocalStyledToolbarSubmenuAnchor.current
    val hovered = interactionSource?.collectIsHoveredAsState()?.value == true
    LaunchedEffect(toolbarContext, hovered, isSubmenuAnchor) {
        if (toolbarContext != null && hovered && !isSubmenuAnchor) {
            toolbarContext.menuLevel.closeActive()
        }
    }
}