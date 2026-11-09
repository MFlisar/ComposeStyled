package com.michaelflisar.composestyled.material3.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Snackbar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.material3.ProvideStyledLocals
import com.michaelflisar.composestyled.ui.components.StyledSnackbar
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi

class Material3Snackbar internal constructor() : StyledSnackbar.Implementation {

    override val defaults: StyledSnackbar.Defaults
        @Composable get() = StyledSnackbar.Defaults(shape = RoundedCornerShape(4.dp))

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { Material3Snackbar() }
    }

    @OptIn(InternalComposeStyledApi::class)
    @Composable
    override fun Render(
        modifier: Modifier,
        shape: Shape,
        containerColor: Color,
        contentColor: Color,
        actionOnNewLine: Boolean,
        action: @Composable (() -> Unit)?,
        dismissAction: @Composable (() -> Unit)?,
        content: @Composable () -> Unit,
    ) {
        ProvideStyledLocals(
            contentColor = contentColor,
            textStyle = StyledTheme.typography.bodyMedium,
        ) {
            Snackbar(
                modifier = modifier,
                shape = shape,
                containerColor = containerColor,
                contentColor = contentColor,
                actionContentColor = contentColor,
                dismissActionContentColor = contentColor,
                actionOnNewLine = actionOnNewLine,
                action = action,
                dismissAction = dismissAction,
                content = content,
            )
        }
    }
}
