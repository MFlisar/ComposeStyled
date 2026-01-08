package com.michaelflisar.composestyled.theme.material3.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import com.michaelflisar.composestyled.core.components.StyledCardConfig
import com.michaelflisar.composestyled.core.runtime.ProvideStyledLocals

/**
 * Material3-based StyledCard.
 */
@Composable
internal fun StyledCardImpl(
    modifier: Modifier,
    shape: Shape,
    config: StyledCardConfig,
    contentPadding: PaddingValues,
    content: @Composable ColumnScope.() -> Unit,
) {
    ProvideStyledLocals(
        contentColor = config.contentColor,
        backgroundColor = config.containerColor
    ) {
        when (config.variant) {
            StyledCardConfig.Variant.Filled -> {
                Card(
                    modifier = modifier,
                    shape = shape,
                    colors = CardDefaults.cardColors(
                        containerColor = config.containerColor,
                        contentColor = config.contentColor,
                        disabledContentColor =  config.containerColor
                    )
                ) {
                    Box(Modifier.padding(contentPadding)) {
                        Column(content = content)
                    }
                }
            }

            StyledCardConfig.Variant.Outlined -> {
                OutlinedCard(
                    modifier = modifier,
                    shape = shape,
                    colors = CardDefaults.outlinedCardColors(
                        containerColor = config.containerColor,
                        contentColor = config.contentColor
                    )
                ) {
                    Box(Modifier.padding(contentPadding)) {
                        Column(content = content)
                    }
                }
            }
        }
    }
}
