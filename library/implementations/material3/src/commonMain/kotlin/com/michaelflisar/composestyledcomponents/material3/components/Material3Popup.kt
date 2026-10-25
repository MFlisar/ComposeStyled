package com.michaelflisar.composestyledcomponents.material3.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.michaelflisar.composestyledcomponents.foundation.components.FoundationPopup
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyledcomponents.ui.components.StyledPopup

@OptIn(InternalComposeStyledApi::class)
class Material3Popup internal constructor(
    renderer: StyledPopup.Implementation,
) : StyledPopup.Implementation by renderer {

    companion object {
        @Composable
        fun create(): Material3Popup {
            val renderer = FoundationPopup.create()
            return remember { Material3Popup(renderer) }
        }
    }
}
