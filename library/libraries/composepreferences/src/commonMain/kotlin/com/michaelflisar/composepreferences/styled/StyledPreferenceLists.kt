package com.michaelflisar.composepreferences.styled

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.michaelflisar.composepreferences.styled.internal.BaseDialogPreference
import com.michaelflisar.composepreferences.styled.internal.LocalPreferenceStyle
import com.michaelflisar.composepreferences.styled.internal.PreferenceEntry
import com.michaelflisar.composepreferences.styled.layout.PreferenceStyledRow
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.classes.IconComposable
import com.michaelflisar.composestyled.classes.CustomColors
import com.michaelflisar.composestyled.tokens.LocalStyledTextStyle
import com.michaelflisar.composestyled.ui.components.StyledButton
import com.michaelflisar.composestyled.ui.components.StyledButtonDefaults
import com.michaelflisar.composestyled.ui.components.StyledCheckbox
import com.michaelflisar.composestyled.ui.components.StyledPopupDropdown
import com.michaelflisar.composestyled.ui.components.StyledPopupDropdownDefaults
import com.michaelflisar.composestyled.ui.components.StyledPopupDropdownMultiSelect
import com.michaelflisar.composestyled.ui.components.StyledPopupDropdownMultiSelectDefaults
import com.michaelflisar.composestyled.ui.components.StyledInput
import com.michaelflisar.composestyled.ui.components.StyledListItem
import com.michaelflisar.composestyled.ui.components.StyledPopupMenu
import com.michaelflisar.composestyled.ui.components.StyledRadioButton
import com.michaelflisar.composestyled.ui.layout.StyledColumn
import com.michaelflisar.composestyled.ui.layout.StyledDialog
import com.michaelflisar.composestyled.ui.primitives.StyledText
import com.michaelflisar.composestyled.ui.state.rememberDialogState
import com.michaelflisar.composestyled.utils.PointerInputBlocker

object StyledPreferenceList {
    sealed class Presentation {
        data object Dialog : Presentation()

        data class Dropdown(
            val clickBehavior: ClickBehavior = ClickBehavior.FullItem,
        ) : Presentation() {
            enum class ClickBehavior {
                /** The whole preference row opens the dropdown. */
                FullItem,

                /** Only the trailing dropdown control opens the dropdown. */
                DropdownOnly,
            }
        }
    }

    class Filter<T : Any>(
        val label: String? = null,
        val matches: ((query: String, item: T) -> Boolean)? = null,
    )
}

/** Single-selection preference presented in a dialog or dropdown. */
@Composable
fun <T : Any> StyledPreferenceList(
    items: List<T>,
    selected: MutableState<T>,
    title: String,
    modifier: Modifier = Modifier,
    key: String? = null,
    subtitle: String? = null,
    icon: IconComposable? = null,
    presentation: StyledPreferenceList.Presentation = StyledPreferenceList.Presentation.Dialog,
    filter: StyledPreferenceList.Filter<T>? = null,
    itemText: (T) -> String = { it.toString() },
    enabled: Boolean = true,
    filterTags: List<String> = emptyList(),
    isEnabled: @Composable () -> Boolean = { enabled },
    isVisible: @Composable () -> Boolean = { true },
    dropdownCustomization: StyledButton.Customization? = null,
    shape: Shape? = null,
) {
    StyledPreferenceList(
        items = items,
        selected = selected.value,
        onValueChange = { selected.value = it },
        title = title,
        modifier = modifier,
        key = key,
        subtitle = subtitle,
        icon = icon,
        presentation = presentation,
        filter = filter,
        itemText = itemText,
        enabled = enabled,
        filterTags = filterTags,
        isEnabled = isEnabled,
        isVisible = isVisible,
        dropdownCustomization = dropdownCustomization,
        shape = shape,
    )
}

