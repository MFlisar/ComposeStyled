package com.michaelflisar.composestyledcomponents.ui.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyledcomponents.StyledTheme

object StyledColumnDefaults {
    val verticalArrangement: Arrangement.Vertical = Arrangement.Top
    val verticalAlignment: Alignment.Vertical = Alignment.Top
    val horizontalAlignment: Alignment.Horizontal = Alignment.Start
    val itemSpacing: Dp
        @Composable get() = StyledTheme.spacing.medium
}

@Composable
fun StyledColumn(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = StyledColumnDefaults.verticalArrangement,
    horizontalAlignment: Alignment.Horizontal = StyledColumnDefaults.horizontalAlignment,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = horizontalAlignment,
        verticalArrangement = verticalArrangement,
        content = content
    )
}

@Composable
fun StyledSpacedColumn(
    modifier: Modifier = Modifier,
    itemSpacing: Dp = StyledColumnDefaults.itemSpacing,
    verticalAlignment: Alignment.Vertical = StyledColumnDefaults.verticalAlignment,
    horizontalAlignment: Alignment.Horizontal = StyledColumnDefaults.horizontalAlignment,
    content: @Composable ColumnScope.() -> Unit,
) {
    StyledColumn(
        modifier = modifier,
        horizontalAlignment = horizontalAlignment,
        verticalArrangement = Arrangement.spacedBy(itemSpacing, verticalAlignment),
        content = content
    )
}



