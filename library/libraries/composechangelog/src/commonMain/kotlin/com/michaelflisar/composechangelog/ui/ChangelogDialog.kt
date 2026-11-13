package com.michaelflisar.composechangelog.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.michaelflisar.composechangelog.ChangelogSetup
import com.michaelflisar.composechangelog.ChangelogState
import com.michaelflisar.composestyled.ui.layout.StyledDialog
import com.michaelflisar.composestyled.ui.layout.StyledDialogDefaults
import com.michaelflisar.composestyled.ui.state.rememberDialogState

@Composable
fun ChangelogDialog(
    state: ChangelogState,
    setup: ChangelogSetup,
    modifier: Modifier = Modifier,
    style: StyledDialog.Style = StyledDialogDefaults.style,
    onDismissRequest: () -> Unit = state::hide,
    properties: StyledDialog.Properties = StyledDialog.Properties(),
    title: (@Composable () -> Unit)? = null,
    buttons: StyledDialog.Buttons = StyledDialog.Buttons(),
) {
    StyledDialog(
        state = rememberDialogState(
            visible = state.visible,
            onDismiss = state::hide
        ),
        modifier = modifier,
        style = style,
        onDismissRequest = onDismissRequest,
        properties = properties,
        title = title,
        buttons = buttons
    ) { contentPadding ->
        ChangelogContent(
            state = state,
            setup = setup,
            modifier = Modifier.fillMaxWidth(),
            contentPadding = contentPadding
        )
    }
}