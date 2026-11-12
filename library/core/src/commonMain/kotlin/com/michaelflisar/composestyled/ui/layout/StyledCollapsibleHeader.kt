package com.michaelflisar.composestyled.ui.layout

import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.zIndex
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * Controls how [StyledCollapsibleHeader] responds to downward scrolling.
 */
enum class StyledCollapsibleHeaderScrollBehavior {
    /**
     * Expand the header as soon as the user scrolls downward, even if the content is not at its start.
     */
    EnterAlways,

    /**
     * Expand the header only after the nested scrolling content reaches its start.
     */
    ExitUntilCollapsed,
}

/**
 * State for [StyledCollapsibleHeader].
 *
 * [collapsedFraction] is `0f` when expanded and `1f` when fully collapsed. Use [collapse] and [expand]
 * to animate the header programmatically.
 */
@Stable
class StyledCollapsibleHeaderState internal constructor(
    initialCollapsedFraction: Float,
) {
    private var fraction by mutableFloatStateOf(initialCollapsedFraction.coerceIn(0f, 1f))
    private var animationJob: Job? = null

    val collapsedFraction: Float
        get() = fraction

    /**
     * Animates the header to its fully collapsed position.
     */
    suspend fun collapse() {
        animateTo(1f)
    }

    /**
     * Animates the header to its fully expanded position.
     */
    suspend fun expand() {
        animateTo(0f)
    }

    internal fun dispatchRawDelta(deltaY: Float, headerHeightPx: Int): Float {
        if (deltaY == 0f || headerHeightPx <= 0) return 0f

        animationJob?.cancel()
        animationJob = null

        val previousFraction = fraction
        val newFraction = (previousFraction - deltaY / headerHeightPx).coerceIn(0f, 1f)
        fraction = newFraction
        return -(newFraction - previousFraction) * headerHeightPx
    }

    private suspend fun animateTo(targetFraction: Float) = coroutineScope {
        animationJob?.cancel()
        val job = launch {
            animate(
                initialValue = fraction,
                targetValue = targetFraction,
                animationSpec = tween(durationMillis = 250),
            ) { value, _ ->
                fraction = value
            }
        }
        animationJob = job
        try {
            job.join()
        } finally {
            if (animationJob === job) animationJob = null
        }
    }
}

private val StyledCollapsibleHeaderStateSaver: Saver<StyledCollapsibleHeaderState, Float> =
    Saver(
        save = { it.collapsedFraction },
        restore = { StyledCollapsibleHeaderState(it) },
    )

/**
 * Creates and remembers a saveable [StyledCollapsibleHeaderState].
 *
 * @param initialCollapsedFraction Initial collapse fraction; changing it resets the remembered state.
 */
@Composable
fun rememberStyledCollapsibleHeaderState(
    initialCollapsedFraction: Float = 0f,
): StyledCollapsibleHeaderState =
    rememberSaveable(initialCollapsedFraction, saver = StyledCollapsibleHeaderStateSaver) {
        StyledCollapsibleHeaderState(initialCollapsedFraction)
    }

/**
 * Places a collapsible header and optional sticky header above content.
 *
 * The layout handles scroll input itself, so the content can be scrollable or static. Scrollable
 * children, such as [StyledLazyColumn], participate in nested scrolling. The default
 * [StyledCollapsibleHeaderScrollBehavior.ExitUntilCollapsed] behavior collapses the header before
 * the content scrolls and expands it after the content reaches its start.
 */
