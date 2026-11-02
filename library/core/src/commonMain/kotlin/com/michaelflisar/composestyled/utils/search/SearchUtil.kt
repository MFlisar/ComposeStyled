package com.michaelflisar.composestyled.utils.search

/** Stateless filtering and ranking for local collections, independent of UI components. */
object SearchUtil {

    /**
     * Ranks exact matches before prefixes and substrings.
     * Matching is case-insensitive by default; set [ignoreCase] to false for case-sensitive matching.
     * Blank queries preserve input order.
     */
    fun <T> search(
        items: Iterable<T>,
        query: String,
        ignoreCase: Boolean = true,
        text: (T) -> String,
    ): List<T> = search(items, query) { item, searchQuery ->
        val candidate = text(item)
        when {
            candidate.equals(searchQuery, ignoreCase = ignoreCase) -> 3
            candidate.startsWith(searchQuery, ignoreCase = ignoreCase) -> 2
            candidate.contains(searchQuery, ignoreCase = ignoreCase) -> 1
            else -> null
        }
    }

    /**
     * Null scores exclude items; higher scores rank first, with ties preserving input order.
     * Blank queries bypass [score] and preserve input order, including duplicates.
     */
    fun <T> search(
        items: Iterable<T>,
        query: String,
        score: (T, String) -> Int?,
    ): List<T> {
        return if (query.isBlank()) {
            items.toList()
        } else {
            items.mapNotNull { item ->
                score(item, query)?.let { item to it }
            }.sortedByDescending { it.second }.map { it.first }
        }
    }
}
