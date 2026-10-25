package com.michaelflisar.composestyledcomponents.ui.components

import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.MutatorMutex
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull

/**
 * State for a [StyledTooltip]. The state must be remembered with [rememberStyledTooltipState].
 */
@Stable
class StyledTooltipState internal constructor(
    initialIsVisible: Boolean,
    val isPersistent: Boolean
) {
    private val mutatorMutex = MutatorMutex()
    private var showJob: CancellableContinuation<Unit>? = null
    private var visibleState by mutableStateOf(initialIsVisible)

    val isVisible: Boolean
        get() = visibleState

    /**
     * Shows the tooltip and suspends until it is dismissed.
     *
     * A non-persistent tooltip is automatically dismissed by its renderer after the standard
     * tooltip timeout.
     */
    suspend fun show(mutatePriority: MutatePriority = MutatePriority.Default) {
        mutatorMutex.mutate(mutatePriority) {
            try {
                val showTooltip: suspend () -> Unit = {
                    suspendCancellableCoroutine { continuation ->
                        showJob = continuation
                        visibleState = true
                    }
                }
                if (isPersistent) {
                    showTooltip()
                } else {
                    withTimeoutOrNull(TooltipDurationMillis) {
                        showTooltip()
                    }
                }
            } finally {
                visibleState = false
            }
        }

    }

    fun dismiss() {
        visibleState = false
        if (isPersistent) {
            showJob?.cancel()
        }
    }

    @InternalComposeStyledApi
    fun updateVisibilityFromRenderer(isVisible: Boolean) {
        visibleState = isVisible
        if (!isVisible) {
            showJob?.cancel()
        }
    }
}

private const val TooltipDurationMillis = 1_500L

/**
 * Remembers tooltip state. Defaults match Material 3: initially hidden and non-persistent.
 */
@Composable
fun rememberStyledTooltipState(
    initialIsVisible: Boolean = false,
    isPersistent: Boolean = false
): StyledTooltipState = remember(isPersistent) {
    StyledTooltipState(
        initialIsVisible = initialIsVisible,
        isPersistent = isPersistent
    )
}
