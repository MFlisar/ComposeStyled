package com.michaelflisar.composestyled.cupertino.liquid.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.michaelflisar.composestyled.foundation.components.FoundationPopup
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.components.StyledPopup

@OptIn(InternalComposeStyledApi::class)
class CupertinoLiquidPopup internal constructor(
    renderer: StyledPopup.Implementation,
) : StyledPopup.Implementation by renderer {

    companion object {
        @Composable
        fun create(): CupertinoLiquidPopup {
            val renderer = FoundationPopup.create()
            return remember { CupertinoLiquidPopup(renderer) }
        }
    }
}
