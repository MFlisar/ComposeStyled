package com.michaelflisar.composedialogs.styled

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import com.michaelflisar.composedialogs.dialogs.date.DatePicker
import com.michaelflisar.composedialogs.dialogs.date.ui.DatePickerRange
import com.michaelflisar.composedialogs.dialogs.date.ui.DatePickerSetup
import com.michaelflisar.composedialogs.styled.provider.StyledDateUIProvider
import com.michaelflisar.composedialogs.styled.shared.composeDialog
import com.michaelflisar.composestyled.isLandscape
import com.michaelflisar.composestyled.ui.layout.StyledDialog
import com.michaelflisar.composestyled.ui.layout.StyledDialogDefaults
import com.michaelflisar.composestyled.ui.state.DialogState
import kotlinx.datetime.LocalDate

/** Shows the ComposeDialogs date picker in a Styled dialog. */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun StyledDialogDate(
    state: DialogState,
    date: MutableState<LocalDate>,
    dateRange: DatePickerRange,
    setup: DatePickerSetup,
    modifier: Modifier = Modifier,
    style: StyledDialog.Style = StyledDialogDefaults.style,
    properties: StyledDialog.Properties = StyledDialog.Properties(),
    title: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    buttons: StyledDialog.Buttons = StyledDialog.Buttons(),
    onDismissRequest: () -> Unit = state::dismiss,
) {
    val landscape = isLandscape
    composeDialog(
        state = state,
        modifier = modifier,
        style = style,
        properties = properties,
        title = title.takeUnless { landscape },
        icon = icon.takeUnless { landscape },
        buttons = buttons,
        onDismissRequest = onDismissRequest,
    ) {
        if (state.visible) {
            DatePicker(
                uiProvider = StyledDateUIProvider,
                date = date,
                dateRange = dateRange,
                setup = setup,
                landscape = landscape,
                title = title,
                icon = icon,
            )
        }
    }
}
