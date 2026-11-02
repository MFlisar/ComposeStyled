package com.michaelflisar.composestyled.ui.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.input.pointer.PointerKeyboardModifiers
import com.michaelflisar.composestyled.platform.currentPlatform

/** Determines how a list item click changes the selection. */
enum class SelectionClickBehavior {
    /** Toggle on Android/iOS; desktop selection on all other platforms, including Web/Wasm. */
    PlatformDefault,

    /** Select one item, toggle with Ctrl/Meta, or select a range with Shift. */
    Desktop,

    /** Toggle only the clicked item, preserving other selected keys and ignoring keyboard modifiers. */
    Toggle,
}

/** Observable multi-selection state with an anchor for modifier-based range selection. */
@Stable
class SelectionState<K> internal constructor(
    initialSelected: Set<K>,
    initialAnchor: K? = initialSelected.firstOrNull(),
    initiallyActive: Boolean = initialSelected.isNotEmpty(),
) {
    private var selectedState by mutableStateOf(initialSelected)
    private var anchorState by mutableStateOf(initialAnchor)
    private var activeState by mutableStateOf(initiallyActive)

    val selected: Set<K>
        get() = selectedState

    val selectedCount: Int
        get() = selectedState.size

    val anchor: K?
        get() = anchorState

    /** Whether selection mode is active, including when no items are selected. */
    val isActive: Boolean
        get() = activeState

    fun isSelected(key: K): Boolean = key in selectedState

    fun select(key: K) {
        selectedState = setOf(key)
        anchorState = key
        activeState = true
    }

    fun deselect(key: K) {
        selectedState = selectedState - key
        if (anchorState == key) {
            anchorState = selectedState.firstOrNull()
        }
        activeState = true
    }

    fun toggle(key: K) {
        if (isSelected(key)) {
            deselect(key)
        } else {
            selectedState = selectedState + key
            anchorState = key
            activeState = true
        }
    }

    fun set(keys: Collection<K>) {
        selectedState = keys.toSet()
        anchorState = anchorState?.takeIf { it in selectedState } ?: selectedState.firstOrNull()
        activeState = true
    }

    fun selectAll(orderedKeys: List<K>) {
        selectedState = orderedKeys.toSet()
        anchorState = orderedKeys.firstOrNull()
        activeState = true
    }

    fun invert(orderedKeys: List<K>) {
        selectedState = orderedKeys.toSet() - selectedState
        anchorState = orderedKeys.firstOrNull { it in selectedState }
        activeState = true
    }

    fun clearSelection() {
        selectedState = emptySet()
        anchorState = null
        activeState = true
    }

    /** Clears selected keys and exits selection mode. */
    fun clearAndClose() {
        selectedState = emptySet()
        anchorState = null
        activeState = false
    }

    fun start() {
        activeState = true
    }

    /**
     * Applies [behavior] using the current item order for desktop range selection.
     * Platform defaults use the runtime platform, not the theme or pointer type.
     */
    fun handleListItemClick(
        index: Int,
        orderedKeys: List<K>,
        modifiers: KeyboardModifierSnapshot = KeyboardModifierSnapshot.None,
        behavior: SelectionClickBehavior = SelectionClickBehavior.PlatformDefault,
    ) {
        require(index in orderedKeys.indices) {
            "Clicked index $index is outside the item list of size ${orderedKeys.size}."
        }

        val clickedKey = orderedKeys[index]
        val toggleOnly = when (behavior) {
            SelectionClickBehavior.PlatformDefault -> currentPlatform().isMobile()
            SelectionClickBehavior.Desktop -> false
            SelectionClickBehavior.Toggle -> true
        }
        val clickedRangeAnchor = anchorState?.takeIf { it in orderedKeys } ?: clickedKey
        val nextSelected = when {
            toggleOnly -> if (isSelected(clickedKey)) selectedState - clickedKey else selectedState + clickedKey
            modifiers.isRangeSelect -> {
                val anchorIndex = orderedKeys.indexOf(clickedRangeAnchor)
                val range = orderedKeys
                    .subList(minOf(anchorIndex, index), maxOf(anchorIndex, index) + 1)
                    .toSet()
                if (modifiers.isToggleSelect) selectedState + range else range
            }

            modifiers.isToggleSelect && isSelected(clickedKey) -> selectedState - clickedKey
            modifiers.isToggleSelect -> selectedState + clickedKey
            else -> setOf(clickedKey)
        }

        selectedState = nextSelected
        anchorState = if (!toggleOnly && modifiers.isRangeSelect) clickedRangeAnchor else clickedKey
        activeState = true
    }
}

