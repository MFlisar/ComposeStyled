package com.michaelflisar.composestyled.cupertino.flat.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Shape
import com.michaelflisar.composestyled.ui.components.StyledFloatingActionButton
import com.michaelflisar.composestyled.foundation.components.FoundationFloatingActionButton
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi

/** Adaptive floating action extension, not a standard UIKit control. */
@OptIn(InternalComposeStyledApi::class)
class CupertinoFlatFloatingActionButton internal constructor() : FoundationFloatingActionButton() {

    override val defaults: StyledFloatingActionButton.Defaults
        @Composable get() = StyledFloatingActionButton.Defaults(shape = CircleShape)

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { CupertinoFlatFloatingActionButton() }
    }
}
