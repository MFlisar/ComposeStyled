package com.michaelflisar.composestyledcomponents.material3.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Snackbar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyledcomponents.ProvideCustomStyledContentLocals
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.ui.components.StyledSnackbar
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi

class Material3Snackbar internal constructor(
    override val defaults: StyledSnackbar.Defaults,
) : StyledSnackbar.Implementation {

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create(defaults: StyledSnackbar.Defaults = defaults()) =
            remember(defaults) { Material3Snackbar(defaults) }

        // -------------
        // 3) defaults
        // -------------

        @Composable
        fun defaults(shape: Shape = RoundedCornerShape(4.dp)) = StyledSnackbar.Defaults(shape)
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
        ProvideCustomStyledContentLocals(
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
