package com.michaelflisar.composepreferences.styled.internal

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import com.michaelflisar.composepreferences.styled.layout.PreferenceStyledRow
import com.michaelflisar.composestyled.classes.IconComposable
import com.michaelflisar.composestyled.ui.layout.StyledDialog
import com.michaelflisar.composestyled.ui.primitives.StyledText
import com.michaelflisar.composestyled.ui.state.VisibilityDialogState

@Composable
internal fun <T> BaseDialogPreference(
    data: T,
    title: String,
    subtitle: String?,
    displaySubtitle: String? = subtitle,
    dialog: VisibilityDialogState,
    modifier: Modifier = Modifier,
    key: String? = null,
    icon: IconComposable? = null,
    filterTags: List<String> = emptyList(),
    enabled: Boolean = true,
    isEnabled: @Composable () -> Boolean = { enabled },
    isVisible: @Composable () -> Boolean = { true },
    buttons: (PreferenceEntryVisuals) -> StyledDialog.Buttons = { StyledDialog.Buttons() },
    trailing: (@Composable RowScope.() -> Unit)? = null,
    onOpen: () -> Unit = {},
    shape: Shape? = null,
    content: @Composable ColumnScope.(
        data: T,
        dialog: VisibilityDialogState,
        padding: PaddingValues,
        visuals: PreferenceEntryVisuals,
    ) -> Unit,
) {
    PreferenceEntry(
        title = title,
        subtitle = subtitle,
        filterTags = filterTags,
        key = key,
        isEnabled = isEnabled,
        isVisible = isVisible,
        breaksSectionShape = shape != null,
    ) { visuals ->
        PreferenceStyledRow(
            title = title,
            subtitle = displaySubtitle,
            icon = icon,
            onClick = {
                onOpen()
                dialog.show()
            },
            enabled = visuals.enabled,
            modifier = modifier,
            style = visuals.style,
            insideSection = visuals.insideSection,
            breadcrumb = visuals.breadcrumb,
            shape = shape,
            trailing = trailing,
        )
        StyledDialog(
            state = dialog,
            title = { StyledText(title) },
            buttons = buttons(visuals),
        ) { padding ->
            content(data, dialog, padding, visuals)
        }
    }
}
