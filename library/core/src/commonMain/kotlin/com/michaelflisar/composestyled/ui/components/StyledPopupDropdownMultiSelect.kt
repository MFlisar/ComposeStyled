package com.michaelflisar.composestyled.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.internal.StyledDropdown
import com.michaelflisar.composestyled.ui.internal.StyledDropdownMultiSelect
import com.michaelflisar.composestyled.ui.internal.StyledDropdownMultiSelectDefaults
import com.michaelflisar.composestyled.ui.internal.StyledDropdownMultiSelectIndex

/** Namespace for multi-selection popup-dropdown types. */
object StyledPopupDropdownMultiSelect {
    /** State and callbacks for the standard select-all and clear-selection actions. */
    class Actions internal constructor(
        internal val legacy: StyledDropdownMultiSelect.Actions,
    ) {
        val canSelectAll: Boolean get() = legacy.canSelectAll
        val canClear: Boolean get() = legacy.canClear
        fun selectAll() = legacy.selectAll()
        fun clearSelection() = legacy.clearSelection()
    }
}

/** Content scope for the selected values and options in a multi-selection popup dropdown. */
@Stable
class StyledPopupDropdownMultiSelectScope<T : Any> internal constructor(
    private val context: StyledDropdown.RenderContext<T, List<T>>,
) {
    /** Renders the currently selected values in the input-like trigger. */
    @Composable
    fun selection(content: @Composable (List<T>) -> Unit) {
        if (context is StyledDropdown.RenderContext.Selection) {
            content(context.selection)
        }
    }

    /** Renders one option row. The option's selection state is provided to [content]. */
    @Composable
    fun item(content: @Composable (item: T, selected: Boolean) -> Unit) {
        if (context is StyledDropdown.RenderContext.DropdownItem) {
            content(context.item, context.selected)
        }
    }
}

object StyledPopupDropdownMultiSelectDefaults {
    @Composable
    fun <T : Any> defaultRenderer(): @Composable StyledPopupDropdownMultiSelectScope<T>.() -> Unit {
        return defaultRenderer(mapper = { it.toString() })
    }

    @Composable
    fun <T : Any> defaultRenderer(
        mapper: @Composable (item: T) -> String = { it.toString() },
        formatter: @Composable (selected: List<String>) -> String =
            StyledDropdownMultiSelectDefaults.formatter,
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
        textStyle: TextStyle? = null,
        dropdownTextStyle: TextStyle? = textStyle,
    ): @Composable StyledPopupDropdownMultiSelectScope<T>.() -> Unit {
        val renderer = StyledDropdownMultiSelectDefaults.defaultRenderer(
            mapper = mapper,
            formatter = formatter,
            color = color,
            fontWeight = fontWeight,
            maxLines = maxLines,
            leadingContent = leadingContent,
            dropdownMapper = dropdownMapper,
            colorDropdown = colorDropdown,
            colorDropdownSelected = colorDropdownSelected,
            fontWeightDropdown = fontWeightDropdown,
            fontWeightDropdownSelected = fontWeightDropdownSelected,
            maxLinesDropdown = maxLinesDropdown,
            textStyle = textStyle,
            dropdownTextStyle = dropdownTextStyle,
        )
        return {
            selection { selected ->
                renderer(StyledDropdown.RenderContext.Selection(selected))
            }
            item { value, selected ->
                renderer(StyledDropdown.RenderContext.DropdownItem(value, selected))
            }
        }
    }

    @Composable
    fun defaultActions(
        selectAllLabel: String = "Select all",
        clearSelectionLabel: String = "Clear selection",
    ): @Composable (StyledPopupDropdownMultiSelect.Actions) -> Unit {
        val actions = StyledDropdownMultiSelectDefaults.defaultActions(
            selectAllLabel = selectAllLabel,
            clearSelectionLabel = clearSelectionLabel,
        )
        return { scopedActions ->
            actions(scopedActions.legacy)
        }
    }

    val sortSelectionByItems: Boolean
        get() = StyledDropdownMultiSelectDefaults.sortSelectionByItems

    @Composable
    fun shape(): Shape = StyledDropdownMultiSelectDefaults.shape()
}

/**
 * Multi-select popup dropdown whose menu rows are rendered through
 * [StyledPopupDropdownMultiSelectScope]. Pass [expanded] to control the popup externally.
 */
