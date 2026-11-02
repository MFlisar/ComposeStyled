package com.michaelflisar.composestyled.ui.layout

import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.IntrinsicMeasurable
import androidx.compose.ui.layout.IntrinsicMeasureScope
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.unit.Constraints

@Composable
internal actual fun ScrollbarIndicator(
    modifier: Modifier,
    scrollState: ScrollableState,
    orientation: Orientation,
) {
    val adapter = when (scrollState) {
        is androidx.compose.foundation.ScrollState -> rememberScrollbarAdapter(scrollState)
        is LazyListState -> rememberScrollbarAdapter(scrollState)
        is LazyGridState -> rememberScrollbarAdapter(scrollState)
        else -> error("Unsupported scroll state for scrollbar indicator: ${scrollState::class}")
    }
    when (orientation) {
        Orientation.Vertical -> ScrollbarLayout(modifier) {
            VerticalScrollbar(
                adapter = adapter,
                modifier = Modifier.fillMaxSize(),
            )
        }
        Orientation.Horizontal -> ScrollbarLayout(modifier) {
            HorizontalScrollbar(
                adapter = adapter,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
private fun ScrollbarLayout(
    modifier: Modifier,
    scrollbar: @Composable () -> Unit,
) {
    Layout(
        content = { scrollbar() },
        modifier = modifier,
        measurePolicy = ScrollbarMeasurePolicy,
    )
}

// Scrollbars are fill-sized overlays and have no intrinsic track dimensions.
private object ScrollbarMeasurePolicy : MeasurePolicy {
    override fun MeasureScope.measure(
        measurables: List<Measurable>,
        constraints: Constraints,
    ): MeasureResult {
        val placeable = measurables.single().measure(constraints)
        return layout(placeable.width, placeable.height) {
            placeable.place(0, 0)
        }
    }

    override fun IntrinsicMeasureScope.minIntrinsicWidth(
        measurables: List<IntrinsicMeasurable>,
        height: Int,
    ): Int = 0

    override fun IntrinsicMeasureScope.maxIntrinsicWidth(
        measurables: List<IntrinsicMeasurable>,
        height: Int,
    ): Int = 0

    override fun IntrinsicMeasureScope.minIntrinsicHeight(
        measurables: List<IntrinsicMeasurable>,
        width: Int,
    ): Int = 0

    override fun IntrinsicMeasureScope.maxIntrinsicHeight(
        measurables: List<IntrinsicMeasurable>,
        width: Int,
    ): Int = 0
}
