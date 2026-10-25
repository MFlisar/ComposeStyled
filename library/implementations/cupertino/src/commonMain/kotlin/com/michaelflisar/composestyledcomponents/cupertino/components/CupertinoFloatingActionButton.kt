package com.michaelflisar.composestyledcomponents.cupertino.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Shape
import com.michaelflisar.composestyledcomponents.ui.components.StyledFloatingActionButton
import com.michaelflisar.composestyledcomponents.foundation.components.FoundationFloatingActionButton
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi

/** Adaptive floating action extension, not a standard UIKit control. */
@OptIn(InternalComposeStyledApi::class)
class CupertinoFloatingActionButton internal constructor(
    override val defaults: StyledFloatingActionButton.Defaults,
) : StyledFloatingActionButton.Implementation by FoundationFloatingActionButton(defaults) {


    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create(
            defaults: StyledFloatingActionButton.Defaults = defaults(),
        ) = remember(defaults) { CupertinoFloatingActionButton(defaults) }

        // -------------
        // 3) defaults
        // -------------

        @Composable
        fun defaults(shape: Shape = CircleShape) = StyledFloatingActionButton.Defaults(shape)
    }
}
