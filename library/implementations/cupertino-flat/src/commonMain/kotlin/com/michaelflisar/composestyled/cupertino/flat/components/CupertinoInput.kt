package com.michaelflisar.composestyled.cupertino.flat.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.ui.components.StyledInput
import com.michaelflisar.composestyled.foundation.components.FoundationInput
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi

@OptIn(InternalComposeStyledApi::class)
class CupertinoFlatInput internal constructor() : FoundationInput() {

    override val defaults: StyledInput.Defaults
        @Composable get() = StyledInput.Defaults(
            shape = RoundedCornerShape(8.dp),
            actionIconButtonShape = CircleShape,
        )

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { CupertinoFlatInput() }
    }
}
