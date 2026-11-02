package com.michaelflisar.composestyled.foundation.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi

/**
 * Measures side navigation between bars; body padding follows its current animated width.
 * Both Scaffold implementations use this so content has identical coordinates.
 */
@InternalComposeStyledApi
@Composable
fun FoundationScaffoldSideNavigationLayout(
    padding: PaddingValues,
    sideNavigation: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues) -> Unit,
) {
    SubcomposeLayout(modifier) { constraints ->
        val top = padding.calculateTopPadding().roundToPx()
        val bottom = padding.calculateBottomPadding().roundToPx()
        val start = padding.calculateStartPadding(layoutDirection).roundToPx()
        val end = padding.calculateEndPadding(layoutDirection).roundToPx()
        val navigationPadding = PaddingValues(
            start = 0.dp,
            top = padding.calculateTopPadding(),
            end = padding.calculateEndPadding(layoutDirection),
            bottom = padding.calculateBottomPadding(),
        )
        val navigation = subcompose("sideNavigation") {
            Box(Modifier.consumeWindowInsets(navigationPadding)) { sideNavigation() }
        }.single().measure(
            constraints.copy(
                minWidth = 0,
                minHeight = 0,
                maxWidth = if (constraints.hasBoundedWidth) {
                    (constraints.maxWidth - end).coerceAtLeast(0)
                } else constraints.maxWidth,
                maxHeight = if (constraints.hasBoundedHeight) {
                    (constraints.maxHeight - top - bottom).coerceAtLeast(0)
                } else constraints.maxHeight,
            )
        )
        val bodyPadding = PaddingValues(
            start = (if (navigation.width > 0) navigation.width else start).toDp(),
            top = padding.calculateTopPadding(),
            end = padding.calculateEndPadding(layoutDirection),
            bottom = padding.calculateBottomPadding(),
        )
        val body = subcompose("body") { Box { content(bodyPadding) } }
            .single().measure(constraints.copy(minWidth = 0, minHeight = 0))
        val width = if (constraints.hasBoundedWidth) constraints.maxWidth else {
            maxOf(
                constraints.minWidth,
                body.width,
                (if (navigation.width > 0) navigation.width else start) + end,
            )
        }
        val height = if (constraints.hasBoundedHeight) constraints.maxHeight else {
            maxOf(constraints.minHeight, body.height, top + navigation.height + bottom)
        }
        layout(width, height) {
            body.place(0, 0)
            navigation.placeRelative(0, top)
        }
    }
}
