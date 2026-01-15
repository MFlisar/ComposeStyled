package com.michaelflisar.composestyled.components.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyled.core.StyledTheme

/**
 * A Column with default paddings and spacings from the StyledTheme
 *
 * padding: [StyledTheme.paddings.medium]
 * spacing: [StyledTheme.spacings.medium]
 *
 */
@Composable
fun ContentColumn(
    modifier: Modifier = Modifier,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    padding: Dp = StyledTheme.paddings.medium,
    spacing: Dp = StyledTheme.spacings.medium,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier.padding(padding),
        horizontalAlignment = horizontalAlignment,
        verticalArrangement = Arrangement.spacedBy(
            space = spacing,
            alignment = verticalAlignment
        ),
        content = content
    )
}