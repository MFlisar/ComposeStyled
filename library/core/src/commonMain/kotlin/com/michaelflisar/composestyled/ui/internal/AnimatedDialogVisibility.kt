package com.michaelflisar.composestyled.ui.internal

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.specials.StyledSwipeActionSide
import com.michaelflisar.composestyled.ui.state.DataDialogState
import com.michaelflisar.composestyled.ui.state.DialogState
import com.michaelflisar.composestyled.ui.state.consumeRestoredVisible
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@InternalComposeStyledApi
const val DIALOG_VISIBILITY_ANIMATION_DURATION_MILLIS = 250

@InternalComposeStyledApi
val LocalStyledDialogEnterAnimationEnabled = staticCompositionLocalOf { true }

@Composable
internal fun rememberDialogEnterAnimationEnabled(state: DialogState): Boolean {
    val skipRestoredEnter = remember(state) { mutableStateOf(state.consumeRestoredVisible()) }
    LaunchedEffect(state, state.visible) {
        if (!state.visible) skipRestoredEnter.value = false
    }
    return !skipRestoredEnter.value
}

@Composable
@OptIn(InternalComposeStyledApi::class)
internal fun rememberDialogCompositionVisible(
    state: DialogState,
    animateExit: Boolean,
): Boolean {
    val retained = remember(state) { mutableStateOf(state.visible) }
    val dataState = state as? DataDialogState<*>
    SideEffect { dataState?.setDismissalAnimationEnabled(animateExit) }
    DisposableEffect(dataState) {
        onDispose {
            dataState?.setDismissalAnimationEnabled(false)
            dataState?.finishDismissalAnimation()
        }
    }
    LaunchedEffect(state, state.visible, animateExit) {
        if (state.visible) {
            retained.value = true
        } else if (animateExit && retained.value) {
            delay(DIALOG_VISIBILITY_ANIMATION_DURATION_MILLIS.milliseconds)
            retained.value = false
            dataState?.finishDismissalAnimation()
        } else {
            retained.value = false
            dataState?.finishDismissalAnimation()
        }
    }
    return state.visible || (animateExit && retained.value)
}
