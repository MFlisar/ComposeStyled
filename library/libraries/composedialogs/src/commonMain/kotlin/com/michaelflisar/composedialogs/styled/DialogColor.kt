package com.michaelflisar.composedialogs.styled

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composedialogs.dialogs.color.ColorPicker
import com.michaelflisar.composedialogs.dialogs.color.ColorPickerConfig
import com.michaelflisar.composedialogs.dialogs.color.ColorPickerDefaults
import com.michaelflisar.composedialogs.dialogs.color.ColorPickerTexts
import com.michaelflisar.composedialogs.styled.provider.ColorUIProviderImpl
import com.michaelflisar.composestyled.isLandscape
import com.michaelflisar.composestyled.ui.layout.StyledDialog
import com.michaelflisar.composestyled.ui.layout.StyledDialogDefaults
import com.michaelflisar.composestyled.ui.state.DialogState

/** Shows the ComposeDialogs color picker in a Styled dialog. */
@Composable
fun DialogColor(
    state: DialogState,
    color: MutableState<Color>,
    modifier: Modifier = Modifier,
    texts: ColorPickerTexts = ColorPickerDefaults.texts(),
    config: ColorPickerConfig = ColorPickerDefaults.config(),
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
        title = StyledDialogDefaults.title(icon, title),
        buttons = buttons,
        onDismissRequest = onDismissRequest,
        content = StyledDialogDefaults.content {
            if (state.visible) {
                ColorPicker(
                    uiProvider = ColorUIProviderImpl,
                    color = color,
                    texts = texts,
                    config = config,
                    landscape = landscape,
                )
            }
        },
    )
}
