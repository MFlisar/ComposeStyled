@file:OptIn(com.michaelflisar.composestyled.runtime.InternalComposeStyledApi::class)

package com.michaelflisar.composestyled.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
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
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.UpdateStyledThemeLocals
import com.michaelflisar.composestyled.registry.LocalStyledRegistry
import com.michaelflisar.composestyled.registry.StyledComponent
import com.michaelflisar.composestyled.registry.StyledComponentWithDefaults
import com.michaelflisar.composestyled.registry.StyledKey
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.tokens.LocalStyledContentColor
import com.michaelflisar.composestyled.ui.icons.StyledIcons
import com.michaelflisar.composestyled.ui.internal.LocalStyledMenuBarPopupContext
import com.michaelflisar.composestyled.ui.internal.LocalStyledMenuBarSubmenuAnchor
import com.michaelflisar.composestyled.ui.internal.MenuBarPopupMenuContent
import com.michaelflisar.composestyled.ui.internal.StyledMenuBarAncestor
import com.michaelflisar.composestyled.ui.internal.StyledMenuBarPopupContext
import com.michaelflisar.composestyled.ui.overlay.StyledPopupContainer
import com.michaelflisar.composestyled.ui.primitives.StyledHorizontalDivider

@InternalComposeStyledApi
object StyledPopupMenuItemRenderer {

    // -------------
    // 1) key
    // -------------

