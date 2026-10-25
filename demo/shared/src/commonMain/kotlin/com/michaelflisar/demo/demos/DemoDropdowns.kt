package com.michaelflisar.demo.demos

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.classes.CustomColors
import com.michaelflisar.composestyledcomponents.classes.toIconComposable
import com.michaelflisar.composestyledcomponents.ui.components.StyledButton
import com.michaelflisar.composestyledcomponents.ui.components.StyledButtonDefaults
import com.michaelflisar.composestyledcomponents.ui.components.StyledDropdown
import com.michaelflisar.composestyledcomponents.ui.components.StyledDropdownDefaults
import com.michaelflisar.composestyledcomponents.ui.components.StyledPopupMenu
import com.michaelflisar.composestyledcomponents.ui.components.StyledDropdownMenuItem
import com.michaelflisar.composestyledcomponents.ui.components.StyledDropdownMenuItemDefaults
import com.michaelflisar.composestyledcomponents.ui.components.StyledDropdownMultiSelect
import com.michaelflisar.composestyledcomponents.ui.components.StyledDropdownMultiSelectDefaults
import com.michaelflisar.composestyledcomponents.ui.components.StyledDropdownPrimaryButton
import com.michaelflisar.composestyledcomponents.ui.components.StyledDropdownSecondaryButton
import com.michaelflisar.composestyledcomponents.ui.components.StyledDropdownTertiaryButton
import com.michaelflisar.composestyledcomponents.ui.components.StyledIcon
import com.michaelflisar.composestyledcomponents.ui.components.StyledSecondaryButton
import com.michaelflisar.composestyledcomponents.ui.components.StyledSecondaryDropdownIndex
import com.michaelflisar.composestyledcomponents.ui.components.StyledTertiaryDropdownMultiSelectIndex
import com.michaelflisar.composestyledcomponents.ui.overlay.StyledPopupContainer
import com.michaelflisar.composestyledcomponents.ui.primitives.StyledHorizontalDivider
import com.michaelflisar.composestyledcomponents.ui.primitives.StyledText
import com.michaelflisar.demo.helper.ExpandState
import com.michaelflisar.demo.helper.RegionContainer
import com.michaelflisar.demo.helper.SubTitle

@Composable
internal fun DemoDropdowns(baseId: Int, expandState: ExpandState) {
    val items = remember { listOf("Apple", "Banana", "Cherry", "Date", "Elderberry") }
    val filter = remember {
        StyledDropdown.Filter<String>("Filter") { query, item ->
            item.contains(query, ignoreCase = true)
        }
    }
    val selected = remember { mutableStateOf(items.first()) }
    val index = remember { mutableStateOf(-1) }
    val selectedItems = remember { mutableStateOf(listOf(items.first())) }
    val selectedIndices = remember { mutableStateOf(listOf(2, 0)) }
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
        StyledSecondaryDropdownIndex(
            items = items,
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
        StyledSecondaryButton(
            onClick = {
                selected.value = items.random()
                index.value = (0 until items.size).random()
                selectedItems.value = items.shuffled().take((1..items.size).random())
                selectedIndices.value =
                    (0 until items.size).shuffled().take((1..items.size).random())
            },
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
        StyledTertiaryDropdownMultiSelectIndex(
            items = items,
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
        val action = remember { mutableStateOf("No action") }
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
            StyledHorizontalDivider()
            StyledDropdownMenuItem(
                text = { StyledText("Unavailable") },
                enabled = false,
                onClick = {},
            )
        }
        StyledDropdownPrimaryButton(menuContent = buttonMenu) {
            StyledText("Actions")
        }
        StyledDropdownSecondaryButton(menuContent = buttonMenu) {
            StyledText("Secondary actions")
        }
        StyledDropdownTertiaryButton(menuContent = buttonMenu) {
            StyledText("Tertiary actions")
        }
        StyledDropdownPrimaryButton(onClick = { action.value = "Direct action" }) {
            StyledText("Direct action")
        }
        StyledDropdownPrimaryButton(menuContent = buttonMenu, enabled = false) {
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
