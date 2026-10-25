package com.michaelflisar.composestyledcomponents.foundation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi

/**
 * Measures the rail inside the area between bars; body padding includes its width.
 * Both Scaffold implementations use this so content has identical coordinates.
 */
@InternalComposeStyledApi
@Composable
fun FoundationScaffoldRailLayout(
    padding: PaddingValues,
    navigationRail: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    railStart: Dp? = null,
    content: @Composable (PaddingValues) -> Unit,
) {
    SubcomposeLayout(modifier) { constraints ->
        val top = padding.calculateTopPadding().roundToPx()
        val bottom = padding.calculateBottomPadding().roundToPx()
        val start = padding.calculateStartPadding(layoutDirection).roundToPx()
        val end = padding.calculateEndPadding(layoutDirection).roundToPx()
        val railPlacementStart = railStart?.roundToPx() ?: start
        val rail = subcompose("rail") { Box { navigationRail() } }.single().measure(
            constraints.copy(
                minWidth = 0,
                minHeight = 0,
                maxWidth = if (constraints.hasBoundedWidth) {
                    (constraints.maxWidth - railPlacementStart - end).coerceAtLeast(0)
                } else constraints.maxWidth,
                maxHeight = if (constraints.hasBoundedHeight) {
                    (constraints.maxHeight - top - bottom).coerceAtLeast(0)
                } else constraints.maxHeight,
            )
        )
        val bodyPadding = PaddingValues(
            start = ((if (rail.width > 0) railPlacementStart else start) + rail.width).toDp(),
            top = padding.calculateTopPadding(),
            end = padding.calculateEndPadding(layoutDirection),
            bottom = padding.calculateBottomPadding(),
        )
        val body = subcompose("body") { Box { content(bodyPadding) } }
            .single().measure(constraints.copy(minWidth = 0, minHeight = 0))
        val width = if (constraints.hasBoundedWidth) constraints.maxWidth else {
            maxOf(constraints.minWidth, body.width, start + rail.width + end)
        }
        val height = if (constraints.hasBoundedHeight) constraints.maxHeight else {
            maxOf(constraints.minHeight, body.height, top + rail.height + bottom)
        }
        layout(width, height) {
            body.place(0, 0)
            rail.placeRelative(railPlacementStart, top)
        }
    }
}
