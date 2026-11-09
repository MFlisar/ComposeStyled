package com.michaelflisar.composepreferences.styled.internal

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.ui.primitives.StyledText

@Composable
internal fun FilterSectionInfo(ancestors: List<PreferenceAncestor>, spacingAfter: Dp) {
    val hierarchy = ancestors
        .map { it.title.trim() }
        .filter(String::isNotEmpty)
        .joinToString(" › ")
    if (hierarchy.isEmpty()) return

    StyledText(
        text = hierarchy,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = StyledTheme.padding.large, end = StyledTheme.padding.small, bottom = spacingAfter),
        color = StyledTheme.colorScheme.onSurfaceVariant,
        style = StyledTheme.typography.bodySmall,
    )
}