package com.michaelflisar.composedialogs.styled

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.ui.components.StyledProgressIndicator
import com.michaelflisar.composestyled.ui.layout.StyledDialog
import com.michaelflisar.composestyled.ui.layout.StyledDialogDefaults
import com.michaelflisar.composestyled.ui.state.DialogState

/** Shows Styled progress and optional message content in a dialog. */
@Composable
fun DialogProgress(
    state: DialogState,
    modifier: Modifier = Modifier,
    content: (@Composable ColumnScope.() -> Unit)? = null,
    progress: Float? = null,
    type: StyledProgressIndicator.Type = StyledProgressIndicator.Type.Linear,
    color: Color = StyledTheme.colorScheme.primary,
    trackColor: Color = color.copy(alpha = 0.24f),
    strokeWidth: Dp = 4.dp,
    style: StyledDialog.Style = StyledDialogDefaults.style,
    properties: StyledDialog.Properties = StyledDialog.Properties(),
    title: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    buttons: StyledDialog.Buttons = StyledDialog.Buttons(),
    onDismissRequest: () -> Unit = state::dismiss,
) {
    StyledDialog(
        state = state,
        modifier = modifier,
        style = style,
        properties = properties,
        title = StyledDialogDefaults.title(icon, title),
        buttons = buttons,
        onDismissRequest = onDismissRequest,
        content = StyledDialogDefaults.content {
            content?.invoke(this)
            if (content != null) {
                Spacer(modifier = Modifier.height(StyledTheme.spacing.small))
            }
            if (type == StyledProgressIndicator.Type.Linear) {
                StyledProgressIndicator(
                    type = type,
                    progress = progress,
                    modifier = Modifier.fillMaxWidth(),
                    color = color,
                    trackColor = trackColor,
                    strokeWidth = strokeWidth,
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    StyledProgressIndicator(
                        type = type,
                        progress = progress,
                        //modifier = Modifier.fillMaxWidth(),
                        color = color,
                        trackColor = trackColor,
                        strokeWidth = strokeWidth,
                    )
                }
            }
        },
    )
}
