package com.michaelflisar.composestyled.components.scaffold.systemui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
internal actual fun StyledUpdateStatusBar(
    color: Color,
    darkIcons: Boolean,
) {
    // No-op on non-Android targets.
}
