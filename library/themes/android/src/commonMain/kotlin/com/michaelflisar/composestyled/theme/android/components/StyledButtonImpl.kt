package com.michaelflisar.composestyled.theme.android.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyled.core.components.StyledButtonConfig
import com.michaelflisar.composestyled.core.runtime.ProvideStyledLocals
import com.michaelflisar.composestyled.core.StyledTheme

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
        Box(
            modifier
                .clip(shape)
                .then(
                    if (backgroundColor != null) {
                        Modifier.background(backgroundColor)
                    } else Modifier
                )
                .then(
                    when (config.variant) {
                        StyledButtonConfig.Variant.Outlined -> Modifier.border(
                            width = Dp.Hairline,
                            color = config.borderColor(enabled),
                            shape = shape
                        )

                        else -> Modifier
                    }
                )
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    enabled = enabled,
                    onClick = onClick
                )
                .padding(contentPadding),
            contentAlignment = Alignment.Center
        ) {
            Row(
                Modifier.defaultMinSize(
                    minWidth = config.minWidth,
                    minHeight = config.minHeight,
                )
                    .padding(contentPadding),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                content = content,
            )
        }
    }
}