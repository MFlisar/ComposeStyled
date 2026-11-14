package com.michaelflisar.composedialogs.styled

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.dialogs.date.DatePickerButtons
import com.michaelflisar.composedialogs.dialogs.date.DatePickerConfig
import com.michaelflisar.composedialogs.dialogs.date.DatePickerDefaults
import com.michaelflisar.composedialogs.dialogs.date.DatePickerMain
import com.michaelflisar.composedialogs.styled.provider.DateUIProviderImpl
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.UpdateStyledThemeLocals
import com.michaelflisar.composestyled.isLandscape
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.layout.StyledDialog
import com.michaelflisar.composestyled.ui.layout.StyledDialogDefaults
import com.michaelflisar.composestyled.ui.state.DialogState
import kotlinx.datetime.LocalDate

/**
 * Shows the ComposeDialogs date picker in a Styled dialog.
 *
 * @param date the selected date state
 * @param config the date picker's formatting, behavior, and supported year range
 */
@OptIn(InternalComposeStyledApi::class)
@Composable
fun DialogDate(
    state: DialogState,
    date: MutableState<LocalDate>,
    config: DatePickerConfig = DatePickerDefaults.config(),
    modifier: Modifier = Modifier,
    style: StyledDialog.Style = StyledDialogDefaults.style,
    properties: StyledDialog.Properties = StyledDialog.Properties(),
    title: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    buttons: StyledDialog.Buttons = StyledDialog.Buttons(),
    onDismissRequest: () -> Unit = state::dismiss,
) {
    val landscape = isLandscape
    StyledDialog(
        state = state,
        modifier = modifier,
        style = style,
        properties = properties,
        title = StyledDialogDefaults.title(
            icon.takeUnless { landscape },
            title.takeUnless { landscape },
        ),
        buttons = buttons,
        onDismissRequest = onDismissRequest,
        content = StyledDialogDefaults.content {
            if (state.visible) {
                DatePickerMain(
                    uiProvider = DateUIProviderImpl,
                    date = date,
                    config = config,
                    landscape = landscape,
                ) { selectedDateEnabled, onSelectedDateClick, todayEnabled, onTodayClick ->
                    if (landscape) {
                        Column {
                            if (icon != null) {
                                Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                                    icon()
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                            if (title != null) {
                                UpdateStyledThemeLocals(
                                    textStyle = StyledTheme.typography.headlineSmall,
                                ) {
                                    Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                                        title()
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                            DatePickerButtons(
                                uiProvider = DateUIProviderImpl,
                                date = date,
                                config = config,
                                landscape = true,
                                selectedDateEnabled = selectedDateEnabled,
                                onSelectedDateClick = onSelectedDateClick,
                                todayEnabled = todayEnabled,
                                onTodayClick = onTodayClick,
                            )
                        }
                    } else {
                        DatePickerButtons(
                            uiProvider = DateUIProviderImpl,
                            date = date,
                            config = config,
                            landscape = false,
                            selectedDateEnabled = selectedDateEnabled,
                            onSelectedDateClick = onSelectedDateClick,
                            todayEnabled = todayEnabled,
                            onTodayClick = onTodayClick,
                        )
                    }
                }
            }
        },
    )
}
