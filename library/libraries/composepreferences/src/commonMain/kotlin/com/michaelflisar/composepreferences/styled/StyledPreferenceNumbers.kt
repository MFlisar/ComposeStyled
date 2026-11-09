package com.michaelflisar.composepreferences.styled

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composepreferences.styled.internal.BaseDialogPreference
import com.michaelflisar.composepreferences.styled.internal.PreferenceEntry
import com.michaelflisar.composepreferences.styled.layout.PreferenceStyledRow
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.classes.IconComposable
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.components.StyledInput
import com.michaelflisar.composestyled.ui.components.StyledNumberPicker
import com.michaelflisar.composestyled.ui.components.StyledSlider
import com.michaelflisar.composestyled.ui.layout.StyledDialog
import com.michaelflisar.composestyled.ui.primitives.StyledText
import com.michaelflisar.composestyled.ui.state.rememberDialogState
import com.michaelflisar.composestyled.utils.number.NumberParser
import com.michaelflisar.composestyled.utils.number.NumberUtil
import com.michaelflisar.composestyled.utils.number.rememberNumberParser
import com.michaelflisar.composestyled.utils.Validator
import com.michaelflisar.composestyled.utils.rememberValidator

/** Opens a numeric editor dialog from a preference row. */
@OptIn(InternalComposeStyledApi::class)
@Composable
fun <T : Number> StyledPreferenceInputNumber(
    value: MutableState<T>,
    title: String,
    modifier: Modifier = Modifier,
    key: String? = null,
    subtitle: String? = null,
    icon: IconComposable? = null,
    enabled: Boolean = true,
    filterTags: List<String> = emptyList(),
    isEnabled: @Composable () -> Boolean = { enabled },
    isVisible: @Composable () -> Boolean = { true },
    stepSize: T = NumberUtil.one(value.value),
    parser: NumberParser<T> = rememberNumberParser(value.value, stepSize),
    validator: Validator<T> = rememberValidator(value.value),
    inputVariant: StyledInput.Variant = StyledInput.Variant.Auto,
) {
    StyledPreferenceInputNumber(
        value = value.value,
        onValueChange = { value.value = it },
        title = title,
        modifier = modifier,
        key = key,
        subtitle = subtitle,
        icon = icon,
        enabled = enabled,
        filterTags = filterTags,
        isEnabled = isEnabled,
        isVisible = isVisible,
        stepSize = stepSize,
        parser = parser,
        validator = validator,
        inputVariant = inputVariant,
    )
}

/** Controlled numeric editor dialog launched from a preference row. */
@OptIn(InternalComposeStyledApi::class)
@Composable
fun <T : Number> StyledPreferenceInputNumber(
    value: T,
    onValueChange: (T) -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    key: String? = null,
    subtitle: String? = null,
    icon: IconComposable? = null,
    enabled: Boolean = true,
    filterTags: List<String> = emptyList(),
    isEnabled: @Composable () -> Boolean = { enabled },
    isVisible: @Composable () -> Boolean = { true },
    stepSize: T = NumberUtil.one(value),
    parser: NumberParser<T> = rememberNumberParser(value, stepSize),
    validator: Validator<T> = rememberValidator(value),
    inputVariant: StyledInput.Variant = StyledInput.Variant.Auto,
) {
    val dialog = rememberDialogState()
    var draft by rememberSaveable { mutableStateOf(value) }
    LaunchedEffect(value, dialog.visible) {
        if (dialog.visible) draft = value
    }
    val strings = LocalStyledPreferenceStrings.current
    BaseDialogPreference(
        data = draft,
        title = title,
        subtitle = subtitle ?: parser.format(value),
        displaySubtitle = subtitle,
        dialog = dialog,
        modifier = modifier,
        key = key,
        icon = icon,
        filterTags = filterTags,
        enabled = enabled,
        isEnabled = isEnabled,
        isVisible = isVisible,
        buttons = {
            StyledDialog.Buttons(
                positive = StyledDialog.Button(
                    onClick = { onValueChange(draft) },
                    label = strings.save,
                ),
                negative = StyledDialog.Button(
                    onClick = {},
                    label = strings.cancel,
                ),
            )
        },
        trailing = { StyledText(parser.format(value)) },
        onOpen = { draft = value },
    ) { data, _, padding, visuals ->
        Column(
            modifier = Modifier.fillMaxWidth().padding(padding),
        ) {
            StyledNumberPicker(
                value = data,
                onValueChange = { draft = it },
                validator = validator,
                parser = parser,
                enabled = visuals.enabled,
                variant = inputVariant,
            )
        }
    }
}

/** Numeric preference that edits inline with a slider or in a number-picker dialog. */
object StyledPreferenceNumber {
    /** Presentation used by [StyledPreferenceNumber]. */
    sealed interface Style {
        data object Picker : Style
        data object Slider : Style
    }
}

