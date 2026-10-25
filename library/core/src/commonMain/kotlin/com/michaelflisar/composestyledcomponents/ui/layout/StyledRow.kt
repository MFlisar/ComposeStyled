package com.michaelflisar.composestyledcomponents.ui.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyledcomponents.StyledTheme

object StyledRowDefaults {
    val horizontalArrangement: Arrangement.Horizontal = Arrangement.Start
    val horizontalAlignment: Alignment.Horizontal = Alignment.Start
    val verticalAlignment: Alignment.Vertical = Alignment.CenterVertically
    val itemSpacing: Dp
        @Composable get() = StyledTheme.spacing.medium
}

@Composable
fun StyledRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = StyledRowDefaults.horizontalArrangement,
    verticalAlignment: Alignment.Vertical = StyledRowDefaults.verticalAlignment,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = verticalAlignment,
        horizontalArrangement = horizontalArrangement,
        content = content
    )
}


@Composable
fun StyledSpacedRow(
    modifier: Modifier = Modifier,
    itemSpacing: Dp = StyledRowDefaults.itemSpacing,
    horizontalAlignment: Alignment.Horizontal = StyledRowDefaults.horizontalAlignment,
    verticalAlignment: Alignment.Vertical = StyledRowDefaults.verticalAlignment,
    content: @Composable RowScope.() -> Unit,
) {
    StyledRow(
        modifier = modifier,
        verticalAlignment = verticalAlignment,
        horizontalArrangement = Arrangement.spacedBy(itemSpacing, horizontalAlignment),
        content = content
    )
}