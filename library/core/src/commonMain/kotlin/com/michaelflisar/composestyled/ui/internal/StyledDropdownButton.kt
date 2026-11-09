package com.michaelflisar.composestyled.ui.internal

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.classes.IconComposable
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.runtime.LocalStyledContext
import com.michaelflisar.composestyled.ui.components.StyledButton
import com.michaelflisar.composestyled.ui.components.StyledButtonDefaults
import com.michaelflisar.composestyled.ui.components.StyledIcon
import com.michaelflisar.composestyled.ui.components.StyledIconDefaults
import com.michaelflisar.composestyled.ui.components.StyledMenuBar
import com.michaelflisar.composestyled.ui.components.LocalStyledPopupMenuDismiss
import com.michaelflisar.composestyled.ui.components.StyledPopup
import com.michaelflisar.composestyled.ui.components.StyledPopupDefaults
import com.michaelflisar.composestyled.ui.components.StyledPopupMenu
import com.michaelflisar.composestyled.ui.components.StyledPopupMenuDefaults
import com.michaelflisar.composestyled.ui.components.StyledPopupMenuScope
import com.michaelflisar.composestyled.ui.overlay.StyledPopupContainer
import com.michaelflisar.composestyled.ui.primitives.StyledExpandIcon

// -------------------------
// public API
// -------------------------

/**
 * A themed menu-bar item that opens after hovering and switches with sibling menus.
 * Its popup is non-focusable by default inside [StyledMenuBar] so pointer hover can reach siblings.
 */
@OptIn(InternalComposeStyledApi::class)
@Composable
internal fun StyledMenuBarItem(
    menu: @Composable StyledPopupMenuScope.() -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    variant: StyledButton.Variant = StyledButton.Variant.Auto,
    // defaults / customisation
    shape: Shape = StyledButtonDefaults.shape,
    customization: StyledButton.Customization? = null,
    // menu
    menuProperties: PopupProperties = StyledPopupDefaults.properties(
        focusable = LocalStyledMenuBarLevel.current == null,
    ),
    menuWidthConfig: StyledPopupMenu.WidthConfig =
        StyledPopupMenu.WidthConfig.Auto(maxWidth = 260.dp),
    menuShape: Shape = StyledPopupMenuDefaults.shape,
    menuContainerColor: Color = StyledPopupMenuDefaults.containerColor,
    menuContentColor: Color = StyledPopupMenuDefaults.contentColor(menuContainerColor),
    menuOffset: DpOffset = StyledPopupMenuDefaults.offset,
    menuHorizontalAlignment: StyledPopupMenu.HorizontalAlignment =
        StyledPopupMenu.HorizontalAlignment.Start,
    menuMaxHeight: Dp = StyledPopupMenuDefaults.maxHeight,
    menuAnimation: StyledPopup.Animation = StyledPopupDefaults.Animations.unfoldAwayFromAnchor,
    // content
    icon: IconComposable? = null,
    content: @Composable RowScope.() -> Unit,
) {
    val menuBarLevel = LocalStyledMenuBarLevel.current
    val menuId = remember(menuBarLevel) { Any() }
    val fallbackExpanded = remember { mutableStateOf(false) }
    val menuLevel = remember(menuBarLevel, menuId) {
        menuBarLevel?.createChildLevel(menuId)
    }
    val effectiveInteractionSource = if (menuBarLevel != null && interactionSource == null) {
        remember(menuBarLevel) { MutableInteractionSource() }
    } else {
        interactionSource
    }
    val hovered = effectiveInteractionSource?.collectIsHoveredAsState()?.value == true
    val anchorHoverTarget = remember(menuBarLevel, menuId) { Any() }
    LaunchedEffect(menuBarLevel, menuId, enabled, hovered) {
        if (menuBarLevel != null) {
            if (enabled) {
                menuBarLevel.setHoverTarget(
                    id = menuId,
                    target = anchorHoverTarget,
                    hovered = hovered,
                    openOnHover = true,
                )
            } else {
                menuBarLevel.close(menuId)
            }
        }
        if (!enabled) {
            fallbackExpanded.value = false
        }
    }
    val popupContext = if (menuBarLevel != null && menuLevel != null) {
        remember(menuBarLevel, menuId, menuLevel) {
            StyledMenuBarPopupContext(
                rootLevel = menuBarLevel,
                rootId = menuId,
                menuLevel = menuLevel,
            )
        }
    } else {
        null
    }
    val expanded = menuBarLevel?.isActive(menuId) ?: fallbackExpanded.value
    val dismiss = {
        if (menuBarLevel != null) {
            menuBarLevel.close(menuId)
        } else {
            fallbackExpanded.value = false
        }
    }
    val dismissFromPopup = {
        if (menuBarLevel != null) {
            menuBarLevel.closeFromPopup(menuId, hovered)
        } else {
            fallbackExpanded.value = false
        }
    }

    StyledPopupContainer(
        popup = {
            CompositionLocalProvider(LocalStyledPopupMenuDismiss provides dismiss) {
                if (popupContext != null) {
                    CompositionLocalProvider(LocalStyledMenuBarPopupContext provides popupContext) {
                        StyledPopupMenu(
                            expanded = expanded && enabled,
                            onDismissRequest = dismissFromPopup,
                            properties = menuProperties,
                            widthConfig = menuWidthConfig,
                            shape = menuShape,
                            containerColor = menuContainerColor,
                            contentColor = menuContentColor,
                            offset = menuOffset,
                            horizontalAlignment = menuHorizontalAlignment,
                            maxHeight = menuMaxHeight,
                            animation = menuAnimation,
                        ) {
                            MenuBarPopupMenuContent(popupContext) {
                                menu()
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
                        horizontalAlignment = menuHorizontalAlignment,
                        maxHeight = menuMaxHeight,
                        animation = menuAnimation,
                    ) {
                        menu()
                    }
                }
            }
        },
        content = {
            StyledButton(
                onClick = {
                    if (menuBarLevel != null) {
                        menuBarLevel.openFromClick(menuId)
                    } else {
                        fallbackExpanded.value = !fallbackExpanded.value
                    }
                },
                variant = variant,
                modifier = modifier,
                enabled = enabled,
                interactionSource = effectiveInteractionSource,
                shape = shape,
                customization = customization,
                content = {
                    DropdownButtonContent(
                        expanded = expanded && enabled,
                        content = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(
                                    StyledTheme.spacing.medium
                                ),
                            ) {
                                icon?.let {
                                    Box(
                                        modifier = Modifier.size(StyledIconDefaults.size),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        StyledIcon(icon = it, contentDescription = null)
                                    }
                                }
                                content()
                            }
                        },
                        iconSize = StyledIconDefaults.size,
                    )
                },
            )
        },
    )
}

