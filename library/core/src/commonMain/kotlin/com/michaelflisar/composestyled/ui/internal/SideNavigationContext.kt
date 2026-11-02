package com.michaelflisar.composestyled.ui.internal

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import com.michaelflisar.composestyled.ui.components.StyledSideNavigation

@Immutable
internal data class SideNavigationContext(
    val expanded: Boolean = false,
    val compactLabelVisibility: StyledSideNavigation.CompactLabelVisibility = StyledSideNavigation.CompactLabelVisibility.Hidden,
)

internal val LocalStyledSideNavigationContext = compositionLocalOf { SideNavigationContext() }
