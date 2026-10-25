package com.michaelflisar.demo.helper

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Stable
class ExpandState(
    singleOnly: Boolean = false,
    initialExpandedIds: Set<Int> = emptySet()
) {
    private var expanded by mutableStateOf(initialExpandedIds)
    private var knownIds = HashSet<Int>()

    private var singleOnlyState by mutableStateOf(singleOnly)

    var singleOnly: Boolean
        get() = singleOnlyState
        set(value) {
            if (value && expanded.size > 1) {
                expanded = setOf(expanded.min())
            }
            singleOnlyState = value
        }

    fun getNextId(): Int {
        val nextId = (knownIds.maxOrNull() ?: -1) + 1
        knownIds.add(nextId)
        return nextId
    }

    val expandedIds: Set<Int>
        get() = expanded.toSet()

    fun isExpanded(id: Int): Boolean = id in expanded

    fun expand(id: Int) {
        expanded = if (singleOnly) setOf(id) else expanded + id
    }

    fun collapse(id: Int) {
        expanded = expanded - id
    }

    fun toggle(id: Int) {
        if (isExpanded(id)) {
            collapse(id)
        } else {
            if (singleOnly)
                collapseAll()
            expand(id)
        }
    }

    fun collapseAll() {
        expanded = emptySet()
    }
}

@Composable
fun rememberExpandState(
    singleOnly: Boolean = false,
    initialExpandedIds: Set<Int> = emptySet()
): ExpandState {
    val state = remember(initialExpandedIds) { ExpandState(singleOnly, initialExpandedIds) }
    SideEffect {
        state.singleOnly = singleOnly
    }
    return state
}