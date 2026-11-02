package com.michaelflisar.composestyled.cupertino.liquid.layout

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.michaelflisar.composestyled.ui.layout.StyledScaffold
import com.michaelflisar.composestyled.foundation.layout.FoundationScaffold
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi

@OptIn(InternalComposeStyledApi::class)
class CupertinoLiquidScaffold internal constructor() : FoundationScaffold() {

    override val defaults: StyledScaffold.Defaults
        @Composable get() = StyledScaffold.Defaults(contentWindowInsets = WindowInsets.safeDrawing)

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { CupertinoLiquidScaffold() }
    }
}
