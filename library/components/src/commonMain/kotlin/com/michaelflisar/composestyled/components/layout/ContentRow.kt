package com.michaelflisar.composestyled.components.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyled.core.StyledTheme

/**
 * A Row with default paddings and spacings from the StyledTheme
 *
 * padding: [StyledTheme.paddings.medium]
 * spacing: [StyledTheme.spacings.medium]
 *
 */
@Composable
fun ContentRow(
    modifier: Modifier = Modifier,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    padding: Dp = StyledTheme.paddings.medium,
    spacing: Dp = StyledTheme.spacings.medium,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier.padding(padding),
        verticalAlignment = verticalAlignment,
        horizontalArrangement = Arrangement.spacedBy(
            space = spacing,
            alignment = horizontalAlignment
        ),
        content = content
    )
}