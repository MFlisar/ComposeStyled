package com.michaelflisar.composestyledcomponents.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.tokens.LocalStyledContentColor
import com.michaelflisar.composestyledcomponents.ui.internal.StyledDropdownBase
import com.michaelflisar.composestyledcomponents.ui.layout.StyledRow
import com.michaelflisar.composestyledcomponents.ui.layout.StyledSpacedRow
import com.michaelflisar.composestyledcomponents.ui.primitives.StyledText

// -------------------------
// context
// -------------------------

object StyledDropdownMultiSelect {
    class Actions internal constructor(
        val canSelectAll: Boolean,
        val canClear: Boolean,
        val selectAll: () -> Unit,
        val clearSelection: () -> Unit,
    )
}

// -------------------------
// defaults
// -------------------------

object StyledDropdownMultiSelectDefaults {

    @Composable
    fun defaultActions(
        selectAllLabel: String = "Select all",
        clearSelectionLabel: String = "Clear selection",
    ): @Composable (StyledDropdownMultiSelect.Actions) -> Unit = { actions ->
        StyledRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            StyledTertiaryButton(onClick = actions.selectAll, enabled = actions.canSelectAll) {
                StyledText(selectAllLabel)
            }
            StyledTertiaryButton(onClick = actions.clearSelection, enabled = actions.canClear) {
                StyledText(clearSelectionLabel)
            }
        }
    }

    @Composable
    fun <T : Any> defaultRenderer(
        mapper: @Composable (item: T) -> String = { it.toString() },
        formatter: @Composable (selected: List<String>) -> String = StyledDropdownMultiSelectDefaults.formatter,
        color: Color = Color.Unspecified,
        fontWeight: FontWeight? = null,
        maxLines: Int = 1,
        leadingContent: @Composable () -> Unit = {},
        dropdownMapper: @Composable (item: T) -> String = mapper,
        colorDropdown: Color = Color.Unspecified,
        colorDropdownSelected: Color = StyledTheme.colorScheme.primary,
        fontWeightDropdown: FontWeight? = null,
        fontWeightDropdownSelected: FontWeight? = FontWeight.Bold,
        maxLinesDropdown: Int = 1,
    ) = StyledDropdown.renderContent(
        content = defaultContent(
            mapper = mapper,
            formatter = formatter,
            color = color,
            fontWeight = fontWeight,
            maxLines = maxLines,
            leadingContent = leadingContent
        ),
        dropdownContent = defaultDropdownContent(
            mapper = dropdownMapper,
            color = colorDropdown,
            colorSelected = colorDropdownSelected,
            fontWeight = fontWeightDropdown,
            fontWeightSelected = fontWeightDropdownSelected,
            maxLines = maxLinesDropdown,
            leadingContent = leadingContent
        )
    )

    @Composable
    fun <T : Any> defaultRenderer(
        content: @Composable (context: StyledDropdown.RenderContext.Selection<List<T>>) -> Unit = defaultContent(),
        dropdownContent: @Composable (context: StyledDropdown.RenderContext.DropdownItem<T>) -> Unit = defaultDropdownContent(),
    ) = StyledDropdown.renderContent(
        content = content,
        dropdownContent = dropdownContent
    )

    @Composable
    fun <T> defaultContent(
        mapper: @Composable (item: T) -> String = { it.toString() },
        formatter: @Composable (selected: List<String>) -> String = StyledDropdownMultiSelectDefaults.formatter,
        color: Color = Color.Unspecified,
        fontWeight: FontWeight? = null,
        maxLines: Int = 1,
        leadingContent: @Composable () -> Unit = {},
    ): @Composable (StyledDropdown.RenderContext.Selection<List<T>>) -> Unit = { context ->
        StyledSpacedRow(itemSpacing = StyledTheme.spacing.medium) {
            leadingContent()
            StyledText(
                text = formatter(context.selection.map { mapper(it) }),
                modifier = Modifier.weight(1f),
                style = StyledTheme.typography.bodyMedium,
                color = color,
                fontWeight = fontWeight,
                maxLines = maxLines
            )
        }
    }

    @Composable
    fun <T : Any> defaultDropdownContent(
        mapper: @Composable (item: T) -> String = { it.toString() },
        color: Color = Color.Unspecified,
        colorSelected: Color = StyledTheme.colorScheme.primary,
        fontWeight: FontWeight? = null,
        fontWeightSelected: FontWeight? = FontWeight.Bold,
        maxLines: Int = 1,
        leadingContent: @Composable () -> Unit = {},
    ): @Composable (StyledDropdown.RenderContext.DropdownItem<T>) -> Unit = { context ->
        StyledSpacedRow(
            modifier = Modifier.fillMaxWidth(),
            itemSpacing = StyledTheme.spacing.medium
        ) {
            StyledCheckbox(
                checked = context.selected,
                onCheckedChange = null,
                tint = (if (context.selected) colorSelected else color)
                    .takeIf { it != Color.Unspecified } ?: LocalStyledContentColor.current,
            )
            leadingContent()
            StyledText(
                text = mapper(context.item),
                color = if (context.selected) colorSelected else color,
                modifier = Modifier.weight(1f),
                fontWeight = if (context.selected) fontWeightSelected else fontWeight,
                maxLines = maxLines
            )
        }
    }

    @Composable
    fun shape() = StyledTheme.shapes.small

    val formatter: @Composable (selected: List<String>) -> String = { selected ->
        if (selected.isEmpty()) {
            ""
        } else {
            selected.joinToString(";")
        }
    }

    const val sortSelectionByItems = true
}

