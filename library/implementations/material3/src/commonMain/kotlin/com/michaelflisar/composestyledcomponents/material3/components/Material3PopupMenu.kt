package com.michaelflisar.composestyledcomponents.material3.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.michaelflisar.composestyledcomponents.material3.CopyMaterialThemeLocals
import com.michaelflisar.composestyledcomponents.ui.components.StyledPopup
import com.michaelflisar.composestyledcomponents.ui.components.StyledPopupMenu

class Material3PopupMenu internal constructor() : StyledPopupMenu.Implementation {
    companion object {
        @Composable
        fun create() = remember { Material3PopupMenu() }
    }

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
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest,
            modifier = modifier.heightIn(max = style.maxHeight),
            offset = offset,
            scrollState = scrollState,
            properties = properties,
            shape = style.shape,
            containerColor = style.containerColor,
            tonalElevation = 0.dp,
            shadowElevation = style.shadowElevation,
            border = style.border,
        ) {
            CopyMaterialThemeLocals(contentColor = style.contentColor) {
                Column(Modifier.padding(contentPadding)) {
                    header?.invoke()
                    content()
                }
            }
        }
    }
}
