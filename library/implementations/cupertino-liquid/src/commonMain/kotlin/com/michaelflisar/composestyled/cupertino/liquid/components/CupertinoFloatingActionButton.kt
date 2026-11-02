package com.michaelflisar.composestyled.cupertino.liquid.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.ui.components.StyledFloatingActionButton
import com.michaelflisar.composestyled.foundation.components.FoundationFloatingActionButton
import com.michaelflisar.composestyled.cupertino.liquid.liquidGlass
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi

/** Adaptive floating action extension, not a standard UIKit control. */
@OptIn(InternalComposeStyledApi::class)
class CupertinoLiquidFloatingActionButton internal constructor() :
    FoundationFloatingActionButton(shadowElevation = 0.dp) {

    override val defaults: StyledFloatingActionButton.Defaults
        @Composable get() = StyledFloatingActionButton.Defaults(shape = CircleShape)

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
        super.Render(
            onClick = onClick,
            modifier = modifier.liquidGlass(shape, containerColor),
            variant = variant,
            shape = shape,
            containerColor = Color.Transparent,
            contentColor = contentColor,
            interactionSource = interactionSource,
            content = content,
        )
    }

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { CupertinoLiquidFloatingActionButton() }
    }
}