@Composable
fun StyledCollapsibleHeader(
    modifier: Modifier = Modifier,
    state: StyledCollapsibleHeaderState = rememberStyledCollapsibleHeaderState(),
    scrollBehavior: StyledCollapsibleHeaderScrollBehavior =
        StyledCollapsibleHeaderScrollBehavior.ExitUntilCollapsed,
    header: @Composable () -> Unit,
    stickyHeader: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val density = LocalDensity.current
    val headerHeightPx = remember { mutableIntStateOf(0) }
    val stickyHeaderHeightPx = remember { mutableIntStateOf(0) }
    val scrollableState = rememberScrollableState { deltaY ->
        state.dispatchRawDelta(deltaY, headerHeightPx.intValue)
    }
    val nestedScrollConnection = remember(state, scrollBehavior) {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val collapsedFractionBefore = state.collapsedFraction
                if (available.y >= 0f) {
                    if (scrollBehavior != StyledCollapsibleHeaderScrollBehavior.EnterAlways) {
                        logScrollEvent(
                            event = "onPreScroll",
                            availableY = available.y,
                            consumedY = 0f,
                            collapsedFractionBefore = collapsedFractionBefore,
                            collapsedFractionAfter = state.collapsedFraction,
                            headerHeightPx = headerHeightPx.intValue,
                            scrollBehavior = scrollBehavior,
                            source = source,
                        )
                        return Offset.Zero
                    }
                }
                val consumed = state.dispatchRawDelta(available.y, headerHeightPx.intValue)
                logScrollEvent(
                    event = "onPreScroll",
                    availableY = available.y,
                    consumedY = consumed,
                    collapsedFractionBefore = collapsedFractionBefore,
                    collapsedFractionAfter = state.collapsedFraction,
                    headerHeightPx = headerHeightPx.intValue,
                    scrollBehavior = scrollBehavior,
                    source = source,
                )
                return Offset(0f, consumed)
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource,
            ): Offset {
                val collapsedFractionBefore = state.collapsedFraction
                if (available.y <= 0f) {
                    logScrollEvent(
                        event = "onPostScroll",
                        availableY = available.y,
                        consumedY = 0f,
                        collapsedFractionBefore = collapsedFractionBefore,
                        collapsedFractionAfter = state.collapsedFraction,
                        headerHeightPx = headerHeightPx.intValue,
                        scrollBehavior = scrollBehavior,
                        source = source,
                    )
                    return Offset.Zero
                }
                if (scrollBehavior != StyledCollapsibleHeaderScrollBehavior.ExitUntilCollapsed) {
                    logScrollEvent(
                        event = "onPostScroll",
                        availableY = available.y,
                        consumedY = 0f,
                        collapsedFractionBefore = collapsedFractionBefore,
                        collapsedFractionAfter = state.collapsedFraction,
                        headerHeightPx = headerHeightPx.intValue,
                        scrollBehavior = scrollBehavior,
                        source = source,
                    )
                    return Offset.Zero
                }
                val consumedY = state.dispatchRawDelta(available.y, headerHeightPx.intValue)
                logScrollEvent(
                    event = "onPostScroll",
                    availableY = available.y,
                    consumedY = consumedY,
                    collapsedFractionBefore = collapsedFractionBefore,
                    collapsedFractionAfter = state.collapsedFraction,
                    headerHeightPx = headerHeightPx.intValue,
                    scrollBehavior = scrollBehavior,
                    source = source,
                )
                return Offset(0f, consumedY)
            }
        }
    }
    val contentTopPadding = with(density) {
        (headerHeightPx.intValue * (1f - state.collapsedFraction)).toDp() +
            (if (stickyHeader == null) 0 else stickyHeaderHeightPx.intValue).toDp()
    }

    Box(
        modifier = modifier
            .nestedScroll(nestedScrollConnection)
            .scrollable(
                state = scrollableState,
                orientation = Orientation.Vertical,
            )
            .clipToBounds(),
    ) {
        Column(
            modifier = Modifier
                .zIndex(1f)
                .offset {
                    IntOffset(
                        x = 0,
                        y = -(headerHeightPx.intValue * state.collapsedFraction).roundToInt(),
                    )
                },
        ) {
            Box(
                Modifier.onSizeChanged { headerHeightPx.intValue = it.height },
            ) {
                header()
            }
            if (stickyHeader != null) {
                Box(
                    Modifier.onSizeChanged { stickyHeaderHeightPx.intValue = it.height },
                ) {
                    stickyHeader()
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(0f)
                .padding(top = contentTopPadding),
        ) {
            content()
        }
    }
}

private fun logScrollEvent(
    event: String,
    availableY: Float,
    consumedY: Float,
    collapsedFractionBefore: Float,
    collapsedFractionAfter: Float,
    headerHeightPx: Int,
    scrollBehavior: StyledCollapsibleHeaderScrollBehavior,
    source: NestedScrollSource,
) {
    println(
        "StyledCollapsibleHeader $event: availableY=$availableY, consumedY=$consumedY, " +
            "collapsedFraction=$collapsedFractionBefore->$collapsedFractionAfter, " +
            "expandedFraction=${1f - collapsedFractionAfter}, " +
            "canExpand=${collapsedFractionAfter > 0f}, " +
            "canCollapse=${collapsedFractionAfter < 1f}, " +
            "mode=$scrollBehavior, source=$source, headerHeightPx=$headerHeightPx",
    )
}
