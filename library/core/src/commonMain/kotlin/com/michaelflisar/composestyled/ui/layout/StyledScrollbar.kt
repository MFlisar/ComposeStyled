package com.michaelflisar.composestyled.ui.layout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.StyledTheme

/**
 * Shared sizing and spacing values for Styled scrollbar layouts.
 */
object StyledScrollbar {

    /** Thickness of the scrollbar track. */
    val scrollbarSize: Dp
        @Composable get() = StyledTheme.padding.medium // TODO: use a proper size for the scrollbar

    /** Space between the scrollable content and a non-overlapping scrollbar. */
    val spaceToScrollbar: Dp
        @Composable get() = StyledTheme.padding.small // TODO: use a proper size for the scrollbar

    /** Whether scrollbar tracks overlap their content by default. */
    val overlapScrollbar: Boolean = false

}

@Stable
private data class ScrollableData(
    val canScroll: Boolean,
    val paddingForScrollbar: Dp,
)

/** Computes indicator visibility and the optional content gutter for a state. */
@Composable
private fun rememberScrollableData(
    state: ScrollableState,
    showScrollbar: Boolean,
    overlapScrollbar: Boolean,
): ScrollableData {
    val canScroll by remember(state) {
        derivedStateOf {
            state.canScrollForward || state.canScrollBackward
        }
    }
    val padding =
        if (!showScrollbar || overlapScrollbar || !canScroll) {
            0.dp
        } else {
            StyledScrollbar.spaceToScrollbar + StyledScrollbar.scrollbarSize
        }

    return remember(canScroll, padding) {
        ScrollableData(canScroll, padding)
    }
}

internal enum class ScrollbarAlign {
    Start,
    End,
    Top,
    Bottom
}

/**
 * Renders the platform-specific indicator for a scrollable state.
 */
@Composable
internal expect fun ScrollbarIndicator(
    modifier: Modifier,
    scrollState: ScrollableState,
    orientation: Orientation,
)

/**
 * Draws a scrollbar indicator for a scrollable state when it can scroll and the
 * active theme allows scrollbar indicators.
 */
@Composable
fun StyledScrollbarIndicator(
    modifier: Modifier,
    state: ScrollableState,
    orientation: Orientation,
    visible: Boolean = true,
) {
    if (!StyledTheme.behavior.showScrollbars)
        return

    val canScroll by remember(state) {
        derivedStateOf {
            state.canScrollForward || state.canScrollBackward
        }
    }
    AnimatedVisibility(
        modifier = modifier,
        visible = visible && canScroll,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        ScrollbarIndicator(
            modifier = Modifier.fillMaxSize(),
            scrollState = state,
            orientation = orientation,
        )
    }
}

/**
 * Adds scrollbar layout to content that manages its own vertical scrolling.
 * Use [StyledVerticalScrollContainer] when this composable should provide the
 * scrolling instead.
 */
@Composable
fun StyledVerticalScrollbarLayout(
    state: ScrollableState,
    modifier: Modifier = Modifier,
    alignment: Alignment.Vertical = Alignment.Bottom,
    overlapScrollbar: Boolean = StyledScrollbar.overlapScrollbar,
    showScrollbar: Boolean = true,
    contentModifier: Modifier = Modifier,
    content: @Composable (Modifier) -> Unit,
) {
    ScrollbarContainer(
        modifier = modifier,
        alignment = if (alignment == Alignment.Top) ScrollbarAlign.Start else ScrollbarAlign.End,
        state = state,
        overlapScrollbar = overlapScrollbar,
        showScrollbar = showScrollbar,
        contentModifier = contentModifier,
        orientation = Orientation.Vertical,
        content = content,
    )
}

/**
 * Adds scrollbar layout to content that manages its own horizontal scrolling.
 * Use [StyledHorizontalScrollContainer] when this composable should provide
 * the scrolling instead.
 */
@Composable
fun StyledHorizontalScrollbarLayout(
    state: ScrollableState,
    modifier: Modifier = Modifier,
    alignment: Alignment.Horizontal = Alignment.End,
    overlapScrollbar: Boolean = StyledScrollbar.overlapScrollbar,
    showScrollbar: Boolean = true,
    contentModifier: Modifier = Modifier,
    content: @Composable (Modifier) -> Unit,
) {
    ScrollbarContainer(
        modifier = modifier,
        alignment = if (alignment == Alignment.Start) ScrollbarAlign.Top else ScrollbarAlign.Bottom,
        state = state,
        overlapScrollbar = overlapScrollbar,
        showScrollbar = showScrollbar,
        contentModifier = contentModifier,
        orientation = Orientation.Horizontal,
        content = content,
    )
}

/**
 * Adds the common scrollbar gutter and indicator around already-scrollable
 * content. When the local or theme-level setting hides scrollbars, it renders
 * content directly with the supplied modifier and does not reserve a gutter.
 */
