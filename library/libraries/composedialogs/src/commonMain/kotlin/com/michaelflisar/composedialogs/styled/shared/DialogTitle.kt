package com.michaelflisar.composedialogs.styled

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.michaelflisar.composestyled.StyledTheme

@Composable
internal fun dialogTitle(
    icon: (@Composable () -> Unit)?,
    title: (@Composable () -> Unit)?,
): (@Composable () -> Unit)? {
    if (icon == null && title == null) return null
    return {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(StyledTheme.spacing.small),
        ) {
            icon?.invoke()
            title?.invoke()
        }
    }
}
