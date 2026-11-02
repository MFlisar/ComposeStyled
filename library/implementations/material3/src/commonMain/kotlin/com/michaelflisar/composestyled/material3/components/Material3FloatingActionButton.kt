package com.michaelflisar.composestyled.material3.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.UpdateStyledThemeLocals
import com.michaelflisar.composestyled.ui.components.StyledFloatingActionButton
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi

class Material3FloatingActionButton internal constructor() : StyledFloatingActionButton.Implementation {

    override val defaults: StyledFloatingActionButton.Defaults
        @Composable get() = StyledFloatingActionButton.Defaults(shape = RoundedCornerShape(16.dp))

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { Material3FloatingActionButton() }
    }

    @OptIn(InternalComposeStyledApi::class)
    @Composable
    override fun Render(
        onClick: () -> Unit,
        modifier: Modifier,
        variant: StyledFloatingActionButton.Variant,
        shape: Shape,
        containerColor: Color,
        contentColor: Color,
        interactionSource: MutableInteractionSource?,
        content: @Composable () -> Unit,
    ) {
        UpdateStyledThemeLocals(contentColor = contentColor) {
            when (variant) {
                StyledFloatingActionButton.Variant.Small -> SmallFloatingActionButton(
                    onClick = onClick,
                    modifier = modifier,
                    shape = shape,
                    containerColor = containerColor,
                    contentColor = contentColor,
                    interactionSource = interactionSource,
                    content = content,
                )
                StyledFloatingActionButton.Variant.Standard -> FloatingActionButton(
                    onClick = onClick,
                    modifier = modifier,
                    shape = shape,
                    containerColor = containerColor,
                    contentColor = contentColor,
                    interactionSource = interactionSource,
                    content = content,
                )
                StyledFloatingActionButton.Variant.Large -> LargeFloatingActionButton(
                    onClick = onClick,
                    modifier = modifier,
                    shape = shape,
                    containerColor = containerColor,
                    contentColor = contentColor,
                    interactionSource = interactionSource,
                    content = content,
                )
            }
        }
    }
}
