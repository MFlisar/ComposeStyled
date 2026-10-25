package com.michaelflisar.composestyledcomponents.ui.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyledcomponents.StyledTheme

@Composable
fun StyledFlowRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    itemVerticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    maxItemsInEachRow: Int = Int.MAX_VALUE,
    maxLines: Int = Int.MAX_VALUE,
    content: @Composable FlowRowScope.() -> Unit,
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement,
        itemVerticalAlignment = itemVerticalAlignment,
        maxItemsInEachRow = maxItemsInEachRow,
        maxLines = maxLines,
        content = content
    )
}

@Composable
fun StyledSpacedFlowRow(
    modifier: Modifier = Modifier,
    itemSpacing: Dp = StyledTheme.spacing.medium,
    rowSpacing: Dp = StyledTheme.spacing.medium,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    itemVerticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    maxItemsInEachRow: Int = Int.MAX_VALUE,
    maxLines: Int = Int.MAX_VALUE,
    content: @Composable FlowRowScope.() -> Unit,
) {
    StyledFlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(itemSpacing, horizontalAlignment),
        verticalArrangement = Arrangement.spacedBy(rowSpacing, verticalAlignment),
        itemVerticalAlignment = itemVerticalAlignment,
        maxItemsInEachRow = maxItemsInEachRow,
        maxLines = maxLines,
        content = content
    )
}