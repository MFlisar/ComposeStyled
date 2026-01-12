package com.michaelflisar.composestyled.core.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyled.core.StyledTheme

/**
 * A FlowRow with default paddings and spacings from the StyledTheme
 *
 * padding: [StyledTheme.paddings.medium]
 * spacing: [StyledTheme.spacings.medium]
 *
 */
@Composable
fun ContentFlowRow(
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    itemVerticalAlignment: Alignment.Vertical = Alignment.Top,
    maxItemsInEachRow: Int = Int.MAX_VALUE,
    maxLines: Int = Int.MAX_VALUE,
    spacing: Dp = StyledTheme.spacings.medium,
    content: @Composable FlowRowScope.() -> Unit,
) {
    FlowRow(
        modifier = modifier.padding(StyledTheme.paddings.medium),
        horizontalArrangement = Arrangement.spacedBy(spacing, horizontalAlignment),
        verticalArrangement = Arrangement.spacedBy(spacing, verticalAlignment),
        itemVerticalAlignment = itemVerticalAlignment,
        maxItemsInEachRow = maxItemsInEachRow,
        maxLines = maxLines,
        content = content
    )
}