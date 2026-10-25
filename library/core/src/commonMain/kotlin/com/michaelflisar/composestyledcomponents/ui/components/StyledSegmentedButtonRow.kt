package com.michaelflisar.composestyledcomponents.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyledcomponents.registry.LocalStyledRegistry
import com.michaelflisar.composestyledcomponents.registry.StyledComponent
import com.michaelflisar.composestyledcomponents.registry.StyledKey
import com.michaelflisar.composestyledcomponents.ui.components.StyledButton.Customization
import com.michaelflisar.composestyledcomponents.ui.primitives.StyledText

object StyledSegmentedButtonRow {

    // -------------
    // 1) key
    // -------------

    val Key = StyledKey<Implementation>("StyledSegmentedButtonRow")

    // -------------
    // 2) defaults
    // -------------

    @Stable
    class Defaults(
        val itemShape: CornerBasedShape,
        val itemContentPadding: PaddingValues,
        val minItemWidth: Dp = 58.dp,
    )

    // -------------
    // 5) implementation
    // -------------

    interface Implementation : StyledComponent<Implementation> {
        override val key: StyledKey<Implementation>
            get() = Key

        val defaults: Defaults

        @Composable
        fun <T : Any> Render(
            items: List<T>,
            selectedIndices: List<Int>,
            multiSelect: Boolean,
            modifier: Modifier,
            enabled: Boolean,
            itemShape: CornerBasedShape,
            itemContentPadding: PaddingValues,
            minItemWidth: Dp,
            customization: Customization?,
            content: @Composable (item: T, selected: Boolean) -> Unit,
            onItemClick: (index: Int) -> Unit,
        )
    }
}

// -------------------------
// defaults
// -------------------------

object StyledSegmentedButtonRowDefaults {

    val itemShape: CornerBasedShape
        @Composable get() = LocalStyledRegistry.current[StyledSegmentedButtonRow.Key].defaults.itemShape

    val itemContentPadding: PaddingValues
        @Composable get() = LocalStyledRegistry.current[StyledSegmentedButtonRow.Key].defaults.itemContentPadding

    val minItemWidth: Dp
        @Composable get() = LocalStyledRegistry.current[StyledSegmentedButtonRow.Key].defaults.minItemWidth

    @Composable
    fun <T : Any> defaultRenderer(
        mapper: @Composable (item: T) -> String = { it.toString() },
    ): @Composable (item: T, selected: Boolean) -> Unit = { item, _ ->
        StyledText(
            text = mapper(item),
        )
    }
}

// -------------------------
// public API
// -------------------------

/**
 * Updates [selected] before notifying [onSelectionChange] with the original item index and item.
 */
@Composable
fun <T : Any> StyledSegmentedButtonRow(
    items: List<T>,
    selected: MutableState<T>,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    customization: Customization? = null,
    itemShape: CornerBasedShape = StyledSegmentedButtonRowDefaults.itemShape,
    itemContentPadding: PaddingValues = StyledSegmentedButtonRowDefaults.itemContentPadding,
    minItemWidth: Dp = StyledSegmentedButtonRowDefaults.minItemWidth,
    content: @Composable (item: T, selected: Boolean) -> Unit = StyledSegmentedButtonRowDefaults.defaultRenderer(),
    onSelectionChange: ((index: Int, item: T) -> Unit)? = null,
) {
    val selectedIndex = items.indexOf(selected.value)
    renderSegmentRow(
        items = items,
        selectedIndices = listOf(selectedIndex).filter { it in items.indices },
        modifier = modifier,
        enabled = enabled,
        itemShape = itemShape,
        itemContentPadding = itemContentPadding,
        minItemWidth = minItemWidth,
        customization = customization,
        content = content,
    ) { indices, clickedIndex ->
        if (indices.isNotEmpty()) {
            selected.value = items[indices.first()]
        }
        onSelectionChange?.invoke(if (indices.isEmpty()) -1 else indices.first(), items[clickedIndex])
    }
}

/**
 * Selects the first item equal to [selected], or displays no selection if it is absent.
 */