// -------------------------
// public API
// -------------------------

/**
 * Updates [selected] before notifying [onSelectionChange] with original list indices and items.
 */
@Composable
fun <T : Any> StyledDropdownMultiSelect(
    items: List<T>,
    selected: MutableState<List<T>>,
    modifier: Modifier = Modifier,
    title: String = "",
    enabled: Boolean = true,
    // defaults / customisation
    customization: StyledButton.Customization? = null,
    filter: StyledDropdown.Filter<T>? = null,
    shape: Shape = StyledDropdownMultiSelectDefaults.shape(),
    popupWidthConfig: StyledPopupMenu.WidthConfig = StyledPopupMenu.WidthConfig.MatchAnchor,
    sortSelectionByItems: Boolean = StyledDropdownMultiSelectDefaults.sortSelectionByItems,
    // content
    content: @Composable (context: StyledDropdown.RenderContext<T, List<T>>) -> Unit = StyledDropdownMultiSelectDefaults.defaultRenderer(),
    popupMaxHeight: Dp = StyledPopupMenuDefaults.maxHeight,
    actions: @Composable (StyledDropdownMultiSelect.Actions) -> Unit = StyledDropdownMultiSelectDefaults.defaultActions(),
    onSelectionChange: ((indices: List<Int>, items: List<T>) -> Unit)? = null,
) {
    StyledDropdownMultiSelect(
        modifier = modifier,
        items = items,
        selected = selected.value,
        title = title,
        enabled = enabled,
        filter = filter,
        customization = customization,
        shape = shape,
        popupWidthConfig = popupWidthConfig,
        sortSelectionByItems = sortSelectionByItems,
        content = content,
        popupMaxHeight = popupMaxHeight,
        actions = actions,
    ) { selectedIndices, selectedItems ->
        selected.value = selectedItems
        onSelectionChange?.invoke(selectedIndices, selectedItems)
    }
}

/**
 * Renders selected items through [StyledDropdown.RenderContext.Selection] and menu entries through
 * [StyledDropdown.RenderContext.DropdownItem]. Item clicks toggle selection without closing the menu.
 */