/** Creates selection state outside composition, for example as a screen or ViewModel property. */
fun <K> selectionStateOf(initialSelected: Collection<K> = emptyList()): SelectionState<K> =
    SelectionState(initialSelected.toSet())

/** [initialSelected] is used only when this composition first creates the state. */
@Composable
fun <K> rememberSelectionState(
    initialSelected: Collection<K> = emptyList(),
): SelectionState<K> = remember {
    selectionStateOf(initialSelected)
}

/**
 * Creates a selection state that survives recreation, using [keySaver] for each selected key
 * and the range-selection anchor.
 */
@Composable
fun <K : Any, S : Any> rememberSaveableSelectionState(
    initialSelected: Collection<K> = emptyList(),
    keySaver: Saver<K, S>,
): SelectionState<K> {
    val stateSaver = remember(keySaver) {
        Saver<SelectionState<K>, List<Any>>(
            save = { state ->
                val selected = ArrayList<Any>(state.selected.map { key ->
                    checkNotNull(with(keySaver) { save(key) }) {
                        "The selection key saver returned null for a non-null key."
                    }
                })
                val saved = arrayListOf<Any>(selected, state.anchor != null, state.isActive)
                state.anchor?.let { anchor ->
                    saved += checkNotNull(with(keySaver) { save(anchor) }) {
                        "The selection key saver returned null for a non-null anchor."
                    }
                }
                saved
            },
            restore = { saved ->
                val selected = (saved[0] as List<*>).map { key ->
                    @Suppress("UNCHECKED_CAST")
                    checkNotNull(with(keySaver) { restore(key as S) }) {
                        "The selection key saver could not restore a selected key."
                    }
                }.toSet()
                val anchor = if (saved[1] as Boolean) {
                    @Suppress("UNCHECKED_CAST")
                    checkNotNull(with(keySaver) { restore(saved[3] as S) }) {
                        "The selection key saver could not restore the anchor."
                    }
                } else {
                    null
                }
                SelectionState(
                    initialSelected = selected,
                    initialAnchor = anchor,
                    initiallyActive = saved[2] as Boolean,
                )
            },
        )
    }
    return rememberSaveable(saver = stateSaver) {
        selectionStateOf(initialSelected)
    }
}

/** Reads the current keyboard modifiers when the click callback runs. */
fun <K, T> SelectionState<K>.handleListItemClick(
    index: Int,
    items: List<T>,
    keyboardModifierState: KeyboardModifierState,
    behavior: SelectionClickBehavior = SelectionClickBehavior.PlatformDefault,
    key: (T) -> K,
) {
    val orderedKeys = items.map(key)
    handleListItemClick(
        index = index,
        orderedKeys = orderedKeys,
        modifiers = keyboardModifierState.modifiers,
        behavior = behavior,
    )
}

/** Applies a list click using the supplied pointer keyboard-modifier snapshot. */
fun <K, T> SelectionState<K>.handleListItemClick(
    index: Int,
    items: List<T>,
    pointerModifiers: PointerKeyboardModifiers,
    behavior: SelectionClickBehavior = SelectionClickBehavior.PlatformDefault,
    key: (T) -> K,
) {
    handleListItemClick(
        index = index,
        orderedKeys = items.map(key),
        modifiers = pointerModifiers.toKeyboardModifierSnapshot(),
        behavior = behavior,
    )
}
