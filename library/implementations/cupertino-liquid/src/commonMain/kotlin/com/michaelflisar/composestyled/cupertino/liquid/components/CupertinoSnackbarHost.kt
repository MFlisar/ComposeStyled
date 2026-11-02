package com.michaelflisar.composestyled.cupertino.liquid.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.michaelflisar.composestyled.foundation.components.FoundationSnackbarHost
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.components.StyledSnackbarHost

@OptIn(InternalComposeStyledApi::class)
class CupertinoLiquidSnackbarHost internal constructor() :
    StyledSnackbarHost.Implementation by FoundationSnackbarHost() {

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { CupertinoLiquidSnackbarHost() }
    }
}
