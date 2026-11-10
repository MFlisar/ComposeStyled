package com.michaelflisar.composepreferences.styled

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composepreferences.styled.internal.PreferenceEntry
import com.michaelflisar.composestyled.ui.components.StyledListItem

/** A preference item whose entire content is caller-defined. */
@Composable
fun PreferenceCustomItem(
    modifier: Modifier = Modifier,
    key: String? = null,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    filterTags: List<String> = emptyList(),
    isEnabled: @Composable () -> Boolean = { enabled },
    isVisible: @Composable () -> Boolean = { true },
    contentPadding: PaddingValues? = null,
    shape: Shape? = null,
    minHeight: Dp? = null,
    containerColor: Color? = null,
    contentColor: Color? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    PreferenceEntry(
        title = "",
        subtitle = null,
        filterTags = filterTags,
        key = key,
        isEnabled = isEnabled,
        isVisible = isVisible,
    ) { visuals ->
        val item = visuals.style.item
        val colors = item.colors
        StyledListItem(
            onClick = onClick,
            modifier = modifier.fillMaxWidth(),
            enabled = visuals.enabled,
            shape = shape ?: if (visuals.insideSection) RectangleShape else item.shape,
            minHeight = minHeight ?: item.minimumItemHeight,
            contentPadding = contentPadding ?: item.contentPadding,
            containerColor = containerColor ?: colors.containerColor,
            contentColor = contentColor ?: colors.contentColor,
            supportingColor = colors.supportingColor,
        ) {
            content()
        }
    }
}
