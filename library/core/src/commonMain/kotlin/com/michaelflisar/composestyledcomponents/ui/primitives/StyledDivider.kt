package com.michaelflisar.composestyledcomponents.ui.primitives

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyledcomponents.StyledTheme

/** Fills the available width. [Dp.Hairline] draws a one-physical-pixel divider. */
@Composable
fun StyledHorizontalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = 1.dp,
    color: Color = StyledTheme.colorScheme.outlineVariant,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(dividerThickness(thickness))
            .background(color),
    )
}

/** Fills the available height; constrain the height directly or through its parent. */
@Composable
fun StyledVerticalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = 1.dp,
    color: Color = StyledTheme.colorScheme.outlineVariant,
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .width(dividerThickness(thickness))
            .background(color),
    )
}

@Composable
private fun dividerThickness(thickness: Dp): Dp {
    require(thickness.value.isFinite() && thickness >= 0.dp) {
        "Divider thickness must be finite and non-negative"
    }
    return if (thickness == Dp.Hairline) {
        with(LocalDensity.current) { 1.toDp() }
    } else {
        thickness
    }
}