@Composable
private fun ScrollbarContainer(
    alignment: ScrollbarAlign,
    state: ScrollableState,
    modifier: Modifier,
    overlapScrollbar: Boolean,
    showScrollbar: Boolean,
    contentModifier: Modifier,
    orientation: Orientation,
    content: @Composable (Modifier) -> Unit,
) {
    val shouldShowScrollbar = showScrollbar && StyledTheme.behavior.showScrollbars
    if (!shouldShowScrollbar) {
        content(modifier)
    } else {
        val data = rememberScrollableData(state, shouldShowScrollbar, overlapScrollbar)
        val paddingStart = animateDpAsState(
            if (alignment == ScrollbarAlign.Start) data.paddingForScrollbar else 0.dp
        ).value
        val paddingEnd = animateDpAsState(
            if (alignment == ScrollbarAlign.End) data.paddingForScrollbar else 0.dp
        ).value
        val paddingTop = animateDpAsState(
            if (alignment == ScrollbarAlign.Top) data.paddingForScrollbar else 0.dp
        ).value
        val paddingBottom = animateDpAsState(
            if (alignment == ScrollbarAlign.Bottom) data.paddingForScrollbar else 0.dp
        ).value
        Box(modifier) {
            Box(
                modifier = contentModifier.padding(
                    start = paddingStart,
                    end = paddingEnd,
                    top = paddingTop,
                    bottom = paddingBottom,
                )
            ) {
                content(Modifier)
            }
            Box(Modifier.matchParentSize()) {
                val indicatorModifier = when (orientation) {
                    Orientation.Vertical -> Modifier
                        .align(if (alignment == ScrollbarAlign.Start) Alignment.CenterStart else Alignment.CenterEnd)
                        .fillMaxHeight()
                        .width(StyledScrollbar.scrollbarSize)

                    Orientation.Horizontal -> Modifier
                        .align(if (alignment == ScrollbarAlign.Top) Alignment.TopCenter else Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height(StyledScrollbar.scrollbarSize)
                }
                StyledScrollbarIndicator(
                    modifier = indicatorModifier,
                    state = state,
                    orientation = orientation,
                    visible = data.canScroll,
                )
            }
        }
    }
}

// -------------------------
// public API
// -------------------------

/**
 * Scrolls row content horizontally and shows a matching scrollbar when the
 * local setting and the active theme allow it (the local default is on). Use
 * this when the container should provide scrolling; use
 * [StyledHorizontalScrollbarLayout] for content that already manages its own
 * scrolling. Do not wrap Styled lazy rows or horizontal grids, which already
 * use the shared scrollbar layout.
 * [contentPadding] is applied inside the scrollable content, so start/end
 * padding scrolls with the content rather than reducing the viewport.
 */
@Composable
fun StyledHorizontalScrollContainer(
    modifier: Modifier = Modifier,
    alignment: Alignment.Horizontal = Alignment.End,
    scrollState: ScrollState = rememberScrollState(),
    overlapScrollbar: Boolean = StyledScrollbar.overlapScrollbar,
    showScrollbar: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    content: @Composable () -> Unit,
) {
    StyledHorizontalScrollbarLayout(
        modifier = modifier,
        alignment = alignment,
        state = scrollState,
        overlapScrollbar = overlapScrollbar,
        showScrollbar = showScrollbar,
    ) { contentModifier ->
        Row(
            modifier = contentModifier
                .horizontalScroll(scrollState)
                .padding(contentPadding),
            content = { content() },
        )
    }
}

/**
 * Scrolls column content vertically and shows a matching scrollbar when the
 * local setting and the active theme allow it (the local default is on). Use
 * this when the container should provide scrolling; use
 * [StyledVerticalScrollbarLayout] for content that already manages its own
 * scrolling. Do not wrap Styled lazy columns or vertical grids, which already
 * use the shared scrollbar layout.
 * [contentPadding] is applied inside the scrollable content, so top/bottom
 * padding scrolls with the content rather than reducing the viewport.
 */
@Composable
fun StyledVerticalScrollContainer(
    modifier: Modifier = Modifier,
    alignment: Alignment.Vertical = Alignment.Bottom,
    scrollState: ScrollState = rememberScrollState(),
    overlapScrollbar: Boolean = StyledScrollbar.overlapScrollbar,
    showScrollbar: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    content: @Composable () -> Unit,
) {
    StyledVerticalScrollbarLayout(
        modifier = modifier,
        alignment = alignment,
        state = scrollState,
        overlapScrollbar = overlapScrollbar,
        showScrollbar = showScrollbar,
    ) { contentModifier ->
        Column(
            modifier = contentModifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(contentPadding),
            content = { content() },
        )
    }
}
