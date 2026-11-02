package com.michaelflisar.composestyled.fluent2.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.michaelflisar.composestyled.foundation.components.FoundationChip
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.components.StyledChip

@OptIn(InternalComposeStyledApi::class)
class Fluent2Chip internal constructor(
    renderer: StyledChip.Implementation,
) : StyledChip.Implementation by renderer {

    companion object {

        @Composable
        fun create(): Fluent2Chip {
            val renderer = FoundationChip.create()
            return remember(renderer) { Fluent2Chip(renderer) }
        }
    }
}
