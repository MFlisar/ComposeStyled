package com.michaelflisar.composedialogs.styled.shared

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.michaelflisar.composedialogs.styled.dialogTitle
import com.michaelflisar.composestyled.ui.layout.StyledDialog
import com.michaelflisar.composestyled.ui.state.DialogState

@Composable
internal fun composeDialog(
    state: DialogState,
    modifier: Modifier,
    style: StyledDialog.Style,
    properties: StyledDialog.Properties,
    title: (@Composable () -> Unit)?,
    icon: (@Composable () -> Unit)?,
    buttons: StyledDialog.Buttons,
    onDismissRequest: () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    StyledDialog(
        state = state,
        modifier = modifier,
        style = style,
        properties = properties,
        title = dialogTitle(icon, title),
        buttons = buttons,
        onDismissRequest = onDismissRequest,
    ) { contentPadding ->
        Column(
            modifier = Modifier.fillMaxWidth().padding(contentPadding),
        ) {
            content()
        }
    }
}

internal fun StyledDialog.Buttons.withPositiveButtonEnabled(enabled: Boolean): StyledDialog.Buttons {
    val positive = positive ?: return this
    val updated = when (val label = positive.label) {
        null -> StyledDialog.Button(
            onClick = positive.onClick,
            variant = positive.variant,
            enabled = positive.enabled && enabled,
            dismissOnClick = positive.dismissOnClick,
            content = positive.content,
        )
        else -> StyledDialog.Button(
            onClick = positive.onClick,
            label = label,
            icon = positive.icon,
            presentation = positive.presentation,
            enabled = positive.enabled && enabled,
            dismissOnClick = positive.dismissOnClick,
            variant = positive.variant,
        )
    }
    return copy(positive = updated)
}
