package com.michaelflisar.composestyledcomponents.cupertino.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.michaelflisar.composestyledcomponents.ui.components.StyledIcon
import com.michaelflisar.composestyledcomponents.foundation.components.FoundationIcon
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi

@OptIn(InternalComposeStyledApi::class)
class CupertinoIcon internal constructor() : StyledIcon.Implementation by FoundationIcon() {

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { CupertinoIcon() }
    }
}
