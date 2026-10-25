package com.michaelflisar.composestyledcomponents.material3.components

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
import com.michaelflisar.composestyledcomponents.ProvideCustomStyledContentLocals
import com.michaelflisar.composestyledcomponents.ui.components.StyledFloatingActionButton
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi

class Material3FloatingActionButton internal constructor(
    override val defaults: StyledFloatingActionButton.Defaults,
) : StyledFloatingActionButton.Implementation {

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create(
            defaults: StyledFloatingActionButton.Defaults = defaults(),
        ) = remember(defaults) { Material3FloatingActionButton(defaults) }

        // -------------
        // 3) defaults
        // -------------

        @Composable
        fun defaults(
            shape: Shape = RoundedCornerShape(16.dp),
        ) = StyledFloatingActionButton.Defaults(shape)
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
        ProvideCustomStyledContentLocals(contentColor = contentColor) {
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
