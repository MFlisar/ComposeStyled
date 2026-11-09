package com.michaelflisar.composedialogs.styled

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.Modifier
import com.michaelflisar.composedialogs.styled.shared.composeDialog
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.ui.layout.StyledDialog
import com.michaelflisar.composestyled.ui.layout.StyledDialogDefaults
import com.michaelflisar.composestyled.ui.primitives.StyledText
import com.michaelflisar.composestyled.ui.state.DialogState

/** Shows Styled information content in a dialog. */
@Composable
fun StyledDialogInfo(
    state: DialogState,
    info: String,
    modifier: Modifier = Modifier,
    infoLabel: String = "",
    labelStyle: TextStyle = StyledTheme.typography.titleSmall,
    infoStyle: TextStyle = StyledTheme.typography.bodyMedium,
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
        if (infoLabel.isNotEmpty()) {
            StyledText(infoLabel, style = labelStyle)
        }
        StyledText(info, style = infoStyle)
    }
}
