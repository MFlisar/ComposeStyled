package com.michaelflisar.composestyled.cupertino.liquid.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.foundation.components.FoundationSegmentedButtonRow
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.components.StyledSegmentedButtonRow

@OptIn(InternalComposeStyledApi::class)
class CupertinoLiquidSegmentedButtonRow internal constructor() : FoundationSegmentedButtonRow() {

    override val defaults: StyledSegmentedButtonRow.Defaults
        @Composable get() = StyledSegmentedButtonRow.Defaults(
            itemShape = RoundedCornerShape(percent = 50),
            itemContentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
            minItemWidth = 58.dp,
        )

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create(): CupertinoLiquidSegmentedButtonRow = remember { CupertinoLiquidSegmentedButtonRow() }
    }
}
