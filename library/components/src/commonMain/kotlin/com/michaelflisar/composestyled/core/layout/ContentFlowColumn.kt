package com.michaelflisar.composestyled.core.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.FlowColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyled.core.StyledTheme

/**
 * A FlowColumn with default paddings and spacings from the StyledTheme
 *
 * padding: [StyledTheme.paddings.medium]
 * spacing: [StyledTheme.spacings.medium]
 *
 */
@Composable
fun ContentFlowColumn(
    modifier: Modifier = Modifier,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    itemHorizontalAlignment: Alignment.Horizontal = Alignment.Start,
    maxItemsInEachColumn: Int = Int.MAX_VALUE,
    maxLines: Int = Int.MAX_VALUE,
    spacing: Dp = StyledTheme.spacings.medium,
    content: @Composable FlowColumnScope.() -> Unit,
) {
    FlowColumn(
        modifier = modifier.padding(StyledTheme.paddings.medium),
        verticalArrangement = Arrangement.spacedBy(spacing, verticalAlignment),
        horizontalArrangement = Arrangement.spacedBy(spacing, horizontalAlignment),
        itemHorizontalAlignment = itemHorizontalAlignment,
        maxItemsInEachColumn = maxItemsInEachColumn,
        maxLines = maxLines,
        content = content
    )
}