package com.michaelflisar.composepreferences.styled.internal

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateMapOf

internal class PreferenceFilterResultTracker {
    private data class Result(
        val hierarchy: List<String>,
        val order: Int,
    )

    private val results = mutableStateMapOf<String, Result>()
    private val firstInBlock = derivedStateOf {
        val orderedResults = results.entries.sortedBy { it.value.order }
        val firsts = mutableSetOf<String>()
        var previousHierarchy: List<String>? = null
        orderedResults.forEach { (identity, result) ->
            if (result.hierarchy != previousHierarchy) firsts.add(identity)
            previousHierarchy = result.hierarchy
        }
        firsts
    }

    fun update(
        identity: String,
        hierarchy: List<String>,
        order: Int,
        isMatch: Boolean,
    ) {
        if (isMatch) {
            val result = Result(hierarchy = hierarchy, order = order)
            if (results[identity] != result) results[identity] = result
        } else {
            results.remove(identity)
        }
    }

    fun remove(identity: String) {
        results.remove(identity)
    }

    fun startsHierarchyBlock(identity: String): Boolean = identity in firstInBlock.value
}