/** Controlled single-selection preference. */
@Composable
fun <T : Any> StyledPreferenceList(
    items: List<T>,
    selected: T,
    onValueChange: (T) -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    key: String? = null,
    subtitle: String? = null,
    icon: IconComposable? = null,
    presentation: StyledPreferenceList.Presentation = StyledPreferenceList.Presentation.Dialog,
    filter: StyledPreferenceList.Filter<T>? = null,
    itemText: (T) -> String = { it.toString() },
    enabled: Boolean = true,
    filterTags: List<String> = emptyList(),
    isEnabled: @Composable () -> Boolean = { enabled },
    isVisible: @Composable () -> Boolean = { true },
    dropdownCustomization: StyledButton.Customization? = null,
    shape: Shape? = null,
) {
    val dialog = rememberDialogState()
    val strings = LocalStyledPreferenceStrings.current
    var query by rememberSaveable { mutableStateOf("") }
    val dropdownExpanded = remember { mutableStateOf(false) }
    val effectiveShape = shape ?: checkNotNull(LocalPreferenceStyle.current).item.shape

    when (presentation) {
        is StyledPreferenceList.Presentation.Dropdown -> PreferenceEntry(
            title = title,
            subtitle = subtitle ?: itemText(selected),
            filterTags = filterTags,
            key = key,
            isEnabled = isEnabled,
            isVisible = isVisible,
        ) { visuals ->
            PreferenceStyledRow(
                title = title,
                subtitle = subtitle,
                icon = icon,
                modifier = modifier,
                onClick =
                    if (presentation.clickBehavior ==
                        StyledPreferenceList.Presentation.Dropdown.ClickBehavior.FullItem
                    ) {
                        { dropdownExpanded.value = true }
                    } else {
                        null
                    },
                enabled = visuals.enabled,
                style = visuals.style,
                insideSection = visuals.insideSection,
                breadcrumb = visuals.breadcrumb,
                trailing = {
                    val textStyle = LocalStyledTextStyle.current
                    val dropdownTextStyle = textStyle.copy(textAlign = TextAlign.Start)
                    val blockDropdownInteraction = presentation.clickBehavior == StyledPreferenceList.Presentation.Dropdown.ClickBehavior.FullItem
                    PointerInputBlocker(blocked = blockDropdownInteraction) {
                        StyledPopupDropdown(
                            items = items,
                            selected = selected,
                            title = "",
                            enabled = visuals.enabled,
                            expanded = dropdownExpanded,
                            filter = filter?.toStyledFilter(itemText, strings.filterOptions),
                            variant = StyledButton.Variant.Plain,
                            customization = dropdownCustomization
                                ?: visuals.style.toDropdownCustomization(),
                            shape = effectiveShape,
                            content = StyledPopupDropdownDefaults.defaultRenderer(
                                mapper = { item -> itemText(item) },
                                textStyle = textStyle,
                                dropdownTextStyle = dropdownTextStyle,
                            ),
                            onSelectionChange = { _, item -> onValueChange(item) },
                            popupWidthConfig = StyledPopupMenu.WidthConfig.WrapContent,
                            popupHorizontalAlignment = StyledPopupMenu.HorizontalAlignment.End,
                        )
                    }
                },
            )
        }

        StyledPreferenceList.Presentation.Dialog -> BaseDialogPreference(
            data = items,
            title = title,
            subtitle = subtitle ?: itemText(selected),
            displaySubtitle = subtitle,
            dialog = dialog,
            modifier = modifier,
            key = key,
            icon = icon,
            filterTags = filterTags,
            enabled = enabled,
            isEnabled = isEnabled,
            isVisible = isVisible,
            trailing = { StyledText(itemText(selected)) },
            onOpen = { query = "" },
        ) { dialogItems, dialogState, padding, visuals ->
            StyledColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(padding),
                itemSpacing = StyledTheme.spacing.medium
            ) {
                if (filter != null) {
                    StyledInput(
                        value = query,
                        onValueChange = { query = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { StyledText(filter.label ?: strings.filterOptions) },
                    )
                }
                StyledColumn(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    itemSpacing = StyledTheme.spacing.medium
                ) {
                    dialogItems.filter { filter?.test(query, it, itemText) != false }
                        .forEach { item ->
                            StyledListItem(
                                headline = { StyledText(itemText(item)) },
                                onClick = {
                                    onValueChange(item)
                                    dialogState.dismiss()
                                },
                                actions = {
                                    StyledRadioButton(
                                        selected = selected == item,
                                        onClick = null,
                                        enabled = visuals.enabled,
                                    )
                                },
                                shape = effectiveShape,
                                enabled = visuals.enabled,
                                minHeight = visuals.style.item.minimumItemHeight,
                                contentPadding = visuals.style.item.contentPadding,
                                containerColor = visuals.style.item.colors.containerColor,
                                contentColor = visuals.style.item.colors.contentColor,
                                supportingColor = visuals.style.item.colors.supportingColor,
                            )
                        }
                }
            }
        }
    }
}

