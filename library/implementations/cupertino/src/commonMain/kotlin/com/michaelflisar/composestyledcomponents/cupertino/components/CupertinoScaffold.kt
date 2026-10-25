package com.michaelflisar.composestyledcomponents.cupertino.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.michaelflisar.composestyledcomponents.ui.components.StyledScaffold
import com.michaelflisar.composestyledcomponents.foundation.components.FoundationScaffold
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi

@OptIn(InternalComposeStyledApi::class)
class CupertinoScaffold internal constructor(
    override val defaults: StyledScaffold.Defaults,
) : StyledScaffold.Implementation by FoundationScaffold(defaults) {

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create(
            defaults: StyledScaffold.Defaults = defaults(),
        ) = remember(defaults) {
            CupertinoScaffold(defaults)
        }

        // -------------
        // 3) defaults
        // -------------

        @Composable
        fun defaults() = StyledScaffold.Defaults()
    }
}
