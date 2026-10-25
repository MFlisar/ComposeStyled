package com.michaelflisar.composestyledcomponents.foundation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.border
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.InputMode
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.LocalInputModeManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyledcomponents.ProvideCustomStyledContentLocals
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.ui.components.StyledFloatingActionButton
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi

/** Shared floating action layout; theme implementations supply their shape defaults. */
@InternalComposeStyledApi
class FoundationFloatingActionButton(
    override val defaults: StyledFloatingActionButton.Defaults,
) : StyledFloatingActionButton.Implementation {

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
        val source = interactionSource ?: remember { MutableInteractionSource() }
        val focused by source.collectIsFocusedAsState()
        val keyboardFocus = focused && LocalInputModeManager.current.inputMode == InputMode.Keyboard
        val size = maxOf(
            StyledTheme.sizes.minimumInteractiveSize,
            when (variant) {
                StyledFloatingActionButton.Variant.Small -> 40.dp
                StyledFloatingActionButton.Variant.Standard -> 56.dp
                StyledFloatingActionButton.Variant.Large -> 96.dp
            },
        )
        ProvideCustomStyledContentLocals(
            contentColor = contentColor,
            textStyle = StyledTheme.typography.labelLarge,
        ) {
            Box(
                modifier = modifier
                    .sizeIn(minWidth = size, minHeight = size)
                    .shadow(6.dp, shape, clip = false)
                    .clip(shape)
                    .background(containerColor)
                    .clickable(interactionSource = source, role = Role.Button, onClick = onClick)
                    .then(
                        if (keyboardFocus) Modifier.border(2.dp, contentColor, shape) else Modifier
                    ),
                contentAlignment = Alignment.Center,
            ) {
                content()
            }
        }
    }
}
