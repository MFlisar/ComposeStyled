package com.michaelflisar.composestyled.ui.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyled.StyledTheme

@Composable
fun StyledGrid(
    columns: Int,
    rows: Int,
    modifier: Modifier = Modifier,
    spacing: Dp = StyledTheme.spacing.medium,
    contentPadding: PaddingValues = PaddingValues(spacing),
    content: @Composable (row: Int, column: Int) -> Unit,
) {
    require(columns > 0) { "StyledGrid requires at least one column." }
    require(rows > 0) { "StyledGrid requires at least one row." }

    Column(
        modifier = modifier.padding(contentPadding),
        verticalArrangement = Arrangement.spacedBy(spacing),
    ) {
        repeat(rows) { row ->
            Row(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(spacing),
            ) {
                repeat(columns) { column ->
                    Box(
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                    ) {
                        content(row, column)
                    }
                }
            }
        }
    }
}
