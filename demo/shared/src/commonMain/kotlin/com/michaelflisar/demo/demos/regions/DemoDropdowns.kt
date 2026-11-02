package com.michaelflisar.demo.demos.regions

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.classes.CustomColors
import com.michaelflisar.composestyled.classes.toIconComposable
import com.michaelflisar.composestyled.ui.components.StyledButton
import com.michaelflisar.composestyled.ui.components.StyledButtonDefaults
import com.michaelflisar.composestyled.ui.components.StyledDropdown
import com.michaelflisar.composestyled.ui.components.StyledDropdownDefaults
import com.michaelflisar.composestyled.ui.components.StyledPopupMenu
import com.michaelflisar.composestyled.ui.components.StyledDropdownMenuItem
import com.michaelflisar.composestyled.ui.components.StyledDropdownMenuItemDefaults
import com.michaelflisar.composestyled.ui.components.StyledDropdownMenuContent
import com.michaelflisar.composestyled.ui.components.StyledDropdownMenuDivider
import com.michaelflisar.composestyled.ui.components.StyledDropdownMultiSelect
import com.michaelflisar.composestyled.ui.components.StyledDropdownMultiSelectDefaults
import com.michaelflisar.composestyled.ui.components.StyledDropdownButton
import com.michaelflisar.composestyled.ui.components.StyledIcon
import com.michaelflisar.composestyled.ui.components.StyledDropdownIndex
import com.michaelflisar.composestyled.ui.components.StyledDropdownMultiSelectIndex
import com.michaelflisar.composestyled.ui.overlay.StyledPopupContainer
import com.michaelflisar.composestyled.ui.primitives.StyledText
import com.michaelflisar.composestyled.utils.ExpandState
import com.michaelflisar.demo.helper.RegionContainer
import com.michaelflisar.demo.helper.SubTitle

