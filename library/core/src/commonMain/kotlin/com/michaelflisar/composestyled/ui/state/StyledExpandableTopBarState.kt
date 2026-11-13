package com.michaelflisar.composestyled.ui.state

import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

/**
 * Scroll behavior used by [StyledExpandableTopBarScrollBehavior].
 */
enum class StyledExpandableTopBarScrollMode {
    /**
     * Expand the toolbar as soon as the user scrolls downward.
     */
    EnterAlways,

    /**
     * Expand the toolbar after content reaches its start when fully collapsed. If partially
     * expanded, downward scroll continues expanding it before scrolling the content.
     */
    ExitUntilCollapsed,
}

/**
 * Continuous expansion state for [com.michaelflisar.composestyled.ui.components.StyledExpandableTopBar].
 *
 * [collapsedFraction] is `0f` when expanded and `1f` when fully collapsed.
 */
@Stable
class StyledExpandableTopBarState internal constructor(
    initialCollapsedFraction: Float,
) {
    private var fraction by mutableFloatStateOf(initialCollapsedFraction.coerceIn(0f, 1f))
    private var scrollRange by mutableIntStateOf(0)
    private var animationJob: Job? = null

    val collapsedFraction: Float
        get() = fraction

    internal val scrollRangePx: Int
        get() = scrollRange

    /**
     * Animates the toolbar to its fully collapsed position.
     */
    suspend fun collapse() {
        animateTo(1f)
    }

    /**
     * Animates the toolbar to its fully expanded position.
     */
    suspend fun expand() {
        animateTo(0f)
    }

    /** Updates the collapse distance used by theme renderers. */
    @InternalComposeStyledApi
    fun updateScrollRange(heightPx: Int) {
        scrollRange = heightPx.coerceAtLeast(0)
    }

    internal fun dispatchRawDelta(deltaY: Float): Float {
        if (deltaY == 0f || scrollRange <= 0) return 0f

        animationJob?.cancel()
        animationJob = null

        val previousFraction = fraction
        val newFraction = (previousFraction - deltaY / scrollRange).coerceIn(0f, 1f)
        fraction = newFraction
        return -(newFraction - previousFraction) * scrollRange
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

private val StyledExpandableTopBarStateSaver: Saver<StyledExpandableTopBarState, Float> =
    Saver(
        save = { it.collapsedFraction },
        restore = { StyledExpandableTopBarState(it) },
    )

/**
 * Creates and remembers a saveable [StyledExpandableTopBarState].
 *
 * @param initialCollapsedFraction Initial collapse fraction; changing it resets the remembered state.
 */
@Composable
fun rememberStyledExpandableTopBarState(
    initialCollapsedFraction: Float = 0f,
): StyledExpandableTopBarState =
    rememberSaveable(initialCollapsedFraction, saver = StyledExpandableTopBarStateSaver) {
        StyledExpandableTopBarState(initialCollapsedFraction)
    }

/**
 * Couples a toolbar state with its nested-scroll connection.
 *
 * Apply [nestedScrollConnection] to an ancestor of the scrolling content, such as the
 * [com.michaelflisar.composestyled.ui.layout.StyledScaffold] modifier, and pass this behavior to
 * [com.michaelflisar.composestyled.ui.components.StyledExpandableTopBar].
 */
@Stable
class StyledExpandableTopBarScrollBehavior internal constructor(
    val state: StyledExpandableTopBarState,
    val scrollMode: StyledExpandableTopBarScrollMode,
) {
    val nestedScrollConnection: NestedScrollConnection = object : NestedScrollConnection {
        override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
            if (available.y >= 0f &&
                scrollMode != StyledExpandableTopBarScrollMode.EnterAlways &&
                (available.y == 0f || state.collapsedFraction >= 1f)
            ) {
                return Offset.Zero
            }
            return Offset(0f, state.dispatchRawDelta(available.y))
        }

        override fun onPostScroll(
            consumed: Offset,
            available: Offset,
            source: NestedScrollSource,
        ): Offset {
            if (available.y <= 0f ||
                scrollMode != StyledExpandableTopBarScrollMode.ExitUntilCollapsed
            ) {
                return Offset.Zero
            }
            return Offset(0f, state.dispatchRawDelta(available.y))
        }
    }
}

/**
 * Creates and remembers a scroll behavior for an expandable toolbar.
 */
@Composable
fun rememberStyledExpandableTopBarScrollBehavior(
    state: StyledExpandableTopBarState = rememberStyledExpandableTopBarState(),
    scrollMode: StyledExpandableTopBarScrollMode =
        StyledExpandableTopBarScrollMode.ExitUntilCollapsed,
): StyledExpandableTopBarScrollBehavior = remember(state, scrollMode) {
    StyledExpandableTopBarScrollBehavior(state, scrollMode)
}
