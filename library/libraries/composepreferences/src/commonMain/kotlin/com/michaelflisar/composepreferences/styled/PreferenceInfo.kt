package com.michaelflisar.composepreferences.styled

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/** Displays informational text without an interaction action. */
@Composable
fun PreferenceInfo(
    title: String,
    text: String? = null,
    modifier: Modifier = Modifier,
    key: String? = null,
    filterTags: List<String> = emptyList(),
    isEnabled: @Composable () -> Boolean = { true },
    isVisible: @Composable () -> Boolean = { true },
) {
    PreferenceItem(
        title = title,
        subtitle = text,
        modifier = modifier,
        key = key,
        filterTags = filterTags,
        isEnabled = isEnabled,
        isVisible = isVisible,
    )
}
