package com.michaelflisar.composestyled.foundation.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.onConsumedWindowInsetsChanged
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
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
        sideNavigationLayout: StyledScaffold.SideNavigationLayout,
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
                sideNavigation = sideNavigation,
                sideNavigationLayout = sideNavigationLayout,
                snackbarHost = snackbarHost,
                floatingActionButton = floatingActionButton,
                floatingActionButtonPosition = floatingActionButtonPosition,
                containerColor = containerColor,
                contentWindowInsets = contentWindowInsets,
                content = content,
            )
        }
    }
}

@InternalComposeStyledApi
@Composable
fun FoundationScaffoldLayout(
    modifier: Modifier,
    topBar: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit,
    sideNavigation: @Composable () -> Unit,
    sideNavigationLayout: StyledScaffold.SideNavigationLayout,
    snackbarHost: @Composable () -> Unit,
    floatingActionButton: @Composable () -> Unit,
    floatingActionButtonPosition: StyledScaffold.FloatingActionButtonPosition,
    containerColor: Color,
    contentWindowInsets: WindowInsets,
    content: @Composable (PaddingValues) -> Unit,
) {
    val fabPadding = StyledTheme.padding.medium
    val snackbarSpacing = StyledTheme.spacing.medium
    val safeDrawingInsets = WindowInsets.safeDrawing
    val sideNavigationStartInsets = safeDrawingInsets.only(WindowInsetsSides.Start)
    val sideNavigationConsumedInsets = remember { ScaffoldSideNavigationConsumedInsets() }
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
        val sideNavigationBesideTopBar =
            sideNavigationLayout.topBehavior == StyledScaffold.SideNavigationLayout.TopBehavior.BesideTopBar
        val sideNavigationBesideBottomBar =
            sideNavigationLayout.bottomBehavior == StyledScaffold.SideNavigationLayout.BottomBehavior.BesideBottomBar
        val sideNavigationBesideBars =
            sideNavigationBesideTopBar || sideNavigationBesideBottomBar
        val insetLeft = remainingInsets.getLeft(this, layoutDirection)
        val insetTop = remainingInsets.getTop(this)
        val insetRight = remainingInsets.getRight(this, layoutDirection)
        val insetBottom = remainingInsets.getBottom(this)
        val sideNavigationMeasurable = if (sideNavigationBesideBars) {
            subcompose(ScaffoldSlot.SideNavigation) {
                Box(Modifier.consumeWindowInsets(sideNavigationConsumedInsets)) {
                    sideNavigation()
                }
            }.single()
        } else {
            null
        }
        val availableSideNavigationWidth = if (constraints.hasBoundedWidth) {
            (constraints.maxWidth - if (layoutDirection == LayoutDirection.Ltr) insetRight else insetLeft)
                .coerceAtLeast(0)
        } else {
            Constraints.Infinity
        }
        // Resolve the sidebar width before constraining the bars; vertical insets are set before
        // the sidebar is measured below.
        val sideNavigationWidth = sideNavigationMeasurable?.maxIntrinsicWidth(
            if (constraints.hasBoundedHeight) constraints.maxHeight else Constraints.Infinity,
        )?.coerceIn(0, availableSideNavigationWidth) ?: 0
        val topBarConstraints = if (sideNavigationBesideTopBar) {
            childConstraints.copy(
                maxWidth = (constraints.maxWidth - sideNavigationWidth).coerceAtLeast(0),
            )
        } else {
            childConstraints
        }
        val bottomBarConstraints = if (sideNavigationBesideBottomBar) {
            childConstraints.copy(
                maxWidth = (constraints.maxWidth - sideNavigationWidth).coerceAtLeast(0),
            )
        } else {
            childConstraints
        }
        val sideNavigationConsumesStartInset = sideNavigationWidth > 0
        val topBarConsumedInsets = if (sideNavigationBesideTopBar && sideNavigationConsumesStartInset) {
            sideNavigationStartInsets
        } else {
            WindowInsets(0, 0, 0, 0)
        }
        val topBarPlaceable = subcompose(ScaffoldSlot.TopBar) {
            Box(Modifier.consumeWindowInsets(topBarConsumedInsets)) {
                topBar()
            }
        }
            .single().measure(topBarConstraints)
        val bottomBarConsumedInsets =
            if (sideNavigationBesideBottomBar && sideNavigationConsumesStartInset) {
                sideNavigationStartInsets
            } else {
                WindowInsets(0, 0, 0, 0)
            }
        val bottomBarPlaceable = subcompose(ScaffoldSlot.BottomBar) {
            Box(Modifier.consumeWindowInsets(bottomBarConsumedInsets)) {
                bottomBar()
            }
        }
            .single().measure(bottomBarConstraints)

        val topBarObstruction = maxOf(topBarPlaceable.height, insetTop)
        val bottomBarObstruction = maxOf(bottomBarPlaceable.height, insetBottom)
        val sideNavigationTopOffset = if (sideNavigationBesideTopBar) 0 else topBarObstruction
        val sideNavigationBottomOffset = if (sideNavigationBesideBottomBar) 0 else bottomBarObstruction

        val width = if (constraints.hasBoundedWidth) {
            constraints.maxWidth
        } else {
            maxOf(
                constraints.minWidth,
                sideNavigationWidth,
                topBarPlaceable.width + if (sideNavigationBesideTopBar) sideNavigationWidth else 0,
                bottomBarPlaceable.width + if (sideNavigationBesideBottomBar) sideNavigationWidth else 0,
            )
        }

        val contentPadding = PaddingValues(
            start = if (sideNavigationBesideBars && sideNavigationWidth > 0) {
                sideNavigationWidth.toDp()
            } else {
                (if (layoutDirection == LayoutDirection.Ltr) insetLeft else insetRight).toDp()
            },
            top = topBarObstruction.toDp(),
            end = (if (layoutDirection == LayoutDirection.Ltr) insetRight else insetLeft).toDp(),
            bottom = bottomBarObstruction.toDp(),
        )
        if (sideNavigationBesideBars) {
            sideNavigationConsumedInsets.apply {
                top = if (sideNavigationBesideTopBar) 0 else sideNavigationTopOffset
                bottom = if (sideNavigationBesideBottomBar) 0 else sideNavigationBottomOffset
            }
        }
        val sideNavigationHeight = if (constraints.hasBoundedHeight) {
            (constraints.maxHeight - sideNavigationTopOffset - sideNavigationBottomOffset)
                .coerceAtLeast(0)
        } else {
            Constraints.Infinity
        }
        val sideNavigationPlaceable = sideNavigationMeasurable?.measure(
            childConstraints.copy(
                minWidth = sideNavigationWidth,
                maxWidth = sideNavigationWidth,
                minHeight = if (constraints.hasBoundedHeight) sideNavigationHeight else 0,
                maxHeight = sideNavigationHeight,
            ),
        )
        val height = if (constraints.hasBoundedHeight) {
            constraints.maxHeight
        } else {
            maxOf(
                constraints.minHeight,
                topBarPlaceable.height + bottomBarPlaceable.height,
                sideNavigationTopOffset +
                    (sideNavigationPlaceable?.height ?: 0) +
                    sideNavigationBottomOffset,
            ).coerceAtMost(constraints.maxHeight)
        }
        val body = subcompose(ScaffoldSlot.Content) {
            Box {
                if (sideNavigationBesideBars) {
                    content(contentPadding)
                } else {
                    FoundationScaffoldSideNavigationLayout(
                        padding = contentPadding,
                        sideNavigation = sideNavigation,
                        content = content,
                    )
                }
            }
        }.single().measure(
            childConstraints.copy(maxWidth = width, maxHeight = height)
        )
        val overlayConstraints = childConstraints.copy(
            maxWidth = (width - insetLeft - insetRight).coerceAtLeast(0),
            maxHeight = (height - insetTop - insetBottom).coerceAtLeast(0),
        )
        val snackbar = subcompose(ScaffoldSlot.Snackbar) { Box { snackbarHost() } }
            .single().measure(overlayConstraints)
        val fab = subcompose(ScaffoldSlot.FloatingActionButton) { Box { floatingActionButton() } }
            .single().measure(overlayConstraints)

        layout(width, height) {
            body.place(0, 0)
            sideNavigationPlaceable?.placeRelative(0, sideNavigationTopOffset)
            topBarPlaceable.placeRelative(
                if (sideNavigationBesideTopBar) sideNavigationWidth else 0,
                0,
            )
            bottomBarPlaceable.placeRelative(
                if (sideNavigationBesideBottomBar) sideNavigationWidth else 0,
                height - bottomBarPlaceable.height,
            )

            val bottomObstruction = maxOf(bottomBarPlaceable.height, insetBottom)
            val fabObstruction = if (fab.width > 0 && fab.height > 0) {
                fab.height + fabPadding.roundToPx()
            } else {
                0
            }
            snackbar.let {
                val x = insetLeft + (overlayConstraints.maxWidth - it.width) / 2
                val y = (height - bottomObstruction - fabObstruction -
                    snackbarSpacing.roundToPx() - it.height)
                    .coerceAtLeast(0)
                it.place(x, y)
            }
            fab.let {
                val x = when (floatingActionButtonPosition) {
                    StyledScaffold.FloatingActionButtonPosition.Start ->
                        if (layoutDirection == LayoutDirection.Ltr) insetLeft + fabPadding.roundToPx()
                        else width - insetRight - fabPadding.roundToPx() - it.width

                    StyledScaffold.FloatingActionButtonPosition.Center -> (width - it.width) / 2

                    StyledScaffold.FloatingActionButtonPosition.End ->
                        if (layoutDirection == LayoutDirection.Ltr) {
                            width - insetRight - fabPadding.roundToPx() - it.width
                        } else {
                            insetLeft + fabPadding.roundToPx()
                        }
                }
                val y = (height - bottomObstruction - fabPadding.roundToPx() - it.height)
                    .coerceAtLeast(0)
                it.place(
                    x.coerceIn(insetLeft, maxOf(insetLeft, width - insetRight - it.width)),
                    y,
                )
            }
        }
    }
}

private enum class ScaffoldSlot {
    TopBar,
    BottomBar,
    SideNavigation,
    Content,
    Snackbar,
    FloatingActionButton,
}

private class ScaffoldSideNavigationConsumedInsets : WindowInsets {
    var left = 0
    var top = 0
    var right = 0
    var bottom = 0

    override fun getLeft(density: Density, layoutDirection: LayoutDirection) = left
    override fun getTop(density: Density) = top
    override fun getRight(density: Density, layoutDirection: LayoutDirection) = right
    override fun getBottom(density: Density) = bottom
}
