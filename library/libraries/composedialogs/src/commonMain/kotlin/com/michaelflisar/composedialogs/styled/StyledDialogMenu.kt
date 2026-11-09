package com.michaelflisar.composedialogs.styled

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composedialogs.styled.shared.composeDialog
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.ui.components.StyledIcon
import com.michaelflisar.composestyled.ui.components.StyledListItem
import com.michaelflisar.composestyled.ui.components.StyledListItemDefaults
import com.michaelflisar.composestyled.ui.icons.StyledIcons
import com.michaelflisar.composestyled.ui.layout.StyledColumn
import com.michaelflisar.composestyled.ui.layout.StyledDialog
import com.michaelflisar.composestyled.ui.layout.StyledDialogDefaults
import com.michaelflisar.composestyled.ui.layout.StyledVerticalScrollContainer
import com.michaelflisar.composestyled.ui.primitives.StyledHorizontalDivider
import com.michaelflisar.composestyled.ui.primitives.StyledText
import com.michaelflisar.composestyled.ui.state.DialogState

/** Entry types for a hierarchical menu rendered as a dialog list. */
object StyledDialogMenu {
    sealed interface Entry {
        @Stable
        class Action(
            val title: String,
            val onClick: () -> Unit,
            val description: String? = null,
            val enabled: Boolean = true,
            val dismissOnClick: Boolean = true,
            val leading: (@Composable () -> Unit)? = null,
        ) : Entry

        @Stable
        class Submenu(
            val title: String,
            val items: List<Entry>,
            val description: String? = null,
            val leading: (@Composable () -> Unit)? = null,
        ) : Entry

        @Stable
        class Header(val title: String) : Entry

        data object Divider : Entry

        @Stable
        class Custom(val content: @Composable () -> Unit) : Entry
    }
}

/** Shows Styled menu entries in a dialog; submenus are navigated as nested lists. */
@Composable
fun StyledDialogMenu(
    state: DialogState,
    items: List<StyledDialogMenu.Entry>,
    modifier: Modifier = Modifier,
    listModifier: Modifier = Modifier,
    dismissOnClick: Boolean = true,
    itemSpacing: Dp = StyledTheme.spacing.small,
    itemContentSpacing: Dp = StyledListItemDefaults.spacing,
    listContentPadding: PaddingValues = PaddingValues(StyledTheme.padding.small),
    itemShape: Shape = StyledListItemDefaults.shape,
    itemMinHeight: Dp = StyledListItemDefaults.minHeight,
    itemContentPadding: PaddingValues = StyledListItemDefaults.contentPadding,
    itemContainerColor: Color = Color.Unspecified,
    itemContentColor: Color = Color.Unspecified,
    itemSupportingColor: Color = Color.Unspecified,
    itemBorderColor: Color = Color.Unspecified,
    style: StyledDialog.Style = StyledDialogDefaults.style,
    properties: StyledDialog.Properties = StyledDialog.Properties(),
    title: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    buttons: StyledDialog.Buttons = StyledDialog.Buttons(),
    onDismissRequest: () -> Unit = state::dismiss,
) {
    var menuPath by remember(items) {
        mutableStateOf<List<StyledDialogMenu.Entry.Submenu>>(emptyList())
    }
    val visibleItems = menuPath.lastOrNull()?.items ?: items

    composeDialog(
        state = state,
        modifier = modifier,
        style = style,
        properties = properties,
        title = title,
        icon = icon,
        buttons = buttons,
        onDismissRequest = onDismissRequest,
    ) {
        StyledVerticalScrollContainer(
            modifier = listModifier.fillMaxWidth(),
            contentPadding = listContentPadding,
        ) {
            StyledColumn(
                itemSpacing = itemSpacing,
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (menuPath.isNotEmpty()) {
                    menuListItem(
                        title = "Back to ${menuPath.last().title}",
                        onClick = { menuPath = menuPath.dropLast(1) },
                        leading = {
                            StyledIcon(
                                icon = StyledIcons.NavigateBack,
                                contentDescription = "Back",
                            )
                        },
                        itemContentSpacing = itemContentSpacing,
                        itemShape = itemShape,
                        itemMinHeight = itemMinHeight,
                        itemContentPadding = itemContentPadding,
                        itemContainerColor = itemContainerColor,
                        itemContentColor = itemContentColor,
                        itemSupportingColor = itemSupportingColor,
                        itemBorderColor = itemBorderColor,
                    )
                }
                visibleItems.forEach { entry ->
                    when (entry) {
                        is StyledDialogMenu.Entry.Action -> {
                            menuListItem(
                                title = entry.title,
                                description = entry.description,
                                onClick = {
                                    entry.onClick()
                                    if (entry.dismissOnClick && dismissOnClick) {
                                        onDismissRequest()
                                    }
                                },
                                enabled = entry.enabled,
                                leading = entry.leading,
                                itemContentSpacing = itemContentSpacing,
                                itemShape = itemShape,
                                itemMinHeight = itemMinHeight,
                                itemContentPadding = itemContentPadding,
                                itemContainerColor = itemContainerColor,
                                itemContentColor = itemContentColor,
                                itemSupportingColor = itemSupportingColor,
                                itemBorderColor = itemBorderColor,
                            )
                        }

                        is StyledDialogMenu.Entry.Submenu -> {
                            menuListItem(
                                title = entry.title,
                                description = entry.description,
                                onClick = { menuPath = menuPath + entry },
                                leading = entry.leading,
                                actions = {
                                    StyledIcon(
                                        icon = StyledIcons.Submenu,
                                        contentDescription = "Open submenu",
                                    )
                                },
                                itemContentSpacing = itemContentSpacing,
                                itemShape = itemShape,
                                itemMinHeight = itemMinHeight,
                                itemContentPadding = itemContentPadding,
                                itemContainerColor = itemContainerColor,
                                itemContentColor = itemContentColor,
                                itemSupportingColor = itemSupportingColor,
                                itemBorderColor = itemBorderColor,
                            )
                        }

                        is StyledDialogMenu.Entry.Header -> {
                            StyledText(
                                text = entry.title,
                                modifier = Modifier.padding(
                                    horizontal = StyledTheme.padding.medium,
                                    vertical = StyledTheme.padding.small,
                                ),
                                style = StyledTheme.typography.titleSmall,
                            )
                        }

                        StyledDialogMenu.Entry.Divider -> {
                            StyledHorizontalDivider()
                        }

                        is StyledDialogMenu.Entry.Custom -> {
                            entry.content()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun menuListItem(
    title: String,
    onClick: () -> Unit,
    description: String? = null,
    enabled: Boolean = true,
    leading: (@Composable () -> Unit)? = null,
    actions: (@Composable RowScope.() -> Unit)? = null,
    itemContentSpacing: Dp,
    itemShape: Shape,
    itemMinHeight: Dp,
    itemContentPadding: PaddingValues,
    itemContainerColor: Color,
    itemContentColor: Color,
    itemSupportingColor: Color,
    itemBorderColor: Color,
) {
    StyledListItem(
        headline = { StyledText(title) },
        supporting = description?.let { { StyledText(it) } },
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.fillMaxWidth(),
        shape = itemShape,
        minHeight = itemMinHeight,
        contentPadding = itemContentPadding,
        spacing = itemContentSpacing,
        containerColor = itemContainerColor,
        contentColor = itemContentColor,
        supportingColor = itemSupportingColor,
        borderColor = itemBorderColor,
        leading = leading,
        actions = actions,
    )
}