/** Preference whose value is edited using an inline slider or a number-picker dialog. */
@OptIn(InternalComposeStyledApi::class)
@Composable
fun <T> StyledPreferenceNumber(
    value: MutableState<T>,
    title: String,
    modifier: Modifier = Modifier,
    style: StyledPreferenceNumber.Style = StyledPreferenceNumber.Style.Picker,
    key: String? = null,
    subtitle: String? = null,
    icon: IconComposable? = null,
    enabled: Boolean = true,
    filterTags: List<String> = emptyList(),
    isEnabled: @Composable () -> Boolean = { enabled },
    isVisible: @Composable () -> Boolean = { true },
    valueRange: ClosedRange<T> = NumberUtil.range(
        NumberUtil.zero(value.value),
        NumberUtil.one(value.value)
    ),
    stepSize: T = NumberUtil.one(value.value),
    validator: Validator<T> = rememberValidator(value.value),
    parser: NumberParser<T> = rememberNumberParser(value.value, stepSize),
    tint: Color? = null,
    inputVariant: StyledInput.Variant = StyledInput.Variant.Auto,
) where T : Number, T : Comparable<T> {
    StyledPreferenceNumber(
        value = value.value,
        onValueChange = { value.value = it },
        title = title,
        modifier = modifier,
        style = style,
        key = key,
        subtitle = subtitle,
        icon = icon,
        enabled = enabled,
        filterTags = filterTags,
        isEnabled = isEnabled,
        isVisible = isVisible,
        valueRange = valueRange,
        stepSize = stepSize,
        validator = validator,
        parser = parser,
        tint = tint,
        inputVariant = inputVariant,
    )
}

/** Controlled [StyledPreferenceNumber] overload. */
@OptIn(InternalComposeStyledApi::class)
@Composable
fun <T> StyledPreferenceNumber(
    value: T,
    onValueChange: (T) -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    style: StyledPreferenceNumber.Style = StyledPreferenceNumber.Style.Picker,
    key: String? = null,
    subtitle: String? = null,
    icon: IconComposable? = null,
    enabled: Boolean = true,
    filterTags: List<String> = emptyList(),
    isEnabled: @Composable () -> Boolean = { enabled },
    isVisible: @Composable () -> Boolean = { true },
    valueRange: ClosedRange<T> = NumberUtil.range(NumberUtil.zero(value), NumberUtil.one(value)),
    stepSize: T = NumberUtil.one(value),
    validator: Validator<T> = rememberValidator(value),
    parser: NumberParser<T> = rememberNumberParser(value, stepSize),
    tint: Color? = null,
    inputVariant: StyledInput.Variant = StyledInput.Variant.Auto,
) where T : Number, T : Comparable<T> {
    val dialog = rememberDialogState()
    var draft by rememberSaveable { mutableStateOf(value) }
    LaunchedEffect(value, dialog.visible) {
        if (dialog.visible) draft = value
    }
    if (style == StyledPreferenceNumber.Style.Picker) {
        val strings = LocalStyledPreferenceStrings.current
        BaseDialogPreference(
            data = draft,
            title = title,
            subtitle = subtitle ?: parser.format(value),
            displaySubtitle = subtitle,
            dialog = dialog,
            modifier = modifier,
            key = key,
            icon = icon,
            filterTags = filterTags,
            enabled = enabled,
            isEnabled = isEnabled,
            isVisible = isVisible,
            buttons = {
                StyledDialog.Buttons(
                    positive = StyledDialog.Button(
                        onClick = { onValueChange(draft) },
                        label = strings.save,
                    ),
                    negative = StyledDialog.Button(
                        onClick = {},
                        label = strings.cancel,
                    ),
                )
            },
            trailing = { StyledText(parser.format(value)) },
            onOpen = { draft = value },
        ) { data, _, padding, visuals ->
            Column(
                modifier = Modifier.fillMaxWidth().padding(padding),
            ) {
                StyledNumberPicker(
                    value = data,
                    onValueChange = { draft = it },
                    validator = validator,
                    parser = parser,
                    enabled = visuals.enabled,
                    variant = inputVariant,
                )
            }
        }
    } else {
        PreferenceEntry(
            title = title,
            subtitle = subtitle ?: parser.format(value),
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
                style = visuals.style,
                insideSection = visuals.insideSection,
                breadcrumb = visuals.breadcrumb,
                inlineContent = {
                    StyledSlider(
                        value = value,
                        onValueChange = onValueChange,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = visuals.enabled,
                        valueRange = valueRange,
                        stepSize = stepSize,
                        activeColor = tint ?: StyledTheme.colorScheme.primary,
                    )
                },
                trailing = {
                    StyledText(parser.format(value))
                },
            )
        }
    }
}
