package com.michaelflisar.composestyled.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.window.PopupProperties
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.ui.internal.LocalStyledToolbarMenuLevel
import com.michaelflisar.composestyled.ui.internal.LocalStyledToolbarPopupContext
import com.michaelflisar.composestyled.ui.internal.StyledToolbarPopupContext
import com.michaelflisar.composestyled.ui.internal.ToolbarPopupMenuContent
import com.michaelflisar.composestyled.ui.overlay.StyledPopupContainer
import com.michaelflisar.composestyled.ui.primitives.StyledExpandIcon

// -------------------------
// public API
// -------------------------

/**
 * A themed toolbar menu button that opens after hovering and switches with sibling menus.
 * Its popup is non-focusable by default inside [StyledToolbar] so pointer hover can reach siblings.
 */
@Composable
fun StyledToolbarMenuButton(
    menuContent: @Composable ColumnScope.(dismiss: () -> Unit) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    role: StyledButton.Role = StyledButton.Role.Default,
    // defaults / customisation
    shape: Shape = StyledButtonDefaults.shape,
    customization: StyledButton.Customization? = null,
    // menu
    menuProperties: PopupProperties = StyledPopupDefaults.properties(
        focusable = LocalStyledToolbarMenuLevel.current == null,
    ),
    menuWidthConfig: StyledPopupMenu.WidthConfig = StyledPopupMenu.WidthConfig.Auto(),
    menuShape: Shape = StyledPopupMenuDefaults.shape,
    menuContainerColor: Color = StyledPopupMenuDefaults.containerColor,
    menuContentColor: Color = StyledPopupMenuDefaults.contentColor(menuContainerColor),
    menuOffset: DpOffset = StyledPopupMenuDefaults.offset,
    menuMaxHeight: Dp = StyledPopupMenuDefaults.maxHeight,
    // content
    content: @Composable RowScope.() -> Unit,
) {
    val toolbarLevel = LocalStyledToolbarMenuLevel.current
    val menuId = remember(toolbarLevel) { Any() }
    val fallbackExpanded = remember { mutableStateOf(false) }
    val menuLevel = remember(toolbarLevel, menuId) {
        toolbarLevel?.createChildLevel(menuId)
    }
    val effectiveInteractionSource = if (toolbarLevel != null && interactionSource == null) {
        remember(toolbarLevel) { MutableInteractionSource() }
    } else {
        interactionSource
    }
    val hovered = effectiveInteractionSource?.collectIsHoveredAsState()?.value == true
    val anchorHoverTarget = remember(toolbarLevel, menuId) { Any() }
    LaunchedEffect(toolbarLevel, menuId, enabled, hovered) {
        if (toolbarLevel != null) {
            if (enabled) {
                toolbarLevel.setHoverTarget(
                    id = menuId,
                    target = anchorHoverTarget,
                    hovered = hovered,
                    openOnHover = true,
                )
            } else {
                toolbarLevel.close(menuId)
            }
        }
        if (!enabled) {
            fallbackExpanded.value = false
        }
    }
    val popupContext = if (toolbarLevel != null && menuLevel != null) {
        remember(toolbarLevel, menuId, menuLevel) {
            StyledToolbarPopupContext(
                rootLevel = toolbarLevel,
                rootId = menuId,
                menuLevel = menuLevel,
            )
        }
    } else {
        null
    }
    val expanded = toolbarLevel?.isActive(menuId) ?: fallbackExpanded.value
    val dismiss = {
        if (toolbarLevel != null) {
            toolbarLevel.close(menuId)
        } else {
            fallbackExpanded.value = false
        }
    }
    val dismissFromPopup = {
        if (toolbarLevel != null) {
            toolbarLevel.closeFromPopup(menuId, hovered)
        } else {
            fallbackExpanded.value = false
        }
    }

    StyledPopupContainer(
        popup = {
            CompositionLocalProvider(LocalStyledPopupMenuDismiss provides dismiss) {
                if (popupContext != null) {
                    CompositionLocalProvider(LocalStyledToolbarPopupContext provides popupContext) {
                        StyledPopupMenu(
                            expanded = expanded && enabled,
                            onDismissRequest = dismissFromPopup,
                            properties = menuProperties,
                            widthConfig = menuWidthConfig,
                            shape = menuShape,
                            containerColor = menuContainerColor,
                            contentColor = menuContentColor,
                            offset = menuOffset,
                            maxHeight = menuMaxHeight,
                        ) {
                            ToolbarPopupMenuContent(popupContext) {
                                menuContent(dismiss)
                            }
                        }
                    }
                } else {
                    StyledPopupMenu(
                        expanded = expanded && enabled,
                        onDismissRequest = dismissFromPopup,
                        properties = menuProperties,
                        widthConfig = menuWidthConfig,
                        shape = menuShape,
                        containerColor = menuContainerColor,
                        contentColor = menuContentColor,
                        offset = menuOffset,
                        maxHeight = menuMaxHeight,
                    ) {
                        menuContent(dismiss)
                    }
                }
            }
        },
        content = {
            StyledButton(
                onClick = {
                    if (toolbarLevel != null) {
                        toolbarLevel.openFromClick(menuId)
                    } else {
                        fallbackExpanded.value = !fallbackExpanded.value
                    }
                },
                role = role,
                modifier = modifier,
                enabled = enabled,
                interactionSource = effectiveInteractionSource,
                shape = shape,
                customization = customization,
                content = {
                    DropdownButtonContent(expanded = expanded && enabled, content = content)
                },
            )
        },
    )
}

