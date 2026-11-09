package com.michaelflisar.demo.demos.showcase.areas

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
import com.michaelflisar.composestyled.ui.components.StyledPopupDropdown
import com.michaelflisar.composestyled.ui.components.StyledPopupDropdownDefaults
import com.michaelflisar.composestyled.ui.components.StyledPopupMenu
import com.michaelflisar.composestyled.ui.components.StyledPopupDropdownMultiSelect
import com.michaelflisar.composestyled.ui.components.StyledPopupDropdownMultiSelectDefaults
import com.michaelflisar.composestyled.ui.components.StyledIcon
import com.michaelflisar.composestyled.ui.components.StyledPopupDropdownIndex
import com.michaelflisar.composestyled.ui.components.StyledPopupDropdownMultiSelectIndex
import com.michaelflisar.composestyled.ui.components.StyledPopupMenuScope
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
        StyledPopupDropdown.Filter<String>("Filter") { query, item ->
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
        StyledPopupDropdown(
            items = items,
            selected = selected,
            modifier = Modifier.fillMaxWidth(),
            title = "Fruit",
            filter = filter,
            popupWidthConfig = StyledPopupMenu.WidthConfig.Auto(),
        )
        StyledPopupDropdown(
            items = items,
            selected = selected,
            modifier = Modifier.fillMaxWidth(),
            title = "End-aligned popup",
            popupWidthConfig = StyledPopupMenu.WidthConfig.Fixed(280.dp),
            popupHorizontalAlignment = StyledPopupMenu.HorizontalAlignment.End,
        )
        StyledPopupDropdownIndex(
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
            variant = StyledButton.Variant.Outlined,
            content = StyledPopupDropdownDefaults.defaultRenderer(
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
            variant = StyledButton.Variant.Outlined,
            customization = StyledButtonDefaults.customize(
                colors = CustomColors.Primary,
                border = StyledTheme.colorScheme.primary
            ),
        ) {
            StyledText("Randomize selection")
        }
        SubTitle("Multiple selection")
        StyledPopupDropdownMultiSelect(
            items = items,
            selected = selectedItems,
            modifier = Modifier.fillMaxWidth(),
            title = "Several fruits",
            filter = filter,
            variant = StyledButton.Variant.Outlined,
            actions = StyledPopupDropdownMultiSelectDefaults.defaultActions("All fruits", "Clear"),
        )
        StyledPopupDropdownMultiSelectIndex(
            items = items,
            selectedIndices = selectedIndices,
            modifier = Modifier.fillMaxWidth(),
            title = "Preserve selection order",
            sortSelectionByItems = false,
            variant = StyledButton.Variant.Outlined,
            popupWidthConfig = StyledPopupMenu.WidthConfig.AtLeastAnchor,
            content = StyledPopupDropdownMultiSelectDefaults.defaultRenderer(
                formatter = { it.joinToString(" / ") },
            ),
        )
        StyledPopupDropdown(
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
        val buttonMenu: @Composable StyledPopupMenuScope.() -> Unit = {
            item(
                text = "Confirm",
                leadingIcon = {
                    StyledIcon(
                        icon = Icons.Default.Check.toIconComposable(),
                        contentDescription = null
                    )
                },
                onClick = {
                    action.value = "Confirmed"
                },
            )
            item(
                text = "Keep menu open",
                onClick = { action.value = "Menu stays open" },
                closeOnClick = false,
            )
            divider()
            item(
                text = "Unavailable",
                enabled = false,
                onClick = {},
            )
        }
        StyledPopupMenu(triggerContent = { StyledText("Actions") }, variant = StyledButton.Variant.Filled, menu = buttonMenu)
        StyledPopupMenu(triggerContent = { StyledText("Secondary actions") }, variant = StyledButton.Variant.Outlined, menu = buttonMenu)
        StyledPopupMenu(triggerContent = { StyledText("Tertiary actions") }, variant = StyledButton.Variant.Plain, menu = buttonMenu)
        StyledButton(onClick = { action.value = "Direct action" }, variant = StyledButton.Variant.Filled) {
            StyledText("Direct action")
        }
        StyledPopupMenu(
            menu = buttonMenu,
            enabled = false,
            variant = StyledButton.Variant.Filled,
            triggerContent = {
                StyledText("Disabled actions")
            },
        )
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
                    items.forEach { value ->
                    item(
                        text = value,
                            leadingIcon = {
                                if (selected.value == value) {
                                    StyledIcon(
                                        icon = Icons.Default.Check.toIconComposable(),
                                        contentDescription = "Selected"
                                    )
                                }
                            },
                            onClick = {
                                selected.value = value
                            },
                        )
                    }
                    item(
                        text = "Unavailable action",
                        onClick = {},
                        enabled = false,
                    )
                    divider()
                    custom(
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