@Composable
fun <T : Any> StyledSegmentedButtonRow(
    items: List<T>,
    selected: T,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    customization: Customization? = null,
    itemShape: CornerBasedShape = StyledSegmentedButtonRowDefaults.itemShape,
    itemContentPadding: PaddingValues = StyledSegmentedButtonRowDefaults.itemContentPadding,
    minItemWidth: Dp = StyledSegmentedButtonRowDefaults.minItemWidth,
    content: @Composable (item: T, selected: Boolean) -> Unit = StyledSegmentedButtonRowDefaults.defaultRenderer(),
    onSelectionChange: (index: Int, item: T) -> Unit,
) {
    renderSegmentRow(
        items = items,
        selectedIndices = listOf(items.indexOf(selected)).filter { it in items.indices },
        modifier = modifier,
        enabled = enabled,
        itemShape = itemShape,
        itemContentPadding = itemContentPadding,
        minItemWidth = minItemWidth,
        customization = customization,
        content = content,
    ) { indices, clickedIndex ->
        onSelectionChange(if (indices.isEmpty()) -1 else indices.first(), items[clickedIndex])
    }
}

/**
 * A nullable selection can be cleared when [forceSelection] is false.
 */
@Composable
fun <T : Any> StyledSegmentedButtonRow(
    items: List<T>,
    selected: MutableState<T?>,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    customization: Customization? = null,
    itemShape: CornerBasedShape = StyledSegmentedButtonRowDefaults.itemShape,
    itemContentPadding: PaddingValues = StyledSegmentedButtonRowDefaults.itemContentPadding,
    minItemWidth: Dp = StyledSegmentedButtonRowDefaults.minItemWidth,
    forceSelection: Boolean = true,
    content: @Composable (item: T, selected: Boolean) -> Unit = StyledSegmentedButtonRowDefaults.defaultRenderer(),
    onSelectionChange: ((index: Int, item: T?) -> Unit)? = null,
) {
    val selectedIndex = selected.value?.let(items::indexOf) ?: -1
    renderSegmentRow(
        items = items,
        selectedIndices = listOf(selectedIndex).filter { it in items.indices },
        forceSelection = forceSelection,
        modifier = modifier,
        enabled = enabled,
        itemShape = itemShape,
        itemContentPadding = itemContentPadding,
        minItemWidth = minItemWidth,
        customization = customization,
        content = content,
    ) { indices, _ ->
        selected.value = indices.firstOrNull()?.let(items::get)
        val index = indices.firstOrNull() ?: -1
        onSelectionChange?.invoke(index, indices.firstOrNull()?.let(items::get))
    }
}

@Composable
fun <T : Any> StyledSegmentedButtonRow(
    items: List<T>,
    selected: T?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    customization: Customization? = null,
    itemShape: CornerBasedShape = StyledSegmentedButtonRowDefaults.itemShape,
    itemContentPadding: PaddingValues = StyledSegmentedButtonRowDefaults.itemContentPadding,
    minItemWidth: Dp = StyledSegmentedButtonRowDefaults.minItemWidth,
    forceSelection: Boolean = true,
    content: @Composable (item: T, selected: Boolean) -> Unit = StyledSegmentedButtonRowDefaults.defaultRenderer(),
    onSelectionChange: (index: Int, item: T?) -> Unit,
) {
    renderSegmentRow(
        items = items,
        selectedIndices = listOfNotNull(selected?.let(items::indexOf)).filter { it in items.indices },
        forceSelection = forceSelection,
        modifier = modifier,
        enabled = enabled,
        itemShape = itemShape,
        itemContentPadding = itemContentPadding,
        minItemWidth = minItemWidth,
        customization = customization,
        content = content,
    ) { indices, _ ->
        val index = indices.firstOrNull() ?: -1
        onSelectionChange(index, indices.firstOrNull()?.let(items::get))
    }
}

/**
 * Updates [selectedIndex] before notifying [onSelectionChange].
 */
