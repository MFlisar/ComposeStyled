package com.michaelflisar.composestyledcomponents.ui.components

import androidx.compose.runtime.Composable

/** A destination shared by bottom navigation and rail. Keys must be stable and unique per container. */
data class StyledNavigationItem<K : Any>(
    val key: K,
    val label: @Composable () -> Unit,
    val icon: @Composable () -> Unit,
    val enabled: Boolean = true,
)
