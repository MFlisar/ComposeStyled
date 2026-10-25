package com.michaelflisar.composestyledcomponents.fluent2.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.michaelflisar.composestyledcomponents.foundation.components.FoundationTooltip
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyledcomponents.ui.components.StyledTooltip

@OptIn(InternalComposeStyledApi::class)
class Fluent2Tooltip internal constructor(
    override val defaults: StyledTooltip.Defaults,
    renderer: StyledTooltip.Implementation,
) : StyledTooltip.Implementation by renderer {

    companion object {
        @Composable
        fun create(defaults: StyledTooltip.Defaults = defaults()): Fluent2Tooltip {
            val renderer = FoundationTooltip.create(defaults)
            return remember(defaults) { Fluent2Tooltip(defaults, renderer) }
        }

        @Composable
        fun defaults() = FoundationTooltip.defaults()
    }
}
