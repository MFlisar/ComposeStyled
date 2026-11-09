package com.michaelflisar.composedialogs.styled

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composedialogs.styled.shared.composeDialog
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.ui.components.StyledListItem
import com.michaelflisar.composestyled.ui.components.StyledListItemDefaults
import com.michaelflisar.composestyled.ui.layout.StyledColumn
import com.michaelflisar.composestyled.ui.layout.StyledDialog
import com.michaelflisar.composestyled.ui.layout.StyledDialogDefaults
import com.michaelflisar.composestyled.ui.layout.StyledVerticalScrollContainer
import com.michaelflisar.composestyled.ui.primitives.StyledHorizontalDivider
import com.michaelflisar.composestyled.ui.primitives.StyledText
import com.michaelflisar.composestyled.ui.state.DialogState

/** Shows selectable Styled list items in a dialog. */
@Composable
fun <T : Any> StyledDialogList(
    state: DialogState,
    items: List<T>,
    onItemClick: (item: T) -> Unit,
    modifier: Modifier = Modifier,
    listModifier: Modifier = Modifier,
    key: ((item: T) -> Any)? = null,
    itemContent: @Composable (item: T) -> Unit = { StyledText(it.toString()) },
    supportingContent: (@Composable (item: T) -> Unit)? = null,
    overlineContent: (@Composable (item: T) -> Unit)? = null,
    leadingContent: (@Composable (item: T) -> Unit)? = null,
    actionsContent: (@Composable RowScope.(item: T) -> Unit)? = null,
    itemEnabled: (item: T) -> Boolean = { true },
    dismissOnItemClick: Boolean = true,
    emptyContent: (@Composable () -> Unit)? = null,
    showDividers: Boolean = false,
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
    val indexedKey: ((Int, T) -> Any)? = key?.let { itemKey ->
        { _, item -> itemKey(item) }
    }

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
                if (items.isEmpty()) {
                    emptyContent?.invoke()
                } else {
                    items.forEachIndexed { index, item ->
                        key(indexedKey?.invoke(index, item) ?: index) {
                            StyledListItem(
                                headline = { itemContent(item) },
                                onClick = {
                                    onItemClick(item)
                                    if (dismissOnItemClick) onDismissRequest()
                                },
                                modifier = Modifier.fillMaxWidth(),
                                enabled = itemEnabled(item),
                                shape = itemShape,
                                minHeight = itemMinHeight,
                                contentPadding = itemContentPadding,
                                spacing = itemContentSpacing,
                                containerColor = itemContainerColor,
                                contentColor = itemContentColor,
                                supportingColor = itemSupportingColor,
                                borderColor = itemBorderColor,
                                supporting = supportingContent?.let { content -> { content(item) } },
                                overline = overlineContent?.let { content -> { content(item) } },
                                leading = leadingContent?.let { content -> { content(item) } },
                                actions = actionsContent?.let { content -> { content(item) } },
                            )
                        }
                        if (showDividers && index < items.lastIndex) {
                            StyledHorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}
