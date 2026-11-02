package com.michaelflisar.composestyled.ui.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

object StyledFlowRowDefaults {
    val horizontalArrangement: Arrangement.Horizontal = Arrangement.Start
    val verticalArrangement: Arrangement.Vertical = Arrangement.Top
    val horizontalAlignment: Alignment.Horizontal = Alignment.Start
    val verticalAlignment: Alignment.Vertical = Alignment.Top
    val itemVerticalAlignment: Alignment.Vertical = Alignment.Top
}

@Composable
fun StyledFlowRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = StyledFlowRowDefaults.horizontalArrangement,
    verticalArrangement: Arrangement.Vertical = StyledFlowRowDefaults.verticalArrangement,
    itemVerticalAlignment: Alignment.Vertical = StyledFlowRowDefaults.itemVerticalAlignment,
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
fun StyledFlowRow(
    itemSpacing: Dp,
    rowSpacing: Dp,
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = StyledFlowRowDefaults.horizontalAlignment,
    verticalAlignment: Alignment.Vertical = StyledFlowRowDefaults.verticalAlignment,
    itemVerticalAlignment: Alignment.Vertical = StyledFlowRowDefaults.itemVerticalAlignment,
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

@Composable
fun StyledFlowRow(
    spacing: Dp,
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = StyledFlowRowDefaults.horizontalAlignment,
    verticalAlignment: Alignment.Vertical = StyledFlowRowDefaults.verticalAlignment,
    itemVerticalAlignment: Alignment.Vertical = StyledFlowRowDefaults.itemVerticalAlignment,
    maxItemsInEachRow: Int = Int.MAX_VALUE,
    maxLines: Int = Int.MAX_VALUE,
    content: @Composable FlowRowScope.() -> Unit,
) {
    StyledFlowRow(
        itemSpacing = spacing,
        rowSpacing = spacing,
        modifier = modifier,
        horizontalAlignment = horizontalAlignment,
        verticalAlignment = verticalAlignment,
        itemVerticalAlignment = itemVerticalAlignment,
        maxItemsInEachRow = maxItemsInEachRow,
        maxLines = maxLines,
        content = content
    )
}