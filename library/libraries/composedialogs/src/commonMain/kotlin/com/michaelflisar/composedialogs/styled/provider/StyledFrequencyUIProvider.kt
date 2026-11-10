package com.michaelflisar.composedialogs.styled.provider

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.dialogs.frequency.FrequencyTextStyle
import com.michaelflisar.composedialogs.dialogs.frequency.FrequencyUIProvider
import com.michaelflisar.composedialogs.dialogs.frequency.classes.Frequency
import com.michaelflisar.composedialogs.dialogs.time.TimePickerSetup
import com.michaelflisar.composedialogs.styled.StyledDialogTime
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.tokens.LocalStyledTextStyle
import com.michaelflisar.composestyled.ui.components.StyledButton
import com.michaelflisar.composestyled.ui.components.StyledInput
import com.michaelflisar.composestyled.ui.components.StyledPopupDropdown
import com.michaelflisar.composestyled.ui.components.StyledTextButton
import com.michaelflisar.composestyled.ui.components.StyledIcon
import com.michaelflisar.composestyled.ui.icons.StyledIcons
import com.michaelflisar.composestyled.ui.primitives.StyledText
import com.michaelflisar.composestyled.ui.state.rememberDialogState
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month

internal object StyledFrequencyUIProvider : FrequencyUIProvider {

    @Composable
    override fun LabeledField(
        label: String,
        modifier: Modifier,
        errorMessage: String?,
        content: @Composable () -> Unit,
    ) {
        StyledLabeledField(
            label = label,
            modifier = modifier,
            errorMessage = errorMessage,
            content = content,
        )
    }

    @Composable
    override fun Text(
        text: String,
        modifier: Modifier,
        style: FrequencyTextStyle,
        color: Color?,
        textAlign: TextAlign?,
        minLines: Int,
        maxLines: Int,
    ) {
        val currentStyle = LocalStyledTextStyle.current
        val textStyle = when (style) {
            FrequencyTextStyle.Current -> currentStyle
            FrequencyTextStyle.CurrentSmall -> currentStyle.copy(
                fontSize = currentStyle.fontSize * 0.8f,
            )
            FrequencyTextStyle.TitleSmall -> StyledTheme.typography.titleSmall
        }
        StyledText(
            text = text,
            modifier = modifier,
            style = textStyle,
            color = color ?: Color.Unspecified,
            textAlign = textAlign,
            minLines = minLines,
            maxLines = maxLines,
        )
    }

