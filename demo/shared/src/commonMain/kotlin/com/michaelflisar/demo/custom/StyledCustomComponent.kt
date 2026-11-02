package com.michaelflisar.demo.custom

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Help
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.classes.toIconComposable
import com.michaelflisar.composestyled.registry.LocalStyledRegistry
import com.michaelflisar.composestyled.registry.StyledComponent
import com.michaelflisar.composestyled.registry.StyledKey
import com.michaelflisar.composestyled.ui.components.StyledButton
import com.michaelflisar.composestyled.ui.components.StyledIcon
import com.michaelflisar.composestyled.ui.components.StyledSurface
import com.michaelflisar.composestyled.ui.layout.StyledColumn
import com.michaelflisar.composestyled.ui.layout.StyledRow
import com.michaelflisar.composestyled.ui.primitives.StyledText

/**
 * example of a custom component that is registered in the StyledRegistry and can be used in a platform independent way.
 */
class StyledCustomComponent(
    val variant: Variant,
) : StyledComponent<StyledCustomComponent> {
    override val key: StyledKey<StyledCustomComponent>
        get() = Key

    enum class Variant {
        Primary,
        Secondary,
        PrimarySurface,
        SecondarySurface,
    }

    companion object {

        val Key = StyledKey<StyledCustomComponent>("StyledCustomComponent")

    }

    @Composable
    fun Render(
        modifier: Modifier,
    ) {
        StyledSurface(
            modifier = modifier,
            color = when (variant) {
                Variant.Primary -> StyledTheme.colorScheme.primary
                Variant.Secondary -> StyledTheme.colorScheme.secondary
                Variant.PrimarySurface -> StyledTheme.colorScheme.primarySurface
                Variant.SecondarySurface -> StyledTheme.colorScheme.secondarySurface
            },
            shape = when (variant) {
                Variant.Primary -> RectangleShape
                Variant.Secondary -> CircleShape
                Variant.PrimarySurface -> RoundedCornerShape(percent = 50)
                Variant.SecondarySurface -> RoundedCornerShape(size = 4.dp)
            }
        ) {
            StyledColumn(
                modifier = Modifier.padding(StyledTheme.padding.medium),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (variant) {
                    Variant.Primary -> {
                        MainContent()
                    }

                    Variant.Secondary -> {
                        StyledRow(itemSpacing = StyledTheme.spacing.medium) {
                            StyledIcon(
                                icon = Icons.Default.Help.toIconComposable()
                            )
                            MainContent()
                        }
                    }

                    Variant.PrimarySurface -> {
                        StyledIcon(
                            icon = Icons.Default.Help.toIconComposable()
                        )
                        MainContent()
                    }

                    Variant.SecondarySurface -> {
                        StyledRow(itemSpacing = StyledTheme.spacing.medium) {
                            StyledIcon(
                                icon = Icons.Default.Help.toIconComposable()
                            )
                            MainContent()
                            StyledButton(
                                onClick = {

                                }
                            ) {
                                StyledText("Click me")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StyledCustomComponent(
    modifier: Modifier = Modifier,
) {
    LocalStyledRegistry.current[StyledCustomComponent.Key].Render(modifier)
}

@Composable
private fun MainContent() {
    StyledText(
        text = "Common content of the custom component",
        modifier = Modifier.padding(StyledTheme.padding.medium),
    )
}