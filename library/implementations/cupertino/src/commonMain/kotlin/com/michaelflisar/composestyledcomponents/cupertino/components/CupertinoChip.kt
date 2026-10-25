package com.michaelflisar.composestyledcomponents.cupertino.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.michaelflisar.composestyledcomponents.foundation.components.FoundationChip
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyledcomponents.ui.components.StyledChip

@OptIn(InternalComposeStyledApi::class)
class CupertinoChip internal constructor(
    renderer: StyledChip.Implementation,
) : StyledChip.Implementation by renderer {

    companion object {

        @Composable
        fun create(): CupertinoChip {
            val renderer = FoundationChip.create()
            return remember(renderer) { CupertinoChip(renderer) }
        }
    }
}
