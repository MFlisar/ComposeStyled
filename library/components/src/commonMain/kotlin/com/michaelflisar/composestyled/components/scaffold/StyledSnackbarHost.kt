package com.michaelflisar.composestyled.components.scaffold

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.components.StyledButton
import com.michaelflisar.composestyled.core.components.StyledText
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Result of a snackbar interaction.
 */
enum class StyledSnackbarResult {
    Dismissed,
    ActionPerformed,
}

/**
 * Duration for a snackbar.
 */
enum class StyledSnackbarDuration {
    Short,
    Long,
    Indefinite,
}

@Immutable
data class StyledSnackbarVisuals(
    val message: String,
    val actionLabel: String? = null,
    val withDismissAction: Boolean = false,
    val duration: StyledSnackbarDuration = StyledSnackbarDuration.Short,
)

/**
 * The data currently shown by a [StyledSnackbarHost].
 */
interface StyledSnackbarData {
    val visuals: StyledSnackbarVisuals

    /** Dismisses the snackbar. */
    fun dismiss()

    /** Called when user pressed the action button (if any). */
    fun performAction()
}

object StyledSnackbarHostDefaults {

    val enterDurationMillis: Int = 150
    val exitDurationMillis: Int = 75

    fun enterAnimation(enterDurationMillis: Int = StyledSnackbarHostDefaults.enterDurationMillis) =
        fadeIn(animationSpec = tween(enterDurationMillis, easing = LinearOutSlowInEasing)) +
                slideInVertically(
                    animationSpec = tween(enterDurationMillis, easing = LinearOutSlowInEasing),
                    initialOffsetY = { fullHeight -> fullHeight }
                )

    fun exitAnimation(exitDurationMillis: Int = StyledSnackbarHostDefaults.exitDurationMillis) =
        fadeOut(animationSpec = tween(exitDurationMillis, easing = FastOutSlowInEasing)) +
                slideOutVertically(
                    animationSpec = tween(exitDurationMillis, easing = FastOutSlowInEasing),
                    targetOffsetY = { fullHeight -> fullHeight }
                )
}

/**
 * Neutral snackbar host state (no Material dependency).
 *
 * Contract:
 * - Call [showStyledSnackbar] from a coroutine.
 * - The snackbar is shown via [StyledSnackbarHost] that reads [currentStyledSnackbarData].
 */
@Stable
class StyledSnackbarHostState {

    private val mutex = Mutex()

    private data class StyledSnackbarRequest(
        val visuals: StyledSnackbarVisuals,
        val clearQueue: Boolean,
        val completion: CompletableDeferred<StyledSnackbarResult>,
    )

    private val queue = ArrayDeque<StyledSnackbarRequest>()

    private var runnerActive = false

    // "currently displayed" is used for immediate cancel from a new request
    private var activeData: StyledSnackbarDataImpl? by mutableStateOf(null)

    val currentStyledSnackbarData: StyledSnackbarData?
        get() = activeData

    /**
     * Shows a snackbar and suspends until it's dismissed or the action is performed.
     *
     * @param clearQueue If `true`, the currently visible snackbar (if any) will be dismissed and
     * all queued snackbars will be discarded before showing the new one.
     */
    suspend fun showSnackbar(
        message: String,
        actionLabel: String? = null,
        withDismissAction: Boolean = false,
        duration: StyledSnackbarDuration = StyledSnackbarDuration.Short,
        clearQueue: Boolean = false,
    ): StyledSnackbarResult {
        val visuals =
            StyledSnackbarVisuals(
                message = message,
                actionLabel = actionLabel,
                withDismissAction = withDismissAction,
                duration = duration,
            )

        val completion = CompletableDeferred<StyledSnackbarResult>()
        val request = StyledSnackbarRequest(
            visuals = visuals,
            clearQueue = clearQueue,
            completion = completion,
        )

        return coroutineScope {
            val shouldStartRunner: Boolean
            val toDismissNow: StyledSnackbarDataImpl?
            val toCancel: List<CompletableDeferred<StyledSnackbarResult>>

            mutex.withLock {
                if (clearQueue) {
                    // cancel all queued requests immediately
                    toCancel = queue.map { it.completion }
                    queue.clear()
                } else {
                    toCancel = emptyList()
                }

                // enqueue new request
                queue.addLast(request)

                // optional immediate dismiss of current
                toDismissNow = if (clearQueue) activeData else null

                // start runner if needed
                shouldStartRunner = !runnerActive
                if (shouldStartRunner) {
                    runnerActive = true
                }
            }

            // cancel queued awaiters (outside lock)
            toCancel.forEach { it.complete(StyledSnackbarResult.Dismissed) }

            // Important: dismiss outside the lock
            toDismissNow?.dismiss()

            if (shouldStartRunner) {
                launch { runQueue() }
            }

            return@coroutineScope completion.await()
        }
    }

