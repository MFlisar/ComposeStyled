package com.michaelflisar.composestyledcomponents.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
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
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.ui.overlay.StyledPopupContainer
import com.michaelflisar.composestyledcomponents.ui.primitives.StyledExpandIcon

/**
 * convenience functions for a dropdown button, which is a button that opens a dropdown menu when clicked.
 * uses a [StyledPopupContainer] where
 * - popup is a [StyledPopupMenu]
 * - content is a [StyledButton] with a [StyledExpandIcon] at the end of the content
 */

// -------------------------
// public API
// -------------------------

@Composable
fun StyledDropdownPrimaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    // defaults / customisation
    shape: Shape = StyledButtonDefaults.shape,
    customization: StyledButton.Customization? = null,
    // content
    content: @Composable RowScope.() -> Unit,
) = StyledDropdownButtonImpl(
    role = StyledButton.Role.Primary,
    onClick = onClick,
    modifier = modifier,
    enabled = enabled,
    interactionSource = interactionSource,
    shape = shape,
    customization = customization,
    content = content,
)

@Composable
fun StyledDropdownPrimaryButton(
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
) = StyledDropdownButtonImpl(
    role = StyledButton.Role.Primary,
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

@Composable
fun StyledDropdownSecondaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    // defaults / customisation
    shape: Shape = StyledButtonDefaults.shape,
    customization: StyledButton.Customization? = null,
    // content
    content: @Composable RowScope.() -> Unit,
) = StyledDropdownButtonImpl(
    role = StyledButton.Role.Secondary,
    onClick = onClick,
    modifier = modifier,
    enabled = enabled,
    interactionSource = interactionSource,
    shape = shape,
    customization = customization,
    content = content,
)

@Composable
fun StyledDropdownSecondaryButton(
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
) = StyledDropdownButtonImpl(
    role = StyledButton.Role.Secondary,
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

@Composable
fun StyledDropdownTertiaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    // defaults / customisation
    shape: Shape = StyledButtonDefaults.shape,
    customization: StyledButton.Customization? = null,
    // content
    content: @Composable RowScope.() -> Unit,
) = StyledDropdownButtonImpl(
    role = StyledButton.Role.Tertiary,
    onClick = onClick,
    modifier = modifier,
    enabled = enabled,
    interactionSource = interactionSource,
    shape = shape,
    customization = customization,
    content = content,
)

@Composable
fun StyledDropdownTertiaryButton(
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
) = StyledDropdownButtonImpl(
    role = StyledButton.Role.Tertiary,
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
    ButtonForRole(
        role = role,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        shape = shape,
        customization = customization,
    ) {
        DropdownButtonContent(expanded = false, content = content)
    }
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
            ButtonForRole(
                role = role,
                onClick = { expanded.value = !expanded.value },
                modifier = modifier,
                enabled = enabled,
                interactionSource = interactionSource,
                shape = shape,
                customization = customization,
            ) {
                DropdownButtonContent(expanded = expanded.value && enabled, content = content)
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
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(StyledTheme.spacing.small),
    ) {
        content()
        StyledExpandIcon(expanded = expanded, sideIsLeft = false)
    }
}

@Composable
private fun ButtonForRole(
    role: StyledButton.Role,
    onClick: () -> Unit,
    modifier: Modifier,
    enabled: Boolean,
    interactionSource: MutableInteractionSource?,
    shape: Shape,
    customization: StyledButton.Customization?,
    content: @Composable RowScope.() -> Unit,
) {
    when (role) {
        StyledButton.Role.Primary -> StyledPrimaryButton(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            interactionSource = interactionSource,
            shape = shape,
            customization = customization,
            content = content,
        )

        StyledButton.Role.Secondary -> StyledSecondaryButton(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            interactionSource = interactionSource,
            shape = shape,
            customization = customization,
            content = content,
        )

        StyledButton.Role.Tertiary -> StyledTertiaryButton(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            interactionSource = interactionSource,
            shape = shape,
            customization = customization,
            content = content,
        )
    }
}
