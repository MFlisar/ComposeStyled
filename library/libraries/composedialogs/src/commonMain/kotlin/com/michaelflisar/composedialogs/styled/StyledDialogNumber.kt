package com.michaelflisar.composedialogs.styled

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.michaelflisar.composedialogs.styled.shared.composeDialog
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.ui.components.StyledInput
import com.michaelflisar.composestyled.ui.components.StyledNumberPicker
import com.michaelflisar.composestyled.ui.layout.StyledDialog
import com.michaelflisar.composestyled.ui.layout.StyledDialogDefaults
import com.michaelflisar.composestyled.ui.state.DialogState
import com.michaelflisar.composestyled.utils.number.NumberParser
import com.michaelflisar.composestyled.utils.number.rememberNumberParser
import com.michaelflisar.composestyled.utils.Validator
import com.michaelflisar.composestyled.utils.rememberValidator

/** Shows the Styled number picker in a dialog. */
@Composable
fun <T : Number> StyledDialogNumber(
    state: DialogState,
    value: MutableState<T>,
    stepSize: T,
    modifier: Modifier = Modifier,
    label: String = "",
    enabled: Boolean = true,
    showButtons: Boolean = true,
    overrun: Pair<T, T>? = null,
    allowManualInput: Boolean = true,
    clearFocusOnButtonPress: Boolean = true,
    textStyle: TextStyle = StyledTheme.typography.bodyMedium.copy(textAlign = TextAlign.Center),
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    parser: NumberParser<T> = rememberNumberParser(value.value, stepSize),
    validator: Validator<T> = rememberValidator(value.value),
    onCurrentValueIsInvalid: (text: String) -> Unit = {},
    variant: StyledInput.Variant = StyledInput.Variant.Auto,
    style: StyledDialog.Style = StyledDialogDefaults.style,
    properties: StyledDialog.Properties = StyledDialog.Properties(),
    title: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    buttons: StyledDialog.Buttons = StyledDialog.Buttons(),
    onDismissRequest: () -> Unit = state::dismiss,
) {
    composeDialog(
        state = state,
        modifier = modifier,
        style = style,
        properties = properties,
        title = title,
        icon = icon,
        buttons = buttons,
        onDismissRequest = onDismissRequest,
    ) {
        StyledNumberPicker(
            value = value,
            validator = validator,
            parser = parser,
            modifier = Modifier.fillMaxWidth(),
            label = label,
            enabled = enabled,
            showButtons = showButtons,
            overrun = overrun,
            allowManualInput = allowManualInput,
            clearFocusOnButtonPress = clearFocusOnButtonPress,
            textStyle = textStyle,
            prefix = prefix,
            suffix = suffix,
            onCurrentValueIsInvalid = onCurrentValueIsInvalid,
            variant = variant,
        )
    }
}
