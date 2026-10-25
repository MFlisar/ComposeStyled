package com.michaelflisar.composestyledcomponents.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyledcomponents.ui.components.StyledButton.Customization

// -------------------------
// public API
// -------------------------

/**
 * Updates [selected] before notifying [onSelectionChange] with original item indices and items.
 */
@Composable
fun <T : Any> StyledSegmentedButtonRowMultiSelect(
    items: List<T>,
    selected: MutableState<List<T>>,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    customization: Customization? = null,
    itemShape: CornerBasedShape = StyledSegmentedButtonRowDefaults.itemShape,
    itemContentPadding: PaddingValues = StyledSegmentedButtonRowDefaults.itemContentPadding,
    minItemWidth: Dp = StyledSegmentedButtonRowDefaults.minItemWidth,
    content: @Composable (item: T, selected: Boolean) -> Unit = StyledSegmentedButtonRowDefaults.defaultRenderer(),
    onSelectionChange: ((indices: List<Int>, items: List<T>) -> Unit)? = null,
) {
    StyledSegmentedButtonRowMultiSelect(
        items = items,
        selected = selected.value,
        modifier = modifier,
        enabled = enabled,
        customization = customization,
        itemShape = itemShape,
        itemContentPadding = itemContentPadding,
        minItemWidth = minItemWidth,
        content = content,
    ) { indices, selectedItems ->
        selected.value = selectedItems
        onSelectionChange?.invoke(indices, selectedItems)
    }
}

@Composable
fun <T : Any> StyledSegmentedButtonRowMultiSelect(
    items: List<T>,
    selected: List<T>,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    customization: Customization? = null,
    itemShape: CornerBasedShape = StyledSegmentedButtonRowDefaults.itemShape,
    itemContentPadding: PaddingValues = StyledSegmentedButtonRowDefaults.itemContentPadding,
    minItemWidth: Dp = StyledSegmentedButtonRowDefaults.minItemWidth,
    content: @Composable (item: T, selected: Boolean) -> Unit = StyledSegmentedButtonRowDefaults.defaultRenderer(),
    onSelectionChange: (indices: List<Int>, items: List<T>) -> Unit,
) {
    renderSegmentRow(
        items = items,
        selectedIndices = items.indices.filter { items[it] in selected },
        multiSelect = true,
        modifier = modifier,
        enabled = enabled,
        customization = customization,
        itemShape = itemShape,
        itemContentPadding = itemContentPadding,
        minItemWidth = minItemWidth,
        content = content,
    ) { indices, _ ->
        onSelectionChange(indices, indices.map { items[it] })
    }
}

@Composable
fun <T : Any> StyledSegmentedButtonRowMultiSelectIndex(
    items: List<T>,
    selectedIndices: MutableState<List<Int>>,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    customization: Customization? = null,
    itemShape: CornerBasedShape = StyledSegmentedButtonRowDefaults.itemShape,
    itemContentPadding: PaddingValues = StyledSegmentedButtonRowDefaults.itemContentPadding,
    minItemWidth: Dp = StyledSegmentedButtonRowDefaults.minItemWidth,
    content: @Composable (item: T, selected: Boolean) -> Unit = StyledSegmentedButtonRowDefaults.defaultRenderer(),
    onSelectionChange: ((indices: List<Int>, items: List<T>) -> Unit)? = null,
) {
    StyledSegmentedButtonRowMultiSelectIndex(
        items = items,
        selectedIndices = selectedIndices.value,
        modifier = modifier,
        enabled = enabled,
        customization = customization,
        itemShape = itemShape,
        itemContentPadding = itemContentPadding,
        minItemWidth = minItemWidth,
        content = content,
    ) { indices, selectedItems ->
        selectedIndices.value = indices
        onSelectionChange?.invoke(indices, selectedItems)
    }
}

@Composable
fun <T : Any> StyledSegmentedButtonRowMultiSelectIndex(
    items: List<T>,
    selectedIndices: List<Int>,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    customization: Customization? = null,
    itemShape: CornerBasedShape = StyledSegmentedButtonRowDefaults.itemShape,
    itemContentPadding: PaddingValues = StyledSegmentedButtonRowDefaults.itemContentPadding,
    minItemWidth: Dp = StyledSegmentedButtonRowDefaults.minItemWidth,
    content: @Composable (item: T, selected: Boolean) -> Unit = StyledSegmentedButtonRowDefaults.defaultRenderer(),
    onSelectionChange: (indices: List<Int>, items: List<T>) -> Unit,
) {
    renderSegmentRow(
        items = items,
        selectedIndices = selectedIndices,
        multiSelect = true,
        modifier = modifier,
        enabled = enabled,
        customization = customization,
        itemShape = itemShape,
        itemContentPadding = itemContentPadding,
        minItemWidth = minItemWidth,
        content = content,
    ) { indices, _ ->
        onSelectionChange(indices, indices.map { items[it] })
    }
}
