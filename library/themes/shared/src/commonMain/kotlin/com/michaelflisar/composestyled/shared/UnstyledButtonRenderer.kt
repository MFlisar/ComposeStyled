package com.michaelflisar.composestyled.shared

import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composeunstyled.UnstyledButton
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.components.StyledButtonRenderer
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.core.runtime.ProvideStyledLocals

@InternalComposeStyledApi
abstract class UnstyledButtonRenderer : StyledButtonRenderer.TokenRenderer {

    @Composable
    override fun render(
        onClick: () -> Unit,
        modifier: Modifier,
        textStyle: TextStyle,
        enabled: Boolean,
        shape: Shape,
        backgroundColor: Color,
        contentColor: Color,
        borderColor: Color?,
        borderWidth: Dp?,
        contentPadding: PaddingValues,
        interactionSource: MutableInteractionSource,
        content: @Composable (RowScope.() -> Unit),
    ) {
        ProvideStyledLocals(
            contentColor = contentColor,
            backgroundColor = backgroundColor,
        ) {
            val modifier = modifier.hoverable(
                interactionSource = interactionSource,
                enabled = enabled
            )
            ProvideStyledLocals(
                textStyle = textStyle
            ) {
                UnstyledButton(
                    onClick = onClick,
                    enabled = enabled,
                    shape = shape,
                    backgroundColor = backgroundColor,
                    contentColor = contentColor,
                    contentPadding = contentPadding,
                    borderColor = borderColor ?: Color.Unspecified,
                    borderWidth = borderWidth ?: 0.dp,
                    modifier = modifier,
                    role = Role.Button,
                    indication = StyledTheme.indications.select(backgroundColor),
                    interactionSource = interactionSource,
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    content = content
                )
            }
        }
    }
}