/** A themed button with an expand indicator; delegates the click to [onClick]. */
@Composable
fun StyledDropdownButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    role: StyledButton.Role = StyledButton.Role.Default,
    // defaults / customisation
    shape: Shape = StyledButtonDefaults.shape,
    customization: StyledButton.Customization? = null,
    // content
    content: @Composable RowScope.() -> Unit,
) = StyledDropdownButtonImpl(
    role = role,
    onClick = onClick,
    modifier = modifier,
    enabled = enabled,
    interactionSource = interactionSource,
    shape = shape,
    customization = customization,
    content = content,
)

/** A themed button that toggles a popup menu; [menuContent] receives a dismiss callback. */
@Composable
fun StyledDropdownButton(
    menuContent: @Composable ColumnScope.(dismiss: () -> Unit) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    role: StyledButton.Role = StyledButton.Role.Default,
    // defaults / customisation
    shape: Shape = StyledButtonDefaults.shape,
    customization: StyledButton.Customization? = null,
    // menu
    expanded: MutableState<Boolean> = remember { mutableStateOf(false) },
    menuWidthConfig: StyledPopupMenu.WidthConfig = StyledPopupMenuDefaults.widthConfig,
    menuShape: Shape = StyledPopupMenuDefaults.shape,
    menuContainerColor: Color = StyledPopupMenuDefaults.containerColor,
    menuContentColor: Color = StyledPopupMenuDefaults.contentColor(menuContainerColor),
    menuOffset: DpOffset = StyledPopupMenuDefaults.offset,
    menuMaxHeight: Dp = StyledPopupMenuDefaults.maxHeight,
    // content
    content: @Composable RowScope.() -> Unit,
) = StyledDropdownButtonImpl(
    role = role,
    menuContent = menuContent,
    expanded = expanded,
    modifier = modifier,
    enabled = enabled,
    interactionSource = interactionSource,
    shape = shape,
    customization = customization,
    menuWidthConfig = menuWidthConfig,
    menuShape = menuShape,
    menuContainerColor = menuContainerColor,
    menuContentColor = menuContentColor,
    menuOffset = menuOffset,
    menuMaxHeight = menuMaxHeight,
    content = content,
)

// -------------------------
// private API
// -------------------------

@Composable
private fun StyledDropdownButtonImpl(
    role: StyledButton.Role,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    // defaults / customisation
    shape: Shape = StyledButtonDefaults.shape,
    customization: StyledButton.Customization? = null,
    // content
    content: @Composable RowScope.() -> Unit,
) {
    StyledButton(
        onClick = onClick,
        role = role,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        shape = shape,
        customization = customization,
        content = { DropdownButtonContent(expanded = false, content = content) },
    )
}

@Composable
private fun StyledDropdownButtonImpl(
    role: StyledButton.Role,
    menuContent: @Composable ColumnScope.(dismiss: () -> Unit) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    // defaults / customisation
    shape: Shape = StyledButtonDefaults.shape,
    customization: StyledButton.Customization? = null,
    // menu

    expanded: MutableState<Boolean> = remember { mutableStateOf(false) },
    menuWidthConfig: StyledPopupMenu.WidthConfig = StyledPopupMenuDefaults.widthConfig,
    menuShape: Shape = StyledPopupMenuDefaults.shape,
    menuContainerColor: Color = StyledPopupMenuDefaults.containerColor,
    menuContentColor: Color = StyledPopupMenuDefaults.contentColor(menuContainerColor),
    menuOffset: DpOffset = StyledPopupMenuDefaults.offset,
    menuMaxHeight: Dp = StyledPopupMenuDefaults.maxHeight,
    // content
    content: @Composable RowScope.() -> Unit,
) {
    LaunchedEffect(enabled) {
        if (!enabled) expanded.value = false
    }

    StyledPopupContainer(
        popup = {
            StyledPopupMenu(
                expanded = expanded.value && enabled,
                onDismissRequest = { expanded.value = false },
                widthConfig = menuWidthConfig,
                shape = menuShape,
                containerColor = menuContainerColor,
                contentColor = menuContentColor,
                offset = menuOffset,
                maxHeight = menuMaxHeight,
            ) {
                menuContent { expanded.value = false }
            }
        },
        content = {
            StyledButton(
                onClick = { expanded.value = !expanded.value },
                role = role,
                modifier = modifier,
                enabled = enabled,
                interactionSource = interactionSource,
                shape = shape,
                customization = customization,
                content = {
                    DropdownButtonContent(expanded = expanded.value && enabled, content = content)
                },
            )
        }
    )
}

// -------------------------
// private functions
// -------------------------

@Composable
private fun DropdownButtonContent(
    expanded: Boolean,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(StyledTheme.spacing.small),
    ) {
        content()
        StyledExpandIcon(
            expanded = expanded,
            rotationDirection = StyledExpandIcon.RotationDirection.Clockwise,
        )
    }
}
