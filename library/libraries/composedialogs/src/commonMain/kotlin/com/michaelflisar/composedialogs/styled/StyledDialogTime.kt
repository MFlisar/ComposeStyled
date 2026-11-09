package com.michaelflisar.composedialogs.styled

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import com.michaelflisar.composedialogs.dialogs.time.TimePicker
import com.michaelflisar.composedialogs.dialogs.time.TimePickerDefaults
import com.michaelflisar.composedialogs.dialogs.time.TimePickerSetup
import com.michaelflisar.composedialogs.dialogs.time.TimePickerTexts
import com.michaelflisar.composedialogs.styled.provider.StyledTimeUIProvider
import com.michaelflisar.composedialogs.styled.shared.composeDialog
import com.michaelflisar.composedialogs.styled.shared.withPositiveButtonEnabled
import com.michaelflisar.composestyled.ui.layout.StyledDialog
import com.michaelflisar.composestyled.ui.layout.StyledDialogDefaults
import com.michaelflisar.composestyled.ui.state.DialogState
import kotlinx.datetime.LocalTime

/** Shows the ComposeDialogs time picker in a Styled dialog. */
@Composable
fun StyledDialogTime(
    state: DialogState,
    time: MutableState<LocalTime>,
    modifier: Modifier = Modifier,
    setup: TimePickerSetup = TimePickerDefaults.setup(),
    texts: TimePickerTexts = TimePickerDefaults.texts(),
    style: StyledDialog.Style = StyledDialogDefaults.style,
    properties: StyledDialog.Properties = StyledDialog.Properties(),
    title: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    buttons: StyledDialog.Buttons = StyledDialog.Buttons(),
    onDismissRequest: () -> Unit = state::dismiss,
) {
    var isValid by remember { mutableStateOf(true) }
    composeDialog(
        state = state,
        modifier = modifier,
        style = style,
        properties = properties,
        title = title,
        icon = icon,
        buttons = buttons.withPositiveButtonEnabled(isValid),
        onDismissRequest = onDismissRequest,
    ) {
        if (state.visible) {
            TimePicker(
                time = time,
                is24Hours = setup.is24Hours,
                minuteStep = setup.minuteStep,
                hourPickType = setup.hourPickType,
                minutePickType = setup.minutePickType,
                texts = texts,
                uiProvider = StyledTimeUIProvider,
                onValidationChanged = { isValid = it },
            )
        }
    }
}