@OptIn(InternalComposeStyledApi::class)
@Composable
fun <T : Any> StyledPopupDropdownMultiSelect(
    items: List<T>,
    selected: MutableState<List<T>>,
    modifier: Modifier = Modifier,
    title: String = "",
    enabled: Boolean = true,
    variant: StyledButton.Variant = StyledButton.Variant.Auto,
    customization: StyledButton.Customization? = null,
    filter: StyledPopupDropdown.Filter<T>? = null,
    shape: Shape = StyledPopupDropdownMultiSelectDefaults.shape(),
    popupWidthConfig: StyledPopupMenu.WidthConfig = StyledPopupMenu.WidthConfig.MatchAnchor,
    popupHorizontalAlignment: StyledPopupMenu.HorizontalAlignment =
        StyledPopupMenu.HorizontalAlignment.Start,
    sortSelectionByItems: Boolean = StyledPopupDropdownMultiSelectDefaults.sortSelectionByItems,
    content: @Composable StyledPopupDropdownMultiSelectScope<T>.() -> Unit =
        StyledPopupDropdownMultiSelectDefaults.defaultRenderer(),
    popupMaxHeight: Dp = StyledPopupMenuDefaults.maxHeight,
    actions: @Composable (StyledPopupDropdownMultiSelect.Actions) -> Unit =
        StyledPopupDropdownMultiSelectDefaults.defaultActions(),
    onSelectionChange: ((indices: List<Int>, items: List<T>) -> Unit)? = null,
    expanded: MutableState<Boolean>? = null,
) {
    StyledDropdownMultiSelect(
        items = items,
        selected = selected,
        modifier = modifier,
        title = title,
        enabled = enabled,
        expanded = expanded,
        variant = variant,
        customization = customization,
        filter = filter?.toLegacyFilter(),
        shape = shape,
        popupWidthConfig = popupWidthConfig,
        popupHorizontalAlignment = popupHorizontalAlignment,
        sortSelectionByItems = sortSelectionByItems,
        content = { context -> content(StyledPopupDropdownMultiSelectScope(context)) },
        popupMaxHeight = popupMaxHeight,
        actions = { legacy -> actions(StyledPopupDropdownMultiSelect.Actions(legacy)) },
        onSelectionChange = onSelectionChange,
    )
}

/**
 * Controlled-value overload for [StyledPopupDropdownMultiSelect]. Pass [expanded] to control the
 * popup externally.
 */
@OptIn(InternalComposeStyledApi::class)
@Composable
fun <T : Any> StyledPopupDropdownMultiSelect(
    items: List<T>,
    selected: List<T>,
    onSelectionChange: (indices: List<Int>, items: List<T>) -> Unit,
    modifier: Modifier = Modifier,
    title: String = "",
    enabled: Boolean = true,
    variant: StyledButton.Variant = StyledButton.Variant.Auto,
    customization: StyledButton.Customization? = null,
    filter: StyledPopupDropdown.Filter<T>? = null,
    shape: Shape = StyledPopupDropdownMultiSelectDefaults.shape(),
    popupWidthConfig: StyledPopupMenu.WidthConfig = StyledPopupMenu.WidthConfig.MatchAnchor,
    popupHorizontalAlignment: StyledPopupMenu.HorizontalAlignment =
        StyledPopupMenu.HorizontalAlignment.Start,
    sortSelectionByItems: Boolean = StyledPopupDropdownMultiSelectDefaults.sortSelectionByItems,
    content: @Composable StyledPopupDropdownMultiSelectScope<T>.() -> Unit =
        StyledPopupDropdownMultiSelectDefaults.defaultRenderer(),
    popupMaxHeight: Dp = StyledPopupMenuDefaults.maxHeight,
    actions: @Composable (StyledPopupDropdownMultiSelect.Actions) -> Unit =
        StyledPopupDropdownMultiSelectDefaults.defaultActions(),
    expanded: MutableState<Boolean>? = null,
) {
    StyledDropdownMultiSelect(
        items = items,
        selected = selected,
        modifier = modifier,
        title = title,
        enabled = enabled,
        expanded = expanded,
        variant = variant,
        customization = customization,
        filter = filter?.toLegacyFilter(),
        shape = shape,
        popupWidthConfig = popupWidthConfig,
        popupHorizontalAlignment = popupHorizontalAlignment,
        sortSelectionByItems = sortSelectionByItems,
        content = { context -> content(StyledPopupDropdownMultiSelectScope(context)) },
        popupMaxHeight = popupMaxHeight,
        actions = { legacy -> actions(StyledPopupDropdownMultiSelect.Actions(legacy)) },
        onSelectionChange = onSelectionChange,
    )
}

private fun <T : Any> StyledPopupDropdown.Filter<T>.toLegacyFilter() =
    StyledDropdown.Filter(label, filter)

