package com.michaelflisar.composestyled.theme.wrapper.material3.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyled.core.components.StyledButton
import com.michaelflisar.composestyled.core.components.StyledButtonWrapperRenderer
import com.michaelflisar.composestyled.theme.wrapper.material3.disabled

internal object StyledButtonImpl : StyledButtonWrapperRenderer {

    @Composable
    override fun Render(
        request: StyledButtonWrapperRenderer.Request,
        onClick: () -> Unit,
        modifier: Modifier,
        enabled: Boolean,
        shape: Shape,
        contentPadding: PaddingValues,
        interactionSource: MutableInteractionSource,
        content: @Composable RowScope.() -> Unit,
    ) {
        val borderColor = request.customColors?.normal?.border// ?: MaterialTheme.colorScheme.outlineVariant
        val borderStroke = borderColor?.let { BorderStroke(width = Dp.Hairline, color = it) }

        var containerColor: Color
        var contentColor: Color
        var disabledContainerColor: Color
        var disabledContentColor: Color

        if (request.customColors != null) {
            containerColor = request.customColors?.normal?.background ?: MaterialTheme.colorScheme.background
            contentColor = request.customColors?.normal?.foreground ?: MaterialTheme.colorScheme.onBackground
            disabledContainerColor = request.customColors?.normal?.background?.disabled() ?: Color.Unspecified
            disabledContentColor = request.customColors?.normal?.foreground?.disabled() ?: Color.Unspecified
        } else {
            when (request.variant) {
                StyledButton.VariantId.FilledPrimary -> {
                    containerColor = MaterialTheme.colorScheme.primary
                    contentColor = MaterialTheme.colorScheme.onPrimary
                    disabledContainerColor = MaterialTheme.colorScheme.primary.disabled()
                    disabledContentColor = MaterialTheme.colorScheme.onPrimary.disabled()
                }
                StyledButton.VariantId.Outlined -> {
                    containerColor = MaterialTheme.colorScheme.background
                    contentColor = MaterialTheme.colorScheme.onBackground
                    disabledContainerColor = MaterialTheme.colorScheme.background.disabled()
                    disabledContentColor = MaterialTheme.colorScheme.onBackground.disabled()
                }
                StyledButton.VariantId.Text -> {
                    containerColor = Color.Unspecified
                    contentColor = MaterialTheme.colorScheme.onBackground
                    disabledContainerColor = Color.Unspecified
                    disabledContentColor = MaterialTheme.colorScheme.onBackground.disabled()
                }
            }
        }

        when (request.variant) {
            StyledButton.VariantId.FilledPrimary -> {
                Button(
                    onClick = onClick,
                    modifier = modifier,
                    enabled = enabled,
                    shape = shape,
                    contentPadding = contentPadding,
                    interactionSource = interactionSource,
                    elevation = ButtonDefaults.buttonElevation(),
                    border = borderStroke,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = containerColor,
                        contentColor = contentColor,
                        disabledContainerColor = disabledContainerColor,
                        disabledContentColor = disabledContentColor
                    ),
                ) {
                    content()
                }
            }
            StyledButton.VariantId.Outlined -> {
                OutlinedButton(
                    onClick = onClick,
                    modifier = modifier,
                    enabled = enabled,
                    shape = shape,
                    contentPadding = contentPadding,
                    interactionSource = interactionSource,
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = containerColor,
                        contentColor = contentColor,
                        disabledContainerColor = disabledContainerColor,
                        disabledContentColor = disabledContentColor
                    ),
                ) {
                    content()
                }
            }
            StyledButton.VariantId.Text -> {
                TextButton(
                    onClick = onClick,
                    modifier = modifier,
                    enabled = enabled,
                    shape = shape,
                    contentPadding = contentPadding,
                    interactionSource = interactionSource,
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = containerColor,
                        contentColor = contentColor,
                        disabledContainerColor = disabledContainerColor,
                        disabledContentColor = disabledContentColor
                    ),
                ) {
                    content()
                }
            }
        }
    }
}