@Composable
internal fun DemoDropdowns(baseId: Int, expandState: ExpandState) {
    val items = remember {
        listOf(
            "Apple", "Banana", "Cherry", "Date", "Elderberry", "Fig",
            "Grape", "Honeydew", "Indian fig", "Jackfruit", "Kiwi", "Lemon",
        )
    }
    val filter = remember {
        StyledDropdown.Filter<String>("Filter") { query, item ->
            item.contains(query, ignoreCase = true)
        }
    }
    val selected = rememberSaveable { mutableStateOf(items.first()) }
    val index = rememberSaveable { mutableStateOf(-1) }
    val selectedItems = rememberSaveable { mutableStateOf(listOf(items.first())) }
    val selectedIndices = rememberSaveable { mutableStateOf(listOf(2, 0)) }
    RegionContainer(
        title = "Dropdowns",
        id = baseId + 1,
        expanded = expandState
    ) {
        SubTitle("Single selection")
        StyledDropdown(
            items = items,
            selected = selected,
            modifier = Modifier.fillMaxWidth(),
            title = "Fruit",
            filter = filter,
        )
        StyledDropdownIndex(
            items = items,
            role = StyledButton.Role.Secondary,
            selectedIndex = index,
            modifier = Modifier.fillMaxWidth(),
            title = "Secondary, initially unselected",
            customization = StyledButtonDefaults.customize(
                colors = CustomColors.Primary,
                border = StyledTheme.colorScheme.primary
            ),
            shape = StyledTheme.shapes.large,
            popupWidthConfig = StyledPopupMenu.WidthConfig.Fixed(300.dp),
            content = StyledDropdownDefaults.defaultRenderer(
                mapper = { it.uppercase() },
                mapperDropdown = { "Choose $it" },
            ),
        )
        StyledButton(
            onClick = {
                selected.value = items.random()
                index.value = (0 until items.size).random()
                selectedItems.value = items.shuffled().take((1..items.size).random())
                selectedIndices.value =
                    (0 until items.size).shuffled().take((1..items.size).random())
            },
            role = StyledButton.Role.Secondary,
            customization = StyledButtonDefaults.customize(
                colors = CustomColors.Primary,
                border = StyledTheme.colorScheme.primary
            ),
        ) {
            StyledText("Randomize selection")
        }
        SubTitle("Multiple selection")
        StyledDropdownMultiSelect(
            items = items,
            selected = selectedItems,
            modifier = Modifier.fillMaxWidth(),
            title = "Several fruits",
            filter = filter,
            actions = StyledDropdownMultiSelectDefaults.defaultActions("All fruits", "Clear"),
        )
        StyledDropdownMultiSelectIndex(
            items = items,
            role = StyledButton.Role.Tertiary,
            selectedIndices = selectedIndices,
            modifier = Modifier.fillMaxWidth(),
            title = "Preserve selection order",
            sortSelectionByItems = false,
            popupWidthConfig = StyledPopupMenu.WidthConfig.AtLeastAnchor,
            content = StyledDropdownMultiSelectDefaults.defaultRenderer(
                formatter = { it.joinToString(" / ") },
            ),
        )
        StyledDropdown(
            items = items,
            selected = items.first(),
            modifier = Modifier.fillMaxWidth(),
            title = "Disabled",
            enabled = false,
            onSelectionChange = { _, _ -> },
        )
        StyledText("Selected index: ${index.value}; selection order: ${selectedIndices.value}")

        SubTitle("Dropdown buttons")
        val action = rememberSaveable { mutableStateOf("No action") }
        val buttonMenu: @Composable ColumnScope.(() -> Unit) -> Unit = { dismiss ->
            StyledDropdownMenuItem(
                text = { StyledText("Confirm") },
                leadingIcon = {
                    StyledIcon(
                        icon = Icons.Default.Check.toIconComposable(),
                        contentDescription = null
                    )
                },
                onClick = {
                    action.value = "Confirmed"
                    dismiss()
                },
            )
            StyledDropdownMenuItem(
                text = { StyledText("Keep menu open") },
                colors = StyledDropdownMenuItemDefaults.colors(
                    textColor = StyledTheme.colorScheme.primary
                ),
                onClick = { action.value = "Menu stays open" },
            )
            StyledDropdownMenuDivider()
            StyledDropdownMenuItem(
                text = { StyledText("Unavailable") },
                enabled = false,
                onClick = {},
            )
        }
        StyledDropdownButton(menuContent = buttonMenu, role = StyledButton.Role.Primary) {
            StyledText("Actions")
        }
        StyledDropdownButton(menuContent = buttonMenu, role = StyledButton.Role.Secondary) {
            StyledText("Secondary actions")
        }
        StyledDropdownButton(menuContent = buttonMenu, role = StyledButton.Role.Tertiary) {
            StyledText("Tertiary actions")
        }
        StyledDropdownButton(
            onClick = { action.value = "Direct action" },
            role = StyledButton.Role.Primary,
        ) {
            StyledText("Direct action")
        }
        StyledDropdownButton(
            menuContent = buttonMenu,
            enabled = false,
            role = StyledButton.Role.Primary,
        ) {
            StyledText("Disabled actions")
        }
        StyledText(action.value)

        SubTitle("Standalone menu")
        val menuExpanded = remember { mutableStateOf(false) }
        StyledPopupContainer(
            popup = {
                StyledPopupMenu(
                    expanded = menuExpanded.value,
                    onDismissRequest = { menuExpanded.value = false },
                    widthConfig = StyledPopupMenu.WidthConfig.Auto(limitToContainerWidth = true),
                ) {
                    items.forEach { item ->
                        StyledDropdownMenuItem(
                            text = { StyledText(item) },
                            leadingIcon = {
                                if (selected.value == item) {
                                    StyledIcon(
                                        icon = Icons.Default.Check.toIconComposable(),
                                        contentDescription = "Selected"
                                    )
                                }
                            },
                            onClick = {
                                selected.value = item
                                menuExpanded.value = false
                            },
                        )
                    }
                    StyledDropdownMenuItem(
                        text = { StyledText("Unavailable action") },
                        onClick = {},
                        enabled = false,
                    )
                    StyledDropdownMenuDivider()
                    StyledDropdownMenuContent(
                        onClick = { action.value = "Custom content action" },
                        content = {
                            StyledText("Custom action")
                            StyledText("Styled like a menu item")
                        },
                    )
                }
            },
            content = {
                StyledButton(
                    onClick = { menuExpanded.value = true }) {
                    StyledText("Open menu")
                }
            }
        )
    }
}
