package com.michaelflisar.composestyled.cupertino.flat.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.michaelflisar.composestyled.foundation.components.FoundationPopup
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.components.StyledPopup

@OptIn(InternalComposeStyledApi::class)
class CupertinoFlatPopup internal constructor(
    renderer: StyledPopup.Implementation,
) : StyledPopup.Implementation by renderer {

    companion object {
        @Composable
        fun create(): CupertinoFlatPopup {
            val renderer = FoundationPopup.create()
            return remember { CupertinoFlatPopup(renderer) }
        }
    }
}
