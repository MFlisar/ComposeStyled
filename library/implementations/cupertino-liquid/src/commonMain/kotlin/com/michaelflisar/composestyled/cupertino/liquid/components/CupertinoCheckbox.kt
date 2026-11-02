package com.michaelflisar.composestyled.cupertino.liquid.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.ui.components.StyledCheckbox
import com.michaelflisar.composestyled.ui.components.StyledToggleBehavior
import com.michaelflisar.composestyled.foundation.components.FoundationCheckbox
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi

@OptIn(InternalComposeStyledApi::class)
class CupertinoLiquidCheckbox internal constructor() : FoundationCheckbox() {

    override val defaults: StyledCheckbox.Defaults
        @Composable get() = StyledCheckbox.Defaults(
            clickBehavior = StyledToggleBehavior.FullItem,
            shape = RoundedCornerShape(8.dp),
        )

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { CupertinoLiquidCheckbox() }
    }
}
