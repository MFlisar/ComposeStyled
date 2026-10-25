package com.michaelflisar.composestyledcomponents.cupertino.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.michaelflisar.composestyledcomponents.ui.components.StyledCheckbox
import com.michaelflisar.composestyledcomponents.foundation.components.FoundationCheckbox
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi

@OptIn(InternalComposeStyledApi::class)
class CupertinoCheckbox internal constructor() : StyledCheckbox.Implementation by FoundationCheckbox() {
    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { CupertinoCheckbox() }
    }
}
