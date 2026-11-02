package com.michaelflisar.composestyled.ui.internal

import com.michaelflisar.composestyled.ui.layout.StyledDialog
import com.michaelflisar.composestyled.ui.state.DialogState

internal fun dialogButtons(
    state: DialogState,
    properties: StyledDialog.Properties,
    buttons: StyledDialog.Buttons,
    onDismissRequest: () -> Unit,
): StyledDialog.Buttons {
    fun action(button: StyledDialog.Button?): StyledDialog.Button? = button?.let {
        it.withOnClick(
            onClick = {
                if (it.enabled && state.visible) {
                    it.onClick()
                    if (state.visible && (it.dismissOnClick ?: properties.dismissOnButtonClick)) {
                        onDismissRequest()
                    }
                }
            },
        )
    }
    return StyledDialog.Buttons(action(buttons.positive), action(buttons.negative), action(buttons.neutral))
}
