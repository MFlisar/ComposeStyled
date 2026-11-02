package com.michaelflisar.composestyled.material3.layout

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.foundation.layout.FoundationBottomSheet
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.layout.StyledBottomSheet

@OptIn(InternalComposeStyledApi::class)
class Material3BottomSheet internal constructor() : FoundationBottomSheet() {
    override val defaults: StyledBottomSheet.Defaults
        @Composable get() = StyledBottomSheet.Defaults(
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            edgeMargin = if (StyledTheme.platform.isDesktopOrWeb()) 24.dp else 16.dp,
        )
    override val verticalActions = false
    override val roleMapping = Material3Dialog.DefaultRoleMapping
    override val actionSpacing
        @Composable get() = 8.dp
    override val actionRowSpacing
        @Composable get() = 12.dp
    override val stackPositiveActionFirst = true
    override val containerColor
        @Composable get() = MaterialTheme.colorScheme.surfaceContainerHigh

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create(): Material3BottomSheet = remember { Material3BottomSheet() }
    }
}