    private suspend fun runQueue() {
        while (true) {
            val next: StyledSnackbarRequest? = mutex.withLock {
                queue.removeFirstOrNull()
            }

            if (next == null) {
                mutex.withLock { runnerActive = false }
                return
            }

            val data = StyledSnackbarDataImpl(next.visuals)
            activeData = data

            try {
                // Wait until dismissed/action or timeout
                if (data.visuals.duration != StyledSnackbarDuration.Indefinite) {
                    val millis = when (data.visuals.duration) {
                        StyledSnackbarDuration.Short -> 4000L
                        StyledSnackbarDuration.Long -> 6000L
                        StyledSnackbarDuration.Indefinite -> Long.MAX_VALUE
                    }
                    coroutineScope {
                        val timeoutJob: Job = launch {
                            delay(millis)
                            data.dismiss()
                        }
                        try {
                            data.finished.await()
                        } finally {
                            timeoutJob.cancel()
                        }
                    }
                } else {
                    data.finished.await()
                }
            } catch (_: CancellationException) {
                // If runner is cancelled, make sure the current snackbar is cleared.
                data.dismiss()
            } finally {
                if (activeData === data) {
                    activeData = null
                }
                next.completion.complete(data.result)
            }
        }
    }

    @Stable
    private class StyledSnackbarDataImpl(
        override val visuals: StyledSnackbarVisuals,
    ) : StyledSnackbarData {
        val finished = CompletableDeferred<Unit>()

        var result: StyledSnackbarResult = StyledSnackbarResult.Dismissed
            private set

        override fun dismiss() {
            if (finished.isCompleted) return
            result = StyledSnackbarResult.Dismissed
            finished.complete(Unit)
        }

        override fun performAction() {
            if (finished.isCompleted) return
            result = StyledSnackbarResult.ActionPerformed
            finished.complete(Unit)
        }
    }
}

/**
 * Layout info produced by [StyledSnackbarHost] and consumable by a container (e.g. Scaffold) for
 * exact placement of dependent UI (e.g. FAB) relative to the snackbar's animation.
 */
@Stable
class StyledSnackbarHostLayoutInfo internal constructor() {
    /** Measured snackbar height in px (0 if none). */
    var snackbarHeightPx by mutableIntStateOf(0)
        internal set

    /** 0f..1f visible fraction (1f means fully visible). */
    var visibilityFraction by mutableStateOf(0f)
        internal set

    /** Height in px currently occupied by the snackbar (height * fraction). */
    val occupiedHeightPx: Float
        get() = snackbarHeightPx * visibilityFraction
}

@Composable
fun rememberStyledSnackbarHostLayoutInfo(): StyledSnackbarHostLayoutInfo =
    remember { StyledSnackbarHostLayoutInfo() }

@Stable
data class StyledSnackbarHostAnimationSpec(
    val enter: EnterTransition,
    val exit: ExitTransition,
    val visibilityFractionSpec: AnimationSpec<Float>,
)

