package com.michaelflisar.composestyled.ui.primitives

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyled.StyledTheme

/** Adds vertical space using the active theme's medium spacing by default. */
@Composable
fun StyledVerticalSpacer(
    size: Dp = StyledTheme.spacing.medium,
    modifier: Modifier = Modifier,
) {
    Spacer(modifier = modifier.width(size))
}