/** Index-backed overload for multi-select lists that contain equal values. */
@OptIn(InternalComposeStyledApi::class)
@Composable
fun <T : Any> StyledPopupDropdownMultiSelectIndex(
    items: List<T>,
    selectedIndices: MutableState<List<Int>>,
    modifier: Modifier = Modifier,
    title: String = "",
    enabled: Boolean = true,
    variant: StyledButton.Variant = StyledButton.Variant.Auto,
    customization: StyledButton.Customization? = null,
    filter: StyledPopupDropdown.Filter<T>? = null,
    shape: Shape = StyledPopupDropdownMultiSelectDefaults.shape(),
    popupWidthConfig: StyledPopupMenu.WidthConfig = StyledPopupMenu.WidthConfig.MatchAnchor,
    popupHorizontalAlignment: StyledPopupMenu.HorizontalAlignment =
        StyledPopupMenu.HorizontalAlignment.Start,
    sortSelectionByItems: Boolean = StyledPopupDropdownMultiSelectDefaults.sortSelectionByItems,
    content: @Composable StyledPopupDropdownMultiSelectScope<T>.() -> Unit =
        StyledPopupDropdownMultiSelectDefaults.defaultRenderer(),
    popupMaxHeight: Dp = StyledPopupMenuDefaults.maxHeight,
    actions: @Composable (StyledPopupDropdownMultiSelect.Actions) -> Unit =
        StyledPopupDropdownMultiSelectDefaults.defaultActions(),
    onSelectionChange: ((indices: List<Int>, items: List<T>) -> Unit)? = null,
) {
    StyledDropdownMultiSelectIndex(
        items = items,
        selectedIndices = selectedIndices,
        modifier = modifier,
        title = title,
        enabled = enabled,
        variant = variant,
        customization = customization,
        filter = filter?.toLegacyFilter(),
        shape = shape,
        popupWidthConfig = popupWidthConfig,
        popupHorizontalAlignment = popupHorizontalAlignment,
        sortSelectionByItems = sortSelectionByItems,
        content = { context -> content(StyledPopupDropdownMultiSelectScope(context)) },
        popupMaxHeight = popupMaxHeight,
        actions = { legacy -> actions(StyledPopupDropdownMultiSelect.Actions(legacy)) },
        onSelectionChange = onSelectionChange,
    )
}

/** Controlled index-backed overload for [StyledPopupDropdownMultiSelectIndex]. */
@OptIn(InternalComposeStyledApi::class)
@Composable
fun <T : Any> StyledPopupDropdownMultiSelectIndex(
    items: List<T>,
    selectedIndices: List<Int>,
    onSelectionChange: (indices: List<Int>, items: List<T>) -> Unit,
    modifier: Modifier = Modifier,
    title: String = "",
    enabled: Boolean = true,
    variant: StyledButton.Variant = StyledButton.Variant.Auto,
    customization: StyledButton.Customization? = null,
    filter: StyledPopupDropdown.Filter<T>? = null,
    shape: Shape = StyledPopupDropdownMultiSelectDefaults.shape(),
    popupWidthConfig: StyledPopupMenu.WidthConfig = StyledPopupMenu.WidthConfig.MatchAnchor,
    popupHorizontalAlignment: StyledPopupMenu.HorizontalAlignment =
        StyledPopupMenu.HorizontalAlignment.Start,
    sortSelectionByItems: Boolean = StyledPopupDropdownMultiSelectDefaults.sortSelectionByItems,
    content: @Composable StyledPopupDropdownMultiSelectScope<T>.() -> Unit =
        StyledPopupDropdownMultiSelectDefaults.defaultRenderer(),
    popupMaxHeight: Dp = StyledPopupMenuDefaults.maxHeight,
    actions: @Composable (StyledPopupDropdownMultiSelect.Actions) -> Unit =
        StyledPopupDropdownMultiSelectDefaults.defaultActions(),
) {
    StyledDropdownMultiSelectIndex(
        items = items,
        selectedIndices = selectedIndices,
        modifier = modifier,
        title = title,
        enabled = enabled,
        variant = variant,
        customization = customization,
        filter = filter?.toLegacyFilter(),
        shape = shape,
        popupWidthConfig = popupWidthConfig,
        popupHorizontalAlignment = popupHorizontalAlignment,
        sortSelectionByItems = sortSelectionByItems,
        content = { context -> content(StyledPopupDropdownMultiSelectScope(context)) },
        popupMaxHeight = popupMaxHeight,
        actions = { legacy -> actions(StyledPopupDropdownMultiSelect.Actions(legacy)) },
        onSelectionChange = onSelectionChange,
    )
}
