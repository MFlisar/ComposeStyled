package com.michaelflisar.composestyled.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp as lerpColor
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.lerp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.UpdateStyledThemeLocals
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.state.StyledExpandableTopBarScrollBehavior

/**
 * A scroll-driven top bar whose single title smoothly translates and changes text style as it collapses.
 *
 * Attach [behavior]'s nested-scroll connection to an ancestor of the scrolling content, typically
 * the [com.michaelflisar.composestyled.ui.layout.StyledScaffold] modifier. The title remains the
 * same composition throughout the transition and inherits an interpolated Styled text style.
 * When expanded, its leading edge aligns with the navigation icon's leading edge.
 * [expandedHeight] optionally sets the total expanded height, including the regular top bar and
 * window insets. If omitted, the height is derived from the expanded title and [expandedTitlePadding].
 * [expandedTitlePadding] controls the title's bottom breathing room.
 * [expandedContainerColor] and [expandedContentColor] animate to [containerColor] and [contentColor]
 * as the bar collapses; they default to the corresponding compact colors.
 */
@Composable
@OptIn(InternalComposeStyledApi::class)
fun StyledExpandableTopBar(
    behavior: StyledExpandableTopBarScrollBehavior,
    modifier: Modifier = Modifier,
    expandedTitlePadding: Dp = StyledTheme.spacing.medium,
    expandedHeight: Dp? = null,
    containerColor: Color = StyledTheme.colorScheme.surface,
    contentColor: Color = StyledTheme.contentColorFor(containerColor),
    expandedContainerColor: Color = containerColor,
    expandedContentColor: Color = contentColor,
    windowInsets: WindowInsets = WindowInsets.safeDrawing.only(
        WindowInsetsSides.Top + WindowInsetsSides.Horizontal
    ),
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    title: @Composable () -> Unit,
) {
    val fraction = behavior.state.collapsedFraction
    val expandedTitleStyle = StyledTheme.typography.headlineMedium
    val compactTitleStyle = StyledTheme.typography.titleLarge
    val animatedTitleStyle = lerp(expandedTitleStyle, compactTitleStyle, fraction)
    val animatedContainerColor = lerpColor(expandedContainerColor, containerColor, fraction)
    val animatedContentColor = lerpColor(expandedContentColor, contentColor, fraction)
    val titleStyleWithContentColor = animatedTitleStyle.copy(color = animatedContentColor)
    val density = LocalDensity.current
    val topInsetPx = windowInsets.getTop(density)
    val bottomInsetPx = windowInsets.getBottom(density)
    val titleLineHeightPx = with(density) {
        val fontSizePx = expandedTitleStyle.fontSize.toPx()
        when {
            expandedTitleStyle.lineHeight.isSp -> expandedTitleStyle.lineHeight.toPx()
            expandedTitleStyle.lineHeight.isEm ->
                expandedTitleStyle.lineHeight.value * fontSizePx
            else -> fontSizePx
        }
    }
    val titlePaddingPx = with(density) { expandedTitlePadding.roundToPx() }
    val defaultExpandedContentHeightPx =
        (titleLineHeightPx + 2 * titlePaddingPx).toInt()
    val topBarHeightPx = remember { mutableIntStateOf(0) }
    val navigationLeadingEdge = remember { mutableFloatStateOf(Float.NaN) }
    val titleLeadingEdge = remember { mutableFloatStateOf(Float.NaN) }
    val layoutDirection = LocalLayoutDirection.current
    val expandedContentHeightPx = expandedHeight?.let { height ->
        if (topBarHeightPx.intValue == 0) {
            defaultExpandedContentHeightPx
        } else {
            val requestedHeightPx = with(density) { height.roundToPx() }
            (requestedHeightPx - topBarHeightPx.intValue)
                .coerceAtLeast(defaultExpandedContentHeightPx)
        }
    } ?: defaultExpandedContentHeightPx
    SideEffect {
        behavior.state.updateScrollRange(expandedContentHeightPx)
    }
    Layout(
        modifier = modifier
            .fillMaxWidth()
            .clipToBounds()
            .background(animatedContainerColor),
        content = {
            StyledTopBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .onSizeChanged { topBarHeightPx.intValue = it.height },
                containerColor = Color.Transparent,
                contentColor = animatedContentColor,
                windowInsets = windowInsets,
                navigationIcon = {
                    Box(
                        Modifier.onGloballyPositioned { coordinates ->
                            val left = coordinates.positionInRoot().x
                            navigationLeadingEdge.floatValue = if (
                                layoutDirection == LayoutDirection.Ltr
                            ) {
                                left
                            } else {
                                left + coordinates.size.width
                            }
                        },
                    ) {
                        navigationIcon()
                    }
                },
                actions = actions,
                title = {
                    Box(
                        Modifier.onGloballyPositioned { coordinates ->
                            val left = coordinates.positionInRoot().x
                            titleLeadingEdge.floatValue = if (
                                layoutDirection == LayoutDirection.Ltr
                            ) {
                                left
                            } else {
                                left + coordinates.size.width
                            }
                        },
                    ) {
                        Box(
                            Modifier.graphicsLayer {
                                val expandedFraction =
                                    1f - behavior.state.collapsedFraction
                                val horizontalOffset = if (
                                    navigationLeadingEdge.floatValue.isFinite() &&
                                    titleLeadingEdge.floatValue.isFinite()
                                ) {
                                    navigationLeadingEdge.floatValue -
                                        titleLeadingEdge.floatValue
                                } else {
                                    0f
                                }
                                translationX = horizontalOffset * expandedFraction
                                translationY = (
                                    topBarHeightPx.intValue / 2f +
                                        (bottomInsetPx - topInsetPx) / 2f +
                                        expandedContentHeightPx -
                                        titlePaddingPx -
                                        titleLineHeightPx / 2f
                                    ) * expandedFraction
                            },
                        ) {
                            UpdateStyledThemeLocals(
                                contentColor = animatedContentColor,
                                textStyle = titleStyleWithContentColor,
                                content = title,
                            )
                        }
                    }
                },
            )
        },
    ) { measurables, constraints ->
        val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)
        val topBar = measurables.single().measure(looseConstraints)
        val width = topBar.width.coerceIn(constraints.minWidth, constraints.maxWidth)
        val visibleExpandedHeight =
            (expandedContentHeightPx * (1f - fraction)).toInt().coerceAtLeast(0)
        val height = (topBar.height + visibleExpandedHeight)
            .coerceIn(constraints.minHeight, constraints.maxHeight)
        layout(width, height) {
            topBar.placeRelative(0, 0)
        }
    }
}