/** Multi-selection preference presented in a dialog or dropdown. */
@Composable
fun <T : Any> StyledPreferenceListMultiSelect(
    items: List<T>,
    selected: MutableState<List<T>>,
    title: String,
    modifier: Modifier = Modifier,
    key: String? = null,
    subtitle: String? = null,
    icon: IconComposable? = null,
    presentation: StyledPreferenceList.Presentation = StyledPreferenceList.Presentation.Dialog,
    filter: StyledPreferenceList.Filter<T>? = null,
    itemText: (T) -> String = { it.toString() },
    enabled: Boolean = true,
    filterTags: List<String> = emptyList(),
    isEnabled: @Composable () -> Boolean = { enabled },
    isVisible: @Composable () -> Boolean = { true },
    dropdownCustomization: StyledButton.Customization? = null,
    shape: Shape? = null,
) {
    StyledPreferenceListMultiSelect(
        items = items,
        selected = selected.value,
        onValueChange = { selected.value = it },
        title = title,
        modifier = modifier,
        key = key,
        subtitle = subtitle,
        icon = icon,
        presentation = presentation,
        filter = filter,
        itemText = itemText,
        enabled = enabled,
        filterTags = filterTags,
        isEnabled = isEnabled,
        isVisible = isVisible,
        dropdownCustomization = dropdownCustomization,
        shape = shape,
    )
}

