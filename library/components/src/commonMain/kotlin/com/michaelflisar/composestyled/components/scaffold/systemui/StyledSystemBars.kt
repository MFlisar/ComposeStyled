package com.michaelflisar.composestyled.components.scaffold.systemui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Platform bridge for system bars (status/navigation bar).
 *
 * This is intentionally lightweight and lives in the components module so we can keep
 * Material3 out of the codebase.
 */
@Composable
internal expect fun StyledUpdateStatusBar(
    color: Color,
    darkIcons: Boolean,
)
