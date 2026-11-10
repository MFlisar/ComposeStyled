package com.michaelflisar.composestyled.foundation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.UpdateStyledThemeLocals
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.internal.LocalStyledSelectionColors
import com.michaelflisar.composestyled.ui.internal.StyledSelectionColors
import com.michaelflisar.composestyled.ui.components.StyledTabRow
import com.michaelflisar.composestyled.ui.layout.StyledHorizontalScrollbarLayout
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@InternalComposeStyledApi
class FoundationTabRow(
    private val indicatorAnimationSpec: FiniteAnimationSpec<Float> = tween(),
) : StyledTabRow.Implementation {

    @Composable
    @OptIn(InternalComposeStyledApi::class)
    override fun Render(
        variant: StyledTabRow.Variant,
        scrollable: Boolean,
        selectedTabIndex: Int,
        modifier: Modifier,
        scrollState: ScrollState?,
        containerColor: Color,
        contentColor: Color,
        edgePadding: Dp,
        minTabWidth: Dp,
        divider: @Composable () -> Unit,
        tabs: @Composable () -> Unit,
    ) {
        val activeScrollState = if (scrollable) {
            requireNotNull(scrollState) { "Scrollable tab rows require a ScrollState" }
        } else {
            null
        }
        val coroutineScope = rememberCoroutineScope()
        val lastAutoScrollTarget = remember { intArrayOf(-1) }
        val indicatorOffset = remember { Animatable(0f) }
        val indicatorWidth = remember { Animatable(0f) }
        val indicatorTargets = remember { IndicatorTargets() }
        val resolvedContentColor = if (contentColor == Color.Unspecified) {
            StyledTheme.contentColorFor(containerColor)
        } else {
            contentColor
        }
        val defaultContainerColor = when (variant) {
            StyledTabRow.Variant.Primary -> StyledTheme.colorScheme.primarySurface
            StyledTabRow.Variant.Secondary -> StyledTheme.colorScheme.secondarySurface
        }
        val defaultContentColor = when (variant) {
            StyledTabRow.Variant.Primary -> StyledTheme.colorScheme.onPrimarySurface
            StyledTabRow.Variant.Secondary -> StyledTheme.colorScheme.onSecondarySurface
        }
        val usesDefaultPalette = containerColor == defaultContainerColor &&
            resolvedContentColor == defaultContentColor
        val defaultSelectedContentColor = StyledTheme.colorScheme.primary
        val selectionColors = remember(
            variant,
            containerColor,
            resolvedContentColor,
            defaultSelectedContentColor,
        ) {
            StyledSelectionColors(
                selectedContentColor = if (usesDefaultPalette) {
                    defaultSelectedContentColor
                } else {
                    resolvedContentColor
                },
                unselectedContentColor = if (usesDefaultPalette) {
                    resolvedContentColor
                } else {
                    resolvedContentColor.copy(alpha = resolvedContentColor.alpha * 0.74f)
                },
                indicatorColor = if (usesDefaultPalette) {
                    defaultSelectedContentColor
                } else {
                    resolvedContentColor
                },
            )
        }
        val indicatorColor = selectionColors.indicatorColor ?: resolvedContentColor
        val indicatorHeight = 2.dp
        val edgePaddingPx = with(LocalDensity.current) { edgePadding.roundToPx() }
        val minScrollableTabWidth = with(LocalDensity.current) { minTabWidth.roundToPx() }
        val indicatorHeightPx = with(LocalDensity.current) { indicatorHeight.roundToPx() }

        fun updateIndicator(targetOffset: Int, targetWidth: Int) {
            if (indicatorTargets.offset == targetOffset && indicatorTargets.width == targetWidth) {
                return
            }
            val initialize = !indicatorTargets.initialized
            indicatorTargets.initialized = true
            indicatorTargets.offset = targetOffset
            indicatorTargets.width = targetWidth
            coroutineScope.launch {
                if (initialize) {
                    indicatorOffset.snapTo(targetOffset.toFloat())
                    indicatorWidth.snapTo(targetWidth.toFloat())
                } else {
                    launch {
                        indicatorOffset.animateTo(targetOffset.toFloat(), indicatorAnimationSpec)
                    }
                    launch {
                        indicatorWidth.animateTo(targetWidth.toFloat(), indicatorAnimationSpec)
                    }
                }
            }
        }

        UpdateStyledThemeLocals(contentColor = resolvedContentColor) {
            BoxWithConstraints(
                modifier = modifier
                    .fillMaxWidth()
                    .background(containerColor)
                    .selectableGroup(),
            ) {
                val viewportWidth = constraints.maxWidth
                Column(Modifier.fillMaxWidth()) {
                    if (scrollable) {
                        val horizontalScrollState = requireNotNull(activeScrollState)
                        StyledHorizontalScrollbarLayout(
                            modifier = Modifier.fillMaxWidth(),
                            state = horizontalScrollState,
                            contentModifier = Modifier.fillMaxWidth(),
                        ) { contentModifier ->
                            Box(contentModifier) {
                                SubcomposeLayout(
                                    modifier = Modifier.horizontalScroll(horizontalScrollState),
                                ) { childConstraints ->
                                    val placeables = subcompose("tabs") {
                                        CompositionLocalProvider(
                                            LocalStyledSelectionColors provides selectionColors,
                                            LocalFoundationTabRowHasIndicator provides true,
                                            content = tabs,
                                        )
                                    }.map { measurable ->
                                        measurable.measure(
                                            childConstraints.copy(
                                                minWidth = minScrollableTabWidth,
                                                maxWidth = Constraints.Infinity,
                                            )
                                        )
                                    }
                                    val contentWidth = placeables.sumOf { it.width } + edgePaddingPx * 2
                                    val contentHeight = placeables.maxOfOrNull { it.height }
                                        ?.coerceAtLeast(childConstraints.minHeight)
                                        ?: childConstraints.minHeight
                                    val selectedTab = placeables.getOrNull(selectedTabIndex)
                                    val indicatorPlaceable = selectedTab?.let {
                                        subcompose("indicator") {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .background(indicatorColor)
                                            )
                                        }.single().measure(
                                            Constraints.fixed(
                                                width = indicatorWidth.value.roundToInt().coerceAtLeast(0),
                                                height = indicatorHeightPx,
                                            )
                                        )
                                    }

                                    layout(contentWidth, contentHeight) {
                                        var left = edgePaddingPx
                                        placeables.forEach { placeable ->
                                            placeable.placeRelative(left, 0)
                                            left += placeable.width
                                        }

                                        if (selectedTab != null && viewportWidth != Constraints.Infinity) {
                                            val tabLeft = edgePaddingPx + placeables
                                                .take(selectedTabIndex)
                                                .sumOf { it.width }
                                            updateIndicator(tabLeft, selectedTab.width)
                                            val visibleWidth = minOf(contentWidth, viewportWidth)
                                            val maxScroll = (contentWidth - visibleWidth).coerceAtLeast(0)
                                            val target = (
                                                tabLeft - (visibleWidth - selectedTab.width) / 2
                                            ).coerceIn(0, maxScroll)
                                            if (target != lastAutoScrollTarget[0]) {
                                                lastAutoScrollTarget[0] = target
                                                coroutineScope.launch {
                                                    horizontalScrollState.animateScrollTo(target)
                                                }
                                            }
                                            indicatorPlaceable?.placeRelative(
                                                x = indicatorOffset.value.roundToInt(),
                                                y = contentHeight - indicatorPlaceable.height,
                                            )
                                        }
                                    }
                                }
                            }
                        }

                    } else {
                        SubcomposeLayout(
                            modifier = Modifier.fillMaxWidth(),
                        ) { rowConstraints ->
                            val measurables = subcompose("tabs") {
                                CompositionLocalProvider(
                                    LocalStyledSelectionColors provides selectionColors,
                                    LocalFoundationTabRowHasIndicator provides true,
                                    content = tabs,
                                )
                            }
                            val availableWidth = rowConstraints.maxWidth
                            val fillWidth = rowConstraints.hasBoundedWidth && measurables.isNotEmpty()
                            val tabWidth = if (fillWidth) availableWidth / measurables.size else 0
                            val remainder = if (fillWidth) availableWidth % measurables.size else 0
                            val placeables = measurables.mapIndexed { index, measurable ->
                                val width = if (fillWidth) {
                                    tabWidth + if (index < remainder) 1 else 0
                                } else {
                                    Constraints.Infinity
                                }
                                measurable.measure(
                                    rowConstraints.copy(
                                        minWidth = if (fillWidth) width else 0,
                                        maxWidth = width,
                                    )
                                )
                            }
                            val rowWidth = if (fillWidth) availableWidth else {
                                placeables.sumOf { it.width }.coerceAtLeast(rowConstraints.minWidth)
                            }
                            val rowHeight = placeables.maxOfOrNull { it.height }
                                ?.coerceAtLeast(rowConstraints.minHeight)
                                ?: rowConstraints.minHeight
                            val selectedTab = placeables.getOrNull(selectedTabIndex)
                            val indicatorPlaceable = selectedTab?.let {
                                subcompose("indicator") {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(indicatorColor)
                                    )
                                }.single().measure(
                                    Constraints.fixed(
                                        width = indicatorWidth.value.roundToInt().coerceAtLeast(0),
                                        height = indicatorHeightPx,
                                    )
                                )
                            }

                            layout(rowWidth, rowHeight) {
                                var left = 0
                                placeables.forEach { placeable ->
                                    placeable.placeRelative(left, 0)
                                    left += placeable.width
                                }
                                if (selectedTab != null) {
                                    val selectedLeft = placeables
                                        .take(selectedTabIndex)
                                        .sumOf { it.width }
                                    updateIndicator(selectedLeft, selectedTab.width)
                                    indicatorPlaceable?.placeRelative(
                                        x = indicatorOffset.value.roundToInt(),
                                        y = rowHeight - indicatorPlaceable.height,
                                    )
                                }
                            }
                        }
                    }

                    divider()
                }
            }
        }
    }
}

internal val LocalFoundationTabRowHasIndicator = staticCompositionLocalOf { false }

private class IndicatorTargets {
    var offset: Int? = null
    var width: Int? = null
    var initialized: Boolean = false
}