/** Controlled multi-selection preference. */
@Composable
fun <T : Any> StyledPreferenceListMultiSelect(
    items: List<T>,
    selected: List<T>,
    onValueChange: (List<T>) -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    key: String? = null,
    subtitle: String? = null,
    icon: IconComposable? = null,
    presentation: StyledPreferenceList.Presentation = StyledPreferenceList.Presentation.Dialog,
    filter: StyledPreferenceList.Filter<T>? = null,
    itemText: (T) -> String = { it.toString() },
    enabled: Boolean = true,
    filterTags: List<String> = emptyList(),
    isEnabled: @Composable () -> Boolean = { enabled },
    isVisible: @Composable () -> Boolean = { true },
    dropdownCustomization: StyledButton.Customization? = null,
    shape: Shape? = null,
) {
    val dialog = rememberDialogState()
    val strings = LocalStyledPreferenceStrings.current
    var draft by remember(selected) { mutableStateOf(selected) }
    var query by rememberSaveable { mutableStateOf("") }
    val dropdownExpanded = remember { mutableStateOf(false) }
    LaunchedEffect(dialog.visible, selected) {
        if (dialog.visible) draft = selected
    }
    val effectiveShape = shape ?: checkNotNull(LocalPreferenceStyle.current).item.shape
    val selectedText = selected.joinToString(", ", transform = itemText)

    when (presentation) {
        is StyledPreferenceList.Presentation.Dropdown -> PreferenceEntry(
            title = title,
            subtitle = subtitle ?: selectedText,
            filterTags = filterTags,
            key = key,
            isEnabled = isEnabled,
            isVisible = isVisible,
        ) { visuals ->
            PreferenceStyledRow(
                title = title,
                subtitle = subtitle,
                icon = icon,
                enabled = visuals.enabled,
                modifier = modifier,
                onClick =
                    if (presentation.clickBehavior ==
                        StyledPreferenceList.Presentation.Dropdown.ClickBehavior.FullItem
                    ) {
                        { dropdownExpanded.value = true }
                    } else {
                        null
                    },
                style = visuals.style,
                insideSection = visuals.insideSection,
                breadcrumb = visuals.breadcrumb,
                trailing = {
                    val textStyle = LocalStyledTextStyle.current
                    val dropdownTextStyle = textStyle.copy(textAlign = TextAlign.Start)
                    val blockDropdownInteraction = presentation.clickBehavior ==
                        StyledPreferenceList.Presentation.Dropdown.ClickBehavior.FullItem
                    PointerInputBlocker(blocked = blockDropdownInteraction) {
                        StyledPopupDropdownMultiSelect(
                            items = items,
                            selected = selected,
                            title = "",
                            modifier = Modifier,
                            enabled = visuals.enabled,
                            expanded = dropdownExpanded,
                            filter = filter?.toStyledFilter(itemText, strings.filterOptions),
                            variant = StyledButton.Variant.Plain,
                            customization = dropdownCustomization
                                ?: visuals.style.toDropdownCustomization(),
                            shape = effectiveShape,
                            content = StyledPopupDropdownMultiSelectDefaults.defaultRenderer(
                                mapper = { item -> itemText(item) },
                                formatter = { it.joinToString(", ") },
                                textStyle = textStyle,
                                dropdownTextStyle = dropdownTextStyle,
                            ),
                            actions = StyledPopupDropdownMultiSelectDefaults.defaultActions(),
                            onSelectionChange = { _, next -> onValueChange(next) },
                            popupWidthConfig = StyledPopupMenu.WidthConfig.WrapContent,
                            popupHorizontalAlignment = StyledPopupMenu.HorizontalAlignment.End,
                        )
                    }
                },
            )
        }

        StyledPreferenceList.Presentation.Dialog -> BaseDialogPreference(
            data = items,
            title = title,
            subtitle = subtitle ?: selectedText,
            displaySubtitle = subtitle,
            dialog = dialog,
            modifier = modifier,
            key = key,
            icon = icon,
            filterTags = filterTags,
            enabled = enabled,
            isEnabled = isEnabled,
            isVisible = isVisible,
            buttons = { visuals ->
                StyledDialog.Buttons(
                    positive = StyledDialog.Button(
                        onClick = { onValueChange(draft) },
                        label = strings.apply,
                        enabled = visuals.enabled,
                    ),
                    negative = StyledDialog.Button(
                        onClick = {},
                        label = strings.cancel,
                        enabled = visuals.enabled,
                    ),
                )
            },
            trailing = { StyledText(selectedText) },
            onOpen = {
                draft = selected
                query = ""
            },
        ) { dialogItems, _, padding, visuals ->
            StyledColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(padding),
                itemSpacing = StyledTheme.spacing.medium
            ) {
                if (filter != null) {
                    StyledInput(
                        value = query,
                        onValueChange = { query = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { StyledText(filter.label ?: strings.filterOptions) },
                    )
                }
                StyledColumn(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    itemSpacing = StyledTheme.spacing.medium
                ) {
                    dialogItems.filter { filter?.test(query, it, itemText) != false }
                        .forEach { item ->
                            val checked = item in draft
                            StyledListItem(
                                headline = { StyledText(itemText(item)) },
                                onClick = {
                                    draft = if (checked) {
                                        draft.filterNot { it == item }
                                    } else {
                                        draft + item
                                    }
                                },
                                enabled = visuals.enabled,
                                actions = {
                                    StyledCheckbox(
                                        checked = checked,
                                        onCheckedChange = null,
                                        enabled = visuals.enabled,
                                    )
                                },
                                shape = effectiveShape,
                                minHeight = visuals.style.item.minimumItemHeight,
                                contentPadding = visuals.style.item.contentPadding,
                                containerColor = visuals.style.item.colors.containerColor,
                                contentColor = visuals.style.item.colors.contentColor,
                                supportingColor = visuals.style.item.colors.supportingColor,
                            )
                        }
                }
            }
        }
    }
}

private fun <T : Any> StyledPreferenceList.Filter<T>.toStyledFilter(
    itemText: (T) -> String,
    label: String,
): StyledPopupDropdown.Filter<T> = StyledPopupDropdown.Filter(this.label ?: label) { query, item ->
    test(query, item, itemText)
}

private fun <T : Any> StyledPreferenceList.Filter<T>.test(
    query: String,
    item: T,
    itemText: (T) -> String,
): Boolean = matches?.invoke(query, item) ?: itemText(item).contains(query, ignoreCase = true)

private fun PreferenceStyle.toDropdownCustomization(): StyledButton.Customization {
    val contentColor = trailingStyle.contentColor ?: item.colors.contentColor
    return StyledButtonDefaults.customize(
        CustomColors(
            filledBackground = item.colors.containerColor,
            filledForeground = contentColor,
            textForeground = contentColor,
        ),
    )
}
