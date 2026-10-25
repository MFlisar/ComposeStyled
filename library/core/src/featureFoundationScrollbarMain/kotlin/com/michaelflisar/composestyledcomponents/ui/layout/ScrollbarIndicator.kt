package com.michaelflisar.composestyledcomponents.ui.layout

import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal actual fun ScrollbarIndicator(
    modifier: Modifier,
    scrollState: ScrollState,
    orientation: Orientation,
) {
    val adapter = rememberScrollbarAdapter(scrollState)
    when (orientation) {
        Orientation.Vertical -> VerticalScrollbar(adapter = adapter, modifier = modifier)
        Orientation.Horizontal -> HorizontalScrollbar(adapter = adapter, modifier = modifier)
    }
}