@Composable
fun <T : Any> StyledDropdownMultiSelect(
    items: List<T>,
    selected: List<T>,
    modifier: Modifier = Modifier,
    title: String = "",
    enabled: Boolean = true,
    // defaults / customisation
    customization: StyledButton.Customization? = null,
    filter: StyledDropdown.Filter<T>? = null,
    shape: Shape = StyledDropdownMultiSelectDefaults.shape(),
    popupWidthConfig: StyledPopupMenu.WidthConfig = StyledPopupMenu.WidthConfig.MatchAnchor,
    sortSelectionByItems: Boolean = StyledDropdownMultiSelectDefaults.sortSelectionByItems,
    // content
    content: @Composable (context: StyledDropdown.RenderContext<T, List<T>>) -> Unit = StyledDropdownMultiSelectDefaults.defaultRenderer(),
    popupMaxHeight: Dp = StyledPopupMenuDefaults.maxHeight,
    actions: @Composable (StyledDropdownMultiSelect.Actions) -> Unit = StyledDropdownMultiSelectDefaults.defaultActions(),
    onSelectionChange: (indices: List<Int>, items: List<T>) -> Unit,
) {
    val selectedIndices = selected.map { items.indexOf(it) }
    StyledDropdownMultiSelectImpl(
        modifier = modifier,
        title = title,
        items = items,
        selected = selectedIndices,
        enabled = enabled,
        customization = customization,
        filter = filter,
        shape = shape,
        popupWidthConfig = popupWidthConfig,
        popupMaxHeight = popupMaxHeight,
        content = content,
        sortSelectionByItems = sortSelectionByItems,
        actions = actions,
        role = StyledButton.Role.Primary,
        onSelectionChange = onSelectionChange
    )
}

/**
 * Updates [selectedIndices] before notifying [onSelectionChange] with original list indices and items.
 */
@Composable
fun <T : Any> StyledDropdownMultiSelectIndex(
    items: List<T>,
    selectedIndices: MutableState<List<Int>>,
    modifier: Modifier = Modifier,
    title: String = "",
    enabled: Boolean = true,
    // defaults / customisation
    customization: StyledButton.Customization? = null,
    filter: StyledDropdown.Filter<T>? = null,
    shape: Shape = StyledDropdownMultiSelectDefaults.shape(),
    popupWidthConfig: StyledPopupMenu.WidthConfig = StyledPopupMenu.WidthConfig.MatchAnchor,
    sortSelectionByItems: Boolean = StyledDropdownMultiSelectDefaults.sortSelectionByItems,
    // content
    content: @Composable (context: StyledDropdown.RenderContext<T, List<T>>) -> Unit = StyledDropdownMultiSelectDefaults.defaultRenderer(),
    popupMaxHeight: Dp = StyledPopupMenuDefaults.maxHeight,
    actions: @Composable (StyledDropdownMultiSelect.Actions) -> Unit = StyledDropdownMultiSelectDefaults.defaultActions(),
    onSelectionChange: ((indices: List<Int>, items: List<T>) -> Unit)? = null,
) {
    StyledDropdownMultiSelectIndex(
        modifier = modifier,
        items = items,
        selectedIndices = selectedIndices.value,
        title = title,
        enabled = enabled,
        filter = filter,
        customization = customization,
        shape = shape,
        popupWidthConfig = popupWidthConfig,
        sortSelectionByItems = sortSelectionByItems,
        content = content,
        popupMaxHeight = popupMaxHeight,
        actions = actions,
        onSelectionChange = { indices, selectedItems ->
            selectedIndices.value = indices
            onSelectionChange?.invoke(indices, selectedItems)
        },
    )
}

/**
 * [selectedIndices] must reference entries in [items].
 * [sortSelectionByItems] orders the rendered selection and callback results by original list index.
 * [filter] affects menu entries only; select-all and clear operate on the entire selection.
 */
@Composable
fun <T : Any> StyledDropdownMultiSelectIndex(
    items: List<T>,
    selectedIndices: List<Int>,
    modifier: Modifier = Modifier,
    title: String = "",
    enabled: Boolean = true,
    // defaults / customisation
    customization: StyledButton.Customization? = null,
    filter: StyledDropdown.Filter<T>? = null,
    shape: Shape = StyledDropdownMultiSelectDefaults.shape(),
    popupWidthConfig: StyledPopupMenu.WidthConfig = StyledPopupMenu.WidthConfig.MatchAnchor,
    sortSelectionByItems: Boolean = StyledDropdownMultiSelectDefaults.sortSelectionByItems,
    // content
    content: @Composable (context: StyledDropdown.RenderContext<T, List<T>>) -> Unit = StyledDropdownMultiSelectDefaults.defaultRenderer(),
    popupMaxHeight: Dp = StyledPopupMenuDefaults.maxHeight,
    actions: @Composable (StyledDropdownMultiSelect.Actions) -> Unit = StyledDropdownMultiSelectDefaults.defaultActions(),
    onSelectionChange: (indices: List<Int>, items: List<T>) -> Unit,
) {
    StyledDropdownMultiSelectImpl(
        modifier = modifier,
        title = title,
        items = items,
        selected = selectedIndices,
        enabled = enabled,
        customization = customization,
        filter = filter,
        shape = shape,
        popupWidthConfig = popupWidthConfig,
        popupMaxHeight = popupMaxHeight,
        content = content,
        sortSelectionByItems = sortSelectionByItems,
        actions = actions,
        role = StyledButton.Role.Primary,
        onSelectionChange = onSelectionChange
    )
}



