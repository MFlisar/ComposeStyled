package com.michaelflisar.composestyledcomponents.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.ui.internal.StyledDropdownBase
import com.michaelflisar.composestyledcomponents.ui.layout.StyledSpacedRow
import com.michaelflisar.composestyledcomponents.ui.primitives.StyledText

/**
 * Shared types and content helpers for single- and multi-selection dropdowns.
 */
object StyledDropdown {

    class Filter<T : Any>(
        val label: String,
        val filter: (filter: String, item: T) -> Boolean,
    )

    sealed interface RenderContext<out T : Any, out S> {
        data class Selection<S>(val selection: S) : RenderContext<Nothing, S>
        data class DropdownItem<T : Any>(
            val item: T,
            val selected: Boolean,
        ) : RenderContext<T, Nothing>
    }

    fun <T : Any, S> renderContent(
        content: @Composable (RenderContext.Selection<S>) -> Unit,
        dropdownContent: @Composable (RenderContext.DropdownItem<T>) -> Unit,
    ): @Composable (RenderContext<T, S>) -> Unit = { context ->
        when (context) {
            is RenderContext.Selection -> content(context)
            is RenderContext.DropdownItem -> dropdownContent(context)
        }
    }
}

// -------------------------
// defaults
// -------------------------

object StyledDropdownDefaults {

