package com.michaelflisar.composestyled.fluent2.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.michaelflisar.composestyled.foundation.components.FoundationPopup
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.components.StyledPopup

@OptIn(InternalComposeStyledApi::class)
class Fluent2Popup internal constructor(
    renderer: StyledPopup.Implementation,
) : StyledPopup.Implementation by renderer {

    companion object {
        @Composable
        fun create(): Fluent2Popup {
            val renderer = FoundationPopup.create()
            return remember { Fluent2Popup(renderer) }
        }
    }
}
