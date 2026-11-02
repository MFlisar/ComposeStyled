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

/** Snapshot of keyboard modifiers relevant to selection and range-selection behavior. */
@Stable
data class KeyboardModifierSnapshot(
    val shift: Boolean = false,
    val ctrl: Boolean = false,
    val meta: Boolean = false,
    val alt: Boolean = false,
) {
    /** True when neither range nor toggle-selection modifiers are pressed. */
    val isSingleSelect: Boolean
        get() = !shift && !ctrl && !meta

    /** True when Ctrl or Meta is pressed. */
    val isToggleSelect: Boolean
        get() = ctrl || meta

    /** True when Shift is pressed. */
    val isRangeSelect: Boolean
        get() = shift

    companion object {
        val None = KeyboardModifierSnapshot()
    }
}

/** Tracks keyboard modifiers from preview key events for use by pointer callbacks. */
@Stable
class KeyboardModifierState internal constructor(
    private val isReadOnly: Boolean = false,
) {
    private var currentSnapshot by mutableStateOf(KeyboardModifierSnapshot.None)

    /** Latest modifier snapshot observed by this state. */
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

    /** Updates the snapshot and returns false so the key event remains unconsumed. */
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

    /** Resets the snapshot to no modifiers. */
    fun clear() {
        if (!isReadOnly) {
            currentSnapshot = KeyboardModifierSnapshot.None
        }
    }

    companion object {
        val None = KeyboardModifierState(isReadOnly = true)
    }
}

/** Remembers a modifier tracker scoped to the current composition. */
@Composable
fun rememberKeyboardModifierState(): KeyboardModifierState =
    remember { KeyboardModifierState() }

/** Current keyboard modifier state provided by [ProvideKeyboardModifierState]. */
val LocalKeyboardModifiers = staticCompositionLocalOf { KeyboardModifierState.None }

/** Provides [state] to descendants for keyboard-aware selection handling. */
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

/** Adds preview-key handling that updates [LocalKeyboardModifiers]. */
@Composable
fun Modifier.onKeyboardModifierEvent(): Modifier {
    val state = LocalKeyboardModifiers.current
    return onPreviewKeyEvent(state::onKeyEvent)
}

/** Converts Compose pointer modifier flags to a reusable modifier snapshot. */
fun PointerKeyboardModifiers.toKeyboardModifierSnapshot(): KeyboardModifierSnapshot =
    KeyboardModifierSnapshot(
        shift = isShiftPressed,
        ctrl = isCtrlPressed,
        meta = isMetaPressed,
        alt = isAltPressed,
    )