@Composable
fun <T : Any> StyledSecondaryDropdownMultiSelect(
    items: List<T>,
    selected: MutableState<List<T>>,
    modifier: Modifier = Modifier,
    title: String = "",
    enabled: Boolean = true,
    customization: StyledButton.Customization? = null,
    filter: StyledDropdown.Filter<T>? = null,
    shape: Shape = StyledDropdownMultiSelectDefaults.shape(),
    popupWidthConfig: StyledPopupMenu.WidthConfig = StyledPopupMenu.WidthConfig.MatchAnchor,
    sortSelectionByItems: Boolean = StyledDropdownMultiSelectDefaults.sortSelectionByItems,
    content: @Composable (context: StyledDropdown.RenderContext<T, List<T>>) -> Unit = StyledDropdownMultiSelectDefaults.defaultRenderer(),
    popupMaxHeight: Dp = StyledPopupMenuDefaults.maxHeight,
    actions: @Composable (StyledDropdownMultiSelect.Actions) -> Unit = StyledDropdownMultiSelectDefaults.defaultActions(),
    onSelectionChange: ((indices: List<Int>, items: List<T>) -> Unit)? = null,
) {
    StyledSecondaryDropdownMultiSelect(
        items = items,
        selected = selected.value,
        modifier = modifier,
        title = title,
        enabled = enabled,
        customization = customization,
        filter = filter,
        shape = shape,
        popupWidthConfig = popupWidthConfig,
        sortSelectionByItems = sortSelectionByItems,
        content = content,
        popupMaxHeight = popupMaxHeight,
        actions = actions,
    ) { indices, selectedItems ->
        selected.value = selectedItems
        onSelectionChange?.invoke(indices, selectedItems)
    }
}

@Composable
fun <T : Any> StyledSecondaryDropdownMultiSelect(
    items: List<T>,
    selected: List<T>,
    modifier: Modifier = Modifier,
    title: String = "",
    enabled: Boolean = true,
    customization: StyledButton.Customization? = null,
    filter: StyledDropdown.Filter<T>? = null,
    shape: Shape = StyledDropdownMultiSelectDefaults.shape(),
    popupWidthConfig: StyledPopupMenu.WidthConfig = StyledPopupMenu.WidthConfig.MatchAnchor,
    sortSelectionByItems: Boolean = StyledDropdownMultiSelectDefaults.sortSelectionByItems,
    content: @Composable (context: StyledDropdown.RenderContext<T, List<T>>) -> Unit = StyledDropdownMultiSelectDefaults.defaultRenderer(),
    popupMaxHeight: Dp = StyledPopupMenuDefaults.maxHeight,
    actions: @Composable (StyledDropdownMultiSelect.Actions) -> Unit = StyledDropdownMultiSelectDefaults.defaultActions(),
    onSelectionChange: (indices: List<Int>, items: List<T>) -> Unit,
) {
    StyledDropdownMultiSelectImpl(
        modifier = modifier,
        title = title,
        items = items,
        selected = selected.map { items.indexOf(it) },
        enabled = enabled,
        customization = customization,
        filter = filter,
        shape = shape,
        popupWidthConfig = popupWidthConfig,
        popupMaxHeight = popupMaxHeight,
        content = content,
        sortSelectionByItems = sortSelectionByItems,
        actions = actions,
        role = StyledButton.Role.Secondary,
        onSelectionChange = onSelectionChange,
    )
}

