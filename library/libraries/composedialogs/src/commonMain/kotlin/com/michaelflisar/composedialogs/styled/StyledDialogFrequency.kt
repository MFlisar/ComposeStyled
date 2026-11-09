package com.michaelflisar.composedialogs.styled

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import com.michaelflisar.composedialogs.dialogs.frequency.FrequencyPicker
import com.michaelflisar.composedialogs.dialogs.frequency.FrequencyPickerDefaults
import com.michaelflisar.composedialogs.dialogs.frequency.FrequencyPickerTexts
import com.michaelflisar.composedialogs.dialogs.frequency.FrequencyPickerType
import com.michaelflisar.composedialogs.dialogs.frequency.FrequencyIntervalInputType
import com.michaelflisar.composedialogs.dialogs.time.TimePickerDefaults
import com.michaelflisar.composedialogs.dialogs.time.TimePickerSetup
import com.michaelflisar.composedialogs.styled.provider.StyledFrequencyUIProvider
import com.michaelflisar.composedialogs.styled.shared.composeDialog
import com.michaelflisar.composedialogs.styled.shared.withPositiveButtonEnabled
import com.michaelflisar.composestyled.ui.layout.StyledDialog
import com.michaelflisar.composestyled.ui.layout.StyledDialogDefaults
import com.michaelflisar.composestyled.ui.state.DialogState
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month

/** Shows the ComposeDialogs frequency editor in a Styled dialog. */
@Composable
fun StyledDialogFrequency(
    state: DialogState,
    selectedType: MutableState<FrequencyPickerType>,
    selectedTime: MutableState<LocalTime>,
    selectedDayOfWeek: MutableState<DayOfWeek>,
    selectedDayOfMonth: MutableState<Int>,
    selectedMonthOfYear: MutableState<Month>,
    selectedInterval: MutableState<Int?>,
    modifier: Modifier = Modifier,
    texts: FrequencyPickerTexts = FrequencyPickerDefaults.texts(),
    supportedTypes: List<FrequencyPickerType> = FrequencyPickerType.entries,
    firstDayOffset: DayOfWeek = DayOfWeek.MONDAY,
    intervalInputType: FrequencyIntervalInputType = FrequencyIntervalInputType.Input,
    timeSetup: TimePickerSetup = TimePickerDefaults.setup(),
    style: StyledDialog.Style = StyledDialogDefaults.style,
    properties: StyledDialog.Properties = StyledDialog.Properties(),
    title: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    buttons: StyledDialog.Buttons = StyledDialog.Buttons(),
    onDismissRequest: () -> Unit = state::dismiss,
) {
    val intervalIsValid = selectedInterval.value?.let { it > 0 } == true
    composeDialog(
        state = state,
        modifier = modifier,
        style = style,
        properties = properties,
        title = title,
        icon = icon,
        buttons = buttons.withPositiveButtonEnabled(intervalIsValid),
        onDismissRequest = onDismissRequest,
    ) {
        if (state.visible) {
            FrequencyPicker(
                uiProvider = StyledFrequencyUIProvider,
                texts = texts,
                supportedTypes = supportedTypes,
                firstDayOffset = firstDayOffset,
                selectedType = selectedType,
                selectedTime = selectedTime,
                selectedDayOfWeek = selectedDayOfWeek,
                selectedDayOfMonth = selectedDayOfMonth,
                selectedMonthOfYear = selectedMonthOfYear,
                selectedInterval = selectedInterval,
                intervalInputType = intervalInputType,
                timeSetup = timeSetup,
            )
        }
    }
}
