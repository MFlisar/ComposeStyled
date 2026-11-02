package com.michaelflisar.composestyled.fluent2.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.foundation.components.FoundationTooltip
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.components.StyledTooltip

@OptIn(InternalComposeStyledApi::class)
class Fluent2Tooltip internal constructor() : FoundationTooltip() {

    override val defaults: StyledTooltip.Defaults
        @Composable get() {
            val shape = StyledTheme.shapes.small
            val containerColor = StyledTheme.colorScheme.surfaceVariant
            val contentColor = StyledTheme.colorScheme.onSurfaceVariant
            val padding = StyledTheme.padding.small
            val contentPadding = remember(padding) {
                PaddingValues(horizontal = padding, vertical = padding)
            }
            return remember(shape, containerColor, contentColor, contentPadding) {
                StyledTooltip.Defaults(
                    shape = shape,
                    containerColor = containerColor,
                    contentColor = contentColor,
                    maxWidth = 240.dp,
                    shadowElevation = 4.dp,
                    contentPadding = contentPadding,
                )
            }
        }

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create(): Fluent2Tooltip = remember { Fluent2Tooltip() }
    }
}