/** A themed button with an expand indicator; delegates the click to [onClick]. */
@Composable
internal fun StyledDropdownButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    variant: StyledButton.Variant = StyledButton.Variant.Auto,
    // defaults / customisation
    shape: Shape = StyledButtonDefaults.shape,
    customization: StyledButton.Customization? = null,
    // content
    content: @Composable RowScope.() -> Unit,
) = StyledDropdownButtonImpl(
    variant = variant,
    onClick = onClick,
    modifier = modifier,
    enabled = enabled,
    interactionSource = interactionSource,
    shape = shape,
    customization = customization,
    content = content,
)

/** A themed button that toggles a popup menu. */
@Composable
internal fun StyledDropdownButton(
    menu: @Composable StyledPopupMenuScope.() -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    variant: StyledButton.Variant = StyledButton.Variant.Auto,
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
    menuHorizontalAlignment: StyledPopupMenu.HorizontalAlignment =
        StyledPopupMenu.HorizontalAlignment.Start,
    menuMaxHeight: Dp = StyledPopupMenuDefaults.maxHeight,
    menuAnimation: StyledPopup.Animation = StyledPopupDefaults.Animations.unfoldAwayFromAnchor,
    // content
    content: @Composable RowScope.() -> Unit,
) = StyledDropdownButtonImpl(
    variant = variant,
    menu = menu,
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
    menuHorizontalAlignment = menuHorizontalAlignment,
    menuMaxHeight = menuMaxHeight,
    menuAnimation = menuAnimation,
    content = content,
)

// -------------------------
// private API
// -------------------------

@OptIn(InternalComposeStyledApi::class)
@Composable
private fun StyledDropdownButtonImpl(
    variant: StyledButton.Variant,
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
    CompositionLocalProvider(
        LocalStyledContext provides StyledButton.Context.Dropdown,
    ) {
        StyledButton(
            onClick = onClick,
            variant = variant,
            modifier = modifier,
            enabled = enabled,
            interactionSource = interactionSource,
            shape = shape,
            customization = customization,
            content = { DropdownButtonContent(expanded = false, content = content) },
        )
    }
}

@OptIn(InternalComposeStyledApi::class)
@Composable
private fun StyledDropdownButtonImpl(
    variant: StyledButton.Variant,
    menu: @Composable StyledPopupMenuScope.() -> Unit,
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
    menuHorizontalAlignment: StyledPopupMenu.HorizontalAlignment =
        StyledPopupMenu.HorizontalAlignment.Start,
    menuMaxHeight: Dp = StyledPopupMenuDefaults.maxHeight,
    menuAnimation: StyledPopup.Animation = StyledPopupDefaults.Animations.unfoldAwayFromAnchor,
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
                horizontalAlignment = menuHorizontalAlignment,
                maxHeight = menuMaxHeight,
                animation = menuAnimation,
            ) {
                menu()
            }
        },
        content = {
            CompositionLocalProvider(
                LocalStyledContext provides StyledButton.Context.Dropdown,
            ) {
                StyledButton(
                    onClick = { expanded.value = !expanded.value },
                    variant = variant,
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
    iconSize: Dp = StyledTheme.sizes.iconSize,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(StyledTheme.spacing.medium),
    ) {
        content()
        StyledExpandIcon(
            expanded = expanded,
            rotationDirection = StyledExpandIcon.RotationDirection.Clockwise,
            size = iconSize,
        )
    }
}