@Composable
fun <T : Any> StyledSecondaryDropdownMultiSelectIndex(
    items: List<T>,
    selectedIndices: MutableState<List<Int>>,
    modifier: Modifier = Modifier,
    title: String = "",
    enabled: Boolean = true,
    customization: StyledButton.Customization? = null,
    filter: StyledDropdown.Filter<T>? = null,
    shape: Shape = StyledDropdownMultiSelectDefaults.shape(),
    popupWidthConfig: StyledPopupMenu.WidthConfig = StyledPopupMenu.WidthConfig.MatchAnchor,
    sortSelectionByItems: Boolean = StyledDropdownMultiSelectDefaults.sortSelectionByItems,
    content: @Composable (context: StyledDropdown.RenderContext<T, List<T>>) -> Unit = StyledDropdownMultiSelectDefaults.defaultRenderer(),
    popupMaxHeight: Dp = StyledPopupMenuDefaults.maxHeight,
    actions: @Composable (StyledDropdownMultiSelect.Actions) -> Unit = StyledDropdownMultiSelectDefaults.defaultActions(),
    onSelectionChange: ((indices: List<Int>, items: List<T>) -> Unit)? = null,
) {
    StyledSecondaryDropdownMultiSelectIndex(
        items = items,
        selectedIndices = selectedIndices.value,
        modifier = modifier,
        title = title,
        enabled = enabled,
        customization = customization,
        filter = filter,
        shape = shape,
        popupWidthConfig = popupWidthConfig,
        sortSelectionByItems = sortSelectionByItems,
        content = content,
        popupMaxHeight = popupMaxHeight,
        actions = actions,
    ) { indices, selectedItems ->
        selectedIndices.value = indices
        onSelectionChange?.invoke(indices, selectedItems)
    }
}

@Composable
fun <T : Any> StyledSecondaryDropdownMultiSelectIndex(
    items: List<T>,
    selectedIndices: List<Int>,
    modifier: Modifier = Modifier,
    title: String = "",
    enabled: Boolean = true,
    customization: StyledButton.Customization? = null,
    filter: StyledDropdown.Filter<T>? = null,
    shape: Shape = StyledDropdownMultiSelectDefaults.shape(),
    popupWidthConfig: StyledPopupMenu.WidthConfig = StyledPopupMenu.WidthConfig.MatchAnchor,
    sortSelectionByItems: Boolean = StyledDropdownMultiSelectDefaults.sortSelectionByItems,
    content: @Composable (context: StyledDropdown.RenderContext<T, List<T>>) -> Unit = StyledDropdownMultiSelectDefaults.defaultRenderer(),
    popupMaxHeight: Dp = StyledPopupMenuDefaults.maxHeight,
    actions: @Composable (StyledDropdownMultiSelect.Actions) -> Unit = StyledDropdownMultiSelectDefaults.defaultActions(),
    onSelectionChange: (indices: List<Int>, items: List<T>) -> Unit,
) {
    StyledDropdownMultiSelectImpl(
        modifier = modifier,
        title = title,
        items = items,
        selected = selectedIndices,
        enabled = enabled,
        customization = customization,
        filter = filter,
        shape = shape,
        popupWidthConfig = popupWidthConfig,
        popupMaxHeight = popupMaxHeight,
        content = content,
        sortSelectionByItems = sortSelectionByItems,
        actions = actions,
        role = StyledButton.Role.Secondary,
        onSelectionChange = onSelectionChange,
    )
}

@Composable
fun <T : Any> StyledTertiaryDropdownMultiSelect(
    items: List<T>,
    selected: MutableState<List<T>>,
    modifier: Modifier = Modifier,
    title: String = "",
    enabled: Boolean = true,
    customization: StyledButton.Customization? = null,
    filter: StyledDropdown.Filter<T>? = null,
    shape: Shape = StyledDropdownMultiSelectDefaults.shape(),
    popupWidthConfig: StyledPopupMenu.WidthConfig = StyledPopupMenu.WidthConfig.MatchAnchor,
    sortSelectionByItems: Boolean = StyledDropdownMultiSelectDefaults.sortSelectionByItems,
    content: @Composable (context: StyledDropdown.RenderContext<T, List<T>>) -> Unit = StyledDropdownMultiSelectDefaults.defaultRenderer(),
    popupMaxHeight: Dp = StyledPopupMenuDefaults.maxHeight,
    actions: @Composable (StyledDropdownMultiSelect.Actions) -> Unit = StyledDropdownMultiSelectDefaults.defaultActions(),
    onSelectionChange: ((indices: List<Int>, items: List<T>) -> Unit)? = null,
) {
    StyledTertiaryDropdownMultiSelect(
        items = items,
        selected = selected.value,
        modifier = modifier,
        title = title,
        enabled = enabled,
        customization = customization,
        filter = filter,
        shape = shape,
        popupWidthConfig = popupWidthConfig,
        sortSelectionByItems = sortSelectionByItems,
        content = content,
        popupMaxHeight = popupMaxHeight,
        actions = actions,
    ) { indices, selectedItems ->
        selected.value = selectedItems
        onSelectionChange?.invoke(indices, selectedItems)
    }
}