@Composable
fun rememberDefaultStyledSnackbarHostAnimationSpec(
    enterDurationMillis: Int = StyledSnackbarHostDefaults.enterDurationMillis,
    exitDurationMillis: Int = StyledSnackbarHostDefaults.exitDurationMillis,
): StyledSnackbarHostAnimationSpec {
    // Important: the fraction spec must match the *enter* timing, otherwise dependent layouts (FAB)
    // will not move in sync with the snackbar's visual animation.
    val enter = StyledSnackbarHostDefaults.enterAnimation(enterDurationMillis)
    val exit = StyledSnackbarHostDefaults.exitAnimation(exitDurationMillis)
    val fractionSpec: AnimationSpec<Float> = tween(
        durationMillis = enterDurationMillis,
        easing = LinearOutSlowInEasing
    )
    return StyledSnackbarHostAnimationSpec(
        enter = enter,
        exit = exit,
        visibilityFractionSpec = fractionSpec,
    )
}

/**
 * Neutral snackbar host.
 *
 * This does NOT implement swipe-to-dismiss yet.
 */
@Composable
fun StyledSnackbarHost(
    hostState: StyledSnackbarHostState,
    modifier: Modifier = Modifier,
    padding: PaddingValues = PaddingValues(0.dp),
    snackbarShape: Shape = StyledTheme.shapes.control,
    containerColor: Color = StyledTheme.colors.surfaceVariant,
    contentColor: Color = StyledTheme.colors.onSurfaceVariant,
    minHeight: Dp = StyledTheme.sizes.minimumInteractiveSize,
    textStyle: TextStyle = StyledTheme.typography.bodyMedium,
    actionTextStyle: TextStyle = StyledTheme.typography.labelMedium,
    actionColor: Color = contentColor,
    dismissActionLabel: String = "Dismiss",
    animationSpec: @Composable () -> StyledSnackbarHostAnimationSpec = { rememberDefaultStyledSnackbarHostAnimationSpec() },
    layoutInfo: StyledSnackbarHostLayoutInfo? = null,
) {
    val data = hostState.currentStyledSnackbarData
    val animSpec = animationSpec()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(padding),
        contentAlignment = Alignment.BottomCenter
    ) {
        val visible = data != null

        val fraction by animateFloatAsState(
            targetValue = if (visible) 1f else 0f,
            animationSpec = animSpec.visibilityFractionSpec,
            label = "snackbarVisibilityFraction"
        )
        layoutInfo?.visibilityFraction = fraction

        AnimatedVisibility(
            visible = visible,
            enter = animSpec.enter,
            exit = animSpec.exit,
        ) {
            val d = data ?: return@AnimatedVisibility

            Row(
                modifier = Modifier
                    .defaultMinSize(minHeight = minHeight)
                    .background(containerColor, snackbarShape)
                    .onSizeChanged { size: IntSize ->
                        layoutInfo?.snackbarHeightPx = size.height
                    }
                    .padding(
                        horizontal = StyledTheme.paddings.large,
                        vertical = StyledTheme.paddings.medium
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                StyledText(
                    text = d.visuals.message,
                    style = textStyle,
                    color = contentColor,
                    modifier = Modifier.weight(1f, fill = true)
                )

                val action = d.visuals.actionLabel
                if (action != null) {
                    Spacer(Modifier.width(StyledTheme.spacings.medium))
                    StyledButton(
                        variant = StyledButton.Variant.Text,
                        onClick = { d.performAction() }
                    ) {
                        StyledText(
                            text = action,
                            style = actionTextStyle,
                            color = actionColor,
                        )
                    }
                } else if (d.visuals.withDismissAction) {
                    Spacer(Modifier.width(StyledTheme.spacings.medium))
                    StyledButton(
                        variant = StyledButton.Variant.Text,
                        onClick = { d.dismiss() }
                    ) {
                        StyledText(
                            text = dismissActionLabel,
                            style = actionTextStyle,
                            color = actionColor,
                        )
                    }
                }
            }
        }

        if (!visible) {
            layoutInfo?.snackbarHeightPx = 0
        }
    }
}
