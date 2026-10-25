package com.michaelflisar.composestyledcomponents.fluent2.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.michaelflisar.composestyledcomponents.foundation.components.FoundationChip
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyledcomponents.ui.components.StyledChip

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
