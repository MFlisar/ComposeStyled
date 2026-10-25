package com.michaelflisar.composestyledcomponents.cupertino.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.michaelflisar.composestyledcomponents.foundation.components.FoundationTooltip
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyledcomponents.ui.components.StyledTooltip

@OptIn(InternalComposeStyledApi::class)
class CupertinoTooltip internal constructor(
    override val defaults: StyledTooltip.Defaults,
    renderer: StyledTooltip.Implementation,
) : StyledTooltip.Implementation by renderer {

    companion object {
        @Composable
        fun create(defaults: StyledTooltip.Defaults = defaults()): CupertinoTooltip {
            val renderer = FoundationTooltip.create(defaults)
            return remember(defaults) { CupertinoTooltip(defaults, renderer) }
        }

        @Composable
        fun defaults() = FoundationTooltip.defaults()
    }
}
