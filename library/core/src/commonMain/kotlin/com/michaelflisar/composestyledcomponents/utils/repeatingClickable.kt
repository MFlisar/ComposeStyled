package com.michaelflisar.composestyledcomponents.utils

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@Composable
internal fun Modifier.repeatingClickable(
    interactionSource: InteractionSource,
    enabled: Boolean,
    initialDelayMillis: Long,
    maxDelayMillis: Long,
    minDelayMillis: Long,
    delayDecayFactor: Float,
    onPress: () -> Unit = {},
    onRepeat: () -> Unit = {},
    onClick: () -> Unit,
): Modifier {
    val currentClickListener by rememberUpdatedState(onClick)
    val currentPressListener by rememberUpdatedState(onPress)
    val currentRepeatListener by rememberUpdatedState(onRepeat)
    return this then Modifier.pointerInput(interactionSource, enabled) {
        coroutineScope {
            awaitEachGesture {
                awaitFirstDown(requireUnconsumed = false)
                currentPressListener()
                var isPressed = true
                val heldButtonJob = launch {
                    var currentDelayMillis = maxDelayMillis
                    delay(initialDelayMillis.milliseconds)
                    while (enabled && isPressed) {
                        delay(currentDelayMillis.milliseconds)
                        if (!isPressed) {
                            break
                        }
                        currentRepeatListener()
                        currentClickListener()
                        val nextMillis =
                            currentDelayMillis - (currentDelayMillis * delayDecayFactor)
                        currentDelayMillis = nextMillis.toLong().coerceAtLeast(minDelayMillis)
                    }
                }
                waitForUpOrCancellation()
                isPressed = false
                heldButtonJob.cancel()
            }
        }
    }
}