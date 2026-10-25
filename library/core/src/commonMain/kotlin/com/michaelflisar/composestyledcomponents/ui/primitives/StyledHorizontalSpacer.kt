package com.michaelflisar.composestyledcomponents.ui.primitives

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyledcomponents.StyledTheme

@Composable
fun StyledHorizontalSpacer(
    size: Dp = StyledTheme.spacing.medium,
    modifier: Modifier = Modifier,
) {
    Spacer(modifier = modifier.height(size))
}