package com.michaelflisar.composestyled.ui.components

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
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.ui.overlay.StyledPopupContainer
import com.michaelflisar.composestyled.ui.primitives.StyledExpandIcon

/**
 * A themed button that opens a dropdown menu when clicked. Uses a
 * [StyledPopupContainer] where
 * - popup is a [StyledPopupMenu]
 * - content is a [StyledButton] with a [StyledExpandIcon] at the end of the content
 */

// -------------------------
// public API
// -------------------------

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