    @Composable
    override fun NumberInput(
        modifier: Modifier,
        value: String,
        onValueChange: (String) -> Unit,
        title: String,
        keyboardType: KeyboardType,
        readOnly: Boolean,
    ) {
        StyledInput(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier
                .heightIn(min = 48.dp)
                .semantics { contentDescription = title },
            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                keyboardType = keyboardType,
            ),
            readOnly = readOnly,
            variant = StyledInput.Variant.Outlined,
        )
    }

    @Composable
    override fun IntervalDropdown(
        modifier: Modifier,
        title: String,
        min: Int,
        max: Int,
        selected: Int,
        onItemSelected: (Int) -> Unit,
    ) {
        val items = remember(min, max) { (min..max).toList() }
        textDropdown(
            modifier = modifier,
            title = title,
            items = items,
            selected = selected,
            itemText = { it.toString() },
            selectedTextStyle = StyledTheme.typography.bodyLarge,
            itemTextStyle = StyledTheme.typography.bodyLarge,
            onItemSelected = onItemSelected,
        )
    }

    @Composable
    override fun InputButton(
        modifier: Modifier,
        title: String,
        value: String,
        onClick: () -> Unit,
    ) {
        ProvideStyledDialogActionContext {
            StyledTextButton(
                onClick = onClick,
                modifier = modifier
                    .heightIn(min = 48.dp)
                    .semantics { contentDescription = "$title: $value" },
            ) {
                StyledText(
                    text = value,
                    modifier = Modifier.weight(1f),
                    style = StyledTheme.typography.bodyLarge,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                )
                StyledIcon(
                    icon = StyledIcons.Dropdown,
                    contentDescription = null,
                    tint = StyledTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }

    @Composable
    override fun TimeInput(
        selectedTime: LocalTime,
        onTimeSelected: (LocalTime) -> Unit,
        title: String,
        setup: TimePickerSetup,
        modifier: Modifier,
    ) {
        val currentSelectedTime = rememberUpdatedState(selectedTime)
        val currentOnTimeSelected = rememberUpdatedState(onTimeSelected)
        val selectedTimeState = remember {
            // Bridge the value/callback API to StyledDialogTime's MutableState API.
            object : MutableState<LocalTime> {
                override var value: LocalTime
                    get() = currentSelectedTime.value
                    set(value) {
                        currentOnTimeSelected.value(value)
                    }

                override fun component1(): LocalTime = value

                override fun component2(): (LocalTime) -> Unit = { value = it }
            }
        }
        val dialogState = rememberDialogState()
        InputButton(
            modifier = modifier.fillMaxWidth(),
            title = title,
            value = selectedTime.toString(),
            onClick = dialogState::show,
        )
        StyledDialogTime(
            state = dialogState,
            time = selectedTimeState,
            setup = setup,
            title = { StyledText(title) },
        )
    }

    @Composable
    override fun DayOfWeekDropdown(
        modifier: Modifier,
        title: String,
        items: List<DayOfWeek>,
        selected: DayOfWeek,
        itemText: (DayOfWeek) -> String,
        onItemSelected: (DayOfWeek) -> Unit,
    ) {
        frequencyDropdown(modifier, title, items, selected, itemText, onItemSelected)
    }

    @Composable
    override fun MonthDropdown(
        modifier: Modifier,
        title: String,
        items: List<Month>,
        selected: Month,
        itemText: (Month) -> String,
        onItemSelected: (Month) -> Unit,
    ) {
        frequencyDropdown(modifier, title, items, selected, itemText, onItemSelected)
    }

    @Composable
    override fun DayOfMonthDropdown(
        modifier: Modifier,
        title: String,
        items: List<Int>,
        selected: Int,
        itemText: (Int) -> String,
        onItemSelected: (Int) -> Unit,
    ) {
        frequencyDropdown(modifier, title, items, selected, itemText, onItemSelected)
    }

    @Composable
    override fun FrequencyTypeDropdown(
        modifier: Modifier,
        title: String,
        items: List<Frequency.Type>,
        selected: Frequency.Type,
        itemText: (Frequency.Type) -> String,
        onItemSelected: (Frequency.Type) -> Unit,
    ) {
        frequencyDropdown(modifier, title, items, selected, itemText, onItemSelected)
    }

    @Composable
    private fun <T : Any> frequencyDropdown(
        modifier: Modifier,
        title: String,
        items: List<T>,
        selected: T,
        itemText: (T) -> String,
        onItemSelected: (T) -> Unit,
    ) {
        val currentTextStyle = LocalStyledTextStyle.current
        textDropdown(
            modifier = modifier,
            title = title,
            items = items,
            selected = selected,
            itemText = itemText,
            selectedTextStyle = currentTextStyle.copy(
                fontSize = currentTextStyle.fontSize * 0.8f,
            ),
            itemTextStyle = currentTextStyle,
            onItemSelected = onItemSelected,
        )
    }

    @Composable
    private fun <T : Any> textDropdown(
        modifier: Modifier,
        title: String,
        items: List<T>,
        selected: T,
        itemText: (T) -> String,
        selectedTextStyle: TextStyle,
        itemTextStyle: TextStyle,
        onItemSelected: (T) -> Unit,
    ) {
        StyledPopupDropdown(
            items = items,
            selected = selected,
            onSelectionChange = { _, item -> onItemSelected(item) },
            modifier = modifier,
            title = title,
            content = {
                selection {
                    StyledText(
                        text = it?.let(itemText).orEmpty(),
                        modifier = Modifier.fillMaxWidth(),
                        style = selectedTextStyle,
                        maxLines = 1,
                    )
                }
                item { value, isSelected ->
                    StyledText(
                        text = itemText(value),
                        modifier = Modifier.fillMaxWidth(),
                        style = itemTextStyle,
                        color = if (isSelected) StyledTheme.colorScheme.primary else Color.Unspecified,
                        maxLines = 1,
                    )
                }
            },
        )
    }
}
