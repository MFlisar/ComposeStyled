package com.michaelflisar.composestyled.ui.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.isAltPressed
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.isMetaPressed
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.pointer.PointerKeyboardModifiers
import androidx.compose.ui.input.pointer.isAltPressed
import androidx.compose.ui.input.pointer.isCtrlPressed
import androidx.compose.ui.input.pointer.isMetaPressed
import androidx.compose.ui.input.pointer.isShiftPressed

@Stable
data class KeyboardModifierSnapshot(
    val shift: Boolean = false,
    val ctrl: Boolean = false,
    val meta: Boolean = false,
    val alt: Boolean = false,
) {
    val isSingleSelect: Boolean
        get() = !shift && !ctrl && !meta

    val isToggleSelect: Boolean
        get() = ctrl || meta

    val isRangeSelect: Boolean
        get() = shift

    companion object {
        val None = KeyboardModifierSnapshot()
    }
}

@Stable
class KeyboardModifierState internal constructor(
    private val isReadOnly: Boolean = false,
) {
    private var currentSnapshot by mutableStateOf(KeyboardModifierSnapshot.None)

    val modifiers: KeyboardModifierSnapshot
        get() = currentSnapshot

    val isShiftPressed: Boolean
        get() = modifiers.shift

    val isCtrlPressed: Boolean
        get() = modifiers.ctrl

    val isMetaPressed: Boolean
        get() = modifiers.meta

    val isAltPressed: Boolean
        get() = modifiers.alt

    fun onKeyEvent(event: KeyEvent): Boolean {
        if (!isReadOnly) {
            currentSnapshot = KeyboardModifierSnapshot(
                shift = event.isShiftPressed,
                ctrl = event.isCtrlPressed,
                meta = event.isMetaPressed,
                alt = event.isAltPressed,
            )
        }
        return false
    }

    fun clear() {
        if (!isReadOnly) {
            currentSnapshot = KeyboardModifierSnapshot.None
        }
    }

    companion object {
        val None = KeyboardModifierState(isReadOnly = true)
    }
}

@Composable
fun rememberKeyboardModifierState(): KeyboardModifierState =
    remember { KeyboardModifierState() }

val LocalKeyboardModifiers = staticCompositionLocalOf { KeyboardModifierState.None }

@Composable
fun ProvideKeyboardModifierState(
    state: KeyboardModifierState,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalKeyboardModifiers provides state,
        content = content,
    )
}

@Composable
fun Modifier.onKeyboardModifierEvent(): Modifier {
    val state = LocalKeyboardModifiers.current
    return onPreviewKeyEvent(state::onKeyEvent)
}

fun PointerKeyboardModifiers.toKeyboardModifierSnapshot(): KeyboardModifierSnapshot =
    KeyboardModifierSnapshot(
        shift = isShiftPressed,
        ctrl = isCtrlPressed,
        meta = isMetaPressed,
        alt = isAltPressed,
    )
