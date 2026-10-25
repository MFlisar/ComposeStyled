package com.michaelflisar.composestyledcomponents.fluent2.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.ui.components.StyledSnackbar
import com.michaelflisar.composestyledcomponents.foundation.components.FoundationSnackbar
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi

@OptIn(InternalComposeStyledApi::class)
class Fluent2Snackbar internal constructor(
    override val defaults: StyledSnackbar.Defaults,
) : StyledSnackbar.Implementation by FoundationSnackbar(defaults) {

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create(defaults: StyledSnackbar.Defaults = defaults()) =
            remember(defaults) { Fluent2Snackbar(defaults) }

        // -------------
        // 3) defaults
        // -------------

        @Composable
        fun defaults(
            shape: Shape = RoundedCornerShape(4.dp),
            contentPadding: PaddingValues = PaddingValues(
                horizontal = 12.dp,
                vertical = 8.dp,
            ),
        ) = StyledSnackbar.Defaults(shape, contentPadding)
    }
}
