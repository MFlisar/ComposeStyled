package com.michaelflisar.composestyled.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

/**
 * Tracks expanded item IDs, optionally allowing only one expanded item via [singleOnly].
 * Can drive StyledExpandableSection's expanded/onToggle parameters to share expansion logic.
 * Use [rememberExpandState] to remember and save the expanded IDs in composition.
 */
@Stable
class ExpandState(
    singleOnly: Boolean = false,
    initialExpandedIds: Set<Int> = emptySet(),
) {
    private var expanded by mutableStateOf(initialExpandedIds)

    private var singleOnlyState by mutableStateOf(singleOnly)

    /** When true, expanding an item collapses all other expanded items. */
    var singleOnly: Boolean
        get() = singleOnlyState
        set(value) {
            if (value && expanded.size > 1) {
                expanded = setOf(expanded.min())
            }
            singleOnlyState = value
        }

    /** A snapshot of the IDs currently expanded. */
    val expandedIds: Set<Int>
        get() = expanded.toSet()

    /** Whether [id] is currently expanded. */
    fun isExpanded(id: Int): Boolean = id in expanded

    /** Expands [id], collapsing other items when [singleOnly] is enabled. */
    fun expand(id: Int) {
        expanded = if (singleOnly) setOf(id) else expanded + id
    }

    /** Collapses [id] if it is expanded. */
    fun collapse(id: Int) {
        expanded = expanded - id
    }

    /** Toggles the expanded state of [id]. */
    fun toggle(id: Int) {
        if (isExpanded(id)) {
            collapse(id)
        } else {
            if (singleOnly)
                collapseAll()
            expand(id)
        }
    }

    /** Collapses every expanded item. */
    fun collapseAll() {
        expanded = emptySet()
    }
}

/**
 * Remembers an [ExpandState] and saves its expanded IDs across state restoration.
 * Updates [singleOnly] on recomposition; changing [initialExpandedIds] resets the state.
 */
@Composable
fun rememberExpandState(
    singleOnly: Boolean = false,
    initialExpandedIds: Set<Int> = emptySet(),
): ExpandState {
    val state = rememberSaveable(
        initialExpandedIds,
        saver = expandStateSaver(singleOnly),
    ) { ExpandState(singleOnly, initialExpandedIds) }
    SideEffect {
        state.singleOnly = singleOnly
    }
    return state
}

private fun expandStateSaver(singleOnly: Boolean): Saver<ExpandState, List<Int>> = Saver(
    save = { it.expandedIds.toList() },
    restore = { ExpandState(singleOnly, it.toSet()) },
)