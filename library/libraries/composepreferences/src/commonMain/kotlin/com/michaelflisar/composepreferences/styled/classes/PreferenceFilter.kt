package com.michaelflisar.composepreferences.styled

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

/** Search query and options shared by the preference filter field and [com.michaelflisar.composepreferences.styled.layout.PreferenceScreen]. */
@Stable
class PreferenceFilter internal constructor(
    query: String,
    scope: Scope,
    includeHierarchyInFilter: Boolean,
) {
    /** Where a query searches in the preference tree. */
    enum class Scope {
        /** Searches entries on the currently open page only. */
        CurrentLevel,

        /** Searches the currently open page and all of its nested groups. */
        CurrentBranch,

        /** Searches entries throughout the complete preference tree. */
        Global,
    }

    /** Current text shown in the filter field. */
    var query by mutableStateOf(query)

    /** Whether filtering is limited to the open page, its nested groups, or the whole tree. */
    var scope by mutableStateOf(scope)

    /** Whether ancestor text is searchable and shown as grouped result context. */
    var includeHierarchyInFilter by mutableStateOf(includeHierarchyInFilter)
}

private val PreferenceFilterSaver: Saver<PreferenceFilter, List<String>> = Saver(
    save = { filter ->
        listOf(
            filter.query,
            filter.scope.name,
            filter.includeHierarchyInFilter.toString(),
        )
    },
    restore = { saved ->
        PreferenceFilter(
            query = saved[0],
            scope = PreferenceFilter.Scope.valueOf(saved[1]),
            includeHierarchyInFilter = saved[2].toBoolean(),
        )
    },
)

/**
 * Remembers saveable query and filtering options for a [com.michaelflisar.composepreferences.styled.layout.PreferenceScreen].
 *
 * The initial values are used only when creating a new filter; restored values take precedence.
 */
@kotlin.jvm.JvmOverloads
@Composable
fun rememberPreferenceFilter(
    initialQuery: String = "",
    initialScope: PreferenceFilter.Scope = PreferenceFilter.Scope.Global,
    initialIncludeHierarchyInFilter: Boolean = true,
): PreferenceFilter =
    rememberSaveable(saver = PreferenceFilterSaver) {
        PreferenceFilter(
            query = initialQuery,
            scope = initialScope,
            includeHierarchyInFilter = initialIncludeHierarchyInFilter,
        )
    }
