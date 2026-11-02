package com.michaelflisar.composestyled.ui.internal

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.toggleableState
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.UpdateStyledThemeLocals
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.tokens.LocalStyledContentColor
import com.michaelflisar.composestyled.ui.components.StyledButton
import com.michaelflisar.composestyled.ui.components.StyledDropdown
import com.michaelflisar.composestyled.ui.components.StyledDropdownMenuItem
import com.michaelflisar.composestyled.ui.components.StyledDropdownMenuItemDefaults
import com.michaelflisar.composestyled.ui.components.StyledIcon
import com.michaelflisar.composestyled.ui.components.StyledInput
import com.michaelflisar.composestyled.ui.components.StyledPopupMenu
import com.michaelflisar.composestyled.ui.icons.StyledIcons
import com.michaelflisar.composestyled.ui.layout.StyledColumn
import com.michaelflisar.composestyled.ui.overlay.StyledPopupContainer
import com.michaelflisar.composestyled.ui.primitives.StyledText
import kotlinx.coroutines.launch

@Composable
internal fun <T : Any, S> StyledDropdownBase(
    modifier: Modifier = Modifier,
    title: String,
    items: List<T>,
    selection: S,
    isSelected: (index: Int) -> Boolean,
    closeOnItemClick: Boolean,
    enabled: Boolean,
    customization: StyledButton.Customization?,
    filter: StyledDropdown.Filter<T>?,
    role: StyledButton.Role,
    shape: Shape,
    popupWidthConfig: StyledPopupMenu.WidthConfig,
    popupMaxHeight: Dp,
    popupHeader: (@Composable () -> Unit)? = null,
    content: @Composable (context: StyledDropdown.RenderContext<T, S>) -> Unit,
    onItemClick: (index: Int, item: T) -> Unit,
) {
    require(popupMaxHeight.value.isFinite() && popupMaxHeight > 0.dp) {
        "Popup maximum height must be finite and positive"
    }
    var filterText by remember(filter != null) { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    LaunchedEffect(enabled) {
        if (!enabled) expanded = false
    }
    val colors = StyledTheme.colorScheme

    StyledPopupContainer(
        modifier = modifier.width(IntrinsicSize.Max),
        popup = {
            StyledPopupMenu(
                expanded = expanded && enabled,
                onDismissRequest = { expanded = false },
                widthConfig = popupWidthConfig,
                offset = DpOffset(0.dp, 4.dp),
                shape = shape,
                scrollState = scrollState,
                border = BorderStroke(1.dp, colors.outlineVariant),
                maxHeight = popupMaxHeight,
                contentPadding = PaddingValues(0.dp),
                popupHeader = if (filter == null && popupHeader == null) null else {
                    {
                        if (filter != null) {
                            StyledColumn(
                                itemSpacing = StyledTheme.spacing.medium,
                                modifier = Modifier.fillMaxWidth().padding(StyledTheme.padding.medium)
                            ) {
                                if (title.isNotEmpty()) {
                                    StyledText(title, style = StyledTheme.typography.titleSmall)
                                }
                                StyledInput(
                                    value = filterText,
                                    onValueChange = {
                                        filterText = it
                                        scope.launch { scrollState.scrollTo(0) }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    label = { StyledText(filter.label) },
                                )
                            }
                        }
                        popupHeader?.invoke()
                    }
                },
            ) {
                DropdownItems(
                    items = items,
                    filter = filter,
                    filterText = filterText,
                    isSelected = isSelected,
                    multiSelect = !closeOnItemClick,
                    content = content,
                    onItemClick = { index, item ->
                        onItemClick(index, item)
                        if (closeOnItemClick) expanded = false
                    },
                )
            }
        },
        content = {
            DropdownAnchor(
                title = title,
                enabled = enabled,
                expanded = expanded,
                shape = shape,
                role = role,
                customization = customization,
                onToggle = {
                    if (expanded) {
                        expanded = false
                    } else {
                        filterText = ""
                        scope.launch { scrollState.scrollTo(0) }
                        expanded = true
                    }
                },
            ) {
                content(StyledDropdown.RenderContext.Selection(selection))
            }
        }
    )
}

@OptIn(InternalComposeStyledApi::class)
@Composable
private fun DropdownAnchor(
    title: String,
    enabled: Boolean,
    expanded: Boolean,
    shape: Shape,
    role: StyledButton.Role,
    customization: StyledButton.Customization?,
    onToggle: () -> Unit,
    content: @Composable () -> Unit,
) {
    val rotation by animateFloatAsState(if (expanded) -180f else 0f)
    val anchorContent: @Composable RowScope.() -> Unit = {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(StyledTheme.spacing.small),
        ) {
            if (title.isNotEmpty()) {
                StyledText(title, style = StyledTheme.typography.labelSmall, maxLines = 1)
            }
            UpdateStyledThemeLocals(
                textStyle = StyledTheme.typography.bodyMedium,
                content = content,
            )
        }
        StyledIcon(
            icon = StyledIcons.Dropdown,
            modifier = Modifier.rotate(rotation),
            tint = LocalStyledContentColor.current,
        )
    }
    val anchorModifier = Modifier.fillMaxWidth()
    StyledButton(
        onClick = onToggle,
        role = role,
        modifier = anchorModifier,
        enabled = enabled,
        shape = shape,
        customization = customization,
        content = anchorContent,
    )
}

@Composable
private fun <T : Any, S> DropdownItems(
    filter: StyledDropdown.Filter<T>?,
    filterText: String,
    items: List<T>,
    isSelected: (Int) -> Boolean,
    multiSelect: Boolean,
    content: @Composable (StyledDropdown.RenderContext<T, S>) -> Unit,
    onItemClick: (Int, T) -> Unit,
) {
    items.forEachIndexed { index, item ->
        if (filter == null || filter.filter(filterText, item)) {
            key(index) {
                val itemSelected = isSelected(index)
                StyledDropdownMenuItem(
                    text = {
                        content(StyledDropdown.RenderContext.DropdownItem(item, itemSelected))
                    },
                    onClick = { onItemClick(index, item) },
                    modifier = Modifier.fillMaxWidth().semantics {
                        if (multiSelect) {
                            role = Role.Checkbox
                            toggleableState = ToggleableState(itemSelected)
                        } else {
                            role = Role.RadioButton
                            selected = itemSelected
                        }
                    },
                    colors = StyledDropdownMenuItemDefaults.colors(
                        textColor = if (itemSelected) {
                            StyledTheme.colorScheme.primary
                        } else LocalStyledContentColor.current,
                    ),
                )
            }
        }
    }
}
