package com.michaelflisar.composestyledcomponents.foundation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyledcomponents.ui.components.StyledSurface
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi

@InternalComposeStyledApi
@Composable
fun FoundationCard(
    modifier: Modifier,
    shape: Shape,
    containerColor: Color,
    contentColor: Color,
    shadowElevation: Dp,
    border: BorderStroke?,
    onClick: (() -> Unit)?,
    enabled: Boolean,
    interactionSource: MutableInteractionSource?,
    content: @Composable () -> Unit,
) {
    StyledSurface(
        modifier = modifier,
        shape = shape,
        color = containerColor,
        contentColor = contentColor,
        shadowElevation = shadowElevation,
        border = border,
    ) {
        Box(
            modifier = if (onClick != null) Modifier.clickable(
                interactionSource = interactionSource,
                indication = LocalIndication.current,
                enabled = enabled,
                role = Role.Button,
                onClick = onClick,
            ) else Modifier,
            propagateMinConstraints = true,
        ) {
            content()
        }
    }
}
