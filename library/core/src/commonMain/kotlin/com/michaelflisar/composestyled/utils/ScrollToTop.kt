package com.michaelflisar.composestyled.utils

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * Scroll host for regular vertical scroll containers. Attach it with
 * [Modifier.supportScrollToTopRequester] to provide viewport coordinates.
 */
class ScrollToTopRequester internal constructor(
    private val scrollState: ScrollState,
    private val topPaddingPx: Int,
    private val animate: Boolean,
) {
    internal var containerCoordinates: LayoutCoordinates? = null

    internal suspend fun scrollToTop(target: LayoutCoordinates, animate: Boolean? = null) {
        val container = checkNotNull(containerCoordinates) {
            "Attach the scroll host with Modifier.supportScrollToTopRequester"
        }
        scrollState.scrollToTop(target, container, topPaddingPx, animate ?: this.animate)
    }

    internal fun distanceToTop(target: LayoutCoordinates): Float? {
        val container = containerCoordinates ?: return null
        if (!target.isAttached || !container.isAttached) {
            return null
        }
        val targetTop = container.localPositionOf(target, Offset.Zero).y
        return targetTop - topPaddingPx
    }
}

/**
 * An item that can independently request positioning at the top of its scroll host.
 * [request] may be called from an event callback such as `onToggle`.
 */
class ScrollToTopTarget internal constructor(
    internal val modifier: Modifier,
    private val scope: CoroutineScope,
    private val distanceToTop: () -> Float?,
    private val requestScroll: suspend (animate: Boolean?) -> Unit,
) {
    private var keepOnTopJob: Job? = null

    /**
     * Requests scrolling this target below the viewport's top padding.
     * When [keepOnTop] is true, the initial request uses the configured animation;
     * later requests immediately follow content-size changes until [stopKeepOnTop] is called.
     */
    suspend fun request(keepOnTop: Boolean = false) {
        stopKeepOnTop()
        if (!keepOnTop) {
            requestScroll(null)
            return
        }

        val initialScrollFinished = CompletableDeferred<Unit>()
        val job = scope.launch {
            val animationJob = launch {
                requestScroll(null)
            }
            var wasBelowTarget = (distanceToTop() ?: 0f) > 0f
            var crossedTarget = false
            while (animationJob.isActive) {
                withFrameNanos { }
                val distance = distanceToTop()
                if (wasBelowTarget && distance != null && distance < -1f) {
                    animationJob.cancel()
                    crossedTarget = true
                    break
                }
                if (distance != null) {
                    wasBelowTarget = distance > 0f
                }
            }
            animationJob.join()
            if (crossedTarget) {
                requestImmediately()
            }
            initialScrollFinished.complete(Unit)
            while (true) {
                withFrameNanos { }
                val distance = distanceToTop() ?: continue
                if (distance > 1f || distance < -1f) {
                    requestImmediately()
                }
            }
        }
        job.invokeOnCompletion { cause ->
            if (cause != null) {
                initialScrollFinished.completeExceptionally(cause)
            }
        }
        keepOnTopJob = job
        initialScrollFinished.await()
    }

    /**
     * Immediately positions this target below the viewport's top padding.
     * Useful when adjusting the target as content changes size.
     */
    suspend fun requestImmediately() {
        requestScroll(false)
    }

    /** Stops any ongoing keep-on-top requests. */
    fun stopKeepOnTop() {
        keepOnTopJob?.cancel()
        keepOnTopJob = null
    }
}

/**
 * Remembers a host for one regular vertical [scrollState]. Attach it to the
 * scroll viewport with [Modifier.supportScrollToTopRequester], and create a
 * [ScrollToTopTarget] for each item that may request scrolling.
 */
@Composable
fun rememberScrollToTopRequester(
    scrollState: ScrollState,
    topPadding: Dp = 0.dp,
    animate: Boolean = true,
): ScrollToTopRequester {
    require(topPadding.value.isFinite() && topPadding >= 0.dp) {
        "topPadding must be finite and non-negative"
    }
    val topPaddingPx = with(LocalDensity.current) { topPadding.roundToPx() }
    return remember(scrollState, topPaddingPx, animate) {
        ScrollToTopRequester(scrollState, topPaddingPx, animate)
    }
}

/**
 * Remembers a target within a regular vertical scroll container.
 * Register the returned target on the item with [Modifier.registerScrollToTopTarget].
 */