    val Key = StyledKey<Implementation>("StyledPopupMenuItemRenderer")
    val DefaultsKey = StyledKey<Defaults>("StyledPopupMenuItemRendererDefaults")

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
            Render(
                text = {
                    Row(
                        horizontalArrangement = horizontalArrangement,
                        verticalAlignment = verticalAlignment,
                        content = content,
                    )
                },
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
@InternalComposeStyledApi
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

@InternalComposeStyledApi
object StyledPopupMenuItemRendererDefaults {
    val shape: Shape
        @Composable get() = LocalStyledRegistry.current[StyledPopupMenuItemRenderer.DefaultsKey].itemShape

    val itemPadding: PaddingValues
        @Composable get() = LocalStyledRegistry.current[StyledPopupMenuItemRenderer.DefaultsKey].itemPadding

    val minHeight: Dp
        @Composable get() {
            val default = LocalStyledRegistry.current[StyledPopupMenuItemRenderer.DefaultsKey].minHeight
            return if (LocalStyledMenuBarPopupContext.current != null) {
                minOf(default, 36.dp)
            } else {
                default
            }
        }

    val contentPadding: PaddingValues
        @Composable get() = PaddingValues(
            horizontal = StyledTheme.padding.medium,
            vertical = if (LocalStyledMenuBarPopupContext.current != null) 0.dp
            else StyledTheme.padding.small,
        )

    @Composable
    fun colors(
        textColor: Color = LocalStyledContentColor.current,
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

    val widthConfig = StyledPopupMenu.WidthConfig.AtLeastAnchor
}

// -------------------------
// public API
// -------------------------

/** Renders a clickable menu row with optional leading and trailing content. */
@Composable
internal fun StyledDropdownMenuItem(
    text: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    enabled: Boolean = true,
    colors: StyledMenuItemColors = StyledPopupMenuItemRendererDefaults.colors(),
    contentPadding: PaddingValues = StyledPopupMenuItemRendererDefaults.contentPadding,
    interactionSource: MutableInteractionSource? = null,
    shape: Shape = StyledPopupMenuItemRendererDefaults.shape,
    itemPadding: PaddingValues = StyledPopupMenuItemRendererDefaults.itemPadding,
    minHeight: Dp = StyledPopupMenuItemRendererDefaults.minHeight,
) {
    val effectiveInteractionSource = menuItemInteractionSource(interactionSource)
    MenuBarMenuItemHoverEffect(effectiveInteractionSource)
    val effectiveLeadingIcon = leadingIcon ?: if (
        LocalStyledMenuBarPopupContext.current != null
    ) {
        { Spacer(Modifier.size(StyledIconDefaults.size)) }
    } else {
        null
    }

    LocalStyledRegistry.current[StyledPopupMenuItemRenderer.Key].Render(
        text, onClick, modifier, effectiveLeadingIcon, trailingIcon, enabled, colors,
        contentPadding, effectiveInteractionSource, shape, itemPadding, minHeight,
    )
}

/** Renders a custom menu row with the same interaction and styling as a menu item. */
@Composable
internal fun StyledDropdownMenuContent(
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    // defaults / customisation
    colors: StyledMenuItemColors = StyledPopupMenuItemRendererDefaults.colors(),
    contentPadding: PaddingValues = StyledPopupMenuItemRendererDefaults.contentPadding,
    interactionSource: MutableInteractionSource? = null,
    shape: Shape = StyledPopupMenuItemRendererDefaults.shape,
    itemPadding: PaddingValues = StyledPopupMenuItemRendererDefaults.itemPadding,
    minHeight: Dp = StyledPopupMenuItemRendererDefaults.minHeight,
    horizontalArrangement: Arrangement.Horizontal =
        Arrangement.spacedBy(StyledTheme.spacing.medium),
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    // content
    content: @Composable RowScope.() -> Unit,
) {
    val effectiveInteractionSource = menuItemInteractionSource(interactionSource)
    MenuBarMenuItemHoverEffect(effectiveInteractionSource)

    LocalStyledRegistry.current[StyledPopupMenuItemRenderer.Key].Render(
        content, onClick, modifier, enabled, colors, contentPadding,
        effectiveInteractionSource, shape, itemPadding, minHeight,
        horizontalArrangement, verticalAlignment,
    )
}


/**
 * Adds a consistently styled, non-interactive heading between dropdown menu item groups.
 */
@OptIn(InternalComposeStyledApi::class)
@Composable
internal fun StyledDropdownMenuSectionHeader(
    text: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = StyledTheme.typography.labelLarge,
    contentColor: Color = StyledTheme.colorScheme.primary,
    itemPadding: PaddingValues = StyledPopupMenuItemRendererDefaults.itemPadding,
    contentPadding: PaddingValues = StyledPopupMenuItemRendererDefaults.contentPadding,
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
internal fun StyledDropdownMenuDivider(
    modifier: Modifier = Modifier,
) {
    StyledHorizontalDivider(
        modifier = modifier.padding(
            vertical = if (LocalStyledMenuBarPopupContext.current != null) StyledTheme.padding.small
            else StyledTheme.spacing.small,
        ),
    )
}

/**
 * Adds a cascading submenu entry that opens beside its parent menu item.
 * The [dismiss] callback closes the whole menu hierarchy, while [dismissSubmenu]
 * closes only this submenu and its descendants.
 */
@OptIn(InternalComposeStyledApi::class)
@Composable
internal fun StyledPopupMenuSubmenu(
    text: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    enabled: Boolean = true,
    properties: PopupProperties = StyledPopupDefaults.properties(
        focusable = LocalStyledMenuBarPopupContext.current == null,
    ),
    animation: StyledPopup.Animation = StyledPopupMenuDefaults.animation,
    content: @Composable StyledPopupMenuScope.() -> Unit,
) {
    val expanded = remember { mutableStateOf(false) }
    val menuBarContext = LocalStyledMenuBarPopupContext.current
    val menuLevel = menuBarContext?.menuLevel
    val submenuId = remember(menuLevel) { Any() }
    val interactionSource = remember { MutableInteractionSource() }
    val hovered by interactionSource.collectIsHoveredAsState()
    val anchorHoverTarget = remember { Any() }
    val submenuPopupContext = if (menuBarContext != null && menuLevel != null) {
        remember(menuBarContext, submenuId) {
            StyledMenuBarPopupContext(
                rootLevel = menuBarContext.rootLevel,
                rootId = menuBarContext.rootId,
                menuLevel = menuLevel.createChildLevel(submenuId),
                ancestors = menuBarContext.ancestors +
                    StyledMenuBarAncestor(menuLevel, submenuId),
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
    val dismissFromPopup = {
        if (menuLevel != null) {
            menuLevel.closeFromPopup(submenuId, hovered)
        } else {
            expanded.value = false
        }
    }

    StyledPopupContainer(
        popup = {
            CompositionLocalProvider(
                LocalStyledPopupMenuDismissSubmenu provides dismissSubmenu,
            ) {
                if (submenuPopupContext != null) {
                    CompositionLocalProvider(
                        LocalStyledMenuBarPopupContext provides submenuPopupContext,
                    ) {
                        StyledPopupMenu(
                            expanded = isExpanded && enabled,
                            onDismissRequest = dismissFromPopup,
                            properties = properties,
                            widthConfig = StyledPopupMenuItemRendererDefaults.widthConfig,
                            placement = StyledPopupMenu.Placement.Submenu,
                            animation = animation,
                            content = {
                                val submenuScope = this
                                MenuBarPopupMenuContent(submenuPopupContext) {
                                    content(submenuScope)
                                }
                            },
                        )
                    }
                } else {
                    StyledPopupMenu(
                        expanded = isExpanded && enabled,
                        onDismissRequest = dismissFromPopup,
                        properties = properties,
                        widthConfig = StyledPopupMenuItemRendererDefaults.widthConfig,
                        placement = StyledPopupMenu.Placement.Submenu,
                        animation = animation,
                        content = content,
                    )
                }
            }
        },
        content = {
            CompositionLocalProvider(
                LocalStyledMenuBarSubmenuAnchor provides (menuBarContext != null),
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
                            icon = StyledIcons.Submenu,
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

/**
 * Adds a menu item that returns to the parent menu from a submenu.
 */
@Composable
internal fun StyledPopupMenuBackItem(
    text: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    StyledDropdownMenuItem(
        text = text,
        onClick = onClick,
        modifier = modifier,
        leadingIcon = {
            StyledIcon(
                icon = StyledIcons.SubmenuBack,
                contentDescription = null,
            )
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
    if (LocalStyledMenuBarPopupContext.current == null || interactionSource != null) {
        return interactionSource
    }
    return remember { MutableInteractionSource() }
}

@Composable
private fun MenuBarMenuItemHoverEffect(
    interactionSource: MutableInteractionSource?,
) {
    val menuBarContext = LocalStyledMenuBarPopupContext.current
    val isSubmenuAnchor = LocalStyledMenuBarSubmenuAnchor.current
    val hovered = interactionSource?.collectIsHoveredAsState()?.value == true
    LaunchedEffect(menuBarContext, hovered, isSubmenuAnchor) {
        if (menuBarContext != null && hovered && !isSubmenuAnchor) {
            menuBarContext.menuLevel.closeActive()
        }
    }
}