@Composable
fun <T : Any> StyledTertiaryDropdownMultiSelect(
    items: List<T>,
    selected: List<T>,
    modifier: Modifier = Modifier,
    title: String = "",
    enabled: Boolean = true,
    customization: StyledButton.Customization? = null,
    filter: StyledDropdown.Filter<T>? = null,
    shape: Shape = StyledDropdownMultiSelectDefaults.shape(),
    popupWidthConfig: StyledPopupMenu.WidthConfig = StyledPopupMenu.WidthConfig.MatchAnchor,
    sortSelectionByItems: Boolean = StyledDropdownMultiSelectDefaults.sortSelectionByItems,
    content: @Composable (context: StyledDropdown.RenderContext<T, List<T>>) -> Unit = StyledDropdownMultiSelectDefaults.defaultRenderer(),
    popupMaxHeight: Dp = StyledPopupMenuDefaults.maxHeight,
    actions: @Composable (StyledDropdownMultiSelect.Actions) -> Unit = StyledDropdownMultiSelectDefaults.defaultActions(),
    onSelectionChange: (indices: List<Int>, items: List<T>) -> Unit,
) {
    StyledDropdownMultiSelectImpl(
        modifier = modifier,
        title = title,
        items = items,
        selected = selected.map { items.indexOf(it) },
        enabled = enabled,
        customization = customization,
        filter = filter,
        shape = shape,
        popupWidthConfig = popupWidthConfig,
        popupMaxHeight = popupMaxHeight,
        content = content,
        sortSelectionByItems = sortSelectionByItems,
        actions = actions,
        role = StyledButton.Role.Tertiary,
        onSelectionChange = onSelectionChange,
    )
}

@Composable
fun <T : Any> StyledTertiaryDropdownMultiSelectIndex(
    items: List<T>,
    selectedIndices: MutableState<List<Int>>,
    modifier: Modifier = Modifier,
    title: String = "",
    enabled: Boolean = true,
    customization: StyledButton.Customization? = null,
    filter: StyledDropdown.Filter<T>? = null,
    shape: Shape = StyledDropdownMultiSelectDefaults.shape(),
    popupWidthConfig: StyledPopupMenu.WidthConfig = StyledPopupMenu.WidthConfig.MatchAnchor,
    sortSelectionByItems: Boolean = StyledDropdownMultiSelectDefaults.sortSelectionByItems,
    content: @Composable (context: StyledDropdown.RenderContext<T, List<T>>) -> Unit = StyledDropdownMultiSelectDefaults.defaultRenderer(),
    popupMaxHeight: Dp = StyledPopupMenuDefaults.maxHeight,
    actions: @Composable (StyledDropdownMultiSelect.Actions) -> Unit = StyledDropdownMultiSelectDefaults.defaultActions(),
    onSelectionChange: ((indices: List<Int>, items: List<T>) -> Unit)? = null,
) {
    StyledDropdownMultiSelectImpl(
        modifier = modifier,
        title = title,
        items = items,
        selected = selectedIndices.value,
        enabled = enabled,
        customization = customization,
        filter = filter,
        shape = shape,
        popupWidthConfig = popupWidthConfig,
        popupMaxHeight = popupMaxHeight,
        content = content,
        sortSelectionByItems = sortSelectionByItems,
        actions = actions,
        role = StyledButton.Role.Tertiary,
    ) { indices, selectedItems ->
        selectedIndices.value = indices
        onSelectionChange?.invoke(indices, selectedItems)
    }
}

