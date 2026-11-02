package com.michaelflisar.composestyled.cupertino.liquid.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.michaelflisar.composestyled.foundation.components.FoundationChip
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.components.StyledChip

@OptIn(InternalComposeStyledApi::class)
class CupertinoLiquidChip internal constructor(
    renderer: StyledChip.Implementation,
) : StyledChip.Implementation by renderer {

    companion object {

        @Composable
        fun create(): CupertinoLiquidChip {
            val renderer = FoundationChip.create(shape = RoundedCornerShape(percent = 50))
            return remember(renderer) { CupertinoLiquidChip(renderer) }
        }
    }
}
