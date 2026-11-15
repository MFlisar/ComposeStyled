package com.michaelflisar.composedialogs.styled

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.dialogs.date.DatePicker
import com.michaelflisar.composedialogs.dialogs.date.DatePickerButtons
import com.michaelflisar.composedialogs.dialogs.date.DatePickerConfig
import com.michaelflisar.composedialogs.dialogs.date.DatePickerDefaults
import com.michaelflisar.composedialogs.dialogs.date.rememberDatePickerState
import com.michaelflisar.composedialogs.styled.provider.DateUIProviderImpl
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.UpdateStyledThemeLocals
import com.michaelflisar.composestyled.isLandscape
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.layout.StyledDialog
import com.michaelflisar.composestyled.ui.layout.StyledDialogDefaults
import com.michaelflisar.composestyled.ui.primitives.StyledText
import com.michaelflisar.composestyled.ui.state.DialogState
import kotlinx.datetime.LocalDate

/**
 * Shows the ComposeDialogs date picker in a Styled dialog.
 *
 * @param date the selected date state
 * @param config the date picker's formatting, behavior, and supported year range
 * @param buttonToday optional content for the Today button; pass null to hide it
 */
@OptIn(InternalComposeStyledApi::class)
@Composable
fun DialogDate(
    state: DialogState,
    date: MutableState<LocalDate>,
    config: DatePickerConfig = DatePickerDefaults.config(),
    buttonToday: (@Composable () -> Unit)? = {
        StyledText(text = DatePickerDefaults.texts().today)
    },
    modifier: Modifier = Modifier,
    style: StyledDialog.Style = StyledDialogDefaults.style,
    properties: StyledDialog.Properties = StyledDialog.Properties(),
    title: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    buttons: StyledDialog.Buttons = StyledDialog.Buttons(),
    onDismissRequest: () -> Unit = state::dismiss,
) {
    val landscape = isLandscape
    val pickerState = key(state.visible) {
        rememberDatePickerState(date, config)
    }
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
                if (landscape) {
                    Row {
                        Column {
                            if (icon != null) {
                                Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                                    icon()
                                }
                                Spacer(modifier = Modifier.height(StyledTheme.spacing.medium))
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
                                state = pickerState,
                                config = config,
                                arrangeVertically = true,
                                horizontalSpacing = StyledTheme.spacing.medium,
                                buttonToday = buttonToday,
                            )
                        }
                        Spacer(modifier = Modifier.width(StyledTheme.spacing.medium))
                        DatePicker(
                            uiProvider = DateUIProviderImpl,
                            state = pickerState,
                            config = config,
                            modifier = Modifier,
                        )
                    }
                } else {
                    Column {
                        DatePickerButtons(
                            uiProvider = DateUIProviderImpl,
                            state = pickerState,
                            config = config,
                            arrangeVertically = false,
                            horizontalSpacing = StyledTheme.spacing.medium,
                            buttonToday = buttonToday,
                        )
                        Spacer(modifier = Modifier.height(StyledTheme.spacing.medium))
                        DatePicker(
                            uiProvider = DateUIProviderImpl,
                            state = pickerState,
                            config = config,
                            modifier = Modifier,
                        )
                    }
                }
            }
        },
    )
}
