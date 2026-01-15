package com.michaelflisar.composestyled.components.scaffold

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable

/**
 * A simple item model for navigation containers like [StyledNavigationRail] and [StyledNavigationBottomBar].
 */
@Immutable
data class StyledNavigationItem(
    val selected: Boolean,
    val onClick: () -> Unit,
    val icon: (@Composable () -> Unit)? = null,
    val label: (@Composable () -> Unit)? = null,
    val enabled: Boolean = true,
)