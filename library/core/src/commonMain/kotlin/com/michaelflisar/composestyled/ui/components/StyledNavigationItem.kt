package com.michaelflisar.composestyled.ui.components

import androidx.compose.runtime.Composable

/** A destination shared by bottom and side navigation. Keys must be stable and unique per container. */
data class StyledNavigationItem<K>(
    val key: K,
    val label: @Composable () -> Unit,
    val icon: @Composable () -> Unit,
    val enabled: Boolean = true,
)
