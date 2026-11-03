package com.michaelflisar.composestyled.ui.components

import androidx.compose.runtime.Composable
import com.michaelflisar.composestyled.classes.IconComposable
import com.michaelflisar.composestyled.ui.primitives.StyledText

/** A destination shared by bottom and side navigation. Keys must be stable and unique per container. */
data class StyledNavigationItem<K>(
    val key: K,
    val label: @Composable () -> Unit,
    val icon: @Composable () -> Unit,
    val enabled: Boolean = true,
) {
    constructor(
        key: K,
        label: String,
        icon: IconComposable?,
        enabled: Boolean = true,
    ) : this(
        key = key,
        label = { StyledText(label) },
        icon = if (icon == null) {
            { }
        } else {
            {
                StyledIcon(
                    icon = icon,
                    contentDescription = label,
                )
            }
        },
        enabled = enabled,
    )
}
