package com.michaelflisar.composestyled.foundation.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.onConsumedWindowInsetsChanged
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.LayoutDirection
import com.michaelflisar.composestyled.UpdateStyledThemeLocals
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.ui.layout.StyledScaffold
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi

/**
 * Shared scaffold layout for non-Material themes, using styled padding and spacing.
 */
@InternalComposeStyledApi
abstract class FoundationScaffold : StyledScaffold.Implementation {

    @Composable
    override fun Render(
        modifier: Modifier,
        topBar: @Composable () -> Unit,
        bottomBar: @Composable () -> Unit,
        sideNavigation: @Composable () -> Unit,
        snackbarHost: @Composable () -> Unit,
        floatingActionButton: @Composable () -> Unit,
        floatingActionButtonPosition: StyledScaffold.FloatingActionButtonPosition,
        containerColor: Color,
        contentColor: Color,
        contentWindowInsets: WindowInsets,
        content: @Composable (PaddingValues) -> Unit,
    ) {
        UpdateStyledThemeLocals(contentColor = contentColor) {
            FoundationScaffoldLayout(
                modifier = modifier,
                topBar = topBar,
                bottomBar = bottomBar,
                snackbarHost = snackbarHost,
                floatingActionButton = floatingActionButton,
                floatingActionButtonPosition = floatingActionButtonPosition,
                containerColor = containerColor,
                contentWindowInsets = contentWindowInsets,
                content = { padding ->
                    FoundationScaffoldSideNavigationLayout(
                        padding = padding,
                        sideNavigation = sideNavigation,
                        content = content,
                    )
                },
            )
        }
    }
}

@Composable
private fun FoundationScaffoldLayout(
    modifier: Modifier,
    topBar: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit,
    snackbarHost: @Composable () -> Unit,
    floatingActionButton: @Composable () -> Unit,
    floatingActionButtonPosition: StyledScaffold.FloatingActionButtonPosition,
    containerColor: Color,
    contentWindowInsets: WindowInsets,
    content: @Composable (PaddingValues) -> Unit,
) {
    val fabPadding = StyledTheme.padding.medium
    val snackbarSpacing = StyledTheme.spacing.medium
    var consumedInsets by remember { mutableStateOf(WindowInsets(0, 0, 0, 0)) }

    SubcomposeLayout(
        modifier = modifier
            .onConsumedWindowInsetsChanged { consumedInsets = it }
            .fillMaxSize()
            .background(containerColor),
    ) { constraints ->
        // Ancestor consumption can change after composition, before this measurement.
        val remainingInsets = contentWindowInsets.exclude(consumedInsets)
        val childConstraints = constraints.copy(minWidth = 0, minHeight = 0)
        val topBarPlaceable = subcompose(ScaffoldSlot.TopBar) { Box { topBar() } }
            .single().measure(childConstraints)
        val bottomBarPlaceable = subcompose(ScaffoldSlot.BottomBar) { Box { bottomBar() } }
            .single().measure(childConstraints)

        val width = if (constraints.hasBoundedWidth) {
            constraints.maxWidth
        } else {
            maxOf(constraints.minWidth, topBarPlaceable.width, bottomBarPlaceable.width)
        }
        val height = if (constraints.hasBoundedHeight) {
            constraints.maxHeight
        } else {
            (topBarPlaceable.height + bottomBarPlaceable.height)
                .coerceIn(constraints.minHeight, constraints.maxHeight)
        }

        val insetStart = remainingInsets.getLeft(this, layoutDirection)
        val insetTop = remainingInsets.getTop(this)
        val insetEnd = remainingInsets.getRight(this, layoutDirection)
        val insetBottom = remainingInsets.getBottom(this)
        val contentPadding = PaddingValues(
            start = (if (layoutDirection == LayoutDirection.Ltr) insetStart else insetEnd).toDp(),
            top = maxOf(topBarPlaceable.height, insetTop).toDp(),
            end = (if (layoutDirection == LayoutDirection.Ltr) insetEnd else insetStart).toDp(),
            bottom = maxOf(bottomBarPlaceable.height, insetBottom).toDp(),
        )
        val body = subcompose(ScaffoldSlot.Content) {
            Box { content(contentPadding) }
        }.single().measure(
            childConstraints.copy(maxWidth = width, maxHeight = height)
        )
        val overlayConstraints = childConstraints.copy(
            maxWidth = (width - insetStart - insetEnd).coerceAtLeast(0),
            maxHeight = (height - insetTop - insetBottom).coerceAtLeast(0),
        )
        val snackbar = subcompose(ScaffoldSlot.Snackbar) { Box { snackbarHost() } }
            .single().measure(overlayConstraints)
        val fab = subcompose(ScaffoldSlot.FloatingActionButton) { Box { floatingActionButton() } }
            .single().measure(overlayConstraints)

        layout(width, height) {
            body.place(0, 0)
            topBarPlaceable.place(0, 0)
            bottomBarPlaceable.place(0, height - bottomBarPlaceable.height)

            val bottomObstruction = maxOf(bottomBarPlaceable.height, insetBottom)
            val fabObstruction = if (fab.width > 0 && fab.height > 0) {
                fab.height + fabPadding.roundToPx()
            } else {
                0
            }
            snackbar.let {
                val x = insetStart + (overlayConstraints.maxWidth - it.width) / 2
                val y = (height - bottomObstruction - fabObstruction -
                    snackbarSpacing.roundToPx() - it.height)
                    .coerceAtLeast(0)
                it.place(x, y)
            }
            fab.let {
                val x = when (floatingActionButtonPosition) {
                    StyledScaffold.FloatingActionButtonPosition.Start ->
                        if (layoutDirection == LayoutDirection.Ltr) insetStart + fabPadding.roundToPx()
                        else width - insetEnd - fabPadding.roundToPx() - it.width

                    StyledScaffold.FloatingActionButtonPosition.Center -> (width - it.width) / 2

                    StyledScaffold.FloatingActionButtonPosition.End ->
                        if (layoutDirection == LayoutDirection.Ltr) {
                            width - insetEnd - fabPadding.roundToPx() - it.width
                        } else {
                            insetStart + fabPadding.roundToPx()
                        }
                }
                val y = (height - bottomObstruction - fabPadding.roundToPx() - it.height)
                    .coerceAtLeast(0)
                it.place(
                    x.coerceIn(insetStart, maxOf(insetStart, width - insetEnd - it.width)),
                    y,
                )
            }
        }
    }
}

private enum class ScaffoldSlot {
    TopBar,
    BottomBar,
    Content,
    Snackbar,
    FloatingActionButton,
}
