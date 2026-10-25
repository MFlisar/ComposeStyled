package com.michaelflisar.composestyledcomponents.foundation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyledcomponents.ProvideCustomStyledContentLocals
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.ui.components.StyledScaffold
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi

/**
 * Shared scaffold layout for non-Material themes, using styled padding and spacing.
 */
@InternalComposeStyledApi
class FoundationScaffold(
    override val defaults: StyledScaffold.Defaults = StyledScaffold.Defaults(),
) : StyledScaffold.Implementation {

    @Composable
    override fun Render(
        modifier: Modifier,
        topBar: @Composable () -> Unit,
        bottomBar: @Composable () -> Unit,
        navigationRail: @Composable () -> Unit,
        snackbarHost: @Composable () -> Unit,
        floatingActionButton: @Composable () -> Unit,
        floatingActionButtonPosition: StyledScaffold.FloatingActionButtonPosition,
        containerColor: Color,
        contentColor: Color,
        contentWindowInsets: WindowInsets,
        content: @Composable (PaddingValues) -> Unit,
    ) {
        ProvideCustomStyledContentLocals(contentColor = contentColor) {
            FoundationScaffoldLayout(
                modifier = modifier,
                topBar = topBar,
                bottomBar = bottomBar,
                snackbarHost = snackbarHost,
                floatingActionButton = floatingActionButton,
                floatingActionButtonPosition = floatingActionButtonPosition,
                containerColor = containerColor,
                contentWindowInsets = contentWindowInsets,
                content = { padding, railStart ->
                    FoundationScaffoldRailLayout(
                        padding = padding,
                        railStart = railStart,
                        navigationRail = navigationRail,
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
    content: @Composable (PaddingValues, Dp) -> Unit,
) {
    val contentHorizontalPadding = StyledTheme.padding.content
    val fabPadding = StyledTheme.padding.medium
    val snackbarSpacing = StyledTheme.spacing.medium

    SubcomposeLayout(
        modifier = modifier
            .fillMaxSize()
            .background(containerColor),
    ) { constraints ->
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

        val insetStart = contentWindowInsets.getLeft(this, layoutDirection)
        val insetTop = contentWindowInsets.getTop(this)
        val insetEnd = contentWindowInsets.getRight(this, layoutDirection)
        val insetBottom = contentWindowInsets.getBottom(this)
        val horizontalPadding = maxOf(
            contentHorizontalPadding.calculateStartPadding(layoutDirection).roundToPx(),
            if (layoutDirection == LayoutDirection.Ltr) insetStart else insetEnd,
        )
        val horizontalPaddingEnd = maxOf(
            contentHorizontalPadding.calculateEndPadding(layoutDirection).roundToPx(),
            if (layoutDirection == LayoutDirection.Ltr) insetEnd else insetStart,
        )
        val contentPadding = PaddingValues(
            start = horizontalPadding.toDp(),
            top = maxOf(topBarPlaceable.height, insetTop).toDp(),
            end = horizontalPaddingEnd.toDp(),
            bottom = maxOf(bottomBarPlaceable.height, insetBottom).toDp(),
        )
        val body = subcompose(ScaffoldSlot.Content) {
            val railStart = if (layoutDirection == LayoutDirection.Ltr) insetStart else insetEnd
            Box { content(contentPadding, railStart.toDp()) }
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
