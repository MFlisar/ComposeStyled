package com.michaelflisar.composestyledcomponents.ui.layout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
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
import com.michaelflisar.composestyledcomponents.StyledTheme

object StyledScrollbar {
    val scrollbarSize: Dp
        @Composable get() = StyledTheme.padding.medium // TODO: use a proper size for the scrollbar
    val spaceToScrollbar: Dp
        @Composable get() = StyledTheme.padding.small // TODO: use a proper size for the scrollbar
    val overlapScrollbar: Boolean = false
}

@Composable
fun StyledHorizontalScrollbarContainer(
    modifier: Modifier = Modifier,
    alignment: Alignment.Horizontal = Alignment.End,
    scrollState: ScrollState = rememberScrollState(),
    overlapScrollbar: Boolean = StyledScrollbar.overlapScrollbar,
    content: @Composable () -> Unit,
) {
    ScrollbarContainer(
        modifier = modifier,
        alignment = if (alignment == Alignment.Start) ScrollbarAlign.Top else ScrollbarAlign.Bottom,
        scrollState = scrollState,
        overlapScrollbar = overlapScrollbar,
        content = content
    )
}

@Composable
fun StyledVerticalScrollbarContainer(
    modifier: Modifier = Modifier,
    alignment: Alignment.Vertical = Alignment.Bottom,
    scrollState: ScrollState = rememberScrollState(),
    overlapScrollbar: Boolean = StyledScrollbar.overlapScrollbar,
    content: @Composable () -> Unit,
) {
    ScrollbarContainer(
        modifier = modifier,
        alignment = if (alignment == Alignment.Top) ScrollbarAlign.Start else ScrollbarAlign.End,
        scrollState = scrollState,
        overlapScrollbar = overlapScrollbar,
        content = content
    )
}


@Stable
private data class ScrollableData(
    val showScrollbar: Boolean,
    val paddingForScrollbar: Dp,
)

@Composable
private fun rememberScrollableData(
    scrollState: ScrollableState,
    overlapScrollbar: Boolean,
): ScrollableData {
    val showScrollbar by remember {
        derivedStateOf {
            scrollState.canScrollForward || scrollState.canScrollBackward
        }
    }
    val padding =
        if (overlapScrollbar || !showScrollbar) 0.dp else (StyledScrollbar.spaceToScrollbar + StyledScrollbar.scrollbarSize)

    return remember(showScrollbar, padding) {
        ScrollableData(showScrollbar, padding)
    }
}


private enum class ScrollbarAlign {
    Start,
    End,
    Top,
    Bottom
}

@Composable
private fun ScrollbarContainer(
    modifier: Modifier = Modifier,
    alignment: ScrollbarAlign,
    scrollState: ScrollState = rememberScrollState(),
    overlapScrollbar: Boolean = StyledScrollbar.overlapScrollbar,
    content: @Composable () -> Unit,
) {
    val data = rememberScrollableData(scrollState, overlapScrollbar)
    Box(
        modifier = modifier
    ) {
        when (alignment) {
            ScrollbarAlign.Start,
            ScrollbarAlign.End,
                -> {
                val paddingStart =
                    animateDpAsState(if (alignment == ScrollbarAlign.Start) data.paddingForScrollbar else 0.dp)
                val paddingEnd =
                    animateDpAsState(if (alignment == ScrollbarAlign.End) data.paddingForScrollbar else 0.dp)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                        .padding(
                            end = paddingEnd.value,
                            start = paddingStart.value
                        )
                ) {
                    content()
                }
                AnimatedVisibility(
                    modifier = Modifier
                        .align(if (alignment == ScrollbarAlign.Start) Alignment.CenterStart else Alignment.CenterEnd),
                    visible = data.showScrollbar,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    VerticalScrollbar(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(StyledScrollbar.scrollbarSize),
                        adapter = rememberScrollbarAdapter(scrollState)
                    )
                }
            }

            ScrollbarAlign.Top,
            ScrollbarAlign.Bottom,
                -> {
                val paddingTop =
                    animateDpAsState(if (alignment == ScrollbarAlign.Top) data.paddingForScrollbar else 0.dp)
                val paddingBottom =
                    animateDpAsState(if (alignment == ScrollbarAlign.Bottom) data.paddingForScrollbar else 0.dp)
                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .horizontalScroll(scrollState)
                        .padding(
                            top = paddingTop.value,
                            bottom = paddingBottom.value
                        )
                ) {
                    content()
                }
                AnimatedVisibility(
                    modifier = Modifier
                        .align(if (alignment == ScrollbarAlign.Top) Alignment.TopCenter else Alignment.BottomCenter),
                    visible = data.showScrollbar,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    HorizontalScrollbar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(StyledScrollbar.scrollbarSize),
                        adapter = rememberScrollbarAdapter(scrollState)
                    )
                }
            }
        }
    }
}