@Composable
fun rememberScrollToTopTarget(
    requester: ScrollToTopRequester,
): ScrollToTopTarget {
    val scope = rememberCoroutineScope()
    return remember(requester, scope) {
        var targetCoordinates: LayoutCoordinates? = null
        ScrollToTopTarget(
            modifier = Modifier.onGloballyPositioned { targetCoordinates = it },
            scope = scope,
            distanceToTop = {
                targetCoordinates?.let(requester::distanceToTop)
            },
            requestScroll = { animateOverride ->
                requester.scrollToTop(
                    checkNotNull(targetCoordinates) {
                        "Register the scroll-to-top target before requesting it"
                    },
                    animateOverride,
                )
            },
        )
    }
}

/**
 * Remembers a target at the top of [itemIndex] in a non-reversed vertical lazy
 * list. The target must start at the top of its item; no target or host modifier
 * is needed because the [LazyListState] already tracks item positions.
 */
@Composable
fun rememberScrollToTopTarget(
    lazyListState: LazyListState,
    itemIndex: Int,
    topPadding: Dp = 0.dp,
    animate: Boolean = true,
): ScrollToTopTarget {
    require(itemIndex >= 0) { "itemIndex must be non-negative" }
    require(topPadding.value.isFinite() && topPadding >= 0.dp) {
        "topPadding must be finite and non-negative"
    }
    val topPaddingPx = with(LocalDensity.current) { topPadding.roundToPx() }
    val scope = rememberCoroutineScope()
    return remember(lazyListState, itemIndex, topPaddingPx, animate, scope) {
        ScrollToTopTarget(
            modifier = Modifier,
            scope = scope,
            distanceToTop = {
                val layoutInfo = lazyListState.layoutInfo
                val targetOffset = layoutInfo.visibleItemsInfo
                    .firstOrNull { it.index == itemIndex }
                    ?.offset
                when {
                    targetOffset != null -> (targetOffset - topPaddingPx).toFloat()
                    layoutInfo.visibleItemsInfo.isEmpty() -> null
                    itemIndex < lazyListState.firstVisibleItemIndex -> Float.NEGATIVE_INFINITY
                    else -> Float.POSITIVE_INFINITY
                }
            },
            requestScroll = { animateOverride ->
                lazyListState.scrollToTop(itemIndex, topPaddingPx, animateOverride ?: animate)
            },
        )
    }
}

/**
 * Adds viewport-coordinate tracking for a [ScrollToTopRequester] to a
 * regular vertical scroll container. This is not needed for lazy lists.
 */
fun Modifier.supportScrollToTopRequester(
    requester: ScrollToTopRequester,
): Modifier = then(
    Modifier.onGloballyPositioned { requester.containerCoordinates = it }
)

/**
 * Registers [target] on an item inside a regular vertical scroll container.
 * Lazy-list targets use their item index and do not need this modifier.
 */
fun Modifier.registerScrollToTopTarget(
    target: ScrollToTopTarget,
): Modifier = then(target.modifier)

/**
 * Positions [target] [topPaddingPx] pixels below [container]'s top.
 * Coordinates must belong to the attached target and its vertical scroll viewport.
 * The result is limited by the available scroll range.
 */
suspend fun ScrollState.scrollToTop(
    target: LayoutCoordinates,
    container: LayoutCoordinates,
    topPaddingPx: Int = 0,
    animate: Boolean = true,
) {
    require(topPaddingPx >= 0) { "topPaddingPx must be non-negative" }
    check(target.isAttached && container.isAttached) {
        "The scroll target and viewport must be attached"
    }
    val targetTop = container.localPositionOf(target, Offset.Zero).y
    val position = (value + targetTop - topPaddingPx).roundToInt().coerceIn(0, maxValue)
    if (animate) {
        animateScrollTo(position)
    } else {
        scrollTo(position)
    }
}

/**
 * Positions the top of [itemIndex] [topPaddingPx] pixels below the viewport's top,
 * including the list's content padding. Supports non-reversed vertical lists.
 * The result is limited by the available scroll range.
 */
suspend fun LazyListState.scrollToTop(
    itemIndex: Int,
    topPaddingPx: Int = 0,
    animate: Boolean = true,
) {
    require(topPaddingPx >= 0) { "topPaddingPx must be non-negative" }
    require(itemIndex in 0 until layoutInfo.totalItemsCount) {
        "itemIndex must refer to an existing lazy list item"
    }
    require(layoutInfo.orientation == Orientation.Vertical && !layoutInfo.reverseLayout) {
        "Scroll-to-top requires a non-reversed vertical lazy list"
    }
    val scrollOffset = layoutInfo.beforeContentPadding - topPaddingPx
    if (animate) {
        animateScrollToItem(itemIndex, scrollOffset)
    } else {
        scrollToItem(itemIndex, scrollOffset)
    }
}
