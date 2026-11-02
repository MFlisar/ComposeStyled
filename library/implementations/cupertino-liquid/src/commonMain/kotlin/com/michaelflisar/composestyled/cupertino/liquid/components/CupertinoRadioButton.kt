package com.michaelflisar.composestyled.cupertino.liquid.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.foundation.components.FoundationRadioButton
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.components.StyledRadioButton
import com.michaelflisar.composestyled.ui.components.StyledToggleBehavior

@OptIn(InternalComposeStyledApi::class)
class CupertinoLiquidRadioButton internal constructor() : FoundationRadioButton() {

    override val defaults: StyledRadioButton.Defaults
        @Composable get() = StyledRadioButton.Defaults(
            clickBehavior = StyledToggleBehavior.ControlOnly,
            shape = RoundedCornerShape(percent = 50),
        )

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { CupertinoLiquidRadioButton() }
    }
}