@Composable
fun <T : Any> StyledSegmentedButtonRowIndex(
    items: List<T>,
    selectedIndex: MutableState<Int>,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    customization: Customization? = null,
    itemShape: CornerBasedShape = StyledSegmentedButtonRowDefaults.itemShape,
    itemContentPadding: PaddingValues = StyledSegmentedButtonRowDefaults.itemContentPadding,
    minItemWidth: Dp = StyledSegmentedButtonRowDefaults.minItemWidth,
    forceSelection: Boolean = true,
    content: @Composable (item: T, selected: Boolean) -> Unit = StyledSegmentedButtonRowDefaults.defaultRenderer(),
    onSelectionChange: ((index: Int, item: T) -> Unit)? = null,
) {
    val currentIndex = selectedIndex.value
    renderSegmentRow(
        items = items,
        selectedIndices = listOf(currentIndex).filter { it in items.indices },
        forceSelection = forceSelection,
        modifier = modifier,
        enabled = enabled,
        itemShape = itemShape,
        itemContentPadding = itemContentPadding,
        minItemWidth = minItemWidth,
        customization = customization,
        content = content,
    ) { indices, clickedIndex ->
        selectedIndex.value = indices.firstOrNull() ?: -1
        onSelectionChange?.invoke(selectedIndex.value, items[clickedIndex])
    }
}

/**
 * An out-of-range [selectedIndex] displays no selection. Set [forceSelection] to false to clear
 * the active segment by clicking it again.
 */
@Composable
fun <T : Any> StyledSegmentedButtonRowIndex(
    items: List<T>,
    selectedIndex: Int,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    customization: Customization? = null,
    itemShape: CornerBasedShape = StyledSegmentedButtonRowDefaults.itemShape,
    itemContentPadding: PaddingValues = StyledSegmentedButtonRowDefaults.itemContentPadding,
    minItemWidth: Dp = StyledSegmentedButtonRowDefaults.minItemWidth,
    forceSelection: Boolean = true,
    content: @Composable (item: T, selected: Boolean) -> Unit = StyledSegmentedButtonRowDefaults.defaultRenderer(),
    onSelectionChange: (index: Int, item: T) -> Unit,
) {
    renderSegmentRow(
        items = items,
        selectedIndices = listOf(selectedIndex).filter { it in items.indices },
        forceSelection = forceSelection,
        modifier = modifier,
        enabled = enabled,
        itemShape = itemShape,
        itemContentPadding = itemContentPadding,
        minItemWidth = minItemWidth,
        customization = customization,
        content = content,
    ) { indices, clickedIndex ->
        onSelectionChange(indices.firstOrNull() ?: -1, items[clickedIndex])
    }
}

// -------------------------
// private API
// -------------------------

@Composable
internal fun <T : Any> renderSegmentRow(
    items: List<T>,
    selectedIndices: List<Int>,
    multiSelect: Boolean = false,
    forceSelection: Boolean = true,
    modifier: Modifier,
    enabled: Boolean,
    itemShape: CornerBasedShape,
    itemContentPadding: PaddingValues,
    minItemWidth: Dp,
    customization: Customization?,
    content: @Composable (item: T, selected: Boolean) -> Unit,
    onSelectionChange: (indices: List<Int>, clickedIndex: Int) -> Unit,
) {
    val currentIndices = selectedIndices.distinct().filter { it in items.indices }.sorted()
    LocalStyledRegistry.current[StyledSegmentedButtonRow.Key].Render(
        items = items,
        selectedIndices = currentIndices,
        multiSelect = multiSelect,
        modifier = modifier,
        enabled = enabled,
        itemShape = itemShape,
        itemContentPadding = itemContentPadding,
        minItemWidth = minItemWidth,
        customization = customization,
        content = content,
        onItemClick = { clickedIndex ->
            val nextIndices = when {
                multiSelect && clickedIndex in currentIndices ->
                    currentIndices.filterNot { it == clickedIndex }
                multiSelect -> (currentIndices + clickedIndex).sorted()
                clickedIndex in currentIndices && !forceSelection -> emptyList()
                else -> listOf(clickedIndex)
            }
            onSelectionChange(nextIndices, clickedIndex)
        },
    )
}