    @Composable
    fun <T : Any> defaultRenderer(
        mapper: @Composable (item: T) -> String = { it.toString() },
        color: Color = Color.Unspecified,
        fontWeight: FontWeight? = null,
        maxLines: Int = 1,
        leadingContent: @Composable () -> Unit = {},
        mapperDropdown: @Composable (item: T) -> String = mapper,
        colorDropdown: Color = Color.Unspecified,
        colorDropdownSelected: Color = StyledTheme.colorScheme.primary,
        fontWeightDropdown: FontWeight? = null,
        fontWeightDropdownSelected: FontWeight? = FontWeight.Bold,
        maxLinesDropdown: Int = 1,
    ) = StyledDropdown.renderContent(
        content = defaultContent(
            mapper = mapper,
            color = color,
            fontWeight = fontWeight,
            maxLines = maxLines,
            leadingContent = leadingContent
        ),
        dropdownContent = defaultDropdownContent(
            mapper = mapperDropdown,
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
        content: @Composable (context: StyledDropdown.RenderContext.Selection<T?>) -> Unit = defaultContent(),
        dropdownContent: @Composable (context: StyledDropdown.RenderContext.DropdownItem<T>) -> Unit = defaultDropdownContent(),
    ) = StyledDropdown.renderContent(
        content = content,
        dropdownContent = dropdownContent
    )

    @Composable
    fun <T> defaultContent(
        mapper: @Composable (item: T) -> String = { it.toString() },
        color: Color = Color.Unspecified,
        fontWeight: FontWeight? = null,
        maxLines: Int = 1,
        leadingContent: @Composable () -> Unit = {},
    ): @Composable (StyledDropdown.RenderContext.Selection<T?>) -> Unit = { context ->
        StyledSpacedRow(itemSpacing = StyledTheme.spacing.medium) {
            leadingContent()
            StyledText(
                text = if (context.selection != null) mapper(context.selection) else "",
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
            leadingContent()
            StyledText(
                text = mapper(context.item),
                color = if (context.selected) colorSelected else color,
                modifier = Modifier.weight(1f),
                fontWeight = if (context.selected) fontWeightSelected else fontWeight,
                style = StyledTheme.typography.bodyMedium,
                maxLines = maxLines
            )
        }
    }

}

// -------------------------
// public API
// -------------------------

/**
 * Updates [selected] before notifying [onSelectionChange] with the original list index and item.
 */
@Composable
fun <T : Any> StyledDropdown(
    items: List<T>,
    selected: MutableState<T>,
    modifier: Modifier = Modifier,
    title: String = "",
    enabled: Boolean = true,
    filter: StyledDropdown.Filter<T>? = null,
    // defaults / customisation
    customization: StyledButton.Customization? = null,
    shape: Shape = StyledTheme.shapes.small,
    popupWidthConfig: StyledPopupMenu.WidthConfig = StyledPopupMenu.WidthConfig.MatchAnchor,
    popupMaxHeight: Dp = StyledPopupMenuDefaults.maxHeight,
    // content
    content: @Composable (context: StyledDropdown.RenderContext<T, T?>) -> Unit = StyledDropdownDefaults.defaultRenderer(),
    onSelectionChange: ((index: Int, item: T) -> Unit)? = null,
) {
    StyledDropdown(
        items = items,
        selected = selected.value,
        modifier = modifier,
        title = title,
        enabled = enabled,
        filter = filter,
        customization = customization,
        shape = shape,
        popupWidthConfig = popupWidthConfig,
        popupMaxHeight = popupMaxHeight,
        content = content,
        onSelectionChange = { index, item ->
            selected.value = item
            onSelectionChange?.invoke(index, item)
        }
    )
}

/**
 * Selects the first item equal to [selected], or displays no selection if it is absent.
 * [onSelectionChange] receives the original list index and item.
 */
@Composable
fun <T : Any> StyledDropdown(
    items: List<T>,
    selected: T,
    modifier: Modifier = Modifier,
    title: String = "",
    enabled: Boolean = true,
    filter: StyledDropdown.Filter<T>? = null,
    // defaults / customisation
    customization: StyledButton.Customization? = null,
    shape: Shape = StyledTheme.shapes.small,
    popupWidthConfig: StyledPopupMenu.WidthConfig = StyledPopupMenu.WidthConfig.MatchAnchor,
    popupMaxHeight: Dp = StyledPopupMenuDefaults.maxHeight,
    // content
    content: @Composable (context: StyledDropdown.RenderContext<T, T?>) -> Unit = StyledDropdownDefaults.defaultRenderer(),
    onSelectionChange: (index: Int, item: T) -> Unit,
) {
    val selectedIndex = items.indexOf(selected)
    StyledDropdownIndex(
        items = items,
        selectedIndex = selectedIndex,
        modifier = modifier,
        title = title,
        enabled = enabled,
        customization = customization,
        filter = filter,
        shape = shape,
        popupWidthConfig = popupWidthConfig,
        popupMaxHeight = popupMaxHeight,
        content = content,
        onSelectionChange = onSelectionChange
    )
}

/**
 * Updates [selectedIndex] before notifying [onSelectionChange].
 */
@Composable
fun <T : Any> StyledDropdownIndex(
    items: List<T>,
    selectedIndex: MutableState<Int>,
    modifier: Modifier = Modifier,
    title: String = "",
    enabled: Boolean = true,
    filter: StyledDropdown.Filter<T>? = null,
    // defaults / customisation
    customization: StyledButton.Customization? = null,
    shape: Shape = StyledTheme.shapes.small,
    popupWidthConfig: StyledPopupMenu.WidthConfig = StyledPopupMenu.WidthConfig.MatchAnchor,
    popupMaxHeight: Dp = StyledPopupMenuDefaults.maxHeight,
    // content
    content: @Composable (context: StyledDropdown.RenderContext<T, T?>) -> Unit = StyledDropdownDefaults.defaultRenderer(),
    onSelectionChange: ((index: Int, item: T) -> Unit)? = null,
) {
    StyledDropdownIndex(
        items = items,
        selectedIndex = selectedIndex.value,
        modifier = modifier,
        title = title,
        enabled = enabled,
        customization = customization,
        filter = filter,
        shape = shape,
        popupWidthConfig = popupWidthConfig,
        popupMaxHeight = popupMaxHeight,
        content = content,
        onSelectionChange = { index, item ->
            selectedIndex.value = index
            onSelectionChange?.invoke(index, item)
        }
    )
}

/**
 * Displays a selection from [items]. [onSelectionChange] receives the index in the
 * original [items] list (even when filtered) and the selected item.
 * An out-of-range [selectedIndex] displays no selection.
 * [content] receives a [RenderContext.Selection] for the closed selection or
 * a [RenderContext.DropdownItem] for each menu entry.
 * The container handles the title and layout. Customize text mapping through
 * [StyledDropdownDefaults.defaultRenderer], or render each context separately.
 */
@Composable
fun <T : Any> StyledDropdownIndex(
    items: List<T>,
    selectedIndex: Int,
    modifier: Modifier = Modifier,
    title: String = "",
    enabled: Boolean = true,
    filter: StyledDropdown.Filter<T>? = null,
    // defaults / customisation
    customization: StyledButton.Customization? = null,
    shape: Shape = StyledTheme.shapes.small,
    popupWidthConfig: StyledPopupMenu.WidthConfig = StyledPopupMenu.WidthConfig.MatchAnchor,
    popupMaxHeight: Dp = StyledPopupMenuDefaults.maxHeight,
    // content
    content: @Composable (context: StyledDropdown.RenderContext<T, T?>) -> Unit = StyledDropdownDefaults.defaultRenderer(),
    onSelectionChange: (index: Int, item: T) -> Unit,
) {
    StyledDropdownImpl(
        role = StyledButton.Role.Primary,
        items = items,
        selectedIndex = selectedIndex,
        modifier = modifier,
        title = title,
        enabled = enabled,
        filter = filter,
        customization = customization,
        shape = shape,
        popupWidthConfig = popupWidthConfig,
        popupMaxHeight = popupMaxHeight,
        content = content,
        onSelectionChange = onSelectionChange,
    )
}

/** State overload of [StyledSecondaryDropdown]. */
@Composable
fun <T : Any> StyledSecondaryDropdown(
    items: List<T>,
    selected: MutableState<T>,
    modifier: Modifier = Modifier,
    title: String = "",
    enabled: Boolean = true,
    filter: StyledDropdown.Filter<T>? = null,
    customization: StyledButton.Customization? = null,
    shape: Shape = StyledTheme.shapes.small,
    popupWidthConfig: StyledPopupMenu.WidthConfig = StyledPopupMenu.WidthConfig.MatchAnchor,
    popupMaxHeight: Dp = StyledPopupMenuDefaults.maxHeight,
    content: @Composable (context: StyledDropdown.RenderContext<T, T?>) -> Unit = StyledDropdownDefaults.defaultRenderer(),
    onSelectionChange: ((index: Int, item: T) -> Unit)? = null,
) {
    StyledSecondaryDropdown(
        items = items,
        selected = selected.value,
        modifier = modifier,
        title = title,
        enabled = enabled,
        filter = filter,
        customization = customization,
        shape = shape,
        popupWidthConfig = popupWidthConfig,
        popupMaxHeight = popupMaxHeight,
        content = content,
        onSelectionChange = { index, item ->
            selected.value = item
            onSelectionChange?.invoke(index, item)
        },
    )
}

/**
 * Renders the dropdown anchor with the secondary button role.
 * [onSelectionChange] receives the original list index and selected item.
 */
@Composable
fun <T : Any> StyledSecondaryDropdown(
    items: List<T>,
    selected: T,
    modifier: Modifier = Modifier,
    title: String = "",
    enabled: Boolean = true,
    filter: StyledDropdown.Filter<T>? = null,
    customization: StyledButton.Customization? = null,
    shape: Shape = StyledTheme.shapes.small,
    popupWidthConfig: StyledPopupMenu.WidthConfig = StyledPopupMenu.WidthConfig.MatchAnchor,
    popupMaxHeight: Dp = StyledPopupMenuDefaults.maxHeight,
    content: @Composable (context: StyledDropdown.RenderContext<T, T?>) -> Unit = StyledDropdownDefaults.defaultRenderer(),
    onSelectionChange: (index: Int, item: T) -> Unit,
) {
    StyledDropdownImpl(
        role = StyledButton.Role.Secondary,
        items = items,
        selectedIndex = items.indexOf(selected),
        modifier = modifier,
        title = title,
        enabled = enabled,
        filter = filter,
        customization = customization,
        shape = shape,
        popupWidthConfig = popupWidthConfig,
        popupMaxHeight = popupMaxHeight,
        content = content,
        onSelectionChange = onSelectionChange,
    )
}

/** State overload of [StyledSecondaryDropdownIndex]. */
@Composable
fun <T : Any> StyledSecondaryDropdownIndex(
    items: List<T>,
    selectedIndex: MutableState<Int>,
    modifier: Modifier = Modifier,
    title: String = "",
    enabled: Boolean = true,
    filter: StyledDropdown.Filter<T>? = null,
    customization: StyledButton.Customization? = null,
    shape: Shape = StyledTheme.shapes.small,
    popupWidthConfig: StyledPopupMenu.WidthConfig = StyledPopupMenu.WidthConfig.MatchAnchor,
    popupMaxHeight: Dp = StyledPopupMenuDefaults.maxHeight,
    content: @Composable (context: StyledDropdown.RenderContext<T, T?>) -> Unit = StyledDropdownDefaults.defaultRenderer(),
    onSelectionChange: ((index: Int, item: T) -> Unit)? = null,
) {
    StyledSecondaryDropdownIndex(
        items = items,
        selectedIndex = selectedIndex.value,
        modifier = modifier,
        title = title,
        enabled = enabled,
        filter = filter,
        customization = customization,
        shape = shape,
        popupWidthConfig = popupWidthConfig,
        popupMaxHeight = popupMaxHeight,
        content = content,
    ) { index, item ->
        selectedIndex.value = index
        onSelectionChange?.invoke(index, item)
    }
}

/** Index-based overload with the secondary anchor role. */
@Composable
fun <T : Any> StyledSecondaryDropdownIndex(
    items: List<T>,
    selectedIndex: Int,
    modifier: Modifier = Modifier,
    title: String = "",
    enabled: Boolean = true,
    filter: StyledDropdown.Filter<T>? = null,
    customization: StyledButton.Customization? = null,
    shape: Shape = StyledTheme.shapes.small,
    popupWidthConfig: StyledPopupMenu.WidthConfig = StyledPopupMenu.WidthConfig.MatchAnchor,
    popupMaxHeight: Dp = StyledPopupMenuDefaults.maxHeight,
    content: @Composable (context: StyledDropdown.RenderContext<T, T?>) -> Unit = StyledDropdownDefaults.defaultRenderer(),
    onSelectionChange: (index: Int, item: T) -> Unit,
) {
    StyledDropdownImpl(
        role = StyledButton.Role.Secondary,
        items = items,
        selectedIndex = selectedIndex,
        modifier = modifier,
        title = title,
        enabled = enabled,
        filter = filter,
        customization = customization,
        shape = shape,
        popupWidthConfig = popupWidthConfig,
        popupMaxHeight = popupMaxHeight,
        content = content,
        onSelectionChange = onSelectionChange,
    )
}

/** State overload of [StyledTertiaryDropdown]. */
@Composable
fun <T : Any> StyledTertiaryDropdown(
    items: List<T>,
    selected: MutableState<T>,
    modifier: Modifier = Modifier,
    title: String = "",
    enabled: Boolean = true,
    filter: StyledDropdown.Filter<T>? = null,
    customization: StyledButton.Customization? = null,
    shape: Shape = StyledTheme.shapes.small,
    popupWidthConfig: StyledPopupMenu.WidthConfig = StyledPopupMenu.WidthConfig.MatchAnchor,
    popupMaxHeight: Dp = StyledPopupMenuDefaults.maxHeight,
    content: @Composable (context: StyledDropdown.RenderContext<T, T?>) -> Unit = StyledDropdownDefaults.defaultRenderer(),
    onSelectionChange: ((index: Int, item: T) -> Unit)? = null,
) {
    StyledTertiaryDropdown(
        items = items,
        selected = selected.value,
        modifier = modifier,
        title = title,
        enabled = enabled,
        filter = filter,
        customization = customization,
        shape = shape,
        popupWidthConfig = popupWidthConfig,
        popupMaxHeight = popupMaxHeight,
        content = content,
        onSelectionChange = { index, item ->
            selected.value = item
            onSelectionChange?.invoke(index, item)
        },
    )
}

/** Uses the tertiary role for the dropdown anchor. */
@Composable
fun <T : Any> StyledTertiaryDropdown(
    items: List<T>,
    selected: T,
    modifier: Modifier = Modifier,
    title: String = "",
    enabled: Boolean = true,
    filter: StyledDropdown.Filter<T>? = null,
    customization: StyledButton.Customization? = null,
    shape: Shape = StyledTheme.shapes.small,
    popupWidthConfig: StyledPopupMenu.WidthConfig = StyledPopupMenu.WidthConfig.MatchAnchor,
    popupMaxHeight: Dp = StyledPopupMenuDefaults.maxHeight,
    content: @Composable (context: StyledDropdown.RenderContext<T, T?>) -> Unit = StyledDropdownDefaults.defaultRenderer(),
    onSelectionChange: (index: Int, item: T) -> Unit,
) {
    StyledDropdownImpl(
        role = StyledButton.Role.Tertiary,
        items = items,
        selectedIndex = items.indexOf(selected),
        modifier = modifier,
        title = title,
        enabled = enabled,
        filter = filter,
        customization = customization,
        shape = shape,
        popupWidthConfig = popupWidthConfig,
        popupMaxHeight = popupMaxHeight,
        content = content,
        onSelectionChange = onSelectionChange,
    )
}

/** State overload of [StyledTertiaryDropdownIndex]. */
@Composable
fun <T : Any> StyledTertiaryDropdownIndex(
    items: List<T>,
    selectedIndex: MutableState<Int>,
    modifier: Modifier = Modifier,
    title: String = "",
    enabled: Boolean = true,
    filter: StyledDropdown.Filter<T>? = null,
    customization: StyledButton.Customization? = null,
    shape: Shape = StyledTheme.shapes.small,
    popupWidthConfig: StyledPopupMenu.WidthConfig = StyledPopupMenu.WidthConfig.MatchAnchor,
    popupMaxHeight: Dp = StyledPopupMenuDefaults.maxHeight,
    content: @Composable (context: StyledDropdown.RenderContext<T, T?>) -> Unit = StyledDropdownDefaults.defaultRenderer(),
    onSelectionChange: ((index: Int, item: T) -> Unit)? = null,
) {
    StyledTertiaryDropdownIndex(
        items = items,
        selectedIndex = selectedIndex.value,
        modifier = modifier,
        title = title,
        enabled = enabled,
        filter = filter,
        customization = customization,
        shape = shape,
        popupWidthConfig = popupWidthConfig,
        popupMaxHeight = popupMaxHeight,
        content = content,
    ) { index, item ->
        selectedIndex.value = index
        onSelectionChange?.invoke(index, item)
    }
}

// -------------------------

/** Index-based overload with the tertiary anchor role. */
@Composable
fun <T : Any> StyledTertiaryDropdownIndex(
    items: List<T>,
    selectedIndex: Int,
    modifier: Modifier = Modifier,
    title: String = "",
    enabled: Boolean = true,
    filter: StyledDropdown.Filter<T>? = null,
    customization: StyledButton.Customization? = null,
    shape: Shape = StyledTheme.shapes.small,
    popupWidthConfig: StyledPopupMenu.WidthConfig = StyledPopupMenu.WidthConfig.MatchAnchor,
    popupMaxHeight: Dp = StyledPopupMenuDefaults.maxHeight,
    content: @Composable (context: StyledDropdown.RenderContext<T, T?>) -> Unit = StyledDropdownDefaults.defaultRenderer(),
    onSelectionChange: (index: Int, item: T) -> Unit,
) {
    StyledDropdownImpl(
        role = StyledButton.Role.Tertiary,
        items = items,
        selectedIndex = selectedIndex,
        modifier = modifier,
        title = title,
        enabled = enabled,
        filter = filter,
        customization = customization,
        shape = shape,
        popupWidthConfig = popupWidthConfig,
        popupMaxHeight = popupMaxHeight,
        content = content,
        onSelectionChange = onSelectionChange,
    )
}

// private API
// -------------------------

@Composable
private fun <T : Any> StyledDropdownImpl(
    role: StyledButton.Role,
    items: List<T>,
    selectedIndex: Int,
    modifier: Modifier,
    title: String,
    enabled: Boolean,
    filter: StyledDropdown.Filter<T>?,
    customization: StyledButton.Customization?,
    shape: Shape,
    popupWidthConfig: StyledPopupMenu.WidthConfig,
    popupMaxHeight: Dp,
    content: @Composable (context: StyledDropdown.RenderContext<T, T?>) -> Unit,
    onSelectionChange: (index: Int, item: T) -> Unit,
) {
    StyledDropdownBase(
        modifier = modifier,
        title = title,
        items = items,
        selection = items.getOrNull(selectedIndex),
        isSelected = { it == selectedIndex },
        closeOnItemClick = true,
        enabled = enabled,
        customization = customization,
        filter = filter,
        role = role,
        shape = shape,
        popupWidthConfig = popupWidthConfig,
        popupMaxHeight = popupMaxHeight,
        content = content,
        onItemClick = onSelectionChange,
    )
}