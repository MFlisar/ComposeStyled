package com.michaelflisar.composestyled.cupertino.flat.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.ui.components.StyledSnackbar
import com.michaelflisar.composestyled.foundation.components.FoundationSnackbar
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi

/** Adaptive transient notification, not a native UIKit snackbar. */
@OptIn(InternalComposeStyledApi::class)
class CupertinoFlatSnackbar internal constructor() : FoundationSnackbar() {

    override val defaults: StyledSnackbar.Defaults
        @Composable get() = StyledSnackbar.Defaults(
            shape = RoundedCornerShape(12.dp),
            contentPadding = PaddingValues(
                horizontal = 16.dp,
                vertical = 4.dp,
            ),
        )

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { CupertinoFlatSnackbar() }
    }
}
