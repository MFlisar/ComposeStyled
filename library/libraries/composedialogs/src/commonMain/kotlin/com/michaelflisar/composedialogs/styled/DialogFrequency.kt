package com.michaelflisar.composedialogs.styled

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.michaelflisar.composedialogs.dialogs.frequency.FrequencyIntervalInputType
import com.michaelflisar.composedialogs.dialogs.frequency.FrequencyPicker
import com.michaelflisar.composedialogs.dialogs.frequency.FrequencyPickerDefaults
import com.michaelflisar.composedialogs.dialogs.frequency.FrequencyPickerTexts
import com.michaelflisar.composedialogs.dialogs.frequency.classes.Frequency
import com.michaelflisar.composedialogs.dialogs.time.TimePickerDefaults
import com.michaelflisar.composedialogs.dialogs.time.TimePickerSetup
import com.michaelflisar.composedialogs.styled.provider.FrequencyUIProviderImpl
import com.michaelflisar.composedialogs.styled.shared.withPositiveButtonEnabled
import com.michaelflisar.composestyled.ui.layout.StyledDialog
import com.michaelflisar.composestyled.ui.layout.StyledDialogDefaults
import com.michaelflisar.composestyled.ui.state.DialogState
import kotlinx.datetime.DayOfWeek

/** Shows the ComposeDialogs frequency editor in a Styled dialog. */
@Composable
fun DialogFrequency(
    state: DialogState,
    frequency: MutableState<Frequency>,
    modifier: Modifier = Modifier,
    texts: FrequencyPickerTexts = FrequencyPickerDefaults.texts(),
    supportedTypes: List<Frequency.Type> = Frequency.Type.entries,
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
    val intervalIsValid = remember(frequency.value.interval) {
        mutableStateOf(frequency.value.interval > 0)
    }
    StyledDialog(
        state = state,
        modifier = modifier,
        style = style,
        properties = properties,
        title = StyledDialogDefaults.title(icon, title),
        buttons = buttons.withPositiveButtonEnabled(intervalIsValid.value),
        onDismissRequest = onDismissRequest,
        content = StyledDialogDefaults.content {
            if (state.visible) {
                FrequencyPicker(
                    uiProvider = FrequencyUIProviderImpl,
                    frequency = frequency,
                    texts = texts,
                    supportedTypes = supportedTypes,
                    firstDayOffset = firstDayOffset,
                    intervalInputType = intervalInputType,
                    timeSetup = timeSetup,
                ) { isValid ->
                    intervalIsValid.value = isValid
                }
            }
        },
    )
}
