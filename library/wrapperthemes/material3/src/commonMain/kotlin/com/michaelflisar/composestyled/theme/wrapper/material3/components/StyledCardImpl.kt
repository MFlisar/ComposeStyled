package com.michaelflisar.composestyled.theme.wrapper.material3.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.michaelflisar.composestyled.core.classes.Emphasis
import com.michaelflisar.composestyled.core.components.StyledCard
import com.michaelflisar.composestyled.core.components.StyledCardWrapperRenderer
import com.michaelflisar.composestyled.core.components.StyledCardWrapperRenderer.Request

internal object StyledCardImpl : StyledCardWrapperRenderer {

    @Composable
    override fun Render(
        request: Request,
        modifier: Modifier,
        emphasis: Emphasis,
        shape: Shape,
        contentPadding: PaddingValues,
        content: @Composable ColumnScope.() -> Unit,
    ) {
        val background = request.customColors?.normal?.background
        val onBackground = request.customColors?.normal?.foreground

        val backgroundFilled = when (emphasis) {
            Emphasis.Low -> MaterialTheme.colorScheme.surface
            Emphasis.Medium -> MaterialTheme.colorScheme.surfaceVariant
            Emphasis.High -> MaterialTheme.colorScheme.primaryContainer
        }
        val onBackgroundFilled = when (emphasis) {
            Emphasis.Low -> MaterialTheme.colorScheme.onSurface
            Emphasis.Medium -> MaterialTheme.colorScheme.onSurfaceVariant
            Emphasis.High -> MaterialTheme.colorScheme.onPrimaryContainer
        }
        // val border = when (emphasis) {
        //     Emphasis.Low -> 1.dp
        //     Emphasis.Medium -> 2.dp
        //     Emphasis.High -> 4.dp
        // }

        when (emphasis) {
            Emphasis.Low -> {
                when (request.id) {
                    StyledCard.VariantId.Filled -> {
                        Card(
                            modifier = modifier,
                            shape = shape,
                            border = null,
                            colors = CardDefaults.cardColors(
                                containerColor = background ?: backgroundFilled,
                                contentColor = onBackground ?: onBackgroundFilled,
                            )
                        ) {
                            Content(contentPadding, content)
                        }
                    }

                    StyledCard.VariantId.Outlined -> {
                        OutlinedCard(
                            modifier = modifier,
                            shape = shape,
                            colors = CardDefaults.outlinedCardColors(
                                containerColor = background ?: Color.Unspecified,
                                contentColor = onBackground ?: Color.Unspecified,
                            )
                        ) {
                            Content(contentPadding, content)
                        }
                    }
                }
            }

            Emphasis.Medium -> {
                when (request.id) {
                    StyledCard.VariantId.Filled -> {
                        ElevatedCard(
                            modifier = modifier,
                            shape = shape,
                            colors = CardDefaults.elevatedCardColors(
                                containerColor = background ?: backgroundFilled,
                                contentColor = onBackground ?: onBackgroundFilled,
                            )
                        ) {
                            Content(contentPadding, content)
                        }
                    }

                    StyledCard.VariantId.Outlined -> {
                        OutlinedCard(
                            modifier = modifier,
                            shape = shape,
                            colors = CardDefaults.outlinedCardColors(
                                containerColor = background ?: Color.Unspecified,
                                contentColor = onBackground ?: Color.Unspecified,
                            )
                        ) {
                            Content(contentPadding, content)
                        }
                    }
                }
            }

            Emphasis.High -> {
                when (request.id) {
                    StyledCard.VariantId.Filled -> {
                        ElevatedCard(
                            modifier = modifier,
                            shape = shape,
                            colors = CardDefaults.elevatedCardColors(
                                containerColor = background ?: backgroundFilled,
                                contentColor = onBackground ?: onBackgroundFilled,
                            )
                        ) {
                            Content(contentPadding, content)
                        }
                    }

                    StyledCard.VariantId.Outlined -> {
                        OutlinedCard(
                            modifier = modifier,
                            shape = shape,
                            colors = CardDefaults.outlinedCardColors(
                                containerColor = background ?: Color.Unspecified,
                                contentColor = onBackground ?: Color.Unspecified,
                            )
                        ) {
                            Content(contentPadding, content)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Content(
    contentPadding: PaddingValues,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = Modifier.padding(contentPadding),
        content = content,
    )
}
