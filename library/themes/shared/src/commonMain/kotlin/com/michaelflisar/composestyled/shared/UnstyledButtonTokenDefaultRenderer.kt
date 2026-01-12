package com.michaelflisar.composestyled.shared

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.composeunstyled.UnstyledButton
import com.michaelflisar.composestyled.core.components.StyledButtonDefaults
import com.michaelflisar.composestyled.core.components.StyledButtonTokenRenderer
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.core.runtime.ProvideStyledLocals

@InternalComposeStyledApi
abstract class UnstyledButtonTokenDefaultRenderer : StyledButtonTokenRenderer {

    @Composable
    override fun Render(
        onClick: () -> Unit,
        modifier: Modifier,
        enabled: Boolean,
        shape: Shape,
        backgroundColor: Color,
        contentColor: Color,
        borderColor: Color?,
        contentPadding: PaddingValues,
        interactionSource: MutableInteractionSource,
        content: @Composable (RowScope.() -> Unit),
    ) {
        ProvideStyledLocals(
            contentColor = contentColor,
            backgroundColor = backgroundColor,
        ) {
            val modifier = modifier.heightIn(
                min = StyledButtonDefaults.MinimumHeight
            ).hoverable(
                interactionSource = interactionSource,
                enabled = enabled
            )
            UnstyledButton(
                onClick = onClick,
                enabled = enabled,
                shape = shape,
                backgroundColor = backgroundColor,
                contentColor = contentColor,
                contentPadding = contentPadding,
                borderColor = borderColor ?: Color.Unspecified,
                borderWidth = if (borderColor != null) 1.dp else 0.dp,
                modifier = modifier,
                role = Role.Button,
                indication = LocalIndication.current,
                interactionSource = interactionSource,
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                content = content
            )
        }
    }

}