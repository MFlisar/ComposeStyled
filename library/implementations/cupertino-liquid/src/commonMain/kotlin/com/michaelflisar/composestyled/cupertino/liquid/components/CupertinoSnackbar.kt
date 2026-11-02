package com.michaelflisar.composestyled.cupertino.liquid.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.cupertino.liquid.liquidGlass
import com.michaelflisar.composestyled.ui.components.StyledSnackbar
import com.michaelflisar.composestyled.foundation.components.FoundationSnackbar
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi

/** Adaptive transient notification, not a native UIKit snackbar. */
@OptIn(InternalComposeStyledApi::class)
class CupertinoLiquidSnackbar internal constructor() : FoundationSnackbar() {

    override val defaults: StyledSnackbar.Defaults
        @Composable get() = StyledSnackbar.Defaults(
            shape = RoundedCornerShape(20.dp),
            contentPadding = PaddingValues(
                horizontal = 16.dp,
                vertical = 4.dp,
            ),
        )

    @Composable
    override fun Render(
        modifier: Modifier,
        shape: Shape,
        containerColor: Color,
        contentColor: Color,
        actionOnNewLine: Boolean,
        action: (@Composable () -> Unit)?,
        dismissAction: (@Composable () -> Unit)?,
        content: @Composable () -> Unit,
    ) {
        super.Render(
            modifier = modifier.liquidGlass(shape, containerColor),
            shape = shape,
            containerColor = Color.Transparent,
            contentColor = contentColor,
            actionOnNewLine = actionOnNewLine,
            action = action,
            dismissAction = dismissAction,
            content = content,
        )
    }

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { CupertinoLiquidSnackbar() }
    }
}