@Composable
fun <T : Any> StyledTertiaryDropdownMultiSelectIndex(
    items: List<T>,
    selectedIndices: List<Int>,
    modifier: Modifier = Modifier,
    title: String = "",
    enabled: Boolean = true,
    customization: StyledButton.Customization? = null,
    filter: StyledDropdown.Filter<T>? = null,
    shape: Shape = StyledDropdownMultiSelectDefaults.shape(),
    popupWidthConfig: StyledPopupMenu.WidthConfig = StyledPopupMenu.WidthConfig.MatchAnchor,
    sortSelectionByItems: Boolean = StyledDropdownMultiSelectDefaults.sortSelectionByItems,
    content: @Composable (context: StyledDropdown.RenderContext<T, List<T>>) -> Unit = StyledDropdownMultiSelectDefaults.defaultRenderer(),
    popupMaxHeight: Dp = StyledPopupMenuDefaults.maxHeight,
    actions: @Composable (StyledDropdownMultiSelect.Actions) -> Unit = StyledDropdownMultiSelectDefaults.defaultActions(),
    onSelectionChange: (indices: List<Int>, items: List<T>) -> Unit,
) {
    StyledDropdownMultiSelectImpl(
        modifier = modifier,
        title = title,
        items = items,
        selected = selectedIndices,
        enabled = enabled,
        customization = customization,
        filter = filter,
        shape = shape,
        popupWidthConfig = popupWidthConfig,
        popupMaxHeight = popupMaxHeight,
        content = content,
        sortSelectionByItems = sortSelectionByItems,
        actions = actions,
        role = StyledButton.Role.Tertiary,
        onSelectionChange = onSelectionChange,
    )
}

// -------------------------
// private API
// -------------------------

@Composable
private fun <T : Any> StyledDropdownMultiSelectImpl(
    modifier: Modifier,
    title: String,
    items: List<T>,
    selected: List<Int>,
    enabled: Boolean,
    customization: StyledButton.Customization?,
    filter: StyledDropdown.Filter<T>?,
    shape: Shape,
    popupWidthConfig: StyledPopupMenu.WidthConfig,
    content: @Composable (context: StyledDropdown.RenderContext<T, List<T>>) -> Unit,
    sortSelectionByItems: Boolean,
    role: StyledButton.Role,
    popupMaxHeight: Dp,
    actions: @Composable (StyledDropdownMultiSelect.Actions) -> Unit,
    onSelectionChange: (indices: List<Int>, items: List<T>) -> Unit,
) {
    val selectedIndices = dropdownSelectionIndices(selected, items.size, sortSelectionByItems)
    val selectedItems = selectedIndices.map { items[it] }
    val notifySelectionChange: (List<Int>) -> Unit = { indices ->
        val effectiveIndices = dropdownSelectionIndices(indices, items.size, sortSelectionByItems)
        onSelectionChange(effectiveIndices, effectiveIndices.map { items[it] })
    }
    StyledDropdownBase(
        modifier = modifier,
        title = title,
        items = items,
        selection = selectedItems,
        isSelected = { it in selectedIndices },
        closeOnItemClick = false,
        enabled = enabled,
        customization = customization,
        filter = filter,
        shape = shape,
        role = role,
        popupWidthConfig = popupWidthConfig,
        popupMaxHeight = popupMaxHeight,
        content = content,
        popupHeader = {
            actions(
                StyledDropdownMultiSelect.Actions(
                    canSelectAll = selectedIndices.size != items.size,
                    canClear = selectedIndices.isNotEmpty(),
                    selectAll = { notifySelectionChange(items.indices.toList()) },
                    clearSelection = { notifySelectionChange(emptyList()) },
                )
            )
        },
        onItemClick = { index, _ ->
            notifySelectionChange(toggleDropdownSelection(selectedIndices, index))
        }
    )
}

internal fun dropdownSelectionIndices(
    selected: List<Int>,
    itemCount: Int,
    sortSelectionByItems: Boolean,
): List<Int> {
    require(selected.all { it in 0 until itemCount }) {
        "Selected indices must refer to entries in items"
    }
    val unique = selected.distinct()
    return if (sortSelectionByItems) unique.sorted() else unique
}

internal fun toggleDropdownSelection(selected: List<Int>, index: Int): List<Int> =
    if (index in selected) selected.filterNot { it == index } else selected + index