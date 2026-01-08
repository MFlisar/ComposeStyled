package com.michaelflisar.composestyled.theme.material3.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyled.core.components.StyledButtonConfig
import com.michaelflisar.composestyled.core.runtime.ProvideStyledLocals
import com.michaelflisar.composestyled.core.theme.StyledTheme

/**
 * Material3-based StyledButton.
 *
 * Beinhaltet die minimalen Varianten wie im Core, um stabil über Adapter zu bleiben.
 */
@Composable
internal fun StyledButtonImpl(
    onClick: () -> Unit,
    modifier: Modifier,
    enabled: Boolean,
    shape: Shape,
    config: StyledButtonConfig,
    contentPadding: PaddingValues,
    interactionSource: MutableInteractionSource?,
    content: @Composable RowScope.() -> Unit,
) {
    val typography = StyledTheme.typography

    val backgroundColor = when (config.variant) {
        StyledButtonConfig.Variant.Filled,
        StyledButtonConfig.Variant.Outlined,
            -> config.containerColor(enabled)

        StyledButtonConfig.Variant.Text -> null
    }
    val contentColor = config.contentColor(enabled)

    ProvideStyledLocals(
        contentColor = contentColor,
        backgroundColor = backgroundColor,
        textStyle = typography.labelMedium
    ) {
        when (config.variant) {
            StyledButtonConfig.Variant.Filled -> Button(
                onClick = onClick,
                modifier = modifier,
                enabled = enabled,
                contentPadding = contentPadding,
                colors = ButtonDefaults.buttonColors(
                    containerColor = config.contentColor,
                    contentColor = config.contentColor,
                    disabledContainerColor = config.disabledContainerColor,
                    disabledContentColor = config.disabledContentColor,
                ),
                shape = shape,
                interactionSource = interactionSource,
                content = content
            )

            StyledButtonConfig.Variant.Outlined -> OutlinedButton(
                onClick = onClick,
                modifier = modifier,
                enabled = enabled,
                contentPadding = contentPadding,
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = config.contentColor,
                    contentColor = config.contentColor,
                    disabledContainerColor = config.disabledContainerColor,
                    disabledContentColor = config.disabledContentColor,
                ),
                border = BorderStroke(
                    width = Dp.Hairline,
                    color = config.borderColor(enabled)
                ),
                shape = shape,
                interactionSource = interactionSource,
                content = content
            )

            StyledButtonConfig.Variant.Text -> TextButton(
                onClick = onClick,
                modifier = modifier,
                enabled = enabled,
                contentPadding = contentPadding,
                colors = ButtonDefaults.textButtonColors(
                    containerColor = config.contentColor,
                    contentColor = config.contentColor,
                    disabledContainerColor = config.disabledContainerColor,
                    disabledContentColor = config.disabledContentColor,
                ),
                shape = shape,
                interactionSource = interactionSource,
                content = content
            )
        }
    }
}
