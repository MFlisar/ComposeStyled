package com.michaelflisar.composestyled.theme.wrapper.material3.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.heightIn
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
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.components.StyledButton
import com.michaelflisar.composestyled.core.components.StyledButtonWrapperRenderer
import com.michaelflisar.composestyled.theme.wrapper.material3.disabled

internal object StyledButtonImpl : StyledButtonWrapperRenderer {

    @Composable
    override fun Render(
        variant: StyledButton.Variant,
        customization: StyledButton.Customization?,
        onClick: () -> Unit,
        modifier: Modifier,
        enabled: Boolean,
        shape: Shape,
        contentPadding: PaddingValues,
        interactionSource: MutableInteractionSource,
        content: @Composable RowScope.() -> Unit,
    ) {
        val borderColor = customization?.border// ?: MaterialTheme.colorScheme.outlineVariant
        val borderStroke = borderColor?.let { BorderStroke(width = Dp.Hairline, color = it.resolve()) }

        var containerColor: Color
        var contentColor: Color
        var disabledContainerColor: Color
        var disabledContentColor: Color

        if (customization != null) {
            containerColor =
                customization.background?.resolve() ?: MaterialTheme.colorScheme.background
            contentColor =
                customization.content?.resolve() ?: MaterialTheme.colorScheme.onBackground
            disabledContainerColor =
                customization.background?.resolve()?.disabled() ?: Color.Unspecified
            disabledContentColor =
                customization.content?.resolve()?.disabled() ?: Color.Unspecified
        } else {
            when (variant) {
                StyledButton.Variant.Primary -> {
                    containerColor = MaterialTheme.colorScheme.primary
                    contentColor = MaterialTheme.colorScheme.onPrimary
                    disabledContainerColor = MaterialTheme.colorScheme.primary.disabled()
                    disabledContentColor = MaterialTheme.colorScheme.onPrimary.disabled()
                }

                StyledButton.Variant.Secondary -> {
                    containerColor = MaterialTheme.colorScheme.secondary
                    contentColor = MaterialTheme.colorScheme.onSecondary
                    disabledContainerColor = MaterialTheme.colorScheme.secondary.disabled()
                    disabledContentColor = MaterialTheme.colorScheme.onSecondary.disabled()
                }

                StyledButton.Variant.Outlined -> {
                    containerColor = MaterialTheme.colorScheme.surface
                    contentColor = MaterialTheme.colorScheme.onSurface
                    disabledContainerColor = MaterialTheme.colorScheme.surface.disabled()
                    disabledContentColor = MaterialTheme.colorScheme.onSurface.disabled()
                }

                StyledButton.Variant.Text -> {
                    containerColor = Color.Unspecified
                    contentColor = MaterialTheme.colorScheme.onSurface
                    disabledContainerColor = Color.Unspecified
                    disabledContentColor = MaterialTheme.colorScheme.onSurface.disabled()
                }
            }
        }

        when (variant) {
            StyledButton.Variant.Primary,
            StyledButton.Variant.Secondary,
                -> {
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

            StyledButton.Variant.Outlined -> {
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

            StyledButton.Variant.Text -> {
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