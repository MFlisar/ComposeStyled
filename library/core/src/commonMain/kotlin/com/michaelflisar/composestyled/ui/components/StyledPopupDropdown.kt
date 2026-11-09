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
import com.michaelflisar.composestyled.ui.internal.StyledDropdownDefaults
import com.michaelflisar.composestyled.ui.internal.StyledDropdownIndex

/** Content scope for the selected value and the options in a single-selection popup dropdown. */
@Stable
class StyledPopupDropdownScope<T : Any> internal constructor(
    private val context: StyledDropdown.RenderContext<T, T?>,
) {
    /** Renders the currently selected value in the input-like trigger. */
    @Composable
    fun selection(content: @Composable (T?) -> Unit) {
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

/** Namespace for popup-dropdown-specific configuration types. */
object StyledPopupDropdown {
    /** Selection-filter configuration for a popup dropdown. */
    class Filter<T : Any>(
        val label: String,
        val filter: (query: String, item: T) -> Boolean,
    )
}

object StyledPopupDropdownDefaults {
    @Composable
    fun <T : Any> defaultRenderer(): @Composable StyledPopupDropdownScope<T>.() -> Unit {
        return defaultRenderer(mapper = { it.toString() })
    }

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
    ): @Composable StyledPopupDropdownScope<T>.() -> Unit {
        val legacyRenderer = StyledDropdownDefaults.defaultRenderer(
            mapper = mapper,
            color = color,
            fontWeight = fontWeight,
            maxLines = maxLines,
            leadingContent = leadingContent,
            mapperDropdown = mapperDropdown,
            colorDropdown = colorDropdown,
            colorDropdownSelected = colorDropdownSelected,
            fontWeightDropdown = fontWeightDropdown,
            fontWeightDropdownSelected = fontWeightDropdownSelected,
            maxLinesDropdown = maxLinesDropdown,
            textStyle = textStyle,
            dropdownTextStyle = dropdownTextStyle,
        )
        return {
            selection { value ->
                legacyRenderer(StyledDropdown.RenderContext.Selection(value))
            }
            item { value, selected ->
                legacyRenderer(StyledDropdown.RenderContext.DropdownItem(value, selected))
            }
        }
    }
}

/**
 * Renders selected-value content through [StyledPopupDropdownScope.selection] and each option
 * through [StyledPopupDropdownScope.item]. Pass [expanded] to control the popup externally.
 */
@OptIn(InternalComposeStyledApi::class)
@Composable
fun <T : Any> StyledPopupDropdown(
    items: List<T>,
    selected: MutableState<T>,
    modifier: Modifier = Modifier,
    title: String = "",
    enabled: Boolean = true,
    filter: StyledPopupDropdown.Filter<T>? = null,
    variant: StyledButton.Variant = StyledButton.Variant.Auto,
    customization: StyledButton.Customization? = null,
    shape: Shape = StyledTheme.shapes.small,
    popupWidthConfig: StyledPopupMenu.WidthConfig = StyledPopupMenu.WidthConfig.MatchAnchor,
    popupHorizontalAlignment: StyledPopupMenu.HorizontalAlignment =
        StyledPopupMenu.HorizontalAlignment.Start,
    popupMaxHeight: Dp = StyledPopupMenuDefaults.maxHeight,
    content: @Composable StyledPopupDropdownScope<T>.() -> Unit =
        StyledPopupDropdownDefaults.defaultRenderer(),
    onSelectionChange: ((index: Int, item: T) -> Unit)? = null,
    expanded: MutableState<Boolean>? = null,
) {
    StyledDropdown(
        items = items,
        selected = selected,
        modifier = modifier,
        title = title,
        enabled = enabled,
        expanded = expanded,
        filter = filter?.toLegacyFilter(),
        variant = variant,
        customization = customization,
        shape = shape,
        popupWidthConfig = popupWidthConfig,
        popupHorizontalAlignment = popupHorizontalAlignment,
        popupMaxHeight = popupMaxHeight,
        content = { context -> content(StyledPopupDropdownScope(context)) },
        onSelectionChange = onSelectionChange,
    )
}

/** Controlled-value overload for [StyledPopupDropdown]. Pass [expanded] to control the popup externally. */
@OptIn(InternalComposeStyledApi::class)
@Composable
fun <T : Any> StyledPopupDropdown(
    items: List<T>,
    selected: T,
    onSelectionChange: (index: Int, item: T) -> Unit,
    modifier: Modifier = Modifier,
    title: String = "",
    enabled: Boolean = true,
    filter: StyledPopupDropdown.Filter<T>? = null,
    variant: StyledButton.Variant = StyledButton.Variant.Auto,
    customization: StyledButton.Customization? = null,
    shape: Shape = StyledTheme.shapes.small,
    popupWidthConfig: StyledPopupMenu.WidthConfig = StyledPopupMenu.WidthConfig.MatchAnchor,
    popupHorizontalAlignment: StyledPopupMenu.HorizontalAlignment =
        StyledPopupMenu.HorizontalAlignment.Start,
    popupMaxHeight: Dp = StyledPopupMenuDefaults.maxHeight,
    content: @Composable StyledPopupDropdownScope<T>.() -> Unit =
        StyledPopupDropdownDefaults.defaultRenderer(),
    expanded: MutableState<Boolean>? = null,
) {
    StyledDropdown(
        items = items,
        selected = selected,
        modifier = modifier,
        title = title,
        enabled = enabled,
        expanded = expanded,
        filter = filter?.toLegacyFilter(),
        variant = variant,
        customization = customization,
        shape = shape,
        popupWidthConfig = popupWidthConfig,
        popupHorizontalAlignment = popupHorizontalAlignment,
        popupMaxHeight = popupMaxHeight,
        content = { context -> content(StyledPopupDropdownScope(context)) },
        onSelectionChange = onSelectionChange,
    )
}

private fun <T : Any> StyledPopupDropdown.Filter<T>.toLegacyFilter() =
    StyledDropdown.Filter(label, filter)

/** Index-backed overload for lists containing equal values that must remain distinct. */
@OptIn(InternalComposeStyledApi::class)
@Composable
fun <T : Any> StyledPopupDropdownIndex(
    items: List<T>,
    selectedIndex: MutableState<Int>,
    modifier: Modifier = Modifier,
    title: String = "",
    enabled: Boolean = true,
    filter: StyledPopupDropdown.Filter<T>? = null,
    variant: StyledButton.Variant = StyledButton.Variant.Auto,
    customization: StyledButton.Customization? = null,
    shape: Shape = StyledTheme.shapes.small,
    popupWidthConfig: StyledPopupMenu.WidthConfig = StyledPopupMenu.WidthConfig.MatchAnchor,
    popupHorizontalAlignment: StyledPopupMenu.HorizontalAlignment =
        StyledPopupMenu.HorizontalAlignment.Start,
    popupMaxHeight: Dp = StyledPopupMenuDefaults.maxHeight,
    content: @Composable StyledPopupDropdownScope<T>.() -> Unit =
        StyledPopupDropdownDefaults.defaultRenderer(),
    onSelectionChange: ((index: Int, item: T) -> Unit)? = null,
) {
    StyledDropdownIndex(
        items = items,
        selectedIndex = selectedIndex,
        modifier = modifier,
        title = title,
        enabled = enabled,
        filter = filter?.toLegacyFilter(),
        variant = variant,
        customization = customization,
        shape = shape,
        popupWidthConfig = popupWidthConfig,
        popupHorizontalAlignment = popupHorizontalAlignment,
        popupMaxHeight = popupMaxHeight,
        content = { context -> content(StyledPopupDropdownScope(context)) },
        onSelectionChange = onSelectionChange,
    )
}

/** Controlled index-backed overload for [StyledPopupDropdownIndex]. */
@OptIn(InternalComposeStyledApi::class)
@Composable
fun <T : Any> StyledPopupDropdownIndex(
    items: List<T>,
    selectedIndex: Int,
    onSelectionChange: (index: Int, item: T) -> Unit,
    modifier: Modifier = Modifier,
    title: String = "",
    enabled: Boolean = true,
    filter: StyledPopupDropdown.Filter<T>? = null,
    variant: StyledButton.Variant = StyledButton.Variant.Auto,
    customization: StyledButton.Customization? = null,
    shape: Shape = StyledTheme.shapes.small,
    popupWidthConfig: StyledPopupMenu.WidthConfig = StyledPopupMenu.WidthConfig.MatchAnchor,
    popupHorizontalAlignment: StyledPopupMenu.HorizontalAlignment =
        StyledPopupMenu.HorizontalAlignment.Start,
    popupMaxHeight: Dp = StyledPopupMenuDefaults.maxHeight,
    content: @Composable StyledPopupDropdownScope<T>.() -> Unit =
        StyledPopupDropdownDefaults.defaultRenderer(),
) {
    StyledDropdownIndex(
        items = items,
        selectedIndex = selectedIndex,
        modifier = modifier,
        title = title,
        enabled = enabled,
        filter = filter?.toLegacyFilter(),
        variant = variant,
        customization = customization,
        shape = shape,
        popupWidthConfig = popupWidthConfig,
        popupHorizontalAlignment = popupHorizontalAlignment,
        popupMaxHeight = popupMaxHeight,
        content = { context -> content(StyledPopupDropdownScope(context)) },
        onSelectionChange = onSelectionChange,
    )
}
