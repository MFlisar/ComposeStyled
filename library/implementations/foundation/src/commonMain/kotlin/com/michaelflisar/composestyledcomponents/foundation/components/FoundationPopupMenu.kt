package com.michaelflisar.composestyledcomponents.foundation.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.window.PopupProperties
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyledcomponents.ui.components.StyledPopup
import com.michaelflisar.composestyledcomponents.ui.components.StyledPopupDefaults
import com.michaelflisar.composestyledcomponents.ui.components.StyledPopupMenu

@InternalComposeStyledApi
class FoundationPopupMenu : StyledPopupMenu.Implementation {

    @Composable
    override fun Render(
        expanded: Boolean,
        onDismissRequest: () -> Unit,
        modifier: Modifier,
        offset: DpOffset,
        properties: PopupProperties,
        scrollState: ScrollState,
        style: StyledPopup.Style,
        contentPadding: androidx.compose.foundation.layout.PaddingValues,
        header: (@Composable () -> Unit)?,
        content: @Composable ColumnScope.() -> Unit,
    ) {
        StyledPopup(
            visible = expanded,
            onDismissRequest = onDismissRequest,
            modifier = modifier,
            popupPositionProvider = StyledPopupDefaults.positionProvider(offset),
            properties = properties,
            style = style,
        ) {
            Column(Modifier.heightIn(max = style.maxHeight)) {
                header?.invoke()
                Column(
                    Modifier.weight(1f, fill = false)
                        .verticalScroll(scrollState)
                        .padding(contentPadding),
                    content = content,
                )
            }
        }
    }
}
