package com.michaelflisar.composestyled.foundation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.UpdateStyledThemeLocals
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.ui.components.StyledSnackbar
import com.michaelflisar.composestyled.ui.components.StyledSnackbarDefaults
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi

@InternalComposeStyledApi
abstract class FoundationSnackbar : StyledSnackbar.Implementation {

    @Composable
    override fun Render(
        modifier: Modifier,
        shape: Shape,
        containerColor: Color,
        contentColor: Color,
        actionOnNewLine: Boolean,
        action: @Composable (() -> Unit)?,
        dismissAction: @Composable (() -> Unit)?,
        content: @Composable () -> Unit,
    ) {
        UpdateStyledThemeLocals(
            contentColor = contentColor,
            textStyle = StyledTheme.typography.bodyMedium,
        ) {
            Column(
                modifier = modifier
                    .widthIn(max = 560.dp)
                    .shadow(6.dp, shape, clip = false)
                    .clip(shape)
                    .background(containerColor)
                    .padding(StyledSnackbarDefaults.contentPadding),
                verticalArrangement = Arrangement.spacedBy(StyledTheme.spacing.small),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().heightIn(
                        min = maxOf(
                            StyledTheme.sizes.minimumInteractiveSize,
                            StyledTheme.sizes.iconSize,
                        )
                    ),
                    horizontalArrangement = Arrangement.spacedBy(StyledTheme.spacing.medium),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(Modifier.weight(1f)) { content() }
                    if (!actionOnNewLine) action?.invoke()
                    dismissAction?.invoke()
                }
                if (actionOnNewLine && action != null) {
                    Box(Modifier.align(Alignment.End)) { action() }
                }
            }
        }
    }
}
