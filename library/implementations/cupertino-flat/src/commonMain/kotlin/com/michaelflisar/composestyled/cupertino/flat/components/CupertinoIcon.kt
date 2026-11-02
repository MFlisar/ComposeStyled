package com.michaelflisar.composestyled.cupertino.flat.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.michaelflisar.composestyled.ui.components.StyledIcon
import com.michaelflisar.composestyled.foundation.components.FoundationIcon
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi

@OptIn(InternalComposeStyledApi::class)
class CupertinoFlatIcon internal constructor() : StyledIcon.Implementation by FoundationIcon() {

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { CupertinoFlatIcon() }
    }
}
