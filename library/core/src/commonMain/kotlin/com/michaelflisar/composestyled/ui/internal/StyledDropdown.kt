package com.michaelflisar.composestyled.ui.internal

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.tokens.LocalStyledTextStyle
import com.michaelflisar.composestyled.ui.components.StyledButton
import com.michaelflisar.composestyled.ui.components.StyledPopupMenu
import com.michaelflisar.composestyled.ui.components.StyledPopupMenuDefaults
import com.michaelflisar.composestyled.ui.layout.StyledRow
import com.michaelflisar.composestyled.ui.primitives.StyledText

/**
 * Shared types and content helpers for single- and multi-selection dropdowns.
 */
internal object StyledDropdown {

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

internal object StyledDropdownDefaults {

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
        textStyle: TextStyle? = null,
        dropdownTextStyle: TextStyle? = textStyle,
    ) = StyledDropdown.renderContent(
        content = defaultContent(
            mapper = mapper,
            color = color,
            fontWeight = fontWeight,
            maxLines = maxLines,
            leadingContent = leadingContent,
            textStyle = textStyle,
        ),
        dropdownContent = defaultDropdownContent(
            mapper = mapperDropdown,
            color = colorDropdown,
            colorSelected = colorDropdownSelected,
            fontWeight = fontWeightDropdown,
            fontWeightSelected = fontWeightDropdownSelected,
            maxLines = maxLinesDropdown,
            leadingContent = leadingContent,
            textStyle = dropdownTextStyle,
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
        textStyle: TextStyle? = null,
    ): @Composable (StyledDropdown.RenderContext.Selection<T?>) -> Unit = { context ->
        val resolvedTextStyle = textStyle?.let(LocalStyledTextStyle.current::merge)
            ?: LocalStyledTextStyle.current
        StyledRow(itemSpacing = StyledTheme.spacing.medium) {
            leadingContent()
            StyledText(
                text = if (context.selection != null) mapper(context.selection) else "",
                modifier = Modifier.weight(1f),
                color = color,
                fontWeight = if (textStyle?.fontWeight != null) null else fontWeight,
                style = resolvedTextStyle,
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
        textStyle: TextStyle? = null,
    ): @Composable (StyledDropdown.RenderContext.DropdownItem<T>) -> Unit = { context ->
        val resolvedTextStyle = textStyle?.let(LocalStyledTextStyle.current::merge)
            ?: LocalStyledTextStyle.current
        StyledRow(
            modifier = Modifier.fillMaxWidth(),
            itemSpacing = StyledTheme.spacing.medium
        ) {
            leadingContent()
            StyledText(
                text = mapper(context.item),
                color = if (context.selected) colorSelected else color,
                modifier = Modifier.weight(1f),
                fontWeight = if (textStyle?.fontWeight != null) {
                    null
                } else if (context.selected) {
                    fontWeightSelected
                } else {
                    fontWeight
                },
                style = resolvedTextStyle,
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
internal fun <T : Any> StyledDropdown(
    items: List<T>,
    selected: MutableState<T>,
    modifier: Modifier = Modifier,
    title: String = "",
    enabled: Boolean = true,
    filter: StyledDropdown.Filter<T>? = null,
    variant: StyledButton.Variant = StyledButton.Variant.Auto,
    // defaults / customisation
    customization: StyledButton.Customization? = null,
    shape: Shape = StyledTheme.shapes.small,
    popupWidthConfig: StyledPopupMenu.WidthConfig = StyledPopupMenu.WidthConfig.MatchAnchor,
    popupHorizontalAlignment: StyledPopupMenu.HorizontalAlignment =
        StyledPopupMenu.HorizontalAlignment.Start,
    popupMaxHeight: Dp = StyledPopupMenuDefaults.maxHeight,
    expanded: MutableState<Boolean>? = null,
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
        variant = variant,
        customization = customization,
        shape = shape,
        popupWidthConfig = popupWidthConfig,
        popupHorizontalAlignment = popupHorizontalAlignment,
        popupMaxHeight = popupMaxHeight,
        expanded = expanded,
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
internal fun <T : Any> StyledDropdown(
    items: List<T>,
    selected: T,
    modifier: Modifier = Modifier,
    title: String = "",
    enabled: Boolean = true,
    filter: StyledDropdown.Filter<T>? = null,
    variant: StyledButton.Variant = StyledButton.Variant.Auto,
    // defaults / customisation
    customization: StyledButton.Customization? = null,
    shape: Shape = StyledTheme.shapes.small,
    popupWidthConfig: StyledPopupMenu.WidthConfig = StyledPopupMenu.WidthConfig.MatchAnchor,
    popupHorizontalAlignment: StyledPopupMenu.HorizontalAlignment =
        StyledPopupMenu.HorizontalAlignment.Start,
    popupMaxHeight: Dp = StyledPopupMenuDefaults.maxHeight,
    expanded: MutableState<Boolean>? = null,
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
        variant = variant,
        customization = customization,
        filter = filter,
        shape = shape,
        popupWidthConfig = popupWidthConfig,
        popupHorizontalAlignment = popupHorizontalAlignment,
        popupMaxHeight = popupMaxHeight,
        expanded = expanded,
        content = content,
        onSelectionChange = onSelectionChange
    )
}

/**
 * Updates [selectedIndex] before notifying [onSelectionChange].
 */
@Composable
internal fun <T : Any> StyledDropdownIndex(
    items: List<T>,
    selectedIndex: MutableState<Int>,
    modifier: Modifier = Modifier,
    title: String = "",
    enabled: Boolean = true,
    filter: StyledDropdown.Filter<T>? = null,
    variant: StyledButton.Variant = StyledButton.Variant.Auto,
    // defaults / customisation
    customization: StyledButton.Customization? = null,
    shape: Shape = StyledTheme.shapes.small,
    popupWidthConfig: StyledPopupMenu.WidthConfig = StyledPopupMenu.WidthConfig.MatchAnchor,
    popupHorizontalAlignment: StyledPopupMenu.HorizontalAlignment =
        StyledPopupMenu.HorizontalAlignment.Start,
    popupMaxHeight: Dp = StyledPopupMenuDefaults.maxHeight,
    expanded: MutableState<Boolean>? = null,
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
        variant = variant,
        customization = customization,
        filter = filter,
        shape = shape,
        popupWidthConfig = popupWidthConfig,
        popupHorizontalAlignment = popupHorizontalAlignment,
        popupMaxHeight = popupMaxHeight,
        expanded = expanded,
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
 * [content] receives `StyledDropdown.RenderContext.Selection` for the closed selection or
 * `StyledDropdown.RenderContext.DropdownItem` for each menu entry.
 * The container handles the title and layout. Customize text mapping through
 * [StyledDropdownDefaults.defaultRenderer], or render each context separately.
 */
@Composable
internal fun <T : Any> StyledDropdownIndex(
    items: List<T>,
    selectedIndex: Int,
    modifier: Modifier = Modifier,
    title: String = "",
    enabled: Boolean = true,
    filter: StyledDropdown.Filter<T>? = null,
    variant: StyledButton.Variant = StyledButton.Variant.Auto,
    // defaults / customisation
    customization: StyledButton.Customization? = null,
    shape: Shape = StyledTheme.shapes.small,
    popupWidthConfig: StyledPopupMenu.WidthConfig = StyledPopupMenu.WidthConfig.MatchAnchor,
    popupHorizontalAlignment: StyledPopupMenu.HorizontalAlignment =
        StyledPopupMenu.HorizontalAlignment.Start,
    popupMaxHeight: Dp = StyledPopupMenuDefaults.maxHeight,
    expanded: MutableState<Boolean>? = null,
    // content
    content: @Composable (context: StyledDropdown.RenderContext<T, T?>) -> Unit = StyledDropdownDefaults.defaultRenderer(),
    onSelectionChange: (index: Int, item: T) -> Unit,
) {
    StyledDropdownImpl(
        variant = variant,
        items = items,
        selectedIndex = selectedIndex,
        modifier = modifier,
        title = title,
        enabled = enabled,
        filter = filter,
        customization = customization,
        shape = shape,
        popupWidthConfig = popupWidthConfig,
        popupHorizontalAlignment = popupHorizontalAlignment,
        popupMaxHeight = popupMaxHeight,
        expanded = expanded,
        content = content,
        onSelectionChange = onSelectionChange,
    )
}

/**
 * Outlined-variant overload for [StyledDropdown] with state-backed selection.
 */
@Composable
internal fun <T : Any> StyledOutlinedDropdown(
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
        selected = selected,
        modifier = modifier,
        title = title,
        enabled = enabled,
        filter = filter,
        variant = StyledButton.Variant.Outlined,
        customization = customization,
        shape = shape,
        popupWidthConfig = popupWidthConfig,
        popupMaxHeight = popupMaxHeight,
        content = content,
        onSelectionChange = onSelectionChange,
    )
}

/**
 * Outlined-variant overload for [StyledDropdown] with controlled selection.
 */
@Composable
internal fun <T : Any> StyledOutlinedDropdown(
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
    StyledDropdown(
        items = items,
        selected = selected,
        modifier = modifier,
        title = title,
        enabled = enabled,
        filter = filter,
        variant = StyledButton.Variant.Outlined,
        customization = customization,
        shape = shape,
        popupWidthConfig = popupWidthConfig,
        popupMaxHeight = popupMaxHeight,
        content = content,
        onSelectionChange = onSelectionChange,
    )
}

/**
 * Outlined-variant overload for [StyledDropdownIndex] with state-backed selection.
 */
@Composable
internal fun <T : Any> StyledOutlinedDropdownIndex(
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
        selectedIndex = selectedIndex,
        modifier = modifier,
        title = title,
        enabled = enabled,
        filter = filter,
        variant = StyledButton.Variant.Outlined,
        customization = customization,
        shape = shape,
        popupWidthConfig = popupWidthConfig,
        popupMaxHeight = popupMaxHeight,
        content = content,
        onSelectionChange = onSelectionChange,
    )
}

/**
 * Outlined-variant overload for [StyledDropdownIndex] with controlled selection.
 */
@Composable
internal fun <T : Any> StyledOutlinedDropdownIndex(
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
    StyledDropdownIndex(
        items = items,
        selectedIndex = selectedIndex,
        modifier = modifier,
        title = title,
        enabled = enabled,
        filter = filter,
        variant = StyledButton.Variant.Outlined,
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
    variant: StyledButton.Variant,
    items: List<T>,
    selectedIndex: Int,
    modifier: Modifier,
    title: String,
    enabled: Boolean,
    filter: StyledDropdown.Filter<T>?,
    customization: StyledButton.Customization?,
    shape: Shape,
    popupWidthConfig: StyledPopupMenu.WidthConfig,
    popupHorizontalAlignment: StyledPopupMenu.HorizontalAlignment,
    popupMaxHeight: Dp,
    expanded: MutableState<Boolean>?,
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
        variant = variant,
        shape = shape,
        popupWidthConfig = popupWidthConfig,
        popupHorizontalAlignment = popupHorizontalAlignment,
        popupMaxHeight = popupMaxHeight,
        expanded = expanded,
        content = content,
        onItemClick = onSelectionChange